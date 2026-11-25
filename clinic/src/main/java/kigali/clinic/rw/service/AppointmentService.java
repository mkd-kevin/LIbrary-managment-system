package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appRepo;

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private PatientRepository patRepo;

    public String saveAppointment(
            Appointment appointment,
            UUID doctorId,
            UUID patientId) {

        if (appointment == null ||
                appointment.getAppointmentDate() == null) {

            return "Appointment date is required";
        }

        Optional<Doctor> doctor =
                docRepo.findById(doctorId);

        if (doctor.isEmpty()) {
            return "Doctor not found";
        }

        Optional<Patient> patient =
                patRepo.findById(patientId);

        if (patient.isEmpty()) {
            return "Patient not found";
        }

        boolean alreadyBooked =
                appRepo.existsByDoctor_IdAndAppointmentDateAndStatusNot(
                        doctorId,
                        appointment.getAppointmentDate(),
                        AppointmentStatus.CANCELLED);

        if (alreadyBooked) {
            return "Doctor is already booked on that date";
        }

        appointment.setDoctor(doctor.get());
        appointment.setPatient(patient.get());

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        appRepo.save(appointment);

        return "Appointment is saved successfully";
    }

    public List<Appointment> getAllAppointments() {
        return appRepo.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appRepo.findById(id);
    }

    public List<Appointment> getAppointmentsByDoctor(UUID doctorId) {
        return appRepo.findByDoctor_Id(doctorId);
    }

    public List<Appointment> getAppointmentsByPatient(UUID patientId) {
        return appRepo.findByPatient_Id(patientId);
    }

    public List<Appointment> getAppointmentsByStatus(
            AppointmentStatus status) {

        return appRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetweenDates(
            LocalDate start,
            LocalDate end) {

        return appRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(
                start,
                end);
    }

    public String updateAppointment(
            UUID id,
            Appointment appointment,
            UUID doctorId,
            UUID patientId) {

        Optional<Appointment> existingAppointment =
                appRepo.findById(id);

        if (existingAppointment.isEmpty()) {
            return "Appointment not found";
        }

        if (appointment.getAppointmentDate() == null) {
            return "Appointment date is required";
        }

        Appointment appointmentToUpdate =
                existingAppointment.get();

        if (doctorId != null) {

            Optional<Doctor> doctor =
                    docRepo.findById(doctorId);

            if (doctor.isEmpty()) {
                return "Doctor not found";
            }

            appointmentToUpdate.setDoctor(doctor.get());
        }

        if (patientId != null) {

            Optional<Patient> patient =
                    patRepo.findById(patientId);

            if (patient.isEmpty()) {
                return "Patient not found";
            }

            appointmentToUpdate.setPatient(patient.get());
        }

        appointmentToUpdate.setAppointmentDate(
                appointment.getAppointmentDate());

        appointmentToUpdate.setReason(
                appointment.getReason());

        if (appointment.getStatus() != null) {
            appointmentToUpdate.setStatus(
                    appointment.getStatus());
        }

        appRepo.save(appointmentToUpdate);

        return "Appointment is updated successfully";
    }

    public String deleteAppointment(UUID id) {

        if (!appRepo.existsById(id)) {
            return "Appointment not found";
        }

        appRepo.deleteById(id);

        return "Appointment is deleted successfully";
    }

    public List<Appointment> getPendingAppointmentsOfDoctor(
            UUID doctorId) {

        return appRepo.findByDoctor_IdAndStatusOrderByAppointmentDateAsc(
                doctorId,
                AppointmentStatus.SCHEDULED);
    }

    public List<Appointment> getOverduePendingAppointments() {

        return appRepo.pendingOverdue(
                AppointmentStatus.SCHEDULED,
                LocalDate.now());
    }

    public List<Object[]> getAppointmentsPerDoctor() {

        return appRepo.appointmentsPerDoctor();
    }

    public List<Appointment> getAppointmentsBySpecialization(
            String specializationName) {

        return appRepo.bySpecialization(
                specializationName);
    }

    public Page<Appointment> getDoctorAppointmentsPage(
            UUID doctorId,
            int page) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        10,
                        Sort.by("appointmentDate").ascending());

        return appRepo.findByDoctor_Id(
                doctorId,
                pageable);
    }

    public List<Object[]> getAppointmentStatsByStatus() {
        return appRepo.countAppointmentsByStatus();
    }

    @Transactional
    public int cancelAppointmentsForDoctorOnDate(
            UUID doctorId,
            LocalDate appointmentDate) {

        return appRepo.cancelAppointmentsForDoctorOnDate(
                doctorId,
                appointmentDate,
                AppointmentStatus.CANCELLED,
                AppointmentStatus.COMPLETED);
    }
}
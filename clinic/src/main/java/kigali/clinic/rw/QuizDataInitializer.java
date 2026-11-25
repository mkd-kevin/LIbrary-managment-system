package kigali.clinic.rw;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Component
public class QuizDataInitializer implements CommandLineRunner {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final PatientRepository patientRepository;
    private final SpecializationRepository specializationRepository;

    public QuizDataInitializer(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            OfficeRepository officeRepository,
            PatientRepository patientRepository,
            SpecializationRepository specializationRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.patientRepository = patientRepository;
        this.specializationRepository = specializationRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Specialization cardiology = ensureSpecialization("Cardiology");
        Specialization pediatrics = ensureSpecialization("Pediatrics");
        Specialization dermatology = ensureSpecialization("Dermatology");
        ensureSpecialization("Neurology");

        Office office101 = ensureOffice(101);
        Office office102 = ensureOffice(102);
        ensureOffice(103);

        Doctor doctorOne = ensureDoctor("John", "Mugisha", office101, true);
        Doctor doctorTwo = ensureDoctor("Grace", "Niyonsaba", office102, true);
        Doctor doctorThree = ensureDoctor("Kevin", "Manzi", null, false);

        addSpecialization(doctorOne, cardiology);
        addSpecialization(doctorOne, pediatrics);
        addSpecialization(doctorTwo, dermatology);
        addSpecialization(doctorThree, cardiology);

        Patient patientOne = ensurePatient("Alice", "Uwase");
        Patient patientTwo = ensurePatient("Brian", "Uwase");
        Patient patientThree = ensurePatient("Claire", "Uwase");
        Patient patientFour = ensurePatient("Diane", "Habimana");
        Patient patientFive = ensurePatient("Eric", "Niyonsaba");

        Set<String> appointmentKeys = new HashSet<>();
        for (Appointment appointment : appointmentRepository.findAll()) {
            appointmentKeys.add(appointmentKey(
                    appointment.getDoctor(),
                    appointment.getPatient(),
                    appointment.getAppointmentDate()));
        }

        ensureAppointment(appointmentKeys, doctorOne, patientOne,
                LocalDate.of(2026, 10, 20), AppointmentStatus.CANCELLED);
        ensureAppointment(appointmentKeys, doctorOne, patientTwo,
                LocalDate.of(2026, 10, 20), AppointmentStatus.SCHEDULED);
        ensureAppointment(appointmentKeys, doctorOne, patientThree,
                LocalDate.of(2026, 10, 20), AppointmentStatus.COMPLETED);
        ensureAppointment(appointmentKeys, doctorOne, patientOne,
                LocalDate.of(2026, 10, 28), AppointmentStatus.COMPLETED);
        ensureAppointment(appointmentKeys, doctorTwo, patientOne,
                LocalDate.of(2026, 11, 3), AppointmentStatus.CONFIRMED);
        ensureAppointment(appointmentKeys, doctorOne, patientThree,
                LocalDate.of(2026, 11, 4), AppointmentStatus.SCHEDULED);
        ensureAppointment(appointmentKeys, doctorTwo, patientFour,
                LocalDate.of(2026, 11, 5), AppointmentStatus.CANCELLED);
        ensureAppointment(appointmentKeys, doctorTwo, patientTwo,
                LocalDate.of(2026, 11, 6), AppointmentStatus.COMPLETED);
        ensureAppointment(appointmentKeys, doctorOne, patientThree,
                LocalDate.of(2026, 11, 7), AppointmentStatus.CONFIRMED);
        ensureAppointment(appointmentKeys, doctorTwo, patientFive,
                LocalDate.of(2026, 11, 9), AppointmentStatus.SCHEDULED);
    }

    private Specialization ensureSpecialization(String name) {
        return specializationRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> {
                    Specialization specialization = new Specialization();
                    specialization.setName(name);
                    return specializationRepository.save(specialization);
                });
    }

    private Office ensureOffice(int officeNumber) {
        Office office = officeRepository.findByOfficeNumber(officeNumber)
                .orElseGet(() -> {
                    Office newOffice = new Office();
                    newOffice.setOfficeNumber(officeNumber);
                    return newOffice;
                });

        if (office.getName() == null || office.getName().isBlank()) {
            office.setName("Office " + officeNumber);
        }

        return officeRepository.save(office);
    }

    private Doctor ensureDoctor(
            String firstName,
            String lastName,
            Office office,
            boolean assignOffice) {
        Optional<Doctor> existingDoctor = doctorRepository.findByFirstName(firstName)
                .stream()
                .filter(doctor -> lastName.equals(doctor.getLastName()))
                .findFirst();

        Doctor doctor = existingDoctor.orElseGet(() -> {
            Doctor newDoctor = new Doctor();
            newDoctor.setFirstName(firstName);
            newDoctor.setLastName(lastName);
            return newDoctor;
        });

        if (assignOffice && doctor.getOffice() == null) {
            doctor.setOffice(office);
        } else if (!assignOffice) {
            doctor.setOffice(null);
        }

        return doctorRepository.save(doctor);
    }

    private Patient ensurePatient(String firstName, String lastName) {
        return patientRepository.findByFirstName(firstName)
                .stream()
                .filter(patient -> lastName.equals(patient.getLastName()))
                .findFirst()
                .orElseGet(() -> {
                    Patient patient = new Patient();
                    patient.setFirstName(firstName);
                    patient.setLastName(lastName);
                    return patientRepository.save(patient);
                });
    }

    private void addSpecialization(Doctor doctor, Specialization specialization) {
        boolean alreadyAssigned = doctor.getSpecializations().stream()
                .anyMatch(existing -> existing.getId().equals(specialization.getId()));

        if (!alreadyAssigned) {
            doctor.getSpecializations().add(specialization);
            doctorRepository.save(doctor);
        }
    }

    private void ensureAppointment(
            Set<String> appointmentKeys,
            Doctor doctor,
            Patient patient,
            LocalDate appointmentDate,
            AppointmentStatus status) {
        if (!appointmentKeys.add(appointmentKey(doctor, patient, appointmentDate))) {
            return;
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStatus(status);
        appointment.setReason("Quiz appointment");
        appointmentRepository.save(appointment);
    }

    private String appointmentKey(
            Doctor doctor,
            Patient patient,
            LocalDate appointmentDate) {
        return doctor.getId() + "|" + patient.getId() + "|" + appointmentDate;
    }
}
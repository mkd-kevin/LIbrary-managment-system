package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private OfficeRepository offRepo;

    @Autowired
    private SpecializationRepository specRepo;

    @Autowired
    private AppointmentRepository appRepo;

    public String saveDoctor(Doctor doctor, UUID officeId) {

        if (docRepo.existsByFirstNameAndLastName(
                doctor.getFirstName(),
                doctor.getLastName())) {

            return "A doctor with this first name and last name already exists";
        }

        Optional<Office> office =
                offRepo.findById(officeId);

        if (office.isEmpty()) {
            return "Office not found";
        }

        if (docRepo.findByOffice(office.get()).isPresent()) {
            return "Office is already assigned to another doctor";
        }

        doctor.setOffice(office.get());
        office.get().setDoctor(doctor);

        docRepo.save(doctor);

        return "Doctor is saved successfully";
    }

    public List<Doctor> getAllDoctors() {
        return docRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id) {
        return docRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor doctor) {

        Optional<Doctor> existingDoctor =
                docRepo.findById(id);

        if (existingDoctor.isEmpty()) {
            return "Doctor not found";
        }

        Doctor doctorToUpdate = existingDoctor.get();

        doctorToUpdate.setFirstName(doctor.getFirstName());
        doctorToUpdate.setLastName(doctor.getLastName());
        doctorToUpdate.setDateOfBirth(doctor.getDateOfBirth());

        docRepo.save(doctorToUpdate);

        return "Doctor is updated successfully";
    }

    @Transactional
    public String deleteDoctor(UUID id) {

        Optional<Doctor> existingDoctor =
                docRepo.findById(id);

        if (existingDoctor.isEmpty()) {
            return "Doctor not found";
        }

        if (appRepo.existsByDoctor_Id(id)) {
            return "Doctor has appointments and cannot be deleted";
        }

        Doctor doctorToDelete = existingDoctor.get();

        Office office = doctorToDelete.getOffice();

        doctorToDelete.setOffice(null);

        if (office != null) {
            office.setDoctor(null);
        }

        docRepo.delete(doctorToDelete);

        return "Doctor is deleted successfully";
    }

    @Transactional
    public String addSpecializationToDoctor(
            UUID doctorId,
            UUID specializationId) {

        Optional<Doctor> doctor =
                docRepo.findById(doctorId);

        if (doctor.isEmpty()) {
            return "Doctor not found";
        }

        Optional<Specialization> specialization =
                specRepo.findById(specializationId);

        if (specialization.isEmpty()) {
            return "Specialization not found";
        }

        if (doctor.get().getSpecializations()
                .contains(specialization.get())) {

            return "Doctor already has this specialization";
        }

        doctor.get().getSpecializations()
                .add(specialization.get());

        docRepo.save(doctor.get());

        return "Specialization is added to doctor successfully";
    }

    @Transactional
    public String removeSpecializationFromDoctor(
            UUID doctorId,
            UUID specializationId) {

        Optional<Doctor> doctor =
                docRepo.findById(doctorId);

        if (doctor.isEmpty()) {
            return "Doctor not found";
        }

        Optional<Specialization> specialization =
                specRepo.findById(specializationId);

        if (specialization.isEmpty()) {
            return "Specialization not found";
        }

        if (!doctor.get().getSpecializations()
                .contains(specialization.get())) {

            return "Doctor does not have this specialization";
        }

        doctor.get().getSpecializations()
                .remove(specialization.get());

        docRepo.save(doctor.get());

        return "Specialization is removed from doctor successfully";
    }


    public List<Doctor> getDoctorsBySpecialization(
            String name) {

        return docRepo.findBySpecialization(name);
    }


    public List<Doctor> getDoctorsWithoutOffice() {

        return docRepo.findDoctorsWithoutOffice();
    }
}
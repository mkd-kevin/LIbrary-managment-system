package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patRepo;

    @Autowired
    private AppointmentRepository appRepo;

    @Autowired
    private DoctorRepository docRepo;

    public String savePatient(Patient patient) {
        patRepo.save(patient);
        return "Patient is saved successfully";
    }

    public List<Patient> getAllPatients() {
        return patRepo.findAll();
    }

    public Optional<Patient> getPatientById(UUID id) {
        return patRepo.findById(id);
    }

    public String updatePatient(UUID id, Patient patient) {

        Optional<Patient> existingPatient = patRepo.findById(id);

        if (existingPatient.isEmpty()) {
            return "Patient not found";
        }

        Patient patientToUpdate = existingPatient.get();

        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());

        patRepo.save(patientToUpdate);

        return "Patient is updated successfully";
    }

    public String deletePatient(UUID id) {

        if (!patRepo.existsById(id)) {
            return "Patient not found";
        }

        if (appRepo.existsByPatient_Id(id)) {
            return "Patient has appointments and cannot be deleted";
        }

        patRepo.deleteById(id);

        return "Patient is deleted successfully";
    }

    public List<Patient> findPatientsByLastName(String lastName) {
        return patRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public Optional<List<Patient>> findPatientsOfDoctor(UUID doctorId) {
        if (!docRepo.existsById(doctorId)) {
            return Optional.empty();
        }

        return Optional.of(patRepo.findPatientsByDoctorId(doctorId));
    }

    public List<Patient> getFrequentPatients(long minAppointments) {
        return patRepo.findFrequentPatients(minAppointments);
    }
}
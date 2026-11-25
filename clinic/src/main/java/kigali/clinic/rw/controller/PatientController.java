package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping({"/api/patient", "/api/patients"})
public class PatientController {

    @Autowired
    private PatientService patServe;

    @PostMapping("/save")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient) {
        String message = patServe.savePatient(patient);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = patServe.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

   @GetMapping("/by-last-name")
public ResponseEntity<?> getPatientsByLastName(
        @RequestParam(name = "lastName", required = false, defaultValue = "") String lastName) {

    List<Patient> patients = patServe.findPatientsByLastName(lastName);

    return ResponseEntity.ok(patients);
}

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable UUID doctorId) {
        Optional<List<Patient>> patients = patServe.findPatientsOfDoctor(doctorId);

        if (patients.isEmpty()) {
            return new ResponseEntity<>(
                    "The doctor with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(patients.get());
    }

    @GetMapping("/frequent")
    public ResponseEntity<List<Patient>> getFrequentPatients(
            @RequestParam(defaultValue = "3") long min) {
        return ResponseEntity.ok(patServe.getFrequentPatients(min));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable UUID id) {

        Optional<Patient> patient = patServe.getPatientById(id);

        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatient(
            @PathVariable UUID id,
            @RequestBody Patient patient) {

        String message = patServe.updatePatient(id, patient);

        if (message.equals("Patient is updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {

        String message = patServe.deletePatient(id);

        if (message.equals("Patient is deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        if (message.equals("Patient not found")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
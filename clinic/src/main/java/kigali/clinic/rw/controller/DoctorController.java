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

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService docServe;

    @PostMapping("/save")
    public ResponseEntity<?> saveDoctor(
            @RequestBody Doctor doctor,
            @RequestParam UUID officeId) {

        String returnedMessage =
                docServe.saveDoctor(doctor, officeId);

        if (returnedMessage.equals(
                "Doctor is saved successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equals(
                "Office not found")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.CONFLICT);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDoctors() {

        List<Doctor> doctors =
                docServe.getAllDoctors();

        return new ResponseEntity<>(
                doctors,
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(
            @PathVariable UUID id) {

        Optional<Doctor> doctor =
                docServe.getDoctorById(id);

        if (doctor.isPresent()) {

            return new ResponseEntity<>(
                    doctor.get(),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "Doctor not found",
                HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable UUID id,
            @RequestBody Doctor doctor) {

        String returnedMessage =
                docServe.updateDoctor(id, doctor);

        if (returnedMessage.equals(
                "Doctor is updated successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable UUID id) {

        String returnedMessage =
                docServe.deleteDoctor(id);

        if (returnedMessage.equals(
                "Doctor is deleted successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equals(
                "Doctor not found")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.CONFLICT);
    }

    
    @GetMapping("/by-specialization")
    public ResponseEntity<?> getDoctorsBySpecialization(
            @RequestParam String name) {

        List<Doctor> doctors =
                docServe.getDoctorsBySpecialization(name);

        return new ResponseEntity<>(
                doctors,
                HttpStatus.OK);
    }

    
    @GetMapping("/without-office")
    public ResponseEntity<?> getDoctorsWithoutOffice() {

        List<Doctor> doctors =
                docServe.getDoctorsWithoutOffice();

        return new ResponseEntity<>(
                doctors,
                HttpStatus.OK);
    }

    @PutMapping("/add-specialization")
    public ResponseEntity<?> addSpecializationToDoctor(
            @RequestParam UUID doctorId,
            @RequestParam UUID specializationId) {

        String returnedMessage =
                docServe.addSpecializationToDoctor(
                        doctorId,
                        specializationId);

        if (returnedMessage.equals(
                "Specialization is added to doctor successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equals("Doctor not found")
                || returnedMessage.equals("Specialization not found")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.CONFLICT);
    }

    @PutMapping("/remove-specialization")
    public ResponseEntity<?> removeSpecializationFromDoctor(
            @RequestParam UUID doctorId,
            @RequestParam UUID specializationId) {

        String returnedMessage =
                docServe.removeSpecializationFromDoctor(
                        doctorId,
                        specializationId);

        if (returnedMessage.equals(
                "Specialization is removed from doctor successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equals("Doctor not found")
                || returnedMessage.equals("Specialization not found")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.CONFLICT);
    }
}
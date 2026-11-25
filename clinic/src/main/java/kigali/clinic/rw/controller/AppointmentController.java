package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appServe;

    @PostMapping("/save")
    public ResponseEntity<?> saveAppointment(
            @RequestBody Appointment appointment,
            @RequestParam UUID doctorId,
            @RequestParam UUID patientId) {

        String returnedMessage =
                appServe.saveAppointment(
                        appointment,
                        doctorId,
                        patientId);

        if (returnedMessage.equals(
                "Appointment is saved successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equals(
                "Doctor is already booked on that date")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.CONFLICT);
        }

        if (returnedMessage.equals(
                "Appointment date is required")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments() {

        List<Appointment> appointments =
                appServe.getAllAppointments();

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }
    @GetMapping("/between")
    public ResponseEntity<?> getAppointmentsBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDate startDate =
                LocalDate.parse(start);

        LocalDate endDate =
                LocalDate.parse(end);

        List<Appointment> appointments =
                appServe.getAppointmentsBetweenDates(
                        startDate,
                        endDate);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/by-status")
    public ResponseEntity<?> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {

        List<Appointment> appointments =
                appServe.getAppointmentsByStatus(status);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/by-doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctor(
            @PathVariable UUID doctorId) {

        List<Appointment> appointments =
                appServe.getAppointmentsByDoctor(doctorId);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<?> getAppointmentsByPatient(
            @PathVariable UUID patientId) {

        List<Appointment> appointments =
                appServe.getAppointmentsByPatient(patientId);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(
            @PathVariable UUID id) {

        Optional<Appointment> appointment =
                appServe.getAppointmentById(id);

        if (appointment.isPresent()) {

            return new ResponseEntity<>(
                    appointment.get(),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "Appointment not found",
                HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable UUID id,
            @RequestBody Appointment appointment,
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) UUID patientId) {

        String returnedMessage =
                appServe.updateAppointment(
                        id,
                        appointment,
                        doctorId,
                        patientId);

        if (returnedMessage.equals(
                "Appointment is updated successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        if (returnedMessage.equals(
                "Appointment date is required")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable UUID id) {

        String returnedMessage =
                appServe.deleteAppointment(id);

        if (returnedMessage.equals(
                "Appointment is deleted successfully")) {

            return new ResponseEntity<>(
                    returnedMessage,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                returnedMessage,
                HttpStatus.NOT_FOUND);
    }

    @GetMapping("/pending/doctor/{doctorId}")
    public ResponseEntity<?> getPendingAppointmentsOfDoctor(
            @PathVariable UUID doctorId) {

        List<Appointment> appointments =
                appServe.getPendingAppointmentsOfDoctor(
                        doctorId);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/pending/overdue")
    public ResponseEntity<?> getOverduePendingAppointments() {

        List<Appointment> appointments =
                appServe.getOverduePendingAppointments();

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/count-per-doctor")
    public ResponseEntity<?> getAppointmentsPerDoctor() {

        List<Object[]> results =
                appServe.getAppointmentsPerDoctor();

        return new ResponseEntity<>(
                results,
                HttpStatus.OK);
    }

    @GetMapping("/by-specialization")
    public ResponseEntity<?> getAppointmentsBySpecialization(
            @RequestParam String name) {

        List<Appointment> appointments =
                appServe.getAppointmentsBySpecialization(name);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

    @GetMapping("/page/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAppointmentsPage(
            @PathVariable UUID doctorId,
            @RequestParam(defaultValue = "0") int page) {

        Page<Appointment> appointments =
                appServe.getDoctorAppointmentsPage(
                        doctorId,
                        page);

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK);
    }

        @GetMapping("/stats/by-status")
        public ResponseEntity<List<Object[]>> getAppointmentStatsByStatus() {
                return ResponseEntity.ok(appServe.getAppointmentStatsByStatus());
        }

        @PatchMapping("/cancel-day")
        public ResponseEntity<String> cancelAppointmentsForDoctorOnDate(
                        @RequestParam UUID doctorId,
                        @RequestParam LocalDate date) {

                int cancelledCount =
                                appServe.cancelAppointmentsForDoctorOnDate(doctorId, date);

                return ResponseEntity.ok(cancelledCount + " appointments cancelled");
        }
}
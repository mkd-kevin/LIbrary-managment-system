package kigali.clinic.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByDoctor_Id(UUID doctorId);

    List<Appointment> findByPatient_Id(UUID patientId);

    boolean existsByDoctor_Id(UUID doctorId);

    boolean existsByPatient_Id(UUID patientId);

    List<Appointment> findByStatusOrderByAppointmentDateAsc(
            AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
            LocalDate start,
            LocalDate end);

    boolean existsByDoctor_IdAndAppointmentDateAndStatusNot(
            UUID doctorId,
            LocalDate appointmentDate,
            AppointmentStatus status);

    List<Appointment> findByDoctor_IdAndStatusOrderByAppointmentDateAsc(
            UUID doctorId,
            AppointmentStatus status);

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.status = :status
        AND a.appointmentDate < :today
        ORDER BY a.appointmentDate ASC
        """)
    List<Appointment> pendingOverdue(
            @Param("status") AppointmentStatus status,
            @Param("today") LocalDate today);

    @Query("""
        SELECT d.firstName, d.lastName, COUNT(a)
        FROM Appointment a
        JOIN a.doctor d
        GROUP BY d.id, d.firstName, d.lastName
        ORDER BY COUNT(a) DESC
        """)
    List<Object[]> appointmentsPerDoctor();

    @Query("""
        SELECT a FROM Appointment a
        JOIN a.doctor d
        JOIN d.specializations s
        WHERE LOWER(s.name) = LOWER(:spec)
        """)
    List<Appointment> bySpecialization(
            @Param("spec") String spec);

    Page<Appointment> findByDoctor_Id(
            UUID doctorId,
            Pageable pageable);

        @Query("""
                SELECT a.status, COUNT(a)
                FROM Appointment a
                GROUP BY a.status
                """)
        List<Object[]> countAppointmentsByStatus();

        @Modifying
        @Query("""
                UPDATE Appointment a
                SET a.status = :cancelledStatus
                WHERE a.doctor.id = :doctorId
                AND a.appointmentDate = :appointmentDate
                AND (a.status IS NULL OR a.status <> :completedStatus)
                """)
        int cancelAppointmentsForDoctorOnDate(
                        @Param("doctorId") UUID doctorId,
                        @Param("appointmentDate") LocalDate appointmentDate,
                        @Param("cancelledStatus") AppointmentStatus cancelledStatus,
                        @Param("completedStatus") AppointmentStatus completedStatus);
}
package auca.ac.rw.AcademicManagementSystem.repository;

import auca.ac.rw.AcademicManagementSystem.model.Registration;
import auca.ac.rw.AcademicManagementSystem.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByStudentId(Long studentId);
    List<Registration> findByCourseId(Long courseId);
    List<Registration> findByStatus(RegistrationStatus status);
}

package auca.ac.rw.AcademicManagementSystem;

import auca.ac.rw.AcademicManagementSystem.model.*;
import auca.ac.rw.AcademicManagementSystem.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AcademicManagementSystemApplicationTests {

    @Autowired TestEntityManager em;
    @Autowired DepartmentRepository deptRepo;
    @Autowired LecturerRepository lecturerRepo;
    @Autowired CourseRepository courseRepo;
    @Autowired StudentRepository studentRepo;
    @Autowired StudentProfileRepository profileRepo;
    @Autowired RegistrationRepository registrationRepo;

    @Test
    void contextLoads() {}

    @Test
    void departmentAndLecturerRelationship() {
        Department dept = deptRepo.save(new Department("Engineering", "ENG"));

        Lecturer lec = new Lecturer("Eve", "Uwase", "eve@uni.ac.rw");
        lec.setDepartment(dept);
        lecturerRepo.save(lec);

        dept.setHeadLecturer(lec);
        deptRepo.save(dept);

        em.flush();
        em.clear();

        Department found = deptRepo.findByCode("ENG").orElseThrow();
        assertThat(found.getName()).isEqualTo("Engineering");
        assertThat(found.getHeadLecturer().getEmail()).isEqualTo("eve@uni.ac.rw");
    }

    @Test
    void studentProfileOneToOne() {
        Department dept = deptRepo.save(new Department("Physics", "PHY"));

        Student student = new Student("Tom", "Habimana", "tom@uni.ac.rw", "STU001");
        student.setDepartment(dept);
        studentRepo.save(student);

        StudentProfile profile = new StudentProfile(
                LocalDate.of(2001, 1, 10), Gender.MALE, "+250700000001", "Kigali");
        profile.setStudent(student);
        profileRepo.save(profile);

        em.flush();
        em.clear();

        StudentProfile found = profileRepo.findByStudentId(student.getId()).orElseThrow();
        assertThat(found.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    void registrationJoinEntity() {
        Department dept = deptRepo.save(new Department("Chemistry", "CHEM"));

        Student student = new Student("Mary", "Ingabire", "mary@uni.ac.rw", "STU002");
        student.setDepartment(dept);
        studentRepo.save(student);

        Course course = new Course("CHEM101", "Intro to Chemistry", 3);
        course.setDepartment(dept);
        courseRepo.save(course);

        Registration reg = new Registration(LocalDate.now(), "2024-2025", "Semester 1");
        reg.setStudent(student);
        reg.setCourse(course);
        registrationRepo.save(reg);

        em.flush();
        em.clear();

        assertThat(registrationRepo.findByStudentId(student.getId())).hasSize(1);
        assertThat(registrationRepo.findByStatus(RegistrationStatus.PENDING)).isNotEmpty();
    }

    @Test
    void lecturerCourseManyToMany() {
        Department dept = deptRepo.save(new Department("Biology", "BIO"));

        Course bio = new Course("BIO101", "Cell Biology", 3);
        bio.setDepartment(dept);
        courseRepo.save(bio);

        Lecturer lec = new Lecturer("Sam", "Bizimana", "sam@uni.ac.rw");
        lec.setDepartment(dept);
        lec.getCourses().add(bio);
        lecturerRepo.save(lec);

        em.flush();
        em.clear();

        Lecturer found = lecturerRepo.findByEmail("sam@uni.ac.rw").orElseThrow();
        assertThat(found.getCourses()).hasSize(1);
    }
}

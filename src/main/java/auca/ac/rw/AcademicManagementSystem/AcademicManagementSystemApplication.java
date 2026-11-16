package auca.ac.rw.AcademicManagementSystem;

import auca.ac.rw.AcademicManagementSystem.model.*;
import auca.ac.rw.AcademicManagementSystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootApplication
public class AcademicManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademicManagementSystemApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner seedData(
            DepartmentRepository deptRepo,
            LecturerRepository lecturerRepo,
            CourseRepository courseRepo,
            StudentRepository studentRepo,
            StudentProfileRepository profileRepo,
            RegistrationRepository registrationRepo
    ) {
        return args -> {

            if (deptRepo.count() > 0) {
                System.out.println("\n========== SEED DATA VERIFICATION ==========");
                System.out.println("Departments  : " + deptRepo.count());
                System.out.println("Lecturers    : " + lecturerRepo.count());
                System.out.println("Courses      : " + courseRepo.count());
                System.out.println("Students     : " + studentRepo.count());
                System.out.println("Profiles     : " + profileRepo.count());
                System.out.println("Registrations: " + registrationRepo.count());
                System.out.println("=============================================\n");
                return;
            }

            Department cs = new Department("Computer Science", "CS");
            Department math = new Department("Mathematics", "MATH");
            deptRepo.save(cs);
            deptRepo.save(math);

            Lecturer alice = new Lecturer("Alice", "Mugisha", "alice.mugisha@university.ac.rw");
            alice.setDepartment(cs);

            Lecturer bob = new Lecturer("Bob", "Nkurunziza", "bob.nkurunziza@university.ac.rw");
            bob.setDepartment(math);

            lecturerRepo.save(alice);
            lecturerRepo.save(bob);

            cs.setHeadLecturer(alice);
            math.setHeadLecturer(bob);
            deptRepo.save(cs);
            deptRepo.save(math);

            Course oop = new Course("CS101", "Object-Oriented Programming", 3);
            oop.setDepartment(cs);

            Course db = new Course("CS201", "Database Systems", 3);
            db.setDepartment(cs);

            Course calculus = new Course("MATH101", "Calculus I", 4);
            calculus.setDepartment(math);

            courseRepo.save(oop);
            courseRepo.save(db);
            courseRepo.save(calculus);

            alice.getCourses().add(oop);
            alice.getCourses().add(db);
            bob.getCourses().add(calculus);
            lecturerRepo.save(alice);
            lecturerRepo.save(bob);

            Student john = new Student("John", "Doe", "john.doe@student.ac.rw", "STU2024001");
            john.setDepartment(cs);

            Student jane = new Student("Jane", "Smith", "jane.smith@student.ac.rw", "STU2024002");
            jane.setDepartment(math);

            studentRepo.save(john);
            studentRepo.save(jane);

            StudentProfile johnProfile = new StudentProfile(
                    LocalDate.of(2002, 5, 15), Gender.MALE, "+250788000001", "KG 123 St, Kigali");
            johnProfile.setStudent(john);

            StudentProfile janeProfile = new StudentProfile(
                    LocalDate.of(2003, 8, 22), Gender.FEMALE, "+250788000002", "KN 45 Ave, Kigali");
            janeProfile.setStudent(jane);

            profileRepo.save(johnProfile);
            profileRepo.save(janeProfile);

            Registration reg1 = new Registration(LocalDate.now(), "2024-2025", "Semester 1");
            reg1.setStudent(john);
            reg1.setCourse(oop);
            reg1.setStatus(RegistrationStatus.APPROVED);
            reg1.setGrade("A");

            Registration reg2 = new Registration(LocalDate.now(), "2024-2025", "Semester 1");
            reg2.setStudent(john);
            reg2.setCourse(db);
            reg2.setStatus(RegistrationStatus.PENDING);

            Registration reg3 = new Registration(LocalDate.now(), "2024-2025", "Semester 1");
            reg3.setStudent(jane);
            reg3.setCourse(calculus);
            reg3.setStatus(RegistrationStatus.APPROVED);
            reg3.setGrade("B+");

            registrationRepo.save(reg1);
            registrationRepo.save(reg2);
            registrationRepo.save(reg3);

            System.out.println("\n========== SEED DATA VERIFICATION ==========");
            System.out.println("Departments  : " + deptRepo.count());
            System.out.println("Lecturers    : " + lecturerRepo.count());
            System.out.println("Courses      : " + courseRepo.count());
            System.out.println("Students     : " + studentRepo.count());
            System.out.println("Profiles     : " + profileRepo.count());
            System.out.println("Registrations: " + registrationRepo.count());
            Lecturer csHead = lecturerRepo.findById(cs.getHeadLecturer().getId()).orElseThrow();
            System.out.println("CS head      : " + csHead);
            System.out.println("John's regs  : " + registrationRepo.findByStudentId(john.getId()).size());
            System.out.println("=============================================\n");
        };
    }
}

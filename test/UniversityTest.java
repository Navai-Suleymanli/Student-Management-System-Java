package test;

import model.Course;
import model.Student;
import model.UniversityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UniversityTest {

    private UniversityManager manager;

    @BeforeEach
    void setUp() {
        manager = new UniversityManager();
    }

    @Test
    void testStudentManagement() {
        Student s1 = new Student("S01", "Alice", "alice@test.com", "2023-01-01");
        Student s2 = new Student("S02", "Bob", "bob@test.com", "2023-01-02");

        assertTrue(manager.addStudent(s1), "Should successfully add a new student");
        assertTrue(manager.addStudent(s2), "Should successfully add a second student");
        assertEquals(2, manager.getStudents().size());


        Student s3Duplicate = new Student("S01", "Alice Clone", "fake@test.com", "2023-01-01");
        assertFalse(manager.addStudent(s3Duplicate), "Should NOT add student with existing ID");
        assertEquals(2, manager.getStudents().size());


        manager.removeStudent(s1);
        assertEquals(1, manager.getStudents().size());
        assertEquals("Bob", manager.getStudents().get(0).getName());
    }

    @Test
    void testCourseManagement() {
        Course c1 = new Course("CS101", "Java Basics", 5);

        assertTrue(manager.addCourse(c1));


        Course c2Duplicate = new Course("CS101", "Java Advanced", 3);
        assertFalse(manager.addCourse(c2Duplicate), "Should not allow duplicate Course Code");


        boolean updated = manager.updateCourse("CS101", "CS102", "Java Pro", 6);
        assertTrue(updated);


        assertNull(manager.getCourseByCode("CS101"));
        assertNotNull(manager.getCourseByCode("CS102"));
        assertEquals("Java Pro", manager.getCourseByCode("CS102").getName());
    }

    @Test
    void testSearchFunctionality() {
        manager.addStudent(new Student("100", "John Doe", "john@email.com", "2022"));
        manager.addStudent(new Student("101", "Jane Smith", "jane@email.com", "2022"));
        manager.addStudent(new Student("102", "Jim Doe", "jim@email.com", "2023"));


        List<Student> results = manager.searchStudents("Doe");
        assertEquals(2, results.size(), "Should find John Doe and Jim Doe");

        List<Student> exactId = manager.searchStudents("101");
        assertEquals(1, exactId.size());
        assertEquals("Jane Smith", exactId.get(0).getName());

        List<Student> emailSearch = manager.searchStudents("jane@email.com");
        assertEquals(1, emailSearch.size());
    }

    @Test
    void testGradeCalculation() {
        Student s = new Student("S1", "Math Genius", "m@m.com", "2023");
        manager.addStudent(s);

        Course c1 = new Course("M101", "Algebra", 5);
        Course c2 = new Course("M102", "Calculus", 5);
        manager.addCourse(c1);
        manager.addCourse(c2);

        assertEquals(0.0, manager.calculateStudentAverage(s), "Average should be 0.0 with no grades");

        manager.assignGrade(s, c1, "90");
        assertEquals(90.0, manager.calculateStudentAverage(s));


        manager.assignGrade(s, c2, "80");

        assertEquals(85.0, manager.calculateStudentAverage(s));
    }

    @Test
    void testGradeEdgeCases() {
        Student s = new Student("S99", "Tester", "t@t.com", "2023");
        manager.addStudent(s);
        Course c1 = new Course("C1", "Test Course", 3);
        manager.addCourse(c1);


        manager.assignGrade(s, c1, "Pass");


        assertEquals(0.0, manager.calculateStudentAverage(s), "Should ignore non-numeric grades safely");

        manager.assignGrade(s, c1, "50"); // First attempt
        manager.assignGrade(s, c1, "100"); // Correction

        assertEquals(100.0, manager.calculateStudentAverage(s), "Should overwrite old grade for same course");
    }
}

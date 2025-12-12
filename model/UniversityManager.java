package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversityManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Student> students;
    private List<Course> courses;
    private Map<String, Map<String, String>> gradeMap;

    public UniversityManager() {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.gradeMap = new HashMap<>();
    }

    public boolean addStudent(Student s) {
        for (Student existing : students) {
            if (existing.getId().equals(s.getId())) return false;
        }
        students.add(s);
        gradeMap.put(s.getId(), new HashMap<>());
        return true;
    }

    public void removeStudent(Student s) {
        students.remove(s);
        gradeMap.remove(s.getId());
    }

    public List<Student> getStudents() { return students; }

    public List<Student> searchStudents(String query) {
        List<Student> result = new ArrayList<>();
        String q = query.toLowerCase();
        for (Student s : students) {
            // Checks ID, Name, Email, OR Date
            if (s.getName().toLowerCase().contains(q) ||
                    s.getId().toLowerCase().contains(q) ||
                    s.getEmail().toLowerCase().contains(q) ||
                    s.getEnrollmentDate().toLowerCase().contains(q)) {
                result.add(s);
            }
        }
        return result;
    }

    public boolean addCourse(Course c) {
        for (Course existing : courses) {
            if (existing.getCode().equals(c.getCode())) return false;
        }
        courses.add(c);
        return true;
    }

    public void removeCourse(Course c) {
        courses.remove(c);
        for (Map<String, String> studentGrades : gradeMap.values()) {
            studentGrades.remove(c.getCode());
        }
    }

    public boolean updateCourse(String originalCode, String newCode, String newName, int newCredits) {
        Course courseToEdit = null;
        for (Course c : courses) {
            if (c.getCode().equals(originalCode)) {
                courseToEdit = c;
                break;
            }
        }
        if (courseToEdit == null) return false;

        if (!originalCode.equals(newCode)) {
            for (Course c : courses) {
                if (c.getCode().equals(newCode)) return false;
            }
        }

        courses.remove(courseToEdit);
        courses.add(new Course(newCode, newName, newCredits));

        if (!originalCode.equals(newCode)) {
            for (Map<String, String> studentGrades : gradeMap.values()) {
                if (studentGrades.containsKey(originalCode)) {
                    String gradeValue = studentGrades.remove(originalCode);
                    studentGrades.put(newCode, gradeValue);
                }
            }
        }
        return true;
    }

    public List<Course> getCourses() { return courses; }

    public Course getCourseByCode(String code) {
        for(Course c : courses) {
            if(c.getCode().equals(code)) return c;
        }
        return null;
    }

    //  grade operations
    public void assignGrade(Student s, Course c, String gradeValue) {
        if (gradeMap.containsKey(s.getId())) {
            gradeMap.get(s.getId()).put(c.getCode(), gradeValue);
        }
    }

    public Map<String, String> getRawGrades(Student s) {
        return gradeMap.getOrDefault(s.getId(), new HashMap<>());
    }

    public double calculateStudentAverage(Student s) {
        Map<String, String> grades = gradeMap.get(s.getId());
        if (grades == null || grades.isEmpty()) return 0.0;

        double sum = 0;
        int count = 0;

        for (String gVal : grades.values()) {
            try {
                double val = Double.parseDouble(gVal);
                sum += val;
                count++;
            } catch (NumberFormatException e) {
            }
        }
        return count == 0 ? 0.0 : sum / count;
    }
}
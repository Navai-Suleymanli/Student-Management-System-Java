package model;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String email;           // NEW
    private String enrollmentDate;  // NEW

    public Student(String id, String name, String email, String enrollmentDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getEnrollmentDate() { return enrollmentDate; }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
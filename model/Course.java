package model;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private int credits;

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCredits() { return credits; }

    @Override
    public String toString() { return name + " (" + code + ")"; }
}
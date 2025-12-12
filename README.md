# Student Management System (Java)

A Java Swing–based Student Management System implemented using the MVC pattern.  
It supports student & course management, grade assignment, academic report generation, and file serialization. The repository includes JUnit 5 tests for core functionality.

---

## Table of contents
- [About](#about)
- [Features](#features)
- [Tech stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick start (run locally)](#quick-start-run-locally)

---

## About
This is a desktop Student Management System built with Java Swing and organized using the Model–View–Controller (MVC) pattern. It’s intended as an educational or small-scale management app demonstrating typical CRUD operations, simple persistence via file serialization, and unit testing of core logic.

---

## Features
- Manage students (add / edit / remove)
- Manage courses and assign courses to students
- Assign grades and generate academic reports
- Save / load university data via file serialization (`university_data.ser`)
- Includes a JUnit 5 test suite for core functionality

---

## Tech stack
- Java (Standard Edition)
- Swing for GUI
- MVC project structure
- JUnit 5 for unit tests

---

## Prerequisites
- Java JDK 11+  
- Any Java IDE (IntelliJ IDEA recommended; `.iml` file included)

---

## Quick start (run locally)

### 1. Clone the repository
```bash
git clone https://github.com/Navai-Suleymanli/Student-Management-System-Java.git
cd Student-Management-System-Java
2. Open in an IDE
Open the project in IntelliJ IDEA (using the .iml file or folder import).

Or import it as a normal Java project in Eclipse/NetBeans.

3. Run the application
Find the class in the GUI package that contains the main(String[] args) method.

Run it using your IDE’s run button.

If you prefer running from terminal (paths may differ):

bash
Copy code
javac -d out $(find . -name "*.java")
java -cp out path.to.MainClass
Running tests
The test directory contains JUnit 5 tests.

In IntelliJ or Eclipse:

Right-click the test folder → Run All Tests

If using a build tool later (Maven/Gradle), tests will run automatically.

Project structure
bash
Copy code
Student-Management-System-Java/
├─ GUI/                  # Swing views & controllers
├─ model/                # Domain model: Student, Course, University...
├─ test/                 # JUnit 5 unit tests
├─ FileManager.java      # File serialization handler
├─ university_data.ser   # Example serialized data snapshot
├─ StudentSystemFinal.iml
└─ README.md
Data & serialization
The app uses Java’s built-in object serialization.
university_data.ser is provided as sample data that the app can load.

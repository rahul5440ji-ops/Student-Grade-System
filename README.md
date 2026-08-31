Student Grade Management System (Java)
A console-based Java project for managing students and their grades.
Features
Add students (roll number + name)
Add / update marks per subject (0–100)
View a single student's report card (per-subject marks, total, average, letter grade, pass/fail)
View all students sorted by average
Class report: class average, top student, pass/fail counts, grade distribution
Remove a student
Persistence: data is saved to `students.txt` and automatically reloaded on the next run
Grading scale
Average	Grade
90–100	A+
80–89	A
70–79	B
60–69	C
50–59	D
below 50	F
A student also fails if any single subject score is below 33, even if their average is 50+.
Project structure
```
StudentGradeSystem/
├── src/
│   ├── Student.java       # Student data model (subjects, marks, average, grade)
│   ├── GradeManager.java  # Collection logic + file save/load
│   └── Main.java          # Console menu / entry point
└── README.md
```
How to compile and run
From the `StudentGradeSystem` folder:
```bash
cd src
javac *.java -d ../bin
cd ../bin
java Main
```
Or compile and run directly without a separate output folder:
```bash
cd src
javac *.java
java Main
```
The program will create/read `students.txt` in the directory you run `java` from — that's your saved data file between sessions.
Example session
```
1. Add Student        -> roll: 101, name: Asha Verma
2. Add Grade           -> roll: 101, subject: Math, marks: 92
2. Add Grade           -> roll: 101, subject: Science, marks: 88
3. View a Student's Report -> shows Asha's report card
5. Class Report        -> shows class average, topper, distribution
8. Save & Exit
```
Extending it
Some natural next steps if you want to build on this:
Add input validation for duplicate subjects, negative roll numbers, etc.
Switch storage from a flat text file to CSV with a header row, or to a small SQLite database via JDBC
Add a GUI (Swing/JavaFX) or a Spring Boot REST API on top of `GradeManager`
Add semester/term support so a student can have grades across multiple terms

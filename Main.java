import java.util.*;

/**
 * Student Grade Management System
 * Console menu that lets a user manage students and their subject grades,
 * view reports, and persist data to a local text file.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GradeManager manager = new GradeManager();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   STUDENT GRADE MANAGEMENT SYSTEM");
        System.out.println("=========================================");

        // Try loading any previously saved data
        try {
            int loaded = manager.loadFromFile();
            if (loaded > 0) {
                System.out.println("Loaded " + loaded + " student record(s) from students.txt");
            }
        } catch (Exception e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addStudent(); break;
                case "2": addGrade(); break;
                case "3": viewStudent(); break;
                case "4": viewAllStudents(); break;
                case "5": classReport(); break;
                case "6": removeStudent(); break;
                case "7": saveData(); break;
                case "8":
                    saveData();
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("----------------- MENU -----------------");
        System.out.println("1. Add Student");
        System.out.println("2. Add / Update Grade for a Student");
        System.out.println("3. View a Student's Report");
        System.out.println("4. View All Students (sorted by average)");
        System.out.println("5. Class Report (average, topper, distribution)");
        System.out.println("6. Remove Student");
        System.out.println("7. Save Data to File");
        System.out.println("8. Save & Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addStudent() {
        System.out.print("Enter roll number: ");
        String roll = scanner.nextLine().trim();
        if (roll.isEmpty()) {
            System.out.println("Roll number cannot be empty.");
            return;
        }
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        if (manager.addStudent(roll, name)) {
            System.out.println("Student added: " + name + " (" + roll + ")");
        } else {
            System.out.println("A student with roll number " + roll + " already exists.");
        }
    }

    private static void addGrade() {
        System.out.print("Enter student roll number: ");
        String roll = scanner.nextLine().trim();
        Student s = manager.getStudent(roll);
        if (s == null) {
            System.out.println("No student found with roll number " + roll);
            return;
        }
        System.out.print("Enter subject name: ");
        String subject = scanner.nextLine().trim();
        System.out.print("Enter marks (0-100): ");
        String marksStr = scanner.nextLine().trim();
        try {
            double marks = Double.parseDouble(marksStr);
            if (manager.addGrade(roll, subject, marks)) {
                System.out.println("Recorded " + subject + " = " + marks + " for " + s.getName());
            } else {
                System.out.println("Marks must be between 0 and 100.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered for marks.");
        }
    }

    private static void viewStudent() {
        System.out.print("Enter student roll number: ");
        String roll = scanner.nextLine().trim();
        Student s = manager.getStudent(roll);
        if (s == null) {
            System.out.println("No student found with roll number " + roll);
            return;
        }
        System.out.println();
        System.out.println("Report card for " + s.getName() + " (" + s.getRollNumber() + ")");
        System.out.println("---------------------------------------------");
        if (s.getSubjectMarks().isEmpty()) {
            System.out.println("  No grades recorded yet.");
        } else {
            for (Map.Entry<String, Double> entry : s.getSubjectMarks().entrySet()) {
                System.out.printf("  %-20s %.2f%n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("---------------------------------------------");
        System.out.printf("  Total: %.2f | Average: %.2f | Grade: %s | Status: %s%n",
                s.getTotalMarks(), s.getAverage(), s.getLetterGrade(),
                s.isPassing() ? "PASS" : "FAIL");
    }

    private static void viewAllStudents() {
        if (manager.isEmpty()) {
            System.out.println("No students in the system yet.");
            return;
        }
        System.out.println();
        System.out.println("All students (sorted by average, highest first):");
        System.out.println("---------------------------------------------------------------");
        for (Student s : manager.getStudentsSortedByAverage(true)) {
            System.out.println(s);
        }
        System.out.println("---------------------------------------------------------------");
    }

    private static void classReport() {
        if (manager.isEmpty()) {
            System.out.println("No students in the system yet.");
            return;
        }
        Student top = manager.getTopStudent();
        int passCount = 0, failCount = 0;
        Map<String, Integer> gradeDistribution = new TreeMap<>();

        for (Student s : manager.getAllStudents()) {
            if (s.isPassing()) passCount++; else failCount++;
            gradeDistribution.merge(s.getLetterGrade(), 1, Integer::sum);
        }

        System.out.println();
        System.out.println("================ CLASS REPORT ================");
        System.out.println("Total students : " + manager.size());
        System.out.printf("Class average  : %.2f%n", manager.getClassAverage());
        System.out.println("Top student    : " + (top != null
                ? top.getName() + " (" + top.getRollNumber() + ") - " + String.format("%.2f", top.getAverage())
                : "N/A"));
        System.out.println("Passed         : " + passCount);
        System.out.println("Failed         : " + failCount);
        System.out.println("Grade distribution:");
        for (Map.Entry<String, Integer> entry : gradeDistribution.entrySet()) {
            System.out.println("   " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("================================================");
    }

    private static void removeStudent() {
        System.out.print("Enter roll number to remove: ");
        String roll = scanner.nextLine().trim();
        if (manager.removeStudent(roll)) {
            System.out.println("Removed student " + roll);
        } else {
            System.out.println("No student found with roll number " + roll);
        }
    }

    private static void saveData() {
        try {
            manager.saveToFile();
            System.out.println("Data saved to students.txt");
        } catch (Exception e) {
            System.out.println("Failed to save data: " + e.getMessage());
        }
    }
}

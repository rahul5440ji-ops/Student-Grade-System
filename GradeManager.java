import java.io.*;
import java.util.*;

/**
 * Manages the collection of Student records: add, remove, search,
 * report generation, and simple file-based persistence (CSV-like .txt).
 */
public class GradeManager {
    private final Map<String, Student> students; // rollNumber -> Student
    private static final String DATA_FILE = "students.txt";

    public GradeManager() {
        students = new LinkedHashMap<>();
    }

    public boolean addStudent(String rollNumber, String name) {
        if (students.containsKey(rollNumber)) {
            return false; // already exists
        }
        students.put(rollNumber, new Student(rollNumber, name));
        return true;
    }

    public boolean removeStudent(String rollNumber) {
        return students.remove(rollNumber) != null;
    }

    public Student getStudent(String rollNumber) {
        return students.get(rollNumber);
    }

    public boolean addGrade(String rollNumber, String subject, double marks) {
        Student s = students.get(rollNumber);
        if (s == null) return false;
        if (marks < 0 || marks > 100) return false;
        s.addOrUpdateMarks(subject, marks);
        return true;
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    public int size() {
        return students.size();
    }

    /**
     * Returns the class average across every student's personal average.
     */
    public double getClassAverage() {
        if (students.isEmpty()) return 0.0;
        double sum = 0;
        for (Student s : students.values()) {
            sum += s.getAverage();
        }
        return sum / students.size();
    }

    public Student getTopStudent() {
        Student top = null;
        for (Student s : students.values()) {
            if (top == null || s.getAverage() > top.getAverage()) {
                top = s;
            }
        }
        return top;
    }

    public List<Student> getStudentsSortedByAverage(boolean descending) {
        List<Student> list = new ArrayList<>(students.values());
        list.sort((a, b) -> descending
                ? Double.compare(b.getAverage(), a.getAverage())
                : Double.compare(a.getAverage(), b.getAverage()));
        return list;
    }

    // ---------- Persistence ----------

    public void saveToFile() throws IOException {
        saveToFile(DATA_FILE);
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student s : students.values()) {
                writer.write(s.toDataString());
                writer.newLine();
            }
        }
    }

    public int loadFromFile() throws IOException {
        return loadFromFile(DATA_FILE);
    }

    public int loadFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return 0;
        }
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Student s = Student.fromDataString(line);
                if (s != null) {
                    students.put(s.getRollNumber(), s);
                    count++;
                }
            }
        }
        return count;
    }
}

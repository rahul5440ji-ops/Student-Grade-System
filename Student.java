import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a single student, identified by a roll number,
 * holding a map of subject -> marks (0-100).
 */
public class Student {
    private final String rollNumber;
    private String name;
    // LinkedHashMap keeps subjects in the order they were added
    private final Map<String, Double> subjectMarks;

    public Student(String rollNumber, String name) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.subjectMarks = new LinkedHashMap<>();
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getSubjectMarks() {
        return subjectMarks;
    }

    public void addOrUpdateMarks(String subject, double marks) {
        subjectMarks.put(subject, marks);
    }

    public boolean removeSubject(String subject) {
        return subjectMarks.remove(subject) != null;
    }

    public double getTotalMarks() {
        double total = 0;
        for (double m : subjectMarks.values()) {
            total += m;
        }
        return total;
    }

    public double getAverage() {
        if (subjectMarks.isEmpty()) return 0.0;
        return getTotalMarks() / subjectMarks.size();
    }

    /**
     * Standard 90/80/70/60/50 grading scale.
     */
    public String getLetterGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        return "F";
    }

    public boolean isPassing() {
        // Fails overall if average < 50 OR any single subject < 33 (typical pass mark)
        if (getAverage() < 50) return false;
        for (double m : subjectMarks.values()) {
            if (m < 33) return false;
        }
        return true;
    }

    /**
     * Serializes this student to a single CSV-friendly line:
     * rollNumber|name|subj1:marks1,subj2:marks2,...
     */
    public String toDataString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rollNumber).append("|").append(name).append("|");
        boolean first = true;
        for (Map.Entry<String, Double> entry : subjectMarks.entrySet()) {
            if (!first) sb.append(",");
            sb.append(entry.getKey()).append(":").append(entry.getValue());
            first = false;
        }
        return sb.toString();
    }

    /**
     * Parses a line produced by toDataString() back into a Student.
     */
    public static Student fromDataString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 2) return null;

        String roll = parts[0];
        String name = parts[1];
        Student student = new Student(roll, name);

        if (parts.length >= 3 && !parts[2].isEmpty()) {
            String[] pairs = parts[2].split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    try {
                        student.addOrUpdateMarks(kv[0], Double.parseDouble(kv[1]));
                    } catch (NumberFormatException ignored) {
                        // skip malformed entry
                    }
                }
            }
        }
        return student;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-20s Avg: %-6.2f Grade: %-3s %s",
                rollNumber, name, getAverage(), getLetterGrade(),
                isPassing() ? "(Pass)" : "(Fail)");
    }
}

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class StudentGradeTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();

    static class Student {
        private final String name;
        private final String rollNumber;
        private final List<Double> marks;

        Student(String name, String rollNumber, List<Double> marks) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.marks = new ArrayList<>(marks);
        }

        double average() {
            return marks.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        double highest() {
            return marks.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        }

        double lowest() {
            return marks.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        }

        String getName() { return name; }
        String getRollNumber() { return rollNumber; }

        void printReport() {
            System.out.printf("%-14s %-22s %-25s %8.2f %8.2f %8.2f%n",
                    rollNumber, name, marks, average(), highest(), lowest());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\\n=== Student Grade Tracker ===");
            System.out.println("1. Add student grades");
            System.out.println("2. View all student reports");
            System.out.println("3. Search student by roll number");
            System.out.println("4. Exit");
            int choice = readInt("Choose an option: ", 1, 4);

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> showAllStudents();
                case 3 -> searchStudent();
                case 4 -> {
                    System.out.println("Thank you for using Student Grade Tracker!");
                    scanner.close();
                    return;
                }
            }
        }
    }

    private static void addStudent() {
        System.out.print("Student name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Roll number: ");
        String roll = scanner.nextLine().trim();
        if (roll.isEmpty()) {
            System.out.println("Roll number cannot be empty.");
            return;
        }
        if (students.stream().anyMatch(s -> s.getRollNumber().equalsIgnoreCase(roll))) {
            System.out.println("That roll number already exists.");
            return;
        }

        int count = readInt("Number of subjects: ", 1, 20);
        List<Double> marks = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            marks.add(readDouble("Marks for subject " + i + " (0-100): ", 0, 100));
        }
        students.add(new Student(name, roll, marks));
        System.out.println("Student grades saved successfully.");
    }

    private static void showAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records yet.");
            return;
        }
        printHeader();
        students.stream().sorted(Comparator.comparing(Student::getRollNumber))
                .forEach(Student::printReport);
    }

    private static void searchStudent() {
        System.out.print("Enter roll number: ");
        String roll = scanner.nextLine().trim();
        for (Student student : students) {
            if (student.getRollNumber().equalsIgnoreCase(roll)) {
                printHeader();
                student.printReport();
                return;
            }
        }
        System.out.println("No student found with that roll number.");
    }

    private static void printHeader() {
        System.out.printf("%-14s %-22s %-25s %8s %8s %8s%n",
                "Roll Number", "Name", "Marks", "Average", "Highest", "Lowest");
        System.out.println("-".repeat(105));
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) { }
            System.out.printf("Enter a whole number between %d and %d.%n", min, max);
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (Double.isFinite(value) && value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) { }
            System.out.printf("Enter a number between %.0f and %.0f.%n", min, max);
        }
    }
}

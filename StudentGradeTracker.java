import java.util.ArrayList;
import java.util.Scanner;

class Student {
    String name;
    int grade;

    Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }
}

public class StudentGradeTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> studentList = new ArrayList<>();

        System.out.println("=== Student Grade Tracker ===");

        // Input student data
        while (true) {
            System.out.print("Enter student name (or type 'done' to finish): ");
            String name = sc.nextLine();

            if (name.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter grade for " + name + ": ");
            int grade;
            try {
                grade = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid grade. Please enter an integer.");
                continue;
            }

            studentList.add(new Student(name, grade));
        }

        if (studentList.isEmpty()) {
            System.out.println("No student data entered.");
            return;
        }

        // Calculations
        int total = 0;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;
        String topStudent = "", lowStudent = "";

        for (Student s : studentList) {
            total += s.grade;

            if (s.grade > highest) {
                highest = s.grade;
                topStudent = s.name;
            }

            if (s.grade < lowest) {
                lowest = s.grade;
                lowStudent = s.name;
            }
        }

        double average = (double) total / studentList.size();

        // Summary Report
        System.out.println("\n=== Summary Report ===");
        for (Student s : studentList) {
            System.out.println(s.name + ": " + s.grade);
        }

        System.out.printf("\nAverage Score: %.2f\n", average);
        System.out.println("Highest Score: " + highest + " (" + topStudent + ")");
        System.out.println("Lowest Score: " + lowest + " (" + lowStudent + ")");

    }
}

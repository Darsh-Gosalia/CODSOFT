import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

class StudentManagementSystem {
    private ArrayList<Student> students;

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public void writeStudentsToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void readStudentsFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = inputStream.readObject();
            if (obj instanceof ArrayList) {
                students = (ArrayList<Student>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle file not found or other exceptions
            e.printStackTrace();
        }
    }
}

public class StudentManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManagementSystem system = new StudentManagementSystem();

        // Read students from file (if available)
        system.readStudentsFromFile("students.dat");

        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Remove Student");
            System.out.println("4. Search Student");
            System.out.println("5. Display All Students");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline character

                switch (choice) {
                    case 1:
                        addStudent(system, scanner);
                        break;
                    case 2:
                        editStudent(system, scanner);
                        break;
                    case 3:
                        removeStudent(system, scanner);
                        break;
                    case 4:
                        searchStudent(system, scanner);
                        break;
                    case 5:
                        system.displayAllStudents();
                        break;
                    case 6:
                        // Write students to file when exiting the application
                        system.writeStudentsToFile("students.dat");
                        System.out.println("Exiting the application. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume the invalid input
            }
        }
    }

    private static void addStudent(StudentManagementSystem system, Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        int rollNumber = getValidRollNumber(scanner);

        System.out.print("Enter student grade: ");
        String grade = scanner.next();
        scanner.nextLine(); // consume the newline character

        Student student = new Student(name, rollNumber, grade);
        system.addStudent(student);
        System.out.println("Student added successfully!");
    }

    private static void editStudent(StudentManagementSystem system, Scanner scanner) {
        int rollNumber = getValidRollNumber(scanner);
        Student student = system.searchStudent(rollNumber);

        if (student != null) {
            System.out.println("Editing Student:");
            System.out.println(student);

            System.out.print("Enter new student name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new student grade: ");
            String grade = scanner.next();
            scanner.nextLine(); // consume the newline character

            Student updatedStudent = new Student(name, rollNumber, grade);
            system.removeStudent(rollNumber);
            system.addStudent(updatedStudent);

            System.out.println("Student information edited successfully!");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void removeStudent(StudentManagementSystem system, Scanner scanner) {
        int rollNumber = getValidRollNumber(scanner);
        system.removeStudent(rollNumber);
        System.out.println("Student removed successfully!");
    }

    private static void searchStudent(StudentManagementSystem system, Scanner scanner) {
        int rollNumber = getValidRollNumber(scanner);
        Student student = system.searchStudent(rollNumber);

        if (student != null) {
            System.out.println("Student found:\n" + student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static int getValidRollNumber(Scanner scanner) {
        int rollNumber;
        while (true) {
            System.out.print("Enter student roll number: ");
            try {
                rollNumber = scanner.nextInt();
                scanner.nextLine(); // consume the newline character
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Roll number must be a number.");
                scanner.nextLine(); // consume the invalid input
            }
        }
        return rollNumber;
    }
}

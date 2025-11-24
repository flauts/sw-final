package com.utec.cs3081.infrastructure.cli;

import com.utec.cs3081.domain.model.Evaluation;
import com.utec.cs3081.domain.model.GradeReport;
import com.utec.cs3081.domain.model.Student;
import com.utec.cs3081.domain.policy.StandardAttendancePolicy;
import com.utec.cs3081.domain.policy.YearlyAgreementPolicy;
import com.utec.cs3081.domain.service.GradeCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {
    private final Scanner scanner;
    private final GradeCalculator calculator;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
        this.calculator = new GradeCalculator(
            new StandardAttendancePolicy(),
            new YearlyAgreementPolicy()
        );
    }

    public void start() {
        System.out.println("=== CS-GradeCalculator ===");
        
        while (true) {
            try {
                processStudent();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.print("\nProcess another student? (y/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private void processStudent() {
        System.out.print("Enter Student Code: ");
        String code = scanner.nextLine();
        Student student = new Student(code);

        // Evaluations
        while (true) {
            System.out.println("\n--- Add Evaluation ---");
            System.out.print("Name (or 'done' to finish): ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) break;

            System.out.print("Score (0-20): ");
            double score = Double.parseDouble(scanner.nextLine());

            System.out.print("Weight (0-100): ");
            double weight = Double.parseDouble(scanner.nextLine());

            try {
                student.addEvaluation(new Evaluation(name, score, weight));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid evaluation: " + e.getMessage());
            }
        }

        // Attendance
        System.out.print("\nHas reached minimum attendance? (true/false): ");
        boolean attendance = Boolean.parseBoolean(scanner.nextLine());
        student.setHasReachedMinimumClasses(attendance);

        // Teachers Agreement
        List<Boolean> agreements = new ArrayList<>();
        System.out.println("\n--- Teachers Agreement (Extra Points) ---");
        System.out.print("Enter number of years to check agreement for: ");
        try {
            int years = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < years; i++) {
                System.out.print("Year " + (i + 1) + " agreement (true/false): ");
                agreements.add(Boolean.parseBoolean(scanner.nextLine()));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, skipping extra points check (assuming none).");
        }

        // Calculate
        GradeReport report = calculator.calculate(student, agreements);

        // Output
        System.out.println("\n=== Grade Report for " + student.getCode() + " ===");
        System.out.println("Final Grade: " + String.format("%.2f", report.getFinalGrade()));
        System.out.println("Details:");
        for (String msg : report.getMessages()) {
            System.out.println("- " + msg);
        }
    }
}

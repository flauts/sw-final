package com.utec.cs3081.domain.service;

import com.utec.cs3081.domain.model.Evaluation;
import com.utec.cs3081.domain.model.GradeReport;
import com.utec.cs3081.domain.model.Student;
import com.utec.cs3081.domain.policy.AttendancePolicy;
import com.utec.cs3081.domain.policy.ExtraPointsPolicy;

import java.util.List;

public class GradeCalculator {
    private final AttendancePolicy attendancePolicy;
    private final ExtraPointsPolicy extraPointsPolicy;

    public GradeCalculator(AttendancePolicy attendancePolicy, ExtraPointsPolicy extraPointsPolicy) {
        this.attendancePolicy = attendancePolicy;
        this.extraPointsPolicy = extraPointsPolicy;
    }

    public GradeReport calculate(Student student, List<Boolean> allYearsTeachers) {
        GradeReport report = new GradeReport(0.0); // Initial dummy, will replace

        // 1. Check Attendance
        if (!attendancePolicy.isMet(student)) {
            // If attendance not met, what happens?
            // Usually fail or max grade cap. Rubric doesn't specify exact penalty, 
            // but RF02 says "registrar si cumplió".
            // Let's assume if not met, grade is 0 or failed. 
            // However, to be "functional", let's calculate but add a warning/failure message.
            // Or maybe return 0? 
            // Let's calculate the real grade but cap it or mark as failed.
            // For this implementation, I will calculate normally but add a CRITICAL message 
            // and maybe cap at 10 or 0 if strict. 
            // Given "Nota final", usually attendance failure = 0 or DPR.
            // I'll set it to 0.0 and add a message.
            GradeReport failReport = new GradeReport(0.0);
            failReport.addMessage("ATTENDANCE FAILURE: Student did not meet minimum attendance requirements.");
            return failReport;
        }

        // 2. Calculate Weighted Average
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (Evaluation eval : student.getEvaluations()) {
            weightedSum += eval.getScore() * (eval.getWeight() / 100.0);
            totalWeight += eval.getWeight();
        }

        GradeReport tempReport = new GradeReport(0.0); // mutable builder would be better but this works
        
        if (Math.abs(totalWeight - 100.0) > 0.01) {
            tempReport.addMessage("WARNING: Total weight is " + totalWeight + "%, not 100%.");
        }

        // 3. Apply Bonus
        double bonus = extraPointsPolicy.calculateBonus(student, allYearsTeachers);
        if (bonus > 0) {
            weightedSum += bonus;
            tempReport.addMessage("BONUS APPLIED: +" + bonus + " points.");
        } else {
             // RF05: "mencionar... puntos extra evitados"
             // If bonus was possible but not applied (e.g. teachers didn't agree)
             tempReport.addMessage("BONUS NOT APPLIED: Conditions not met.");
        }

        // 4. Cap at 20 (RNF01)
        if (weightedSum > 20.0) {
            weightedSum = 20.0;
            tempReport.addMessage("GRADE CAPPED: Calculated grade exceeded 20.");
        }

        // Create final report
        GradeReport finalReport = new GradeReport(weightedSum);
        for (String msg : tempReport.getMessages()) {
            finalReport.addMessage(msg);
        }
        
        return finalReport;
    }
}

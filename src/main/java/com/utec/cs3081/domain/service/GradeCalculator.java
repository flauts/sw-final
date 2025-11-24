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
        // 1. Check Attendance
        if (!attendancePolicy.isMet(student)) {
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

        GradeReport finalReport = new GradeReport(0.0);
        
        if (Math.abs(totalWeight - 100.0) > 0.01) {
            finalReport.addMessage("WARNING: Total weight is " + totalWeight + "%, not 100%.");
        }

        // 3. Apply Bonus
        double bonus = extraPointsPolicy.calculateBonus(student, allYearsTeachers);
        if (bonus > 0) {
            weightedSum += bonus;
            finalReport.addMessage("BONUS APPLIED: +" + bonus + " points.");
        } else {
             finalReport.addMessage("BONUS NOT APPLIED: Conditions not met.");
        }

        // 4. Cap at 20 (RNF01)
        if (weightedSum > 20.0) {
            weightedSum = 20.0;
            finalReport.addMessage("GRADE CAPPED: Calculated grade exceeded 20.");
        }

        return new GradeReport(weightedSum) {{
            for (String msg : finalReport.getMessages()) {
                addMessage(msg);
            }
        }};
    }
}

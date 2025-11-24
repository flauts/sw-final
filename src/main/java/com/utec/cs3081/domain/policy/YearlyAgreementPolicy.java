package com.utec.cs3081.domain.policy;

import com.utec.cs3081.domain.model.Student;
import java.util.List;

public class YearlyAgreementPolicy implements ExtraPointsPolicy {
    private static final double BONUS_POINTS = 1.0;

    @Override
    public double calculateBonus(Student student, List<Boolean> allYearsTeachers) {
        if (allYearsTeachers == null || allYearsTeachers.isEmpty()) {
            return 0.0;
        }
        // RF03: "si los docentes del curso están de acuerdo" -> implies ALL must agree?
        // Or just "if the list contains true"? The prompt says "Variable: allYearsTeachers con valores True/False".
        // Usually "agreement" implies consensus or majority.
        // Let's assume consensus (all must be true) for now, or maybe just checking if the policy is active for the year.
        // Re-reading RF03: "registrar... si los docentes... están de acuerdo".
        // And RF05: "puntos extra evitados".
        // Let's assume if ALL are true, then bonus. Or maybe it's a list of years?
        // "Variable: allYearsTeachers con valores True/False" -> List<Boolean>.
        // Let's assume if ALL elements in the list are true, then bonus is applied.
        // Wait, "para cada año académico". Maybe the list represents agreement for *this* student's year?
        // Let's assume the input list represents the agreement status for the relevant context.
        // If the list contains ANY false, then no agreement? Or is it a list of votes?
        // Let's go with: if the list is not empty and ALL are true, then bonus.
        
        boolean allAgreed = true;
        for (Boolean agreed : allYearsTeachers) {
            if (!Boolean.TRUE.equals(agreed)) {
                allAgreed = false;
                break;
            }
        }
        
        return allAgreed ? BONUS_POINTS : 0.0;
    }
}

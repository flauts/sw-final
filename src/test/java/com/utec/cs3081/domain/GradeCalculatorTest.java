package com.utec.cs3081.domain;

import com.utec.cs3081.domain.model.Evaluation;
import com.utec.cs3081.domain.model.GradeReport;
import com.utec.cs3081.domain.model.Student;
import com.utec.cs3081.domain.policy.StandardAttendancePolicy;
import com.utec.cs3081.domain.policy.YearlyAgreementPolicy;
import com.utec.cs3081.domain.service.GradeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    private GradeCalculator calculator;
    private Student student;

    @BeforeEach
    void setUp() {
        calculator = new GradeCalculator(new StandardAttendancePolicy(), new YearlyAgreementPolicy());
        student = new Student("S001");
        student.setHasReachedMinimumClasses(true); // Default to true
    }

    @Test
    void shouldReturnWeightedSum_WhenAttendanceMetAndNoBonus() {
        student.addEvaluation(new Evaluation("Exam 1", 15.0, 50.0));
        student.addEvaluation(new Evaluation("Exam 2", 10.0, 50.0));
        
        // 15 * 0.5 + 10 * 0.5 = 7.5 + 5.0 = 12.5
        GradeReport report = calculator.calculate(student, Collections.emptyList());
        
        assertEquals(12.5, report.getFinalGrade(), 0.01);
    }

    @Test
    void shouldCapGradeAt20_WhenSumExceeds20() {
        student.addEvaluation(new Evaluation("Perfect", 20.0, 100.0));
        // Bonus points
        List<Boolean> agreements = Arrays.asList(true, true);
        
        // 20 + 1 (bonus) = 21 -> Cap at 20
        GradeReport report = calculator.calculate(student, agreements);
        
        assertEquals(20.0, report.getFinalGrade(), 0.01);
        assertTrue(report.getMessages().contains("GRADE CAPPED: Calculated grade exceeded 20."));
    }

    @Test
    void shouldFail_WhenAttendanceNotMet() {
        student.setHasReachedMinimumClasses(false);
        student.addEvaluation(new Evaluation("Exam 1", 20.0, 100.0));
        
        GradeReport report = calculator.calculate(student, Collections.emptyList());
        
        assertEquals(0.0, report.getFinalGrade(), 0.01);
        assertTrue(report.getMessages().stream().anyMatch(m -> m.contains("ATTENDANCE FAILURE")));
    }

    @Test
    void shouldApplyBonus_WhenTeachersAgree() {
        student.addEvaluation(new Evaluation("Exam 1", 10.0, 100.0));
        List<Boolean> agreements = Arrays.asList(true, true, true);
        
        // 10 + 1 = 11
        GradeReport report = calculator.calculate(student, agreements);
        
        assertEquals(11.0, report.getFinalGrade(), 0.01);
        assertTrue(report.getMessages().stream().anyMatch(m -> m.contains("BONUS APPLIED")));
    }

    @Test
    void shouldNotApplyBonus_WhenTeachersDisagree() {
        student.addEvaluation(new Evaluation("Exam 1", 10.0, 100.0));
        List<Boolean> agreements = Arrays.asList(true, false, true);
        
        // 10 + 0 = 10
        GradeReport report = calculator.calculate(student, agreements);
        
        assertEquals(10.0, report.getFinalGrade(), 0.01);
        assertTrue(report.getMessages().stream().anyMatch(m -> m.contains("BONUS NOT APPLIED")));
    }
    
    @Test
    void shouldHandleZeroEvaluations() {
        GradeReport report = calculator.calculate(student, Collections.emptyList());
        assertEquals(0.0, report.getFinalGrade(), 0.01);
    }
    
    @Test
    void shouldThrowException_WhenWeightsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
             new Evaluation("Bad", 10.0, 150.0);
        });
    }
    
    @Test
    void shouldThrowException_WhenExceedingMaxEvaluations() {
        // Add 10 evaluations (maximum allowed)
        for (int i = 1; i <= 10; i++) {
            student.addEvaluation(new Evaluation("Eval " + i, 10.0, 10.0));
        }
        
        // Attempting to add the 11th should throw exception
        assertThrows(IllegalStateException.class, () -> {
            student.addEvaluation(new Evaluation("Eval 11", 10.0, 10.0));
        });
    }
}

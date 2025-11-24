package com.utec.cs3081.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student {
    private final String code;
    private final List<Evaluation> evaluations;
    private boolean hasReachedMinimumClasses;

    public Student(String code) {
        this.code = code;
        this.evaluations = new ArrayList<>();
        this.hasReachedMinimumClasses = false;
    }

    public void addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
    }

    public List<Evaluation> getEvaluations() {
        return Collections.unmodifiableList(evaluations);
    }

    public void setHasReachedMinimumClasses(boolean hasReachedMinimumClasses) {
        this.hasReachedMinimumClasses = hasReachedMinimumClasses;
    }

    public boolean hasReachedMinimumClasses() {
        return hasReachedMinimumClasses;
    }

    public String getCode() {
        return code;
    }
}

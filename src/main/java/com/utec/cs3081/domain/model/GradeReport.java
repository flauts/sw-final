package com.utec.cs3081.domain.model;

import java.util.ArrayList;
import java.util.List;

public class GradeReport {
    private final double finalGrade;
    private final List<String> messages;

    public GradeReport(double finalGrade) {
        this.finalGrade = finalGrade;
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}

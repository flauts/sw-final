package com.utec.cs3081.domain.model;

public class Evaluation {
    private final String name;
    private final double score;
    private final double weight; // Percentage (0-100)

    public Evaluation(String name, double score, double weight) {
        if (weight < 0 || weight > 100) {
            throw new IllegalArgumentException("Weight must be between 0 and 100");
        }
        if (score < 0 || score > 20) {
            throw new IllegalArgumentException("Score must be between 0 and 20");
        }
        this.name = name;
        this.score = score;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public double getWeight() {
        return weight;
    }
}

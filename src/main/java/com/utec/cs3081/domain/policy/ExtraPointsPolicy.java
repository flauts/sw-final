package com.utec.cs3081.domain.policy;

import com.utec.cs3081.domain.model.Student;
import java.util.List;

public interface ExtraPointsPolicy {
    double calculateBonus(Student student, List<Boolean> allYearsTeachers);
}

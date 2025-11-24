package com.utec.cs3081.domain.policy;

import com.utec.cs3081.domain.model.Student;

public class StandardAttendancePolicy implements AttendancePolicy {
    @Override
    public boolean isMet(Student student) {
        return student.hasReachedMinimumClasses();
    }
}

package com.utec.cs3081.domain.policy;

import com.utec.cs3081.domain.model.Student;

public interface AttendancePolicy {
    boolean isMet(Student student);
}

package com.buildit.hr.domain.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeIdentifierFactory {
    public String nextEmployeeID() {
        return UUID.randomUUID().toString();
    }
}



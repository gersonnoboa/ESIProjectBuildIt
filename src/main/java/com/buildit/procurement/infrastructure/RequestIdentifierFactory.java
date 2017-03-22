package com.buildit.procurement.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RequestIdentifierFactory {
    public String nextPlantHireRequestID() {
        return UUID.randomUUID().toString();
    }
}



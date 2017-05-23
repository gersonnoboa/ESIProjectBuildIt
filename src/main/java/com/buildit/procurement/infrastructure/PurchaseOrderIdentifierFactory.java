package com.buildit.procurement.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by gerson on 21/05/17.
 */

@Service
public class PurchaseOrderIdentifierFactory {
    public String nextPurchaseOrderID() {
        return UUID.randomUUID().toString();
    }
}

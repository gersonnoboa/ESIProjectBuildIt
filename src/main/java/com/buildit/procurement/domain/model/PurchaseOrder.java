package com.buildit.procurement.domain.model;

import com.buildit.common.ResourceSupport;
import com.buildit.common.domain.BusinessPeriod;
import com.buildit.invoicing.domain.model.*;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Oleksandr on 3/21/2017.
 */

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PurchaseOrder extends ResourceSupport{
    @Id
    String _id;

    @Enumerated(EnumType.STRING)
    POStatus orderStatus;

    @Column(precision = 8, scale = 2)
    BigDecimal total;

    public static PurchaseOrder of(String id, PlantInventoryEntry plant, BusinessPeriod rentalPeriod) {
        PurchaseOrder po = new PurchaseOrder();
        po._id = id;
        //po.plant = plant;
        //po.rentalPeriod = rentalPeriod;
        po.orderStatus = POStatus.PENDING;
        return po;
    }

    public void confirmReservation(BigDecimal price) {
        //total = price.multiply(BigDecimal.valueOf(rentalPeriod.numberOfWorkingDays()));
        orderStatus = POStatus.PENDING;
    }

    public void handleRejection() {
        orderStatus = POStatus.REJECTED;
    }

    public void handleClosure(){
        orderStatus = POStatus.CLOSED;
    }

    public void handleAcceptance() {
        orderStatus = POStatus.OPEN;
    }
}
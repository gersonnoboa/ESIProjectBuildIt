package com.buildit.procurement.domain.model;

import com.buildit.common.domain.BusinessPeriod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Oleksandr on 3/21/2017.
 */

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName="of")
public class PlantHireRequest {
    @Id
    String _id;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name="employee_href", column=@Column(name="siteEngineer"))})
    EmployeeId siteEngineer;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name="employee_href", column=@Column(name="worksEngineer"))})
    EmployeeId worksEngineer;

    @Embedded
    Comment comment;

    @Embedded
    ConstructionSite site;

    @Embedded
    BusinessPeriod rentalPeriod;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Embedded
    PlantInventoryEntry plant;

    @Embedded
    PurchaseOrder order;

    @Embedded
    PlantSupplier supplier;

    @Column(precision = 8, scale = 2)
    BigDecimal price;

    public void handleRejection() {
        status = POStatus.REJECTED;
    }

    public void handleClosure() {


        if (order.orderStatus == POStatus.PENDING){
            LocalDate now = LocalDate.now();

            int difference = rentalPeriod.getStartDate().getDayOfYear() - now.getDayOfYear();
            if (difference >= 1){
                status = POStatus.CLOSED;
            }
        }
        else{
            //request for cancellation
        }


    }

    public void handleAcceptance() {
        status = POStatus.OPEN;
    }

    public void handleUpdate(PlantHireRequestUpdate newPhr) throws Exception{

        if (status == POStatus.PENDING){
            this.comment = newPhr.getComment();
            this.rentalPeriod = newPhr.getRentalPeriod();
            this.plant = newPhr.getPlant();
        }
        else{
            throw new Exception("Plant hire request can't be edited anymore");
        }

    }

    public void handleExtension(PlantHireRequest newPhr) {
        this.rentalPeriod = newPhr.getRentalPeriod();
        this.order = newPhr.getOrder();
    }
}

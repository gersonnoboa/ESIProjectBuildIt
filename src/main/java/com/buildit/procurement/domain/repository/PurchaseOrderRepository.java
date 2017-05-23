package com.buildit.procurement.domain.repository;

import com.buildit.procurement.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gerson on 23/05/17.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,String> {
}

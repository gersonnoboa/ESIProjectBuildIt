package com.buildit.invoicing.domain.repository;

import com.buildit.hr.domain.model.Employee;
import com.buildit.invoicing.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gerson on 04/05/17.
 */

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
}

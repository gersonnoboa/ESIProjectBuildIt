package com.buildit.hr.domain.repository;

import com.buildit.hr.domain.model.Employee;
import com.buildit.procurement.domain.model.PlantHireRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by Oleksandr on 3/21/2017.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,String>{
}
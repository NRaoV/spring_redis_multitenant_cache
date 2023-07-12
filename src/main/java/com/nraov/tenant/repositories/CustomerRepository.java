package com.nraov.tenant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nraov.tenant.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}


package com.nraov.tenant.controllers;

import java.util.List;

import com.nraov.config.Constants;
import com.nraov.config.TenantContext;
import com.nraov.tenant.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nraov.tenant.services.CustomerService;

@RestController
@RequestMapping(value = "/tenant", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@GetMapping(value = "/customers")
	public ResponseEntity<Object> getAllCustomers() {
		//setTenantContext();
		List<Customer> customers = this.customerService.getAll();
		//clearTenantContext();
		return ResponseEntity.ok(customers);
	}

	@GetMapping(value = "/customers/{id}")
	public ResponseEntity<Object> getCustomerById(@PathVariable("id") String id) {
		//setTenantContext();
		Long _id = Long.valueOf(id);
		Customer customer = this.customerService.getCustomerById(_id);
		//clearTenantContext();
		return ResponseEntity.ok(customer);
	}

	@PostMapping(value = "/customers")
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
		//setTenantContext();
		Customer created = this.customerService.add(customer);
		//clearTenantContext();
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/customers")
	public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer) {
		//setTenantContext();
		Customer updated = this.customerService.update(customer);
		//clearTenantContext();
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping(value = "/customers/{id}")
	public ResponseEntity<Object> deleteCustomerById(@PathVariable("id") String id) {
		//setTenantContext();
		Long _id = Long.valueOf(id);
		this.customerService.delete(_id);
		//clearTenantContext();
		return ResponseEntity.ok().build();
	}

	private void setTenantContext() {
		ThreadLocal<String> tenantId = new ThreadLocal<>();
		tenantId.set(Constants.SUPER_ADMIN_TENANT + "_1");
		TenantContext.setTenant(tenantId);
	}

	private void clearTenantContext() {
		TenantContext.clear();
		log.info("Life Cycle End");
	}
}

package com.nraov.tenant.services;

import java.util.List;

import com.nraov.tenant.models.Customer;

public interface CustomerService {

	public List<Customer> getAll();

	public Customer add(Customer customer);

	public Customer update(Customer customer);

	public void delete(long id);

	public Customer getCustomerById(long id);
}

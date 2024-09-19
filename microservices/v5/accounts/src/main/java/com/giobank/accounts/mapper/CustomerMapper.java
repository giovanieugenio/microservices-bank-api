package com.giobank.accounts.mapper;

import com.giobank.accounts.dto.CustomerDto;
import com.giobank.accounts.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public CustomerDto toCustomerDto(Customer customer, CustomerDto dto){
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setMobileNumber(customer.getMobileNumber());
        return dto;
    }

    public Customer toCustomer(CustomerDto dto, Customer customer){
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setMobileNumber(dto.getMobileNumber());
        return customer;
    }
}

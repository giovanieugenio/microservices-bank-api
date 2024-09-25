package com.giobank.accounts.service.impl;

import com.giobank.accounts.dto.AccountsDto;
import com.giobank.accounts.dto.CardsDto;
import com.giobank.accounts.dto.CustomerDetailsDto;
import com.giobank.accounts.dto.LoansDto;
import com.giobank.accounts.entity.Accounts;
import com.giobank.accounts.entity.Customer;
import com.giobank.accounts.exception.ResourceNotFoundException;
import com.giobank.accounts.mapper.AccountsMapper;
import com.giobank.accounts.mapper.CustomerMapper;
import com.giobank.accounts.repository.AccountsRepository;
import com.giobank.accounts.repository.CustomerRepository;
import com.giobank.accounts.service.client.CardsFeignClient;
import com.giobank.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService{

    private AccountsRepository accountsRepository;

    private  CustomerRepository customerRepository;

    private CustomerMapper customerMapper;

    private AccountsMapper accountsMapper;

    @Qualifier("com.giobank.accounts.service.client.CardsFeignClient")
    private CardsFeignClient cardsFeignClient;

    @Qualifier("com.giobank.accounts.service.client.LoansFeignClient")
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto dto = customerMapper.toCustomerDetailsDto(
                customer, new CustomerDetailsDto());
        dto.setAccountsDto(accountsMapper.toAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDto = loansFeignClient.fetchLoanDetails(mobileNumber, correlationId);
        if (loansDto != null){
            dto.setLoansDto(loansDto.getBody());
        }

        ResponseEntity<CardsDto> cardsDto = cardsFeignClient.fetchCardDetails(mobileNumber, correlationId);
        if (cardsDto != null){
            dto.setCardsDto(cardsDto.getBody());
        }

        return dto;
    }
}

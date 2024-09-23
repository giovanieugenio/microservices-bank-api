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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService{

    private final AccountsRepository accountsRepository;

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final AccountsMapper accountsMapper;

    private final CardsFeignClient cardsFeignClient;

    private final LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto dto = customerMapper.toCustomerDetailsDto(
                customer, new CustomerDetailsDto());
        dto.setAccountsDto(accountsMapper.toAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDto = loansFeignClient.fetchLoanDetails(mobileNumber);
        dto.setLoansDto(loansDto.getBody());

        ResponseEntity<CardsDto> cardsDto = cardsFeignClient.fetchCardDetails(mobileNumber);
        dto.setCardsDto(cardsDto.getBody());

        return dto;
    }
}

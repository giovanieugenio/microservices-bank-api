package com.giobank.accounts.service.impl;

import com.giobank.accounts.constants.AccountsConstants;
import com.giobank.accounts.dto.AccountsDto;
import com.giobank.accounts.dto.AccountsMsgDto;
import com.giobank.accounts.dto.CustomerDto;
import com.giobank.accounts.entity.Accounts;
import com.giobank.accounts.entity.Customer;
import com.giobank.accounts.exception.CustomerAlreadyExistsException;
import com.giobank.accounts.exception.ResourceNotFoundException;
import com.giobank.accounts.mapper.AccountsMapper;
import com.giobank.accounts.mapper.CustomerMapper;
import com.giobank.accounts.repository.AccountsRepository;
import com.giobank.accounts.repository.CustomerRepository;
import com.giobank.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountsServiceImpl implements IAccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final AccountsRepository accountsRepository;

    private final CustomerRepository customerRepository;

    private final AccountsMapper accountsMapper;

    private final CustomerMapper customerMapper;

    private final StreamBridge streamBridge;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer =  customerMapper.toCustomer(customerDto, new Customer());
        Optional<Customer> existMobileNumber =
                customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (existMobileNumber.isPresent()){
            throw new CustomerAlreadyExistsException("Mobile number already registered");
        }
        Customer savedCustomer = customerRepository.save(customer);
        Accounts savedAccount = accountsRepository.save(generateNewAccount(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);
    }

    private Accounts generateNewAccount(Customer customer){
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 100000L + new Random().nextInt(900000);

        accounts.setAccountNumber(randomAccNumber);
        accounts.setAccountType(AccountsConstants.SAVINGS);
        accounts.setBranchAddress(AccountsConstants.ADDRESS);

        return accounts;
    }

    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = customerMapper.toCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(accountsMapper.toAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    ()-> new ResourceNotFoundException("Account", "accountNumber", accountsDto.getAccountNumber().toString())
            );
            accountsMapper.toAccounts(accountsDto, accounts);
            var updatedAccount = accountsRepository.save(accounts);

            Long customerId = updatedAccount.getCustomerId();
            var customer = customerRepository.findById(customerId).orElseThrow(
                    ()-> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
            );
            customerMapper.toCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdate = false;
        if (accountNumber != null){
            var accounts = accountsRepository.findById(accountNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Account", "accountsNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdate = true;
        }
        return isUpdate;
    }

    private void sendCommunication(Accounts accounts, Customer customer){
        var accountsMsgDto = new AccountsMsgDto(
                accounts.getAccountNumber(),
                customer.getName(),
                customer.getEmail(),
                customer.getMobileNumber()
        );
        log.info("Sending communication request for the details {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the communication request successfully processed ?: {}", result);
    }
}

package com.giobank.accounts.mapper;

import com.giobank.accounts.dto.AccountsDto;
import com.giobank.accounts.entity.Accounts;
import org.springframework.stereotype.Service;

@Service
public class AccountsMapper {

    public AccountsDto toAccountsDto(Accounts accounts, AccountsDto dto){
        dto.setAccountNumber(accounts.getAccountNumber());
        dto.setAccountType(accounts.getAccountType());
        dto.setBranchAddress(accounts.getBranchAddress());
        return dto;
    }

    public Accounts toAccounts(AccountsDto dto, Accounts accounts){
        accounts.setAccountNumber(dto.getAccountNumber());
        accounts.setAccountType(dto.getAccountType());
        accounts.setBranchAddress(dto.getBranchAddress());
        return accounts;
    }
}

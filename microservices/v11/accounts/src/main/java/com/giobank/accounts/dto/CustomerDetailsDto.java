package com.giobank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Schema(
        name = "Customer Details",
        description = "schema to hold customer, account, cards and loans information"
)
@Data
public class CustomerDetailsDto {

    @Schema(description = "Name of the customer", example = "Giovani Eugenio")
    @NotEmpty(message = "Name cannot be empty")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")
    private String name;

    @Schema(description = "Email address of the customer", example = "giovani@gmail.com")
    @NotEmpty(message = "Name cannot be empty")
    @Email(message = "Email must be in a valid format")
    private String email;

    @Schema(description = "Mobile number of the customer", example = "9752310452")
    @NotEmpty(message = "Mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile must 10 digits")
    private String mobileNumber;

    @Schema(description = "Accounts details of the customer")
    private AccountsDto accountsDto;

    @Schema(description = "Cards details of the customer")
    private CardsDto cardsDto;

    @Schema(description = "Loans details of the customer")
    private LoansDto loansDto;
}

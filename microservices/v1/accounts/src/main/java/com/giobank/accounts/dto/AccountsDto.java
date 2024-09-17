package com.giobank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Accounts",
        description = "schema to hold account information"
)
@Data
public class AccountsDto {

    @Schema(description = "Account number of the customer")
    @NotEmpty(message = "Account number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must 10 digits")
    private Long accountNumber;

    @Schema(description = "Account type of the customer", example = "Current")
    @NotEmpty(message = "Account type cannot be empty")
    private String accountType;

    @Schema(description = "Address of the customer")
    @NotEmpty(message = "Address cannot be empty")
    private String branchAddress;
}

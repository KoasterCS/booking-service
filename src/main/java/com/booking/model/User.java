package com.booking.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    @NotEmpty(message = "firstName may not be null")
    private String firstName;
    @NotEmpty(message = "lastName may not be null")
    private String lastName;
    @NotEmpty(message = "email may not be null")
    private String email;
}

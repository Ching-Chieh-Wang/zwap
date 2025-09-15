package com.zwap.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterQry implements Serializable {

    @NotBlank(message = "ID token is required")
    private String idToken;

    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    @Pattern(regexp = "^(https?://).*$", message = "Photo URL must be a valid URL")
    private String photoUrl;
}

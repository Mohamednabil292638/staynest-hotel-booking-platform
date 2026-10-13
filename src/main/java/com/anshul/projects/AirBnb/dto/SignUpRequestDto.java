/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Register a new guest account")
public class SignUpRequestDto {

    @Schema(example = "newuser@example.com")
    private String email;

    @Schema(example = "SecurePass@123")
    private String password;

    @Schema(example = "Anshul Negi")
    private String name;
}

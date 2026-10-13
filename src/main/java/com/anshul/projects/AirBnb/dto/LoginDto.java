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
@Schema(description = "Login with demo guest or manager account")
public class LoginDto {

    @Schema(description = "Email", example = "guest@demo.com")
    private String email;

    @Schema(description = "Password", example = "Demo@123")
    private String password;
}

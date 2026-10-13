/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service;

import com.anshul.projects.AirBnb.dto.LoginDto;
import com.anshul.projects.AirBnb.dto.SignUpRequestDto;
import com.anshul.projects.AirBnb.dto.UserDto;

public interface AuthService {

    public UserDto signUp(SignUpRequestDto signUpRequestDto);

    public String[] login(LoginDto loginDto);

    public String refreshToken(String refreshToken);

    public void logout(String refreshToken);
}

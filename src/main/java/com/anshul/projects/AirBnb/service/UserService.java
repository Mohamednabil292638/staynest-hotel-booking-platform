/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service;

import com.anshul.projects.AirBnb.dto.UserProfileUpdateRequestDto;
import com.anshul.projects.AirBnb.entity.User;

public interface UserService {


    User getUserById(Long userId);

    void updateProfile(UserProfileUpdateRequestDto profileUpdateRequestDto);
}

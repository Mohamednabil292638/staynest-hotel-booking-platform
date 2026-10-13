/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import com.anshul.projects.AirBnb.entity.Enums.Gender;
import com.anshul.projects.AirBnb.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;
    private User userId;
    private String name;
    private Gender gender;
    private Integer age;


}

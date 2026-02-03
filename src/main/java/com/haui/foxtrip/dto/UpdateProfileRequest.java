package com.haui.foxtrip.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateProfileRequest {
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
}

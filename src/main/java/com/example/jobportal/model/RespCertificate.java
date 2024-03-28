package com.example.jobportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespCertificate {

    private String certificate;
    private String downloadURL;
    private long fileSize;
}

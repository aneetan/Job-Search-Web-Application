package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Certifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificateId;
    private String certification;

    @ManyToOne
    @JoinColumn(name="userId")
    private PersonalDetails details;

}

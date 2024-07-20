package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class additionalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addId;

    @Lob
    @Column(length = 100)
    private String bio;
    private String jobTitle;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private PersonalDetails personalDetails;

    public additionalDetails(){

    }

    public additionalDetails(String bio, String jobTitle) {
        this.bio = bio;
        this.jobTitle = jobTitle;
    }
}

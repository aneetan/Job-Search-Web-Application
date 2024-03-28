package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Certifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificateId;
    private String certTitle;
    private String certification;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name="userId")
    private PersonalDetails personalDetails;

    public Certifications(){

    }

    public Certifications( String certification, byte[] data) {
        this.certTitle = certTitle;
        this.certification = certification;
        this.data = data;
        this.personalDetails = personalDetails;
    }
}

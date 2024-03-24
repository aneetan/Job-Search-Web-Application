package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class userDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userDocId;
    private String cv;
    private String profile;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private PersonalDetails personalDetails;


}

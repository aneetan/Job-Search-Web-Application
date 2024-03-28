package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class userDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userDocId;

    private String cvName;

    private String profileName;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private PersonalDetails personalDetails;

    public userDocs(){

    }



    public userDocs(String cvName, String profileName) {
        this.cvName = cvName;
        this.profileName = profileName;
    }
}

package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Status{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;

    private String status;

    @OneToOne
    @JoinColumn(name = "companyId", referencedColumnName = "companyId")
    private Company company;

    public Status(){
    }

    public Status(String status) {
        this.status = status;
    }
}

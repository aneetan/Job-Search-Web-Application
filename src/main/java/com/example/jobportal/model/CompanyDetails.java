package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
public class CompanyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyDetailsId;

    private String companyAddress;
    private String industry;
    private String employeeNo;
    private String companyContact;
    private String companyUrl;

    @OneToOne
    @JoinColumn(name = "companyId", referencedColumnName = "companyId")
    private Company company;



    public CompanyDetails(){

    }
    public CompanyDetails(String companyAddress, String industry, String employeeNo, String companyContact, String companyUrl) {
        this.companyAddress = companyAddress;
        this.industry = industry;
        this.employeeNo = employeeNo;
        this.companyContact = companyContact;
        this.companyUrl = companyUrl;
    }
}

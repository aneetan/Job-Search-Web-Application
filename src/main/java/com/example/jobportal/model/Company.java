package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.engine.internal.Cascade;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Transactional
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyId;
    private String companyName;
    private String companyEmail;
    private String companyPassword;

//    @OneToOne(mappedBy = "company", cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "companyDetailsId")
//    private CompanyDetails companyDetails;
//
//    @OneToOne(mappedBy = "company", cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "companyDocId")
//    private CompanyDocs companyDocs;

    public Company(){

    }
    public Company(String companyName, String companyEmail, String companyPassword){
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPassword = companyPassword;
    }


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }

//    public CompanyDetails getCompanyDetails() {
//        return companyDetails;
//    }
//
//    public void setCompanyDetails(CompanyDetails companyDetails) {
//        this.companyDetails = companyDetails;
//    }
//
//    public CompanyDocs getCompanyDocs() {
//        return companyDocs;
//    }
//
//    public void setCompanyDocs(CompanyDocs companyDocs) {
//        this.companyDocs = companyDocs;
//    }
}

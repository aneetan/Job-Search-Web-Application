package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Entity
@Data
public class CompanyDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int docId;
    private String logoName;

    @OneToOne
    @JoinColumn(name = "companyId", referencedColumnName = "companyId")
    private  Company company;

    private String verifiedDocName;

    @OneToOne
    @JoinColumn(name = "companyDetailsId", referencedColumnName = "companyDetailsId")
    private  CompanyDetails companyDetails;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "companyDocs")
    private Set<JobDetails> jobDetails;

    public CompanyDocs(){

    }
    public CompanyDocs(String logoName, String verifiedDocName) {
        this.logoName = logoName;
        this.verifiedDocName = verifiedDocName;
    }



    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getVerifiedDocName() {
        return verifiedDocName;
    }

    public void setVerifiedDocName(String verifiedDocName) {
        this.verifiedDocName = verifiedDocName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


}

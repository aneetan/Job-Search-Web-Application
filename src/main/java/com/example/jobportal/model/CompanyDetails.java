package com.example.jobportal.model;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@Entity
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

    public int getCompanyDetailsId() {
        return companyDetailsId;
    }

    public void setCompanyDetailsId(int companyDetailsId) {
        this.companyDetailsId = companyDetailsId;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

package com.example.jobportal.controller;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.CompanyDetails;
import com.example.jobportal.model.CompanyDocs;
import com.example.jobportal.repository.CompanyDetailsRepository;
import com.example.jobportal.repository.CompanyRepository;
import com.example.jobportal.repository.companyDocsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class JobController {
    @Autowired
    private CompanyRepository cRepo;
    @Autowired
    private CompanyDetailsRepository cdRepo;
    @Autowired
    private companyDocsRepository docsRepo;

    @GetMapping("/")
    public String frontPage()
    {
        return "index.html";
    }

    @GetMapping("/adminLogin")
    public String adminLogin(){
        return "adminLogin.html";
    }

    @PostMapping("/companyLogin")
    public String companyLogin(@ModelAttribute Company c, HttpSession session, Model model){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashPw = encoder.encode(c.getCompanyPassword());

        Company company = cRepo.findByCompanyEmail(c.getCompanyEmail());
        System.out.println(hashPw);

        if (company != null && encoder.matches(c.getCompanyPassword(), company.getCompanyPassword())){
            session.setAttribute("activeCompany", c.getCompanyEmail());
            session.setMaxInactiveInterval(30);

            return "companylanding.html";
        } else {
            model.addAttribute("login", "Invalid username or password");
            return "adminLogin.html";
        }
    }

    @GetMapping("/adminRegister")
    public String companySignup(){
        return "adminRegister.html";
    }

    @PostMapping("/adminReg")
    public String companyRegister(@ModelAttribute("c") Company c, Model model){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Hash the password
        String hashedPassword = passwordEncoder.encode(c.getCompanyPassword());

        // Check if email already exists in the database
        if (cRepo.existsByCompanyEmail(c.getCompanyEmail())) {
            model.addAttribute("error", "Email already exists");
            return "adminRegister.html";
        }

        Company company = new Company(c.getCompanyName(), c.getCompanyEmail(), hashedPassword);
        cRepo.save(company);

        System.out.println(company);
        System.out.println(cRepo);

        model.addAttribute("Company", company);
        return "companyDetails.html";
    }

    @PostMapping("/companyDetails")
    public String goToCompanyDocs(@ModelAttribute CompanyDetails cd,@ModelAttribute("Company") Company company, Model model){
        CompanyDetails companyDetails = new CompanyDetails(cd.getCompanyAddress(), cd.getIndustry(), cd.getEmployeeNo(), cd.getCompanyContact(), cd.getCompanyUrl());

//        Company comp = (Company) model.getAttribute("Company");
        companyDetails.setCompany(company); // Set the Company reference
        System.out.println(company);
        System.out.println(companyDetails);

        model.addAttribute("Company", company );
        cdRepo.save(companyDetails);

        model.addAttribute("cd", companyDetails);
        return "companyDocs.html";
    }

    @PostMapping("/saveCompanyData")
    public String registerCompany(@ModelAttribute CompanyDocs doc, @ModelAttribute("Company") Company company, Model model){
        CompanyDocs companyDocs = new CompanyDocs(doc.getLogoName(), doc.getVerifiedDocName());
        companyDocs.setCompany(company); // Set the CompanyDetails reference

        docsRepo.save(companyDocs);

        model.addAttribute("login", "Please login to Continue");
        return "adminLogin.html";
    }


//    ------------------user login and registration -----------------------------------
    @GetMapping("/userLogin")
    public String login(){
        return "login.html";
    }

    @GetMapping("/goToUserRegistration")
    public String goToUserRegistration(){
        return "registration.html";
    }

    @PostMapping("/goToAdditional")
    public  String goToAdditional(){
        return "additional.html";
    }

    @PostMapping("/goTodocs")
    public String goToDocs(){
        return "docs.html";
    }




}

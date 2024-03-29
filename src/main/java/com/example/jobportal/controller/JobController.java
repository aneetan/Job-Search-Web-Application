package com.example.jobportal.controller;

import com.example.jobportal.model.*;
import com.example.jobportal.repository.*;
import com.example.jobportal.service.FileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
        company.setStatus("Pending");
        cRepo.save(company);

        model.addAttribute("Company", company);
        return "companyDetails.html";
    }

    @PostMapping("/companyDetails")
    public String goToCompanyDocs(@ModelAttribute CompanyDetails cd,@ModelAttribute("Company") Company company, Model model){
        CompanyDetails companyDetails = new CompanyDetails(cd.getCompanyAddress(), cd.getIndustry(), cd.getEmployeeNo(), cd.getCompanyContact(), cd.getCompanyUrl());

        companyDetails.setCompany(company);
        System.out.println(company);
        System.out.println(companyDetails);

        model.addAttribute("Company", company );
        cdRepo.save(companyDetails);

        model.addAttribute("cd", companyDetails);
        return "companyDocs.html";
    }

    @PostMapping("/saveCompanyData")
    public String registerCompany(@ModelAttribute CompanyDocs doc, @ModelAttribute("Company") Company company,@ModelAttribute("cd") CompanyDetails cd, Model model,
                                  @RequestParam("logo") MultipartFile logo, @RequestParam("docName") MultipartFile docName ) throws IOException {

        String logoName = logo.getOriginalFilename();
        fileService.saveFile(logo, logoName);

        String verfiedDocName = docName.getOriginalFilename();
        fileService.saveFile(docName, verfiedDocName);

        CompanyDocs companyDocs = new CompanyDocs(logoName, verfiedDocName);
        companyDocs.setCompany(company);
        companyDocs.setCompanyDetails(cd);

        docsRepo.save(companyDocs);

        model.addAttribute("login", "Please login to Continue");
        return "adminLogin.html";
    }


//    ------------------user login and registration -----------------------------------
    @Autowired
    private PersonalDetailsRepository pRepo;

    @Autowired
    private AdditionalDetailsRepository addRepo;
    @Autowired
    private FileRepository uRepo;
    @GetMapping("/userLogin")
    public String login(){
        return "login.html";
    }

    @GetMapping("/goToUserRegistration")
    public String goToUserRegistration(){
        return "registration.html";
    }

    @PostMapping("/goToAdditional")
    public  String goToAdditional(@ModelAttribute("personalDetails") PersonalDetails details, Model model){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(details.getPassword());

        // Check if email already exists in the database
        if (pRepo.existsByEmail(details.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "registration.html";
        }

        PersonalDetails pd = new PersonalDetails(details.getEmail(), details.getName(), hashedPassword, details.getAddress(), details.getPhoneNo());
        pRepo.save(pd);

        model.addAttribute("pDetails", pd);
        return "additional.html";
    }


    @PostMapping("/goTodocs")
    public String goToDocs(@ModelAttribute additionalDetails add, @ModelAttribute("pDetails") PersonalDetails details, Model model){
        additionalDetails addDetails = new additionalDetails(add.getBio(), add.getJobTitle());
        addDetails.setPersonalDetails(details);

        model.addAttribute("pDetails", details);
        addRepo.save(addDetails);

        model.addAttribute("add", addDetails);

        return "docs.html";
    }

    @Autowired
    private EducationRepository eduRepo;

    @PostMapping(value = "/submitEducation", produces = "text/plain", consumes = "application/json")
    @ResponseBody
    public String submitEducation(@RequestBody Education formData, Model model) {
        Education education = new Education(formData.getSchool(), formData.getDegree(), formData.getField(), formData.getGrade(), formData.getStartDate(), formData.getEndDate());
        education.setPersonalDetails(formData.getPersonalDetails());
        eduRepo.save(education);

        return "additional.html";
    }

    @Autowired
    private ExperienceRepository exRepo;

    @PostMapping(value = "/submitExperience", produces = "text/plain", consumes = "application/json")
    @ResponseBody
    public Experience submitExperience(@RequestBody Experience formData, Model model) {
        // Save the experience data
        Experience experience = new Experience(formData.getTitle(), formData.getCompany(), formData.getEmpType(), formData.getLocationType(), formData.getStartDateEx(), formData.getEndDateEx());
        experience.setPersonalDetails(formData.getPersonalDetails());
        exRepo.save(experience);

        List<Experience> experienceList = new ArrayList<>(); // Assuming findAll() retrieves all experiences
        experienceList.add(experience);
        return formData;
    }

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fRepo;

    @PostMapping("/userRegister")
    public String registerUser( @ModelAttribute userDocs u,  @ModelAttribute("pDetails") PersonalDetails details,  @RequestParam("cv") MultipartFile cv,
                                @RequestParam("profile") MultipartFile profile, Model model) throws IOException {

        String cvFileName = cv.getOriginalFilename();
        fileService.saveFile(cv, cvFileName);

        String profileFileName = profile.getOriginalFilename();
        fileService.saveFile(profile, profileFileName);

        System.out.println(details);
        userDocs docs = new userDocs(cvFileName, profileFileName);
        docs.setPersonalDetails(details);

        fRepo.save(docs);

        model.addAttribute("login", "Please login to continue");

        // Redirect to success page
        return "login.html";
    }


    @PostMapping("/loginUser")
    public String userLogin(@ModelAttribute PersonalDetails p, @ModelAttribute CompanyDetails cd, HttpSession session, Model model, @ModelAttribute CompanyDocs d){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashPw = encoder.encode(p.getPassword());

        String adminEmail = "admin@gmail.com";
        String adminPw = encoder.encode("admin@123");

        if (p.getEmail().equals(adminEmail) && encoder.matches(p.getPassword(), adminPw)) {
            session.setAttribute("activeUser", adminEmail);
            session.setMaxInactiveInterval(30);


            model.addAttribute("dataList", docsRepo.findByCompany_Status("Pending"));

            return "superAdmin.html";

        } else {
            PersonalDetails user = pRepo.findByEmail(p.getEmail());
            if (user != null && encoder.matches(p.getPassword(), user.getPassword())){
                session.setAttribute("activeUser", p.getEmail());
                session.setMaxInactiveInterval(30);

                return "userLanding.html";
            } else {
                model.addAttribute("login", "Invalid username or password");
                return "login.html";
            }
        }
    }



    @GetMapping("/approvedComp")
    public String approvedCompanies(Model model, @ModelAttribute CompanyDocs doc){

        model.addAttribute("dataList", docsRepo.findByCompany_Status("Approved"));

        return "approvedComp.html";
    }

    @GetMapping("/superAdmin")
    public String superAdmin(Model model){
        model.addAttribute("dataList", docsRepo.findAll());

        return "superAdmin.html";
    }


    @GetMapping("/approve/{id}")
    public String approveComp(@PathVariable("id") int id, Model model, HttpSession session){
        if(session.getAttribute("activeUser") == null) {
            session.setAttribute("login", "Please login first");
            return "login.html";
        }

        Company company = cRepo.getById(id);
        company.setStatus("Approved");

        cRepo.save(company);


        model.addAttribute("dataList", docsRepo.findAll());

        return "superAdmin.html";
    }

    @GetMapping("/decline/{id}")
    public String declineComp(@PathVariable("id") int id, Model model, HttpSession session){
        if(session.getAttribute("activeUser") == null) {
            session.setAttribute("login", "Please login first");
            return "login.html";
        }

        Company company = cRepo.getById(id);
        company.setStatus("Declined");

        cRepo.save(company);


        model.addAttribute("dataList", docsRepo.findAll());
        return "superAdmin.html";
    }





}

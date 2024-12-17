package com.springboot.jobapp.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        List<Company> allCompanies = companyService.getAllCompanies();
        if (allCompanies.size() >= 1) {
            return new ResponseEntity<>(allCompanies, HttpStatus.OK);
        }
        return new ResponseEntity<>("No companies available!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if (company != null) {
            return new ResponseEntity<>(company, HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found!", HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> addCompany(@RequestBody Company company) {
        Company companyCreated = companyService.createCompany(company);
        if (companyCreated != null) {
            return new ResponseEntity<>(companyCreated, HttpStatus.CREATED);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@RequestBody Company company, @PathVariable Long id) {
        Company updatedCompany = companyService.updateCompany(company, id);
        if (updatedCompany != null) {
            return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
        }
        return new ResponseEntity<>("Company with id " + id + " not found!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompanyById(@PathVariable Long id) {
        Company deletedCompany = companyService.deleteCompany(id);
        if (deletedCompany != null) {
            return new ResponseEntity<>(deletedCompany, HttpStatus.OK); // Return the deleted company
        }
        return new ResponseEntity<>("Company not found!", HttpStatus.NOT_FOUND); // Return the error message
    }

}

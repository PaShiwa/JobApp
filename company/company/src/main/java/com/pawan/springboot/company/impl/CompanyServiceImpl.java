package com.pawan.springboot.company.impl;

import com.pawan.springboot.company.Company;
import com.pawan.springboot.company.CompanyRepository;
import com.pawan.springboot.company.CompanyService;
import com.pawan.springboot.company.clients.ReviewClient;
import com.pawan.springboot.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }


    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Company company, Long id) {
        Optional<Company> existingCompany = companyRepository.findById(id);
        if (existingCompany.isPresent()) {
            Company updatedCompany = existingCompany.get();
            updatedCompany.setName(company.getName());
            updatedCompany.setDescription(company.getDescription());
            companyRepository.save(updatedCompany);
            return updatedCompany;
        }
        return null;
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        Long companyId = reviewMessage.getCompanyId();
        if(!companyRepository.existsById(companyId)){
            return;
        }
        Company company = companyRepository.findById(reviewMessage.getCompanyId())
                .orElseThrow(()-> new NotFoundException("Company Not Found for " + reviewMessage.getCompanyId()));
        Double averageRating = reviewClient.getAverageRating(reviewMessage.getCompanyId());
        company.setAvgRating(averageRating);
        companyRepository.save(company);
    }


    @Override
    public Company deleteCompany(Long id) {
        Optional<Company> existingCompany = companyRepository.findById(id);
        if (existingCompany.isPresent()) {
            companyRepository.deleteById(id);
            return existingCompany.get();
        }
        return null;
    }

}

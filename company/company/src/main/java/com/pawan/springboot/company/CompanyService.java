package com.pawan.springboot.company;

import com.pawan.springboot.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();

    Company updateCompany(Company company, Long id);

    Company createCompany(Company company);

    Company deleteCompany(Long id);

    Company getCompanyById(Long id);

    public void updateCompanyRating(ReviewMessage reviewMessage);
}

package com.pawan.springboot.job.mapper;

import com.pawan.springboot.job.Job;
import com.pawan.springboot.job.dto.JobDTO;
import com.pawan.springboot.job.ext.Company;
import com.pawan.springboot.job.ext.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDTO(Job job, Company company, List<Review> reviewList){
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMinSalary());
        jobDTO.setMinSalary(job.getMaxSalary());
        jobDTO.setCompany(company);
        jobDTO.setReviews(reviewList);
        return jobDTO;
    }
}

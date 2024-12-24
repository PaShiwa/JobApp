package com.pawan.springboot.job.impl;

import com.pawan.springboot.job.Job;
import com.pawan.springboot.job.JobRepository;
import com.pawan.springboot.job.JobService;
import com.pawan.springboot.job.dto.JobDTO;
import com.pawan.springboot.job.ext.Company;
import com.pawan.springboot.job.ext.Review;
import com.pawan.springboot.job.feignClients.CompanyClient;
import com.pawan.springboot.job.feignClients.ReviewClient;
import com.pawan.springboot.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private JobRepository jobRepository;
    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    int attempt =0;
    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient=companyClient;
        this.reviewClient=reviewClient;
    }

    @Override
    //@CircuitBreaker(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    //Retry(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt: "+  ++attempt);
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS;
        jobDTOS =jobs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return jobDTOS;
    }

    public List<JobDTO> companyBreakerFallback(Exception e) {
        List<JobDTO> dummyJobs = new ArrayList<>();
        JobDTO dummyJob = new JobDTO();
        dummyJob.setId(-1L); // Dummy ID
        dummyJob.setTitle("Fallback Job"); // Dummy Job Name
        dummyJobs.add(dummyJob);
        return dummyJobs;
    }


    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return convertToDTO(job);
    }

    @Override
    public Job addJob(Job job) {
        job.setId(null);
        Job savedJob = jobRepository.save(job);
        return savedJob;
    }

    @Override
    public Job deleteJobById(Long id) {
        Optional<Job> deletedJob = jobRepository.findById(id);
        if (deletedJob.isPresent()) {
            Job jobDeleted = deletedJob.get();
            jobRepository.deleteById(id);
            return jobDeleted;
        }
        return null;
    }

    @Override
    public Job updateJob(Long id, Job job) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job existingJob = jobOptional.get();
            existingJob.setTitle(job.getTitle());
            existingJob.setDescription(job.getDescription());
            existingJob.setMinSalary(job.getMinSalary());
            existingJob.setMaxSalary(job.getMaxSalary());
            existingJob.setLocation(job.getLocation());
            jobRepository.save(existingJob);
            return existingJob;
        }
        return null;
    }

    private JobDTO convertToDTO(Job job) {
        //Company company= restTemplate.getForObject("http://COMPANY-SERVICE/companies/" + job.getCompanyId(), Company.class);

        Company company = companyClient.getCompany(job.getCompanyId());

//        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://REVIEW-SERVICE/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                });
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

        return JobMapper.mapToJobWithCompanyDTO(job,company,reviews);
    }
}

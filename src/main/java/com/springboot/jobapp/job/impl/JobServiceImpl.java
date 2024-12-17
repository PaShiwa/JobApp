package com.springboot.jobapp.job.impl;

import com.springboot.jobapp.job.Job;
import com.springboot.jobapp.job.JobRepository;
import com.springboot.jobapp.job.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    private JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public Job createJob(Job job) {
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
}

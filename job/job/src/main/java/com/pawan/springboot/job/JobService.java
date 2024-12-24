package com.pawan.springboot.job;

import com.pawan.springboot.job.dto.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> findAll();

    Job addJob(Job job);

    JobDTO getJobById(Long id);

    Job deleteJobById(Long id);

    Job updateJob(Long id, Job job);
}

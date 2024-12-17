package com.springboot.jobapp.job;

import java.util.List;

public interface JobService {

    List<Job> findAll();

    Job createJob(Job job);

    Job getJobById(Long id);

    Job deleteJobById(Long id);

    Job updateJob(Long id, Job job);
}

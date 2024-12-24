package com.pawan.springboot.job;

import com.pawan.springboot.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<JobDTO> jobs = jobService.findAll();
        if (jobs.size() >= 1) {
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        }
        return new ResponseEntity<>("Empty Job List.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        JobDTO job = jobService.getJobById(id);
        if (job != null) {
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        return new ResponseEntity<>("Job not found!", HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> addJob(@RequestBody Job job) {
        Job jobCreated = jobService.addJob(job);
        if (jobCreated != null) {
            return new ResponseEntity<>(jobCreated, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Create Company First!", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJobByID(@PathVariable Long id) {
        Job job = jobService.deleteJobById(id);
        if (job != null) {
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        return new ResponseEntity<>("No job found!", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJobById(@PathVariable Long id, @RequestBody Job job) {
        Job jobUpdated = jobService.updateJob(id, job);
        if (jobUpdated != null) {
            return new ResponseEntity<>(jobUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>("No job found!", HttpStatus.NOT_FOUND);
    }
}

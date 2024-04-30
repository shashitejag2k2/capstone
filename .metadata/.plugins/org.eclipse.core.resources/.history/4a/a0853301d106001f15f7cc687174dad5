package com.hv.jobhunt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hv.jobhunt.Models.JobSeeker;

public interface JobSeekerRepo extends JpaRepository<JobSeeker, Integer> {
	
	JobSeeker findByJobSeekerId(int id);
	
	JobSeeker findByEmailId(String emailId);
	
	JobSeeker findByEmailIdAndPassword(String emailId,String Password);
	boolean existsByEmailId(String emailId);

}

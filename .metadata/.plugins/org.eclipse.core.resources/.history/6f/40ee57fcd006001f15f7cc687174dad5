package com.hv.jobhunt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.JobListing;

@Repository
public interface JobRepository extends JpaRepository<JobListing, Integer> {
	
	List<JobListing> findByMinimumSalaryGreaterThanEqual(double highpackage);
	List<JobListing> findByPostedBy(String email);
	JobListing deleteByjobId(int jobId);
	List<JobListing> findByJobTitleContaining(String keyword);
	
	JobListing findByjobId(int jobId);
	List<JobListing> findByJobTitleContainingIgnoreCase(String searchKeyword);
	boolean existsByJobId(int jobId);
}

package com.hv.jobhunt.Models;


import javax.persistence.*;


@Entity
@Table(name = "joblisting")
public class JobListing {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
	    private int jobId;
	    @Column(name = "job_title")
	    private String jobTitle;
	    
	    
	    
	    @Column(name = "employee_type")
	    private String employeeType;
	    
	    @Column(name = "job_description")
	    private String jobDescription;
	    
	    @Column(name = "key_skills")
	    private String keySkills;
	    
	    @Column(name = "minimum_work_experience")
	    private int minimumWorkExperience;
	    
	    @Column(name = "maximum_work_experience")
	    private int maximumWorkExperience;
	    
	    @Column(name = "location")
	    private String location;
	    
	    @Column(name = "job_mode")
	    private String jobMode;
	    
	    @Column(name = "educational_qualification")
	    private String educationalQualification;
	    
	    @Column(name = "company_name")
	    private String companyName;
	    
	    @Column(name = "minimum_salary")
	    private double minimumSalary;
	    
	    @Column(name = "maximum_salary")
	    private double maximumSalary;
	    
	    @Column(name = "posted_by")
	    private String postedBy;
	    
	    @Column(name = "job_application_status")
	    private String jobApplicationStatus;

	    public String getJobApplicationStatus() {
			return jobApplicationStatus;
		}

		public void setJobApplicationStatus(String jobApplicationStatus) {
			this.jobApplicationStatus = jobApplicationStatus;
		}

		public String getPostedBy() {
			return postedBy;
		}

		public void setPostedBy(String postedBy) {
			this.postedBy = postedBy;
		}

		// Constructor
	    public JobListing() {
	    }

	    // Getters and Setters
	    public int getJobId() {
	        return jobId;
	    }

	    public void setJobId(int jobId) {
	        this.jobId = jobId;
	    }

	    public String getJobTitle() {
	        return jobTitle;
	    }

	    public void setJobTitle(String jobTitle) {
	        this.jobTitle = jobTitle;
	    }

	    

	    public String getEmployeeType() {
	        return employeeType;
	    }

	    public void setEmployeeType(String employeeType) {
	        this.employeeType = employeeType;
	    }

	    public String getJobDescription() {
	        return jobDescription;
	    }

	    public void setJobDescription(String jobDescription) {
	        this.jobDescription = jobDescription;
	    }

	    public String getKeySkills() {
	        return keySkills;
	    }

	    public void setKeySkills(String keySkills) {
	        this.keySkills = keySkills;
	    }

	    public int getMinimumWorkExperience() {
	        return minimumWorkExperience;
	    }

	    public void setMinimumWorkExperience(int minimumWorkExperience) {
	        this.minimumWorkExperience = minimumWorkExperience;
	    }

	    public int getMaximumWorkExperience() {
	        return maximumWorkExperience;
	    }

	    public void setMaximumWorkExperience(int maximumWorkExperience) {
	        this.maximumWorkExperience = maximumWorkExperience;
	    }

	    public String getLocation() {
	        return location;
	    }

	    public void setLocation(String location) {
	        this.location = location;
	    }

	    public String getJobMode() {
	        return jobMode;
	    }

	    public void setJobMode(String jobMode) {
	        this.jobMode = jobMode;
	    }

	    public String getEducationalQualification() {
	        return educationalQualification;
	    }

	    public void setEducationalQualification(String educationalQualification) {
	        this.educationalQualification = educationalQualification;
	    }

	    public String getCompanyName() {
	        return companyName;
	    }

	    public void setCompanyName(String companyName) {
	        this.companyName = companyName;
	    }

	    public double getMinimumSalary() {
	        return minimumSalary;
	    }

	    public void setMinimumSalary(double minimumSalary) {
	        this.minimumSalary = minimumSalary;
	    }

	    public double getMaximumSalary() {
	        return maximumSalary;
	    }

	    public void setMaximumSalary(double maximumSalary) {
	        this.maximumSalary = maximumSalary;
	    }

	    // toString method to represent the object as a string
	    @Override
	    public String toString() {
	        return "JobListing{" +
	                "jobId=" + jobId +
	                ", jobTitle='" + jobTitle + '\'' +
	               
	                ", employeeType='" + employeeType + '\'' +
	                ", jobDescription='" + jobDescription + '\'' +
	                ", keySkills='" + keySkills + '\'' +
	                ", minimumWorkExperience=" + minimumWorkExperience +
	                ", maximumWorkExperience=" + maximumWorkExperience +
	                ", location='" + location + '\'' +
	                ", jobMode='" + jobMode + '\'' +
	                ", educationalQualification='" + educationalQualification + '\'' +
	                ", companyName='" + companyName + '\'' +
	                ", minimumSalary=" + minimumSalary +
	                ", maximumSalary=" + maximumSalary +
	                '}';
	    }
	}




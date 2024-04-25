package com.hv.jobhunt.Models;

import java.util.Date;



import javax.persistence.*;

@Entity
@Table(name = "employeer")
public class Employeer {

    @Id
    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "name")
    private String name;

    @Column(name = "email_id")
    private String emailId; 

    @Column(name = "password")
    private String password;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "subscription_type")
    private String subscriptionType;
    
    @Column(name = "subscription_expriration_date")
    private Date subscriptionExprirationDate;
    
    @Column(name="status")
    private String status;

   

    public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public int getEmployeeId() {
		return employeeId;
	}



	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmailId() {
		return emailId;
	}



	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getCompanyName() {
		return companyName;
	}



	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}



	public String getSubscriptionType() {
		return subscriptionType;
	}



	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}



	public Date getSubscriptionExprirationDate() {
		return subscriptionExprirationDate;
	}



	public void setSubscriptionExprirationDate(Date subscriptionExprirationDate) {
		this.subscriptionExprirationDate = subscriptionExprirationDate;
	}



	public Employeer() {
    }

}
    
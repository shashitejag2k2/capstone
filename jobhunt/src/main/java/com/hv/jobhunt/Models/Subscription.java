package com.hv.jobhunt.Models;



import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "subscription_table")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subscription_type")
    private String subscriptionType;

    @Column(name = "no_of_jobs")
    private int numberOfJobs;


    @Column(name = "price")
    private double price;

    

    public Subscription() {
    }

   

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(int numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", numberOfJobs=" + numberOfJobs +
               
                ", price=" + price +
                '}';
    }





	
}

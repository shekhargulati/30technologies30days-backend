package com.shekhar.challenge.domain;

import java.util.Date;

public class Feedback {
    
    private String id;
    private String name;
    private String description;
    private Date receivedOn = new Date();
    
    public Feedback() {
    }
    
    

    public Feedback(String id, String name, String description, Date receivedOn) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.receivedOn = receivedOn;
    }



    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }
    
    

}

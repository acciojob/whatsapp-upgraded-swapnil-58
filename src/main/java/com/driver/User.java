package com.driver;

import org.springframework.beans.factory.annotation.Autowired;

public class User {
    private String name;
    private String mobile;
    @Autowired
    WhatsappRepository wr;
    public User(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

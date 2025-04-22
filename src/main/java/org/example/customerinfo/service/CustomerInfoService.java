package org.example.customerinfo.service;

import org.springframework.stereotype.Service;

@Service
public class CustomerInfoService {

    public void addCustomerInfo(String customerInfo) {
        // Logic to add customer information
        System.out.println("Customer Info: " + customerInfo);
    }
}

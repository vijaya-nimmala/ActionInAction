package org.example.customerinfo.controller;

import com.fasterxml.jackson.annotation.JacksonInject;
import lombok.ToString;
import org.example.customerinfo.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/details")
public class DetailsController {

    private final DetailsService detailsService;

    public DetailsController(DetailsService detailsService) {
        this.detailsService = detailsService;
    }
    @GetMapping
    public String getDetails() {
        return detailsService.getDetails();
    }
}
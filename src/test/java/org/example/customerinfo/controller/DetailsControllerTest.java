package org.example.customerinfo.controller;

import org.example.customerinfo.service.DetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DetailsControllerTest {

    @MockBean
    private DetailsService detailsService;

    @Test
    public void testGetDetails() {
        DetailsController detailsController = new DetailsController(detailsService);

        Mockito.when(detailsService.getDetails()).thenReturn("Test details");

        String result = detailsController.getDetails();

        assertEquals("Test details", result);
    }
}
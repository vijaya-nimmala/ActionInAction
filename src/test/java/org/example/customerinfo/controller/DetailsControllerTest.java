package org.example.customerinfo.controller;

import org.example.customerinfo.service.DetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DetailsControllerTest {

    @Mock
    private DetailsService detailsService;

    @Test
    public void testGetDetails() {
        DetailsController detailsController = new DetailsController(detailsService);

        Mockito.when(detailsService.getDetails()).thenReturn("Test details");

        String result = detailsController.getDetails();

        assertEquals("Test details", result);
    }
}
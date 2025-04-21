package org.example.customerinfo.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetailsServiceTest {

    @Test
    public void testGetDetails() {
        DetailsService detailsService = new DetailsService();
        assertEquals("Details", detailsService.getDetails());
    }
}
package org.example.customerinfo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CustomerInfoServiceTest {

    private CustomerInfoService customerInfoService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @MockBean
    private CustomerInfoService mockCustomerInfoService;

    @BeforeEach
    public void setUp() {
        customerInfoService = new CustomerInfoService();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testAddCustomerInfo() {
        String info = "Test Customer Info";
        customerInfoService.addCustomerInfo(info);
        assertEquals("Customer Info: " + info + System.lineSeparator(), outContent.toString());
    }
}
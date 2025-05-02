```java
package org.example.customerinfo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerInfoServiceTest {

    @InjectMocks
    private CustomerInfoService customerInfoService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testAddCustomerInfo() {
        String customerInfo = "Test customer info";
        customerInfoService.addCustomerInfo(customerInfo);

        assertTrue(outContent.toString().contains("Customer Info: " + customerInfo));
    }
}
```
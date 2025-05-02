Here is a simple JUnit test case using JUnit5 for your `CustomerInfoService` class. Please note that to achieve 100% code coverage and 100% condition coverage, we need to have access to the detailed logic of the `addCustomerInfo` method. However, given the provided information, here is a simple test case:

```java
package org.example.customerinfo.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerInfoServiceTest {

    @Test
    public void testAddCustomerInfo() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CustomerInfoService customerInfoService = new CustomerInfoService();
        String customerInfo = "Test Customer Info";
        customerInfoService.addCustomerInfo(customerInfo);

        String expectedOutput = "Customer Info: " + customerInfo + "\r\n"; 
        assertEquals(expectedOutput, outContent.toString());
    }
}
```
This test case validates the console output of the `addCustomerInfo` method. It creates a `CustomerInfoService` instance, invokes the `addCustomerInfo` method, and asserts that the console output is as expected.
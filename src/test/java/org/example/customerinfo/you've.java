The Java class you've provided is the main class for a Spring Boot application and it doesn't contain any business logic to test. The main method just starts the Spring application context. Testing this would mean testing the Spring Boot framework itself, which is not the responsibility of application developers.

However, if you still want to write a test for this, here is a basic one. The test merely checks if the Spring context starts up correctly:

```java
package org.example.customerinfo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class CustomerinfoApplicationTests {

    @Test
    public void contextLoads() {
    }

}
```

Remember, meaningful tests should cover the business logic of your application, not the functionality of the framework itself.
```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CustomerinfoApplication.class)
class CustomerinfoApplicationTests {

    @Test
    void main() {
        CustomerinfoApplication.main(new String[] {});
    }

    @Test
    void contextLoads() {
    }

}
```
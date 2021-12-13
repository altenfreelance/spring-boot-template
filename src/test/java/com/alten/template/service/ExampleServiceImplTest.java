package com.alten.template.service;

import com.alten.template.exception.ExampleException;
import com.alten.template.model.ExamplePOJO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-suite")
@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 0)
@SpringBootTest
class ExampleServiceImplTest {

    @Autowired
    private ExampleService service;

    @Test
    void getTestObject() {
        ExamplePOJO examplePOJO = service.getTestObject();
        assertEquals("Custom property is Testing Suite", examplePOJO.getCustomProperty());
        assertNull(examplePOJO.getUserID());
    }

    @Test
    void getTestObject_withId() {
        String userId = "testID";
        ExamplePOJO examplePOJO = service.getTestObject(userId);
        assertEquals("Custom property is Testing Suite", examplePOJO.getCustomProperty());
        assertEquals(examplePOJO.getUserID(), userId);
    }

    @Test
    void getBadTestObjectToShowErrorHandling() {
        assertThrows(ExampleException.class, () -> {service.getBadTestObjectToShowErrorHandling();});
    }
}
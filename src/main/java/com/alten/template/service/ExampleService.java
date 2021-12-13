package com.alten.template.service;

import com.alten.template.model.ExamplePOJO;

public interface ExampleService {
    ExamplePOJO getTestObject();
    ExamplePOJO getTestObject(String userID);
    ExamplePOJO getBadTestObjectToShowErrorHandling();
}

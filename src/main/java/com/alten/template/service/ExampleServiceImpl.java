package com.alten.template.service;

import com.alten.template.exception.ExampleException;
import com.alten.template.model.ExamplePOJO;
import com.alten.template.model.ExampleProperties;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExampleServiceImpl implements ExampleService {

    Logger logger = LoggerFactory.getLogger(ExampleServiceImpl.class);

    @Autowired
    private ExampleProperties customProperties;

    @Override
    public ExamplePOJO getTestObject() {
        return getTestObject(null);
    }

    @Override
    public ExamplePOJO getTestObject(String userID) {
        logger.debug("Attempting to get TestObject");

        ExamplePOJO examplePOJO = new ExamplePOJO();
        examplePOJO.setCustomProperty("Custom property is " + customProperties.getCustomPropOne());
        examplePOJO.setUserID(userID);
        examplePOJO.setTestDate(new Date());

        logger.info("Got Test Object");
        return examplePOJO;
    }

    @SneakyThrows
    @Override
    public ExamplePOJO getBadTestObjectToShowErrorHandling() {
        throw new ExampleException("dummy");
    }
}

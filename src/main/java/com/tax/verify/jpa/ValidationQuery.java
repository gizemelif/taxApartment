package com.tax.verify.jpa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ValidationQuery {

    public String getValidationQuery(String driver) {
        Properties properties = loadProperties();
        return properties.getProperty(driver, "SELECT * FROM SCHEDULED_VD_TC_INDEX u WHERE u.state = 'WAITING' ORDER BY u.created_at ASC LIMIT 1");
    }

    private Properties loadProperties() {
        String propertyFilename = "application.properties";
        try {
            Properties props = new Properties();
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(propertyFilename);
            props.load(resourceAsStream);
            resourceAsStream.close();
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load properties file '" + propertyFilename + "'.", e);
        }
    }

}

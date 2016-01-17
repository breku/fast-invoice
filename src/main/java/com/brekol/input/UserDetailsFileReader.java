package com.brekol.input;

import com.brekol.input.AbstractFileReader;
import com.brekol.input.model.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by brekol on 17.01.16.
 */
@Service
public class UserDetailsFileReader extends AbstractFileReader {
    private static final String AGREEMENT_NUMBER_KEY = "numerUmowy";

    public UserDetails getUserDetails(String fileName) {
        final Properties properties = getFileAsProperties(fileName);
        return new UserDetails(properties.getProperty(AGREEMENT_NUMBER_KEY));
    }
}

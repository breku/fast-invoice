package com.brekol;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * Created by brekol on 17.01.16.
 */
@Component
public class InputValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputValidator.class);

    public void validateInput(String[] args) {
        validateArgumentsSize(args);
        validateFileExists(args);
    }

    private void validateFileExists(String[] args) {
        try {
            for (String arg : args) {
                URL url = Resources.getResource(arg);
                Resources.toString(url, Charsets.UTF_8);
            }
        } catch (IOException e) {
            LOGGER.error("IOException, the file has not been found", e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void validateArgumentsSize(String[] args) {
        if (args.length != 2) {
            LOGGER.error("Input size is invalid, you have to provide 2 files as arguments.");
            throw new IllegalStateException("Input size is invalid, you have to provide 2 files as arguments. <invoice> <custom-user-details>");
        }
    }
}

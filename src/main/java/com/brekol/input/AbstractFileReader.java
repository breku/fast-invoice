package com.brekol.input;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Created by brekol on 17.01.16.
 */
public abstract class AbstractFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileReader.class);

    protected List<String> getFileAsString(String fileName) {
        try {
            URL url = Resources.getResource(fileName);
            return Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Error during reading a file", fileName);
            throw new FastInvoceException(e.getMessage(), e);
        }
    }

    protected Properties getFileAsProperties(String fileName) {
        try {
            final URL url = Resources.getResource(fileName);
            final String fileAsString = Resources.toString(url, Charsets.UTF_8);
            final Properties properties = new Properties();
            properties.load(new StringReader(fileAsString));
            return properties;
        } catch (final IOException e) {
            LOGGER.error("Error during reading a file", fileName);
            throw new FastInvoceException(e.getMessage(), e);
        }
    }
}

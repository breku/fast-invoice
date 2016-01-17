package com.brekol;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
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
        final URL url = Resources.getResource(fileName);
        final ByteSource byteSource = Resources.asByteSource(url);
        final Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = byteSource.openBufferedStream();
            properties.load(inputStream);
            return properties;
        } catch (final IOException e) {
            LOGGER.error("Error during reading a file", fileName);
            throw new FastInvoceException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}

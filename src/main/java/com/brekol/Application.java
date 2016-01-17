package com.brekol;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by brekol on 17.01.16.
 */
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        LOGGER.info("asdfasdf");

    }
}


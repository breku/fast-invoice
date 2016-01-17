package com.brekol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by brekol on 17.01.16.
 */
@Component
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private InvoiceFileReader invoiceFileReader;

    @Autowired
    private UserDetailsFileReader userDetailsFileReader;

    public void execute(String[] args) {
        LOGGER.info(">> Starting application.");

        inputValidator.validateInput(args);
        final List<InvoiceDetails> invoiceDetailsList = invoiceFileReader.getInvoiceDetailsList(args[0]);
        final UserDetails userDetails = userDetailsFileReader.getUserDetails(args[1]);

        LOGGER.info("<< Finished.");
    }
}

package com.brekol;

import com.brekol.input.InvoiceFileReader;
import com.brekol.input.UserDetailsFileReader;
import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import com.brekol.output.PdfAttachmentService;
import com.brekol.output.PdfInvoiceService;
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

    @Autowired
    private PdfInvoiceService pdfInvoiceService;

    @Autowired
    private PdfAttachmentService pdfAttachmentService;

    public void execute(String[] args) {
        LOGGER.info(">> Starting application.");

        inputValidator.validateInput(args);
        final List<InvoiceDetails> invoiceDetailsList = invoiceFileReader.getInvoiceDetailsList(args[0]);
        final UserDetails userDetails = userDetailsFileReader.getUserDetails(args[1]);
        pdfInvoiceService.createPdfs(invoiceDetailsList, userDetails);
        pdfAttachmentService.createAttachments(invoiceDetailsList, userDetails);

        LOGGER.info("<< Finished.");
    }
}

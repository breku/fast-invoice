package com.brekol.output;

import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by brekol on 21.01.16.
 */
public abstract class AbstractPdfService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPdfService.class);
    protected Font font;
    protected Font boldFont;

    AbstractPdfService() {
        initializeFont();
    }

    protected void saveDocumentAsPdf(Document document, UserDetails userDetails, InvoiceDetails invoiceDetails) {
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(getPdfFileName(userDetails, invoiceDetails))));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error during getting instance of pdf writer (saving document)", e);
        }
    }

    protected void addText(Document document, int aligment, String text) throws DocumentException {
        final Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(aligment);
        document.add(paragraph);
    }
    protected void createLineSeparator(Document document) throws DocumentException {
        document.add(new Chunk(new LineSeparator(0.2f, 100f, BaseColor.BLACK, Element.ALIGN_LEFT, 0)));
    }

    protected PdfPCell createPdfCell(String text) {
        PdfPCell result = new PdfPCell(new Phrase(text, font));
        result.setHorizontalAlignment(Element.ALIGN_CENTER);
        result.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return result;
    }



    protected String getLastDayOfPreviousMonth() {
        final LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);
        return shiftedDate.getYear() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.dayOfMonth().getMaximumValue();
    }

    protected String getFirstDayOfPreviousMonth() {
        final LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);
        return shiftedDate.getYear() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.dayOfMonth().getMinimumValue();
    }

    protected abstract String getPdfFileName(UserDetails userDetails, InvoiceDetails invoiceDetails);

    private void initializeFont() {
        try {
            final BaseFont bf = BaseFont.createFont("OpenSans-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(bf, 12, Font.NORMAL);
            boldFont = new Font(bf, 12, Font.BOLD);
        } catch (DocumentException | IOException e) {
            LOGGER.error("Error during font loading", e);
        }
    }
}

package com.brekol.output;

import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by brekol on 17.01.16.
 */
@Service
public class PdfService {

    private static final int DEFAULT_FONT_SIZE = 12;
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfService.class);
    private Font font;

    public void createPdfs(List<InvoiceDetails> invoiceDetailsList, UserDetails userDetails) {
        initializeFont();
        createPdf(invoiceDetailsList.get(0), userDetails);

        for (final InvoiceDetails invoiceDetails : invoiceDetailsList) {
        }
    }

    private void initializeFont() {
        try {
            final BaseFont bf = BaseFont.createFont("OpenSans-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(bf, 12, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            LOGGER.error("Error during font loading", e);
        }
    }

    private void createPdf(InvoiceDetails invoiceDetails, UserDetails userDetails) {

        Document document = new Document();
        saveDocumentAsPdf(document);
        document.open();
        addPdfContent(invoiceDetails, userDetails, document);
        document.close();
    }

    private void addPdfContent(InvoiceDetails invoiceDetails, UserDetails userDetails, Document document) {
        try {
            addText(document, Element.ALIGN_LEFT, getHeader(invoiceDetails));
            createLineSeparator(document);
            document.add(createSellerAndBuyerTable(userDetails));
            createLineSeparator(document);
        } catch (DocumentException e) {
            LOGGER.error("Error during generatring pdf", e);
        }
    }

    private void createLineSeparator(Document document) throws DocumentException {
        document.add(new Chunk(new LineSeparator(0.2f, 100f, BaseColor.BLACK, Element.ALIGN_LEFT, 0)));
    }

    private void saveDocumentAsPdf(Document document) {
        try {
            PdfWriter.getInstance(document, new FileOutputStream("itext.pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error during getting instance of pdf writer (saving document)", e);
        }
    }

    private PdfPTable createSellerAndBuyerTable(UserDetails userDetails) {
        PdfPTable table = new PdfPTable(new float[]{7f, 10f, 4f, 7f, 12f});
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(createPdfCell("Sprzedawca:"));
        table.addCell(createPdfCell(userDetails.getCompanyName()));
        table.addCell(createPdfCell(""));
        table.addCell(createPdfCell("Nabywca:"));
        table.addCell(createPdfCell("Consdata Sp. z o.o."));
        table.completeRow();
        table.addCell(createPdfCell("Adres:"));
        table.addCell(createPdfCell(userDetails.getAddress()));
        table.addCell(createPdfCell(""));
        table.addCell(createPdfCell("Adres:"));
        table.addCell(createPdfCell("Składowa 4, 61-897 Poznań"));
        table.completeRow();
        table.addCell(createPdfCell("NIP:"));
        table.addCell(createPdfCell(userDetails.getNip()));
        table.addCell(createPdfCell(""));
        table.addCell(createPdfCell("NIP:"));
        table.addCell(createPdfCell("7822261960"));
        table.completeRow();
        table.addCell(createPdfCell("Telefon:"));
        table.addCell(createPdfCell(userDetails.getPhoneNumber()));
        table.addCell(createPdfCell(""));
        table.addCell(createPdfCell("Telefon:"));
        table.addCell(createPdfCell("+48 61 41 51 000"));
        table.completeRow();
        return table;
    }

    private PdfPCell createPdfCell(String text) {
        PdfPCell result = new PdfPCell(new Phrase(text, font));
        result.setBorder(Rectangle.NO_BORDER);
        return result;
    }

    private void addText(Document document, int aligment, String text) throws DocumentException {
        final Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(aligment);
        document.add(paragraph);
    }

    private String getHeader(InvoiceDetails invoiceDetails) {
        LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);
        return "Faktura nr " + invoiceDetails.getNumber() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.getYear();
    }
}

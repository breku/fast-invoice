package com.brekol.output;

import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by brekol on 17.01.16.
 */
@Service
public class PdfService {

    private static final int DEFAULT_FONT_SIZE = 12;

    public void createPdfs(List<InvoiceDetails> invoiceDetailsList, UserDetails userDetails) {

        createPdf(invoiceDetailsList.get(0), userDetails.getAgreementNumber());

        for (final InvoiceDetails invoiceDetails : invoiceDetailsList) {
        }
    }

    private void createPdf(InvoiceDetails invoiceDetails, String agreementNumber) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            final Color blueColor = Color.getHSBColor(0.55f, 1, 1);

            addText(contentStream, getHeader(invoiceDetails) , 100, 700,24, blueColor);

            contentStream.close();

            document.save("Hello World.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (COSVisitorException e) {
            e.printStackTrace();
        }
    }

    private String getHeader(InvoiceDetails invoiceDetails) {
        LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);

        return "Faktura nr " + invoiceDetails.getNumber() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.getYear();
    }

    private void addText(PDPageContentStream contentStream, String text, int x, int y) throws IOException {
        addText(contentStream, text, x, y, DEFAULT_FONT_SIZE, Color.BLACK);
    }

    private void addText(PDPageContentStream contentStream, String text, int x, int y, int fontSize) throws IOException {
        addText(contentStream, text, x, y, fontSize, Color.BLACK);
    }

    private void addText(PDPageContentStream contentStream, String text, int x, int y, Color color) throws IOException {
        addText(contentStream, text, x, y, DEFAULT_FONT_SIZE, color);
    }

    private void addText(PDPageContentStream contentStream, String text, int x, int y, int fontSize, Color color) throws IOException {
        PDFont font = PDType1Font.HELVETICA_BOLD;
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);
        contentStream.moveTextPositionByAmount(x, y);
        contentStream.drawString(text);
        contentStream.endText();
    }
}

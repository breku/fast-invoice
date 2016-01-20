package com.brekol.output;

import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
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

import java.io.File;
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
    private static final double VAT_FACTOR = 0.23;
    private Font font;

    public void createPdfs(List<InvoiceDetails> invoiceDetailsList, UserDetails userDetails) {
        initializeFont();
        for (final InvoiceDetails invoiceDetails : invoiceDetailsList) {
            createPdf(invoiceDetails, userDetails);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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

        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        saveDocumentAsPdf(document, userDetails, invoiceDetails);
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
            document.add(createDatesAndBankAccountTable(userDetails));
            createLineSeparator(document);
            document.add(createPaymentTable(invoiceDetails));
            document.add(Chunk.NEWLINE);
            document.add(createSummary(invoiceDetails));
        } catch (DocumentException e) {
            LOGGER.error("Error during generatring pdf", e);
        }
    }

    private Element createSummary(InvoiceDetails invoiceDetails) {
//        final PdfPTable table = new PdfPTable(new float[]{15f, 3f, 3f, 3f, 3f, 3f});
        final PdfPTable table = new PdfPTable(new float[]{2f, 12f, 5f, 5f, 5f, 5f, 5f, 5f,});
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);

        final PdfPCell pdfCell = createPdfCellWithoutBorder("Razem do zapłaty");
        pdfCell.setColspan(4);
        table.addCell(pdfCell);

        table.addCell(createPdfCellWithoutBorderWithCenterAlignment(invoiceDetails.getValue()));
        table.addCell(createPdfCellWithoutBorderWithCenterAlignment("23%"));
        table.addCell(createPdfCellWithoutBorderWithCenterAlignment(calculateValueWithVat(invoiceDetails.getValue())));
        table.addCell(createPdfCellWithoutBorderWithCenterAlignment(calculateTotalValue(invoiceDetails)));
        return table;
    }

    private Element createPaymentTable(InvoiceDetails invoiceDetails) {
        final PdfPTable table = new PdfPTable(new float[]{2f, 12f, 5f, 5f, 5f, 5f, 5f, 5f,});
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.addCell(createPdfCell("Lp."));
        table.addCell(createPdfCell("Nazwa"));
        table.addCell(createPdfCell("Ilość"));
        table.addCell(createPdfCell("Cena netto"));
        table.addCell(createPdfCell("Wartość netto"));
        table.addCell(createPdfCell("Stawka VAT"));
        table.addCell(createPdfCell("Kwota VAT"));
        table.addCell(createPdfCell("Wartość brutto"));

        table.addCell(createPdfCell("1"));
        table.addCell(createPdfCell(invoiceDetails.getName()));
        table.addCell(createPdfCell(invoiceDetails.getNumberOfDays()));
        table.addCell(createPdfCell(invoiceDetails.getSalary()));
        table.addCell(createPdfCell(invoiceDetails.getValue()));
        table.addCell(createPdfCell("23%"));
        table.addCell(createPdfCell(calculateValueWithVat(invoiceDetails.getValue())));
        table.addCell(createPdfCell(calculateTotalValue(invoiceDetails)));
        return table;
    }

    private String calculateTotalValue(InvoiceDetails invoiceDetails) {
        final String vat = calculateValueWithVat(invoiceDetails.getValue());
        final String value = invoiceDetails.getValue();
        return String.valueOf(round(Double.parseDouble(vat) + Double.parseDouble(value), 2));
    }

    private String calculateValueWithVat(String nettoValue) {
        final double parsedInvoiceNetto = Double.parseDouble(nettoValue);
        final double result = parsedInvoiceNetto * VAT_FACTOR;
        return String.valueOf(round(result, 2));
    }

    private Element createDatesAndBankAccountTable(UserDetails userDetails) {
        final PdfPTable table = new PdfPTable(new float[]{3f, 7f});
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.addCell(createPdfCellWithoutBorder("Data wystawienia:"));
        table.addCell(createPdfCellWithoutBorder(getLastDayOfPreviousMonth()));
        table.addCell(createPdfCellWithoutBorder("Data sprzedaży:"));
        table.addCell(createPdfCellWithoutBorder(getLastDayOfPreviousMonth()));
        table.addCell(createPdfCellWithoutBorder("Sposób płatności:"));
        table.addCell(createPdfCellWithoutBorder("Przelew"));
        table.addCell(createPdfCellWithoutBorder("Numer konta:"));
        table.addCell(createPdfCellWithoutBorder(userDetails.getBankAccountNumber()));
        return table;
    }

    private void createLineSeparator(Document document) throws DocumentException {
        document.add(new Chunk(new LineSeparator(0.2f, 100f, BaseColor.BLACK, Element.ALIGN_LEFT, 0)));
    }

    private void saveDocumentAsPdf(Document document, UserDetails userDetails, InvoiceDetails invoiceDetails) {
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(getPdfFileName(userDetails, invoiceDetails))));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error during getting instance of pdf writer (saving document)", e);
        }
    }

    private String getPdfFileName(UserDetails userDetails, InvoiceDetails invoiceDetails) {
        final String filePrefix = Iterables.getLast(Splitter.on("/").split(userDetails.getAgreementNumber()));
        final String result = Joiner.on("-").join(filePrefix, "faktura", invoiceDetails.getNumber());
        return result + ".pdf";
    }

    private PdfPTable createSellerAndBuyerTable(UserDetails userDetails) {
        PdfPTable table = new PdfPTable(new float[]{7f, 10f, 5f, 5f, 12f});
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.addCell(createPdfCellWithoutBorder("Sprzedawca:"));
        table.addCell(createPdfCellWithoutBorder(userDetails.getCompanyName()));
        table.addCell(createPdfCellWithoutBorder(""));
        table.addCell(createPdfCellWithoutBorder("Nabywca:"));
        table.addCell(createPdfCellWithoutBorder("Consdata Sp. z o.o."));
        table.completeRow();
        table.addCell(createPdfCellWithoutBorder("Adres:"));
        table.addCell(createPdfCellWithoutBorder(userDetails.getAddress()));
        table.addCell(createPdfCellWithoutBorder(""));
        table.addCell(createPdfCellWithoutBorder("Adres:"));
        table.addCell(createPdfCellWithoutBorder("Składowa 4, 61-897 Poznań"));
        table.completeRow();
        table.addCell(createPdfCellWithoutBorder("NIP:"));
        table.addCell(createPdfCellWithoutBorder(userDetails.getNip()));
        table.addCell(createPdfCellWithoutBorder(""));
        table.addCell(createPdfCellWithoutBorder("NIP:"));
        table.addCell(createPdfCellWithoutBorder("7822261960"));
        table.completeRow();
        table.addCell(createPdfCellWithoutBorder("Telefon:"));
        table.addCell(createPdfCellWithoutBorder(userDetails.getPhoneNumber()));
        table.addCell(createPdfCellWithoutBorder(""));
        table.addCell(createPdfCellWithoutBorder("Telefon:"));
        table.addCell(createPdfCellWithoutBorder("+48 61 41 51 000"));
        table.completeRow();
        return table;
    }

    private PdfPCell createPdfCellWithoutBorderWithCenterAlignment(String text) {
        PdfPCell result = createPdfCellWithoutBorder(text);
        result.setHorizontalAlignment(Element.ALIGN_CENTER);
        return result;
    }

    private PdfPCell createPdfCellWithoutBorder(String text) {
        PdfPCell result = new PdfPCell(new Phrase(text, font));
        result.setBorder(Rectangle.NO_BORDER);
        return result;
    }

    private PdfPCell createPdfCell(String text) {
        PdfPCell result = new PdfPCell(new Phrase(text, font));
        result.setHorizontalAlignment(Element.ALIGN_CENTER);
        result.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return result;
    }

    private void addText(Document document, int aligment, String text) throws DocumentException {
        final Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(aligment);
        document.add(paragraph);
    }

    private String getLastDayOfPreviousMonth() {
        final LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);
        return shiftedDate.getYear() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.dayOfMonth().getMaximumValue();
    }

    private String getHeader(InvoiceDetails invoiceDetails) {
        final LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);
        return "Faktura nr " + invoiceDetails.getNumber() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.getYear();
    }
}

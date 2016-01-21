package com.brekol.output;

import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by brekol on 21.01.16.
 */
@Service
public class PdfAttachmentService extends AbstractPdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfAttachmentService.class);

    public void createAttachments(List<InvoiceDetails> invoiceDetailsList, UserDetails userDetails) {
        for (final InvoiceDetails invoiceDetails : invoiceDetailsList) {
            createAttachment(invoiceDetails, userDetails);
        }
    }

    @Override
    protected String getPdfFileName(UserDetails userDetails, InvoiceDetails invoiceDetails) {
        final String filePrefix = Iterables.getLast(Splitter.on("/").split(userDetails.getAgreementNumber()));
        final String result = Joiner.on("-").join(filePrefix, "zalczanik", invoiceDetails.getNumber());
        return result + ".pdf";
    }

    private void createAttachment(InvoiceDetails invoiceDetails, UserDetails userDetails) {

        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        saveDocumentAsPdf(document, userDetails, invoiceDetails);
        document.open();
        addAttachmentContent(document, invoiceDetails, userDetails);
        document.close();
    }

    private void addAttachmentContent(Document document, InvoiceDetails invoiceDetails, UserDetails userDetails) {
        try {
            addHeader(document, invoiceDetails, userDetails);
            document.add(Chunk.NEWLINE);
            addText(document, Element.ALIGN_LEFT, "");
            document.add(Chunk.NEWLINE);
            addText(document, Element.ALIGN_LEFT, "Zlecenie realizacji zadania dla Wykonawcy.");
            addFirstParagraph(document);
            document.add(Chunk.NEWLINE);
            createLineSeparator(document);

            addFirstList(document, invoiceDetails);
            addSignTable(document);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);



            addSecondList(document, invoiceDetails);
            addSignTable(document);
        } catch (DocumentException e) {
            LOGGER.error("Error during generating attachment", e);
        }
    }

    private void addSecondList(Document document, InvoiceDetails invoiceDetails) throws DocumentException {
        document.add(new Chunk("Odbiór zadania nastąpił w dniu ", font));
        document.add(new Chunk(getLastDayOfPreviousMonth(), boldFont));
        document.add(new Chunk(" dokonany przez Constada Sp. z o.o.", font));

        com.itextpdf.text.List firstSubList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED, 10);
        firstSubList.add(new ListItem("Dokumenty projektowe.", font));
        firstSubList.add(new ListItem("Kod klas Java", font));

        com.itextpdf.text.List secondSubList = getTaskCostList(invoiceDetails);



        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 10);
        list.add(new ListItem("Odebrane produkty:", font));
        list.add(firstSubList);
        list.add(new ListItem("Zaakceptowane koszty realizacji zadania:", font));
        list.add(secondSubList);
        list.add(new ListItem("Podpisy", font));
        document.add(list);
        addText(document, Element.ALIGN_LEFT, " ");

    }

    private void addSignTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{1,1});
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        table.addCell(createPdfCell("Wykonawca"));
        table.addCell(createPdfCell("Zamawiający"));
        table.completeRow();
        table.addCell(createPdfCell(" "));
        table.addCell(createPdfCell(" "));
        document.add(table);
    }

    private void addFirstList(Document document, InvoiceDetails invoiceDetails) throws DocumentException {
        com.itextpdf.text.List firstSubList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED, 10);
        firstSubList.add(new ListItem("Tworzenie dokumentów projektowych.", font));
        firstSubList.add(new ListItem("Tworzenie dokumentacji technicznej.", font));
        firstSubList.add(new ListItem("Pisanie kodu źródłowego Java.", font));

        com.itextpdf.text.List secondSubList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED, 10);
        secondSubList.add(new ListItem("Dokumenty projektowe.", font));
        secondSubList.add(new ListItem("Kod klas Java.", font));

        com.itextpdf.text.List thirdSubList = getTaskCostList(invoiceDetails);

        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 10);
        list.add(new ListItem("Specyfikacja zadania:", font));
        list.add(firstSubList);
        list.add(new ListItem("Produkty zadania:", font));
        list.add(secondSubList);
        list.add(new ListItem("Uzgodnione koszty realizacji zadania:", font));
        list.add(thirdSubList);
        list.add(new ListItem("Podpisy", font));
        document.add(list);
        addText(document, Element.ALIGN_LEFT, " ");

    }

    private com.itextpdf.text.List getTaskCostList(InvoiceDetails invoiceDetails) {
        com.itextpdf.text.List thirdSubList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED, 10);
        final ListItem listItem1 = new ListItem("", font);
        listItem1.add(new Chunk("Dni robocze: ", font));
        listItem1.add(new Chunk(invoiceDetails.getNumberOfDays(), boldFont));
        final ListItem listItem2 = new ListItem("", font);
        listItem2.add(new Chunk("Koszty zakwaterowania i dojazdu: ", font));
        listItem2.add(new Chunk("0", boldFont));
        final ListItem listItem3 = new ListItem("", font);
        listItem3.add(new Chunk("Inne koszty: ", font));
        listItem3.add(new Chunk("0", boldFont));
        thirdSubList.add(listItem1);
        thirdSubList.add(listItem2);
        thirdSubList.add(listItem3);
        return thirdSubList;
    }

    private void addHeader(Document document, InvoiceDetails invoiceDetails, UserDetails userDetails) throws DocumentException {
        document.add(new Chunk("                 ZAŁĄCZNIK NR 1/", font));
        document.add(new Chunk(invoiceDetails.getProjectName(), boldFont));
        document.add(new Chunk(" DO UMOWY NR ", font));
        document.add(new Chunk(userDetails.getAgreementNumber(), boldFont));
    }

    private void addFirstParagraph(Document document) throws DocumentException {
        document.add(new Chunk("Złożone w dniu ", font));
        document.add(new Chunk(getFirstDayOfPreviousMonth(), boldFont));
        document.add(new Chunk(" przez Consdata Sp. z o.o. i zaakceptowane przez Wykonawcę w dniu ", font));
        document.add(new Chunk(getLastDayOfPreviousMonth(), boldFont));
    }
}

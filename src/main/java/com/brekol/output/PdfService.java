package com.brekol.output;

import com.brekol.input.model.InvoiceDetails;
import com.brekol.input.model.UserDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.joda.time.LocalDate;
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

    public void createPdfs(List<InvoiceDetails> invoiceDetailsList, UserDetails userDetails) {

        createPdf(invoiceDetailsList.get(0), userDetails);

        for (final InvoiceDetails invoiceDetails : invoiceDetailsList) {
        }
    }

    private void createPdf(InvoiceDetails invoiceDetails, UserDetails userDetails) {

        Document document = new Document();
        // step 2
        try {
            PdfWriter.getInstance(document, new FileOutputStream("itext.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BaseFont bf = null;
        try {
            bf= BaseFont.createFont("OpenSans-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font font = new Font(bf, 12, Font.NORMAL);


        // step 3
        document.open();
        // step 4
        try {
            document.add(new Paragraph(getHeader(invoiceDetails),font));
            document.add(new Paragraph(userDetails.getCompanyName(),font));
            document.add(new Paragraph("ąłżóćźę",font));

            String[] encoding = bf.getCodePagesSupported();
            for (int i = 0; i < encoding.length; i++) {
                document.add(new Paragraph("encoding[" + i + "] = " + encoding[i]));
            }
            document.add(Chunk.NEWLINE);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // step 5
        document.close();
    }

    private String getHeader(InvoiceDetails invoiceDetails) {
        LocalDate today = new LocalDate();
        final LocalDate shiftedDate = today.minusMonths(1);

        return "Faktura nr " + invoiceDetails.getNumber() + "/" + shiftedDate.getMonthOfYear() + "/" + shiftedDate.getYear();
    }
}

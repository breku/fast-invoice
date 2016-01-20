package com.brekol.input;

import com.brekol.input.model.InvoiceDetails;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brekol on 17.01.16.
 */
@Service
public class InvoiceFileReader extends AbstractFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceFileReader.class);

    private static final int NUMBER_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int NUMBER_OF_DAYS_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int INVOICE_VALUE_INDEX = 4;

    public List<InvoiceDetails> getInvoiceDetailsList(final String fileName) {
        final List<String> fileLines = getFileAsString(fileName);
        final List<InvoiceDetails> result = new ArrayList<>();
        final List<List<String>> partition = Lists.partition(fileLines, 6);
        for (List<String> fileLinesPerInvoice : partition) {
            result.add(createInvoceDetailsFromLines(fileLinesPerInvoice));
        }

        return result;
    }

    private InvoiceDetails createInvoceDetailsFromLines(List<String> lines) {
        final String number = getValueFromLines(NUMBER_INDEX, lines);
        final String name = getValueFromLines(NAME_INDEX, lines);
        final String numberOfDays = getValueFromLines(NUMBER_OF_DAYS_INDEX, lines);
        final String salary = getValueFromLinesSplitOnSpace(SALARY_INDEX, lines);
        final String invoiceValue = getValueFromLines(INVOICE_VALUE_INDEX, lines);
        final String projectName = Iterables.getLast(Splitter.on(" ").splitToList(name));
        return new InvoiceDetails(number, name, replaceCommaWithDot(numberOfDays), replaceCommaWithDot(salary), replaceCommaWithDot(invoiceValue), projectName);
    }

    private String replaceCommaWithDot(String input) {
        return input.replace(",", ".");
    }


    private String getValueFromLinesSplitOnSpace(final int index, List<String> lines) {
        final List<String> strings = Splitter.on(" ").trimResults().splitToList(lines.get(index));
        return Iterables.getLast(strings);
    }

    private String getValueFromLines(final int index, List<String> lines) {
        final List<String> strings = Splitter.on(":").trimResults().splitToList(lines.get(index));
        return Iterables.getLast(strings);
    }


}

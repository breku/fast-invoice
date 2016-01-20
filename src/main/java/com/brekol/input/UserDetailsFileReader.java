package com.brekol.input;

import com.brekol.input.model.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by brekol on 17.01.16.
 */
@Service
public class UserDetailsFileReader extends AbstractFileReader {
    private static final String AGREEMENT_NUMBER_KEY = "agreementNumber";
    private static final String COMPANY_NAME_KEY = "companyName";
    private static final String ADDRESS_KEY = "address";
    private static final String NIP_KEY = "nip";
    private static final String PHONE_NUMBER_KEY = "phoneNumber";
    private static final String BANK_ACCOUNT_NUMBER_KEY = "bankAccountNumber";

    public UserDetails getUserDetails(String fileName) {
        final Properties properties = getFileAsProperties(fileName);
        final String agreementNumber = properties.getProperty(AGREEMENT_NUMBER_KEY);
        final String companyName = properties.getProperty(COMPANY_NAME_KEY);
        final String address = properties.getProperty(ADDRESS_KEY);
        final String nip = properties.getProperty(NIP_KEY);
        final String phoneNumber = properties.getProperty(PHONE_NUMBER_KEY);
        final String bankAccountNumber = properties.getProperty(BANK_ACCOUNT_NUMBER_KEY);
        return new UserDetails(agreementNumber, companyName, address, nip, phoneNumber, bankAccountNumber);
    }
}

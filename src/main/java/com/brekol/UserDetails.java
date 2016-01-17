package com.brekol;

/**
 * Created by brekol on 17.01.16.
 */
public class UserDetails {

    private String agreementNumber;

    public UserDetails(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }
}

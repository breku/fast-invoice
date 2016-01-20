package com.brekol.input.model;

/**
 * Created by brekol on 17.01.16.
 */
public class UserDetails {

    private String agreementNumber;
    private String companyName;
    private String address;
    private String nip;
    private String phoneNumber;

    public UserDetails(String agreementNumber, String companyName, String address, String nip, String phoneNumber) {
        this.agreementNumber = agreementNumber;
        this.companyName = companyName;
        this.address = address;
        this.nip = nip;
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }
}

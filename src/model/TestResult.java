package model;

import java.util.Date;

public class TestResult {
    private int recordId;
    private Date donationDate;
    private String memberId;
    private String hepatitisB;
    private String hepatitisC;
    private String syphilis;
    private String unexpectedAntibody;
    private String bloodSubtype;

    // Getter and Setter methods

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Date getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Date donationDate) {
        this.donationDate = donationDate;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getHepatitisB() {
        return hepatitisB;
    }

    public void setHepatitisB(String hepatitisB) {
        this.hepatitisB = hepatitisB;
    }

    public String getHepatitisC() {
        return hepatitisC;
    }

    public void setHepatitisC(String hepatitisC) {
        this.hepatitisC = hepatitisC;
    }

    public String getSyphilis() {
        return syphilis;
    }

    public void setSyphilis(String syphilis) {
        this.syphilis = syphilis;
    }

    public String getUnexpectedAntibody() {
        return unexpectedAntibody;
    }

    public void setUnexpectedAntibody(String unexpectedAntibody) {
        this.unexpectedAntibody = unexpectedAntibody;
    }

    public String getBloodSubtype() {
        return bloodSubtype;
    }

    public void setBloodSubtype(String bloodSubtype) {
        this.bloodSubtype = bloodSubtype;
    }
}

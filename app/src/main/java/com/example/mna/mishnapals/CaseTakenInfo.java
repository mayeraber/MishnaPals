package com.example.mna.mishnapals;

/**
 * Created by MNA on 8/10/2017.
 */

public class CaseTakenInfo {

    private String caseId;
    private String masechtaTaken;

    public CaseTakenInfo(){}

    public CaseTakenInfo(String masechta){masechtaTaken = masechta;}

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getMasechtaTaken() {
        return masechtaTaken;
    }

    public void setMasechtaTaken(String masechtaTaken) {
        this.masechtaTaken = masechtaTaken;
    }
}

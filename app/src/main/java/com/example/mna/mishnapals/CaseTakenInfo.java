package com.example.mna.mishnapals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by MNA on 8/10/2017.
 */

public class CaseTakenInfo {

    private String caseId;
    private String masechtaTaken;
    public boolean finished;

    public String caseMasID; //firebase key for this new entry

    public CaseTakenInfo(){}

    public CaseTakenInfo(String masechta){masechtaTaken = masechta;}

    public CaseTakenInfo(String masechta,String caseID){masechtaTaken = masechta; caseId = caseID;}

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseMasID() {
        return caseMasID;
    }

    public void setCaseMasID(String caseMasID) {
        this.caseMasID = caseMasID;
    }

    public String getMasechtaTaken() {
        return masechtaTaken;
    }

    public void setMasechtaTaken(String masechtaTaken) {
        this.masechtaTaken = masechtaTaken;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
}

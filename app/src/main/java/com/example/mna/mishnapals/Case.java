package com.example.mna.mishnapals;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MNA on 7/28/2017.
 */

public class Case implements Serializable {
    String firstName, fathersName, lastName;
    String userNameOpened;
    Calendar endDate;
    String caseId;

    Masechta[] takenMasechtos, availMasechtos;

    public Case()
    {
    }

    public Case(String fn, String fathersN)
    {
        firstName = fn;

        fathersName = fathersN;
    }

    public String getCaseId() {return caseId;}

    public void setCaseId(String caseId) {this.caseId = caseId.substring(caseId.length()-5, caseId.length());}

    public String getFirstName() {
        return firstName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserNameOpened() {
        return userNameOpened;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserNameOpened(String userNameOpened) {
        this.userNameOpened = userNameOpened;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }
}

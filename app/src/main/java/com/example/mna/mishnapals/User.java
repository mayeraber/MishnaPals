/*
Class that represents a single user, storing his basic information.
This is used in the 'EmailPassword' class and the 'SignInOptions'
class to put his info into a 'User' package to push into the db
*/
package com.example.mna.mishnapals;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MNA on 8/7/2017.
 */

public class User
{
    private String userId;
    private  String userEmail;
    private HashMap<String,Case> cases;
    private List<Case> casesList;
    public User()
    {
        cases = null;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        casesList = null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, Case> getCases() {
        return cases;
    }

    public void setCases(HashMap<String, Case> cases) {
        this.cases = cases;
    }

    public void addCase(Case newCase){
        cases.put(newCase.getCaseId(), newCase);
    }

    public void removeCase(String id) {
        cases.remove(id);
    }
}

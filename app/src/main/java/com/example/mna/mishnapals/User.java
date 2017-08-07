package com.example.mna.mishnapals;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MNA on 8/7/2017.
 */

public class User
{
    private String userId;
    private HashMap<String,Case> cases;

    public User()
    {

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

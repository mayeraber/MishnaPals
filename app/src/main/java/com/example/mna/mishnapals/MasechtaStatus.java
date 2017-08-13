package com.example.mna.mishnapals;

import java.io.Serializable;

/**
 * Created by MNA on 8/8/2017.
 */

public class MasechtaStatus implements Serializable
{

    String masechta;
    boolean status;

    public MasechtaStatus(){}
    public MasechtaStatus(String masechtaName)//, boolean stat)
    {
        masechta = masechtaName;
        status = false;
        //status = stat;
    }

    public String getMasechta() {
        return masechta;
    }

    public void setMasechta(String masechta) {
        this.masechta = masechta;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



}


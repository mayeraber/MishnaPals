package com.example.mna.mishnapals;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MNA on 7/28/2017.
 */

public class Case implements Serializable {
    String firstName, fathersName, lastName;
    String userNameOpened;
    Calendar endDate;
    String caseId;
    List<List<MasechtaStatus>> masechtos;
    // boolean Berachos, Peah, Demai, Kelaim, Shviis, Terumos, Maaseros, MaaserSheini, Chalah, Orlah, Bikurim;

    Masechta[] takenMasechtos, availMasechtos;

    public Case() {


       /* Berachos=false;
        Peah=false;
        Demai=false;
        Kelaim=false;
        Shviis=false;
        Terumos=false;
        Maaseros=false;
        MaaserSheini=false;
        Chalah=false;
        Orlah=false;
        Bikurim=false;*/

    }

    public Case(String fn, String fathersN) {
        firstName = fn;

        fathersName = fathersN;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId.substring(caseId.length() - 5, caseId.length());
    }

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

    public void createMasechtos() {
        masechtos = new ArrayList<>();
        ArrayList<MasechtaStatus> mase = new ArrayList<>();;

        String[] zeraimEng = new String[]{"Berachos", "Peah", "Demai", "Kelaim", "Shviis", "Terumos", "Maaseros", "Maaser Sheini", "Chalah", "Orlah", "Bikurim"};
        for (String mas : zeraimEng) {
            mase.add(new MasechtaStatus(mas));
        }
        masechtos.add(mase);

        String[] moedEng = new String[]{"Shabbos", "Eiruvin", "Pesachim", "Shkalim", "Yoma", "Succah", "Beizah", "Rosh Hashana", "Taanis", "Megillah", "Moed Katan", "Chagigah"};
        mase = new ArrayList<>();
        for (String mas : moedEng) {
            mase.add(new MasechtaStatus(mas));
        }
        masechtos.add(mase);

        String[] nashimEng = new String[]{"Yevamos", "Kesubos", "Nedarim", "Nazir", "Sotah", "Gittin", "Kiddushin"};
        mase = new ArrayList<>();
        for (String mas : nashimEng) {
            mase.add(new MasechtaStatus(mas));
        }
        masechtos.add(mase);

        String[] nezikinEng = new String[]{"Bava Kama", "Bava Metzia", "Bava Basra", "Sanherdin", "Makkos", "Shevuos", "Edios", "Avodah Zarah", "Avos", "Horios"};
        mase = new ArrayList<>();
        for (String mas : nezikinEng) {
            mase.add(new MasechtaStatus(mas));
        }
        masechtos.add(mase);

        String[] kodshimEng = new String[]{"Zevachim", "Menachos", "Chullin", "Bechoros", "Eiruchin", "Temurah", "Kerisos", "Meilah", "Tamid", "Middos", "Kinim"};
        mase = new ArrayList<>();
        for (String mas : kodshimEng) {
            mase.add(new MasechtaStatus(mas));
        }
        masechtos.add(mase);

        String[] taharosEng = new String[]{"Keilim", "Ohalos", "Negaim", "Parah", "Taharos", "Mikvaos", "Niddah", "Machshirin", "Zavim", "Tevul Yom", "Yadayim", "Uktzin"};
        mase = new ArrayList<>();
        for (String mas : taharosEng) {
            mase.add(new MasechtaStatus(mas));
        }
        masechtos.add(mase);
    }

}

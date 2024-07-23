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

    //Class to save seder and masechta numerical value for use if a user removes an uncompleted masechta from their 'taken' list
    //and we then need to set its 'taken' value in the 'cases' list to false.
    //TODO perhaps set this up in a different way so that it doesnt have to be stored in the tree for each taken masechta,
    // like perhaps set it static in the database
    public static class Combo {
        int sederNum;

        int masechtaNum;
        public Combo(int e1, int e2) {
            sederNum = e1;
            masechtaNum = e2;
        }

        public int getSederNum() {
            return sederNum;
        }
        public int getMasechtaNum() {
            return masechtaNum;
        }
    }
    public Combo getSeder() {

        int position = -1;
        List<String> zeraim = Arrays.asList("Berachos", "Peah", "Demai", "Kelaim", "Shviis", "Terumos", "Maaseros", "Maaser Sheini", "Chalah", "Orlah", "Bikurim");
        List<String> moed = Arrays.asList("Shabbos", "Eiruvin", "Pesachim", "Shkalim", "Yoma", "Succah", "Beizah", "Rosh Hashana", "Taanis", "Megillah", "Moed Katan", "Chagigah");
        List<String> nashim = Arrays.asList("Yevamos", "Kesubos", "Nedarim", "Nazir", "Sotah", "Gittin", "Kiddushin");
        List<String> nezikin = Arrays.asList("Bava Kama", "Bava Metzia", "Bava Basra", "Sanherdin", "Makkos", "Shevuos", "Edios", "Avodah Zarah", "Avos", "Horios");
        List<String> kodshim =Arrays.asList("Zevachim", "Menachos", "Chullin", "Bechoros", "Eiruchin", "Temurah", "Kerisos", "Meilah", "Tamid", "Middos", "Kinim");
        List<String> taharos = Arrays.asList("Keilim", "Ohalos", "Negaim", "Parah", "Taharos", "Mikvaos", "Niddah", "Machshirin", "Zavim", "Tevul Yom", "Yadayim", "Uktzin");
        if (zeraim.contains(masechtaTaken)) {
            position = zeraim.indexOf(masechtaTaken);
            return new Combo(0, position);
        }
        else if (moed.contains(masechtaTaken)) {
            position = moed.indexOf(masechtaTaken);
            return new Combo(1, position);
        }
        else if (nashim.contains(masechtaTaken)) {
            position = nashim.indexOf(masechtaTaken);
            return new Combo(2, position);
        }
        else if (nezikin.contains(masechtaTaken)) {
            position = nezikin.indexOf(masechtaTaken);
            return new Combo(3, position);
        }
        else if (kodshim.contains(masechtaTaken)) {
            position = kodshim.indexOf(masechtaTaken);
            return new Combo(4, position);
        }
        else if (taharos.contains(masechtaTaken)) {
            position = taharos.indexOf(masechtaTaken);
            return new Combo(5, position);
        }

        return null;
    }
}

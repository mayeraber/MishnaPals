package mna.mishnapals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.List;

/**
 * Created by MNA on 7/28/2017.
 */

public class Case implements Serializable {
    String firstName, fathersName, lastName;
    String userNameOpened;
    //Calendar endDate;
    String caseId;
    //List<List<MasechtaStatus>> masechtos;
    //save case id as entered by user to show on-screen to be more user friendly
    String caseId_raw;
    ArrayList<ArrayList<MasechtaStatus>> masechtos;
    List<Integer> date;
    boolean privateCase;

    String firebaseID;

    public Case() {
        date = new ArrayList<>();
        masechtos = new ArrayList<>();
    }

    public Case(String fn, String fathersN) {
        firstName = fn;

        fathersName = fathersN;
        date = new ArrayList<>();
        masechtos = new ArrayList<>();
    }

    public String getCaseId() {
        return caseId;
    }

    public String getCaseIdRaw() {
        return caseId_raw;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId.substring(caseId.length() - 5, caseId.length());
        this.caseId_raw = caseId.substring(caseId.length() - 5, caseId.length());
    }

    public void setCaseIdPrivate(String caseId) {
        this.caseId = caseId;
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

    public void createMasechtos() {
        //this.masechtos = new ArrayList<>();
        ArrayList<MasechtaStatus> mase = new ArrayList<>();

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

    public boolean isPrivateCase() {
            return privateCase;
        }

    public void setPrivateCase(boolean privateCase) {
            this.privateCase = privateCase;
        }

    //changed some date function names to avoid conflicts with firebase built-in functions
    public void setEndShloshim(int day, int month, int year)
    {
        date.add(year);
        date.add(month);
        date.add(day);
    }
    public List<Integer> getDate()
    {
        return date;
    }

    public List<Integer> getDateYearLast()
    {
        List<Integer> dateF = new ArrayList<>();
        dateF.add(date.get(1));
        dateF.add(date.get(2));
        dateF.add(date.get(0));
        return dateF;
    }

    public Calendar shloshimDateCal() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.getDate().get(0), this.getDate().get(1), this.getDate().get(2));
        return cal;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public ArrayList<ArrayList<MasechtaStatus>> getMasechtos() {return masechtos; }
}

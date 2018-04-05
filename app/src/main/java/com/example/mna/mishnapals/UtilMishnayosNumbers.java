/*
Utility class containing all masechtos names in Hebrew and English, as well as a hashmap connecting each masechta's English name to a tuple containing
its Seder number and the number of that Masechta in the Seder.
This class is used in 'CompletedMasechta' class to provide easy acces to the corect branch in the db treet to set the value of a masechta's 'completed' flag
 */
package com.example.mna.mishnapals;

import java.util.HashMap;

/**
 * Created by MNA on 10/9/2017.
 */

public class UtilMishnayosNumbers {

    public static HashMap<String, SederMishnaNumberTuple> mishnaNums;

    public UtilMishnayosNumbers() {
        mishnaNums = new HashMap<>();
        String[] zeraimEng, zeraimHeb, moedEng, moedHeb, nashimEng, nashimHeb, nezikinEng, nezikinHeb, kodshimEng, kodshimHeb, taharosEng, taharosHeb;

        zeraimEng = new String[]{"Berachos", "Peah", "Demai", "Kelaim", "Shviis", "Terumos", "Maaseros", "Maaser Sheini", "Chalah", "Orlah", "Bikurim"};
        zeraimHeb = new String[]{"ברכות", "פאה", "דמאי ", "כלאים", "שביעית", "תרומות", "מעשרות", "מעשר שני", "חלה", "ערלה", "ביכורים"};

        moedEng = new String[]{"Shabbos", "Eiruvin", "Pesachim", "Shkalim", "Yoma", "Succah", "Beizah", "Rosh Hashana", "Taanis", "Megillah", "Moed Katan", "Chagigah"};
        moedHeb = new String[]{"שבת", "עירובין", "פסחים", "שקלים", "יומא", "סוכה", "ביצה", "ראש השנה", "תענית", "מגילה", "מועד קטן", "חגיגה"};

        nashimEng = new String[]{"Yevamos", "Kesubos", "Nedarim", "Nazir", "Sotah", "Gittin", "Kiddushin"};
        nashimHeb = new String[]{"יבמות", "כתובות", "נדרים", "נזיר", "סוטה", "גיטין", "קידושין"};

        nezikinEng = new String[]{"Bava Kama", "Bava Metzia", "Bava Basra", "Sanherdin", "Makkos", "Shevuos", "Edios", "Avodah Zarah", "Avos", "Horios"};
        nezikinHeb = new String[]{"בבא קמא", "בבא מציעא", "בבא בתרא", "סנהדרין", "מכות", "שבועות", "עדיות", "עבודה זרה", "אבות", "הוריות"};

        kodshimEng = new String[]{"Zevachim", "Menachos", "Chullin", "Bechoros", "Eiruchin", "Temurah", "Kerisos", "Meilah", "Tamid", "Middos", "Kinim"};
        kodshimHeb = new String[]{"זבחים", "מנחות", "חולין", "בכורות", "ערכין", "תמורה", "כריתות", "מעילה", "תמיד", "מדות", "קנים"};

        taharosEng = new String[]{"Keilim", "Ohalos", "Negaim", "Parah", "Taharos", "Mikvaos", "Niddah", "Machshirin", "Zavim", "Tevul Yom", "Yadayim", "Uktzin"};
        taharosHeb = new String[]{"כלים", "אהלות", "נגעים", "פרה", "טהרות", "מקואות", "נדה", "מכשירין", "זבים", "טבול יום", "ידים", "עוקצין"};

        for (int i = 0; i < zeraimEng.length; i++) {
            mishnaNums.put(zeraimEng[i], new SederMishnaNumberTuple(0, i));
        }
        for (int i = 0; i < moedEng.length; i++) {
            mishnaNums.put(moedEng[i], new SederMishnaNumberTuple(1, i));
        }
        for (int i = 0; i < nashimEng.length; i++) {
            mishnaNums.put(nashimEng[i], new SederMishnaNumberTuple(2, i));
        }
        for (int i = 0; i < nezikinEng.length; i++) {
            mishnaNums.put(nezikinEng[i], new SederMishnaNumberTuple(3, i));
        }
        for (int i = 0; i < kodshimEng.length; i++) {
            mishnaNums.put(kodshimEng[i], new SederMishnaNumberTuple(4, i));
        }
        for (int i = 0; i < taharosEng.length; i++) {
            mishnaNums.put(taharosEng[i], new SederMishnaNumberTuple(5, i));
        }
    }

    class SederMishnaNumberTuple {
        int sederNum;
        int masechtaNum;

        public SederMishnaNumberTuple(int sederNum, int masechtaNum) {
            this.sederNum = sederNum;
            this.masechtaNum = masechtaNum;
        }

        public int getSederNum() {
            return sederNum;
        }

        public void setSederNum(int sederNum) {
            this.sederNum = sederNum;
        }

        public int getMasechtaNum() {
            return masechtaNum;
        }

        public void setMasechtaNum(int masechtaNum) {
            this.masechtaNum = masechtaNum;
        }
    }

}
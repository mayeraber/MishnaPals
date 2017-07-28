package com.example.mna.mishnapals;

/**
 * Created by MNA on 7/25/2017.
 */

public class Masechta {
    String hebName, engName;
    int numPerakim;
    int numMishnayos;

    public Masechta(String engName, String hebName, int numPerakim, int numMishnayos)
    {
        this.hebName = hebName;
        this.engName = engName;
        this.numPerakim = numPerakim;
        this.numMishnayos = numMishnayos;
    }

}

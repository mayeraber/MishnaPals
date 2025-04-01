/*
Class to hold basic info about a masechta
 */
package mna.mishnapals;

import java.io.Serializable;

/**
 * Created by MNA on 7/25/2017.
 */

public class Masechta implements Serializable{
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

package com.example.mna.mishnapals;

import java.io.IOException;

public final class InternetCheckUtility {

    private InternetCheckUtility(){}

    //method based on https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public static boolean internetStatus() {
        boolean connected = false;

        Runtime rt = Runtime.getRuntime();
        try {
            Process ipProcess = rt.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitval = ipProcess.waitFor();
            connected = (exitval == 0);
        }
        catch (IOException e) {e.printStackTrace();}
        catch (InterruptedException e) {e.printStackTrace();}

        return connected;
    }
}

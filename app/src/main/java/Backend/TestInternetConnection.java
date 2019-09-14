package Backend;

import android.app.AlertDialog;
import android.os.Looper;

import java.io.IOException;

import lab.Frontend.MainView.MainActivity;

public class TestInternetConnection implements Runnable {
    @Override
    public void run() {

        while(true){
            if(!isOnline()){
                new AlertDialog.Builder(MainActivity.cont)
                        .setTitle("KEINE INTERNETVERBINDUNG")
                        .setMessage("Um die TCK-App zu benützen brauchst du eine aktive Internetverbindung. Bitte versuche diese herzustellen.")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            try {
                wait(100);
            } catch (InterruptedException e) {}
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

}

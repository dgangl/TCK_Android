package lab.tck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import Backend.Entry;
import Backend.LocalStorage;
import Backend.Person;
import Interfaces.MyBooleanCompletion;

public class Start_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);


        Person p = readCSV();

        p.loginUser();


        /*Entry entry = new Entry(new Date(), "", null, 2, true, 3, null, "PRIVATSPIEL");

        entry.uploadToDatabase(new MyBooleanCompletion() {
            @Override
            public void onCallback(boolean bool) {
                System.out.println("AAC: " + bool);
            }
        });*/



        if(p != null){
            LocalStorage.saveUser(p);
            startActivity(new Intent(Start_Activity.this, MainActivity.class));
        }else{
            startActivity(new Intent(Start_Activity.this, LoginActivity.class));
        }


    }

    public Person readCSV() {


        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("user.csv")))) {
            String line = br.readLine();
            if (line != null) {

                String[] array = line.split(";");
                Person p = new Person(array[0],array[1],array[2],Boolean.getBoolean(array[3]),null);
                return p;
            }else{

                return null;
            }
        } catch (Exception e) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("user.csv")))) {
                String line = br.readLine();
                if (line != null) {

                    String[] array = line.split(";");
                    Person p = new Person(array[0],array[1],array[2],Boolean.getBoolean(array[3]),null);
                    return p;
                }else{

                    return null;
                }
            } catch (Exception p) {

                return null;
            }
        }


    }



}

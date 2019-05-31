package lab.tck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Backend.BackendFeedDatabase;
import Backend.Entry;
import Interfaces.MyBooleanCompletion;
import Interfaces.MyEntryArrayInterface;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        List<Long> temp = new ArrayList<>();
        temp.add(1l);
        temp.add(2l);


        /*Entry entry = new Entry(new Date(), "Erstes Entry", null, 2, true, temp, null, "PRIVATSPIEL");
        entry.uploadToDatabase(new MyBooleanCompletion() {
            @Override
            public void onCallback(boolean bool) {
                System.out.println("AAC" + bool);
            }
        });*/



        BackendFeedDatabase be = new BackendFeedDatabase();
        be.loadAllEvents(new MyEntryArrayInterface() {
            @Override
            public void onCallback(List<Entry> entryList) {
                System.out.println("AAC" +entryList.size());
            }
        });



        //Hide TitleBar & StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new FeedFragment())
                .commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragemnt = null;
            switch (item.getItemId()) {
                case R.id.nav_feed:
                    selectedFragemnt = new FeedFragment();
                    break;
                case R.id.nav_calendar:
                    selectedFragemnt = new CalendarFragment();
                    break;
                case R.id.nav_user:
                    selectedFragemnt = new UserFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, selectedFragemnt)
                    .commit();

            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();


        /*user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
    }




}

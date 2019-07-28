package lab.Frontend.MainView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lab.Frontend.MainView.Fragments.CalendarFragment;
import lab.Frontend.MainView.Fragments.FeedFragment;
import lab.Frontend.MainView.Fragments.UserFragment;
import lab.tck.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    public static Drawable image;
    public static Context cont;
    public static AppCompatActivity activity;
    TextView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        cont = this;

        List<Long> temp = new ArrayList<>();
        temp.add(1l);
        temp.add(2l);

        tx = findViewById(R.id.headerTextView);


        /*Entry entry = new Entry(new Date(), "Erstes Entry", null, 2, true, temp, null, "PRIVATSPIEL");
        entry.uploadToDatabase(new MyBooleanCompletion() {
            @Override
            public void onCallback(boolean bool) {
                System.out.println("AAC" + bool);
            }
        });*/





        try {
            InputStream tempi = getAssets().open("questionmarkimage.jpeg");
            image = Drawable.createFromStream(tempi, null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Hide TitleBar & StatusBar
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#021B79"));
        }

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
                            tx.setText("Meine Spiele");
                            selectedFragemnt = new FeedFragment();
                            break;
                        case R.id.nav_calendar:
                            tx.setText("Platzkalender");
                            selectedFragemnt = new CalendarFragment();
                            break;
                        case R.id.nav_user:
                            tx.setText("Benutzer");
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
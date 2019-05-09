package lab.tck;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide TitleBar & StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
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

}

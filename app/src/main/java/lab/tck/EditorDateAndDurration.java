package lab.tck;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Backend.Entry;

public class EditorDateAndDurration extends AppCompatActivity {

    private Button buttonDatePicker;
    private NumberPicker pickerDurration;
    private Button buttonCoosePlace;
    private Calendar c;
    private Entry newEntry = new Entry();
    private double durration = 0;
    private DatePickerDialog dpd;
    private TimePickerDialog tpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_date_and_durration);

        //Actionbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Toolbar
        Toolbar mToolbar =  findViewById(R.id.toolbar1);

        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //UI
        buttonDatePicker = findViewById(R.id.editor_datepicker);
        buttonCoosePlace = findViewById(R.id.editor_choosePlace);
        pickerDurration = findViewById(R.id.editor_durration);

        String[] durrations =  new String[20];
        for (int i = 1; i  <= 20; i++) {
            durrations[i-1] = i + " Stunden";
        }

        pickerDurration.setMinValue(1);
        pickerDurration.setMaxValue(20);
        pickerDurration.setDisplayedValues(durrations);


        //Buttons
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);

                dpd = new DatePickerDialog(EditorDateAndDurration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int day) {
                        tpd = new TimePickerDialog(EditorDateAndDurration.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(year, month, day, hourOfDay, minute);
                                buttonDatePicker.setText(new SimpleDateFormat("dd.MM.yyyy hh.mm").format(c.getTime()));
                            }
                        }, mHour, mMinute, true);

                        tpd.show();
                    }
                }, mYear, mMonth, mDay);

                dpd.show();

            }
        });


        buttonCoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorDateAndDurration.this, EditorPlaces.class);
                startActivity(intent);
            }
        });

    }
}

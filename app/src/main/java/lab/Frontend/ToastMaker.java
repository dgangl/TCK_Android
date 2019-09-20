package lab.Frontend;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import lab.tck.R;

public class ToastMaker extends AppCompatActivity {

    private TextView text;



    public void createToast(Context context, String inputString) {
        Toast t = Toast.makeText(context, inputString, Toast.LENGTH_LONG);

        t.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_custom_view, null);

        text = layout.findViewById(R.id.toast);
        text.setText(inputString);


        t.setView(layout);
        t.show();
    }
}

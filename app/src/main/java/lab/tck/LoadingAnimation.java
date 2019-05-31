package lab.tck;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;

public class LoadingAnimation extends AppCompatActivity {
    private AlertDialog.Builder builder;

    private void startLoadingAnimation(){



        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingAnimation.this);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

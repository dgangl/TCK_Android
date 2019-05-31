package lab.tck;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoadingAnimation extends AppCompatActivity {
    private  AlertDialog alertDialog;

    public void startLoadingAnimation(){
        ImageView mImageViewFilling = (ImageView) findViewById(R.id.loading);
        mImageViewFilling.setBackgroundResource(R.drawable.loading);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingAnimation.this);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setView(mImageViewFilling);
        alertDialog.show();

    }

    public void closeLoadingAnimation(){
        alertDialog.dismiss();
    }
}

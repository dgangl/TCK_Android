package lab.tck;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;

public class LoadingAnimation extends AppCompatActivity {
    private  AlertDialog alertDialog;
    private Context context;

    public void startLoadingAnimation(Context context){
        this.context = context;

        ImageView mImageViewFilling =  new ImageView(context);
        mImageViewFilling.setBackground(Drawable.createFromPath("@drawable/round.xml"));

        Ion.with(mImageViewFilling).animateGif(AnimateGifMode.ANIMATE).load("file:///android_asset/loading.gif");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setView(mImageViewFilling);
        alertDialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = 500;
        layoutParams.height = 500;



        // Apply the newly created layout parameters to the alert dialog window
        alertDialog.getWindow().setAttributes(layoutParams);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.round);


    }

    public void closeLoadingAnimation(){

        alertDialog.dismiss();
    }
}

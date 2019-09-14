package lab.Frontend.MainView.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import lab.Frontend.MainView.MainActivity;
import lab.Frontend.New_Reservation.Activities.EditorDateAndDurration;
import lab.Frontend.LoadingAnimation;
import lab.Frontend.New_Reservation.Activities.EditorMembers;
import lab.Frontend.ToastMaker;
import lab.tck.R;

public class CalendarFragment extends Fragment {
    private Button neuEntry;
    private WebView wv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root =  inflater.inflate(R.layout.fragment_calendar, container, false);

        neuEntry = root.findViewById(R.id.calendar_newEntry);
        final LoadingAnimation la = new LoadingAnimation();

        String url = "https://tck-app-a1572.firebaseapp.com/home";
        wv = (WebView) root.findViewById(R.id.webview);
        wv.loadUrl(url);
        WebSettings settings = wv.getSettings();
        settings.setDomStorageEnabled(true);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());


        /*
        WebView wv = root.findViewById(R.id.webview);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setHorizontalScrollBarEnabled(false);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl("https://tck-app-a1572.firebaseapp.com/");

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastMaker tm = new ToastMaker();
                tm.createToast(root.getContext(), description);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("!!!!!!!!!!!!!!!!!!", "funkt");
            }
        });*/



        neuEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.cont, EditorDateAndDurration.class);
                startActivity(intent);
            }
        });


        return root;
    }

}


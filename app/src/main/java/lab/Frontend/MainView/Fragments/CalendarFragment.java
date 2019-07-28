package lab.Frontend.MainView.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import lab.Frontend.MainView.MainActivity;
import lab.Frontend.New_Reservation.Activities.EditorDateAndDurration;
import lab.Frontend.LoadingAnimation;
import lab.tck.R;

public class CalendarFragment extends Fragment {
    private Button neuEntry;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, null);

        neuEntry = root.findViewById(R.id.calendar_newEntry);

        final LoadingAnimation la = new LoadingAnimation();
        //la.startLoadingAnimation(root.getContext());

        WebView webView = root.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("Finished Loading!");
                //la.closeLoadingAnimation();
            }
        });

        webView.loadUrl("https://tck-app-a1572.firebaseapp.com");






        neuEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Do this back to EditorDateAndDurration
                Intent intent = new Intent(MainActivity.cont, EditorDateAndDurration.class);
                startActivity(intent);
            }
        });
        return root;

    }
}

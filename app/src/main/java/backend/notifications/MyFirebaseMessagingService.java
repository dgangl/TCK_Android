package backend.notifications;

import android.app.Notification;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import backend.LocalStorage;
import lab.tck.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Log.d("Msg", "Message received [" + remoteMessage.getNotification().getBody() + "]");

            Notification notification = new NotificationCompat.Builder(this, "all")
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(getBaseContext().getApplicationInfo().icon)
                    .build();


            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(100, notification);
        }catch (Exception e){
            Log.e("Error", e.toString());
        }
    }



    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        LocalStorage ls = new LocalStorage();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(ls.loadUser().nummer).update("apnToken", token);


    }

}

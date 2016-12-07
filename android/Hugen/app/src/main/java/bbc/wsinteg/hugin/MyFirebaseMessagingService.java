package bbc.wsinteg.hugin;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by cablej01 on 06/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String,String> m = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + m);
            String id = remoteMessage.getMessageId();
            if(id == null)
                id = remoteMessage.getData().get("title");
            handleData(id, m);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String id = remoteMessage.getMessageId();
            if(id == null)
                id = remoteMessage.getNotification().getTitle();
            handleNotification(id, remoteMessage.getNotification());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private void handleNotification(String id, RemoteMessage.Notification message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", message.getTitle());
        intent.putExtra("body", message.getBody());
        NewsDatabase ndb = new NewsDatabase(getApplicationContext());
        ndb.addItem(id, message.getTitle(), message.getBody());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handleData(String id, Map<String,String> m) {
        Intent intent = new Intent(this, MainActivity.class);
        for(Map.Entry<String,String> e : m.entrySet()) {
            intent.putExtra(e.getKey(), e.getValue());
        }
        NewsDatabase ndb = new NewsDatabase(getApplicationContext());
        ndb.addItem(id, m.get("title"), m.get("body"));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

package com.hkl.mynoti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import static com.hkl.mynoti.MyBroadcastReceiver.ACTION_SNOOZE;
import static com.hkl.mynoti.MyBroadcastReceiver.EXTRA_NOTIFICATION_ID;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    public static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String TAG = "MyNoti";
    public Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createNotificationChannel();
//        Log.e(TAG, "onCreate");
//
//        LongOperation lo = new LongOperation(this);
//        lo.execute("Test 1", "Test 2", "Test 3");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendNotificationp();
            }
        }, 5000);
//        sendNoti();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
//    private NotificationManager mNM = null;
    private int mLastNotificationId;
//    private Context mContext = null;
    private static final int SERVICE_NOTIF_ID = 1;
    private static final int MISSED_CALLS_NOTIF_ID = 2;
    Notification mNotif;
    int xnotificationId = SERVICE_NOTIF_ID;
    public void sendNoti(Context ctx, int aNotificationId) {
//        if(mContext == null) {
//            mContext = ctx;
//        }
//        if(mNM == null) {
//            mNM = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
//        }
//        mNM.cancelAll();
//        Intent intent = new Intent();
//        Intent notifIntent = new Intent(mContext, getClass());
        Intent snoozeIntent = new Intent(ctx, MyBroadcastReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, aNotificationId);
//        notifIntent.putExtra("Notification", true);
//        notifIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(ctx, aNotificationId, snoozeIntent, 0);
//
//        PendingIntent.getBroadcast(
//                        mContext, notificationId, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mLastNotificationId = 5; // Do not conflict with hardcoded notifications ids !
//        Notification notif;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                        .setContentTitle("title")
                        .setContentText("message")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(Notification.PRIORITY_DEFAULT)
                        .setCategory(Notification.CATEGORY_SERVICE)
//                        .setVisibility(Notification.VISIBILITY_SECRET)
//                        .setLights(
//                                ContextCompat.getColor(context, R.color.notification_led_color),
//                                context.getResources().getInteger(R.integer.notification_ms_on),
//                                context.getResources()
//                                        .getInteger(R.integer.notification_ms_off))
//                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true)
                        .setAutoCancel(false)
                        .addAction(R.drawable.ic_launcher_foreground, ctx.getString(R.string.snooze),
                                pendingIntent)
                        ;

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("title")
//                .setContentText("text")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(false)
//                .addAction(R.drawable.ic_launcher_foreground, ctx.getString(R.string.snooze),
//                        snoozePendingIntent);

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(notificationId, builder.build());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(aNotificationId, builder.build());
//        mNM.notify(notificationId, mNotif);
    }
    void sendNotification1(Context ctx, String title, int notificationId) {

        // Create an explicit intent for an Activity in your app
        /* Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0); */

        Intent snoozeIntent = new Intent(ctx, MyBroadcastReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);

        Log.e(TAG, snoozeIntent.getExtras().toString());

        Log.e(TAG, "snoozeIntent id: " + snoozeIntent.getIntExtra(EXTRA_NOTIFICATION_ID, -1));

        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(ctx, notificationId, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("title")
                .setContentText("text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                // Add the action button
                .addAction(R.drawable.ic_launcher_foreground, ctx.getString(R.string.snooze),
                        snoozePendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    private class LongOperation extends AsyncTask<String, String, String> {

        private static final String TAG = "longoperation";
        private Context ctx;
        private AtomicInteger notificationId = new AtomicInteger(0);

        LongOperation(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            for (String s : params) {
                Log.e(TAG, s);

                publishProgress(s);

                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                }
            }
            return "Executed";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            for (String title: values) {
//                sendNotification(title, notificationId.incrementAndGet());
//                sendNotification1(ctx, title, notificationId.incrementAndGet());
//                sendNoti(ctx, notificationId.incrementAndGet());
                sendNotificationp();
                xnotificationId ++;
            }
        }

        void sendNotification(String title, int notificationId) {

            // Create an explicit intent for an Activity in your app
        /* Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0); */

            Intent snoozeIntent = new Intent(ctx, MyBroadcastReceiver.class);
            snoozeIntent.setAction(ACTION_SNOOZE);
            snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);

            Log.e(TAG, snoozeIntent.getExtras().toString());

            Log.e(TAG, "snoozeIntent id: " + snoozeIntent.getIntExtra(EXTRA_NOTIFICATION_ID, -1));

            PendingIntent snoozePendingIntent =
                    PendingIntent.getBroadcast(ctx, notificationId, snoozeIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(String.format("%s (id %d)", title, notificationId))
                    .setContentText("Much longer text that cannot fit one line...")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(false)
                    // Add the action button
                    .addAction(R.drawable.ic_launcher_foreground, ctx.getString(R.string.snooze),
                            snoozePendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());
        }
    }
    /**
     * Send a sample notification using the NotificationCompat API.
     */
    ArrayList<Runnable> ar = new ArrayList<>();

    public void sendNotificationp() {

        // BEGIN_INCLUDE(build_action)
        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
         * notification service can fire it on our behalf.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        Intent intentgoogle = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.google.com"));
        Intent intentyahoo = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.yahoo.com"));
        Intent intentfb = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.facebook.com"));
        Intent intentown = new Intent(this, getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent pendingIntentGoogle = PendingIntent.getActivity(this, 0, intentgoogle, 0);
        PendingIntent pendingIntentYahoo = PendingIntent.getActivity(this, 0, intentyahoo, 0);
        PendingIntent pendingIntentFacebook = PendingIntent.getActivity(this, 0, intentfb, 0);
        PendingIntent pendingIntentOwn = PendingIntent.getActivity(this, 0, intentown, 0);
        // END_INCLUDE(build_action)
        // BEGIN_INCLUDE (build_notification)
        /**
         * Use NotificationCompat.Builder to set up our notification.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        /** Set the icon that will appear in the notification bar. This icon also appears
         * in the lower right hand corner of the notification itself.
         *
         * Important note: although you can use any drawable as the small icon, Android
         * design guidelines state that the icon should be simple and monochrome. Full-color
         * bitmaps or busy images don't render well on smaller screens and can end up
         * confusing the user.
         */
        builder.setSmallIcon(R.drawable.ic_stat_notification);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.ic_stat_notification, "Google", pendingIntentGoogle);
        builder.addAction(R.drawable.ic_stat_notification, "Yahoo", pendingIntentYahoo);
        // Set the notification to auto-cancel. This means that the notification will disappear
        // after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(false).setOngoing(true).setCategory(NotificationCompat.CATEGORY_ALARM).setLocalOnly(true).setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setWhen(0);
        builder.setFullScreenIntent(pendingIntentFacebook, true);


        /**
         *Build the notification's appearance.
         * Set the large icon, which appears on the left of the notification. In this
         * sample we'll set the large icon to be the same as our app icon. The app icon is a
         * reasonable default if you don't have anything more compelling to use as an icon.
         */
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */
        builder.setContentTitle("BasicNotifications Sample");
        builder.setContentText("Time to learn about notifications!");
        builder.setSubText("Tap to view documentation about notifications.");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        // END_INCLUDE (build_notification)

        // BEGIN_INCLUDE(send_notification)
        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());;
        notificationManager.notify(NOTIFICATION_ID + 1, builder.build());
        // END_INCLUDE(send_notification)
    }
}
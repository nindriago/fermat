package com.bitdubai.android_core.app.common.version_1.notifications;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bitdubai.android_core.app.AppActivity;
import com.bitdubai.android_core.app.ApplicationSession;
import com.bitdubai.android_core.app.common.version_1.ApplicationConstants;
import com.bitdubai.android_core.app.common.version_1.connection_manager.FermatAppConnectionManager;
import com.bitdubai.fermat.R;
import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatStructure;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.FermatBundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * Created by mati on 2016.03.01..
 */
public class NotificationService extends Service {
    public static String LOG_TAG = "NotificationService";
    private final IBinder mBinder = new LocalBinder();
    // map from AppPublicKey to notificationId
    private Map<String,Integer> lstNotifications;

    private Map<Integer,NotificationCompat.Builder> mapNotifications;
    private int notificationIdCount;
    //for progress notifications
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;


    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
    public NotificationService() {
        this.lstNotifications = new HashMap<>();
        this.mapNotifications = new HashMap<>();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Log.v(LOG_TAG, "in onBind: " + intent.getExtras().get(LOG_TAG));
        return mBinder;
    }
    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
    }
    public void notificate(String code,FermatStructure fermatStructure){
        Notification.Builder builder = null;
        if (fermatStructure != null) {
            // notificationIdCount++;
            // lstNotifications.put(fermatStructure.getPublicKey(),notificationIdCount);
            AppConnections fermatAppConnection = FermatAppConnectionManager.getFermatAppConnection(fermatStructure.getPublicKey(), this, ApplicationSession.getInstance().getAppManager().getAppsSession(fermatStructure.getPublicKey()));
            NotificationPainter notificationPainter = null;
            try {
                notificationPainter = fermatAppConnection.getNotificationPainter(code);
            }catch (Exception e){
            }

            if (notificationPainter != null) {
                if(notificationPainter.showNotification()) {  //get if notification settings enabled view
                    RemoteViews remoteViews = notificationPainter.getNotificationView(code);
                    Intent intent = new Intent(this,AppActivity.class);
                    intent.putExtra(ApplicationConstants.INTENT_DESKTOP_APP_PUBLIC_KEY, fermatStructure.getPublicKey());
                    intent.putExtra(ApplicationConstants.ACTIVITY_CODE_TO_OPEN,notificationPainter.getActivityCodeResult());
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pi = PendingIntent
                            .getActivity(this, 0, intent, 0);
                    if (remoteViews != null) {
                        builder = new Notification.Builder(this).setSmallIcon(R.drawable.fermat_logo_310_x_310).setTicker("ticker")
                                .setPriority(Notification.PRIORITY_LOW).setAutoCancel(true)
                                .setAutoCancel(true)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                .setLights(Color.YELLOW, 3000, 3000)
                                .setContent(remoteViews)
                                .setWhen(System.currentTimeMillis());
                    } else {
                        builder = new Notification.Builder(this)
                                .setTicker(notificationPainter.getNotificationTitle())
                                .setSmallIcon((notificationPainter.getIcon() <= 0) ? R.drawable.fermat_logo_310_x_310 : notificationPainter.getIcon())
                                .setContentTitle(notificationPainter.getNotificationTitle())
                                .setContentText(notificationPainter.getNotificationTextBody())
                                .setContentIntent(pi)
                                .setAutoCancel(true)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                .setLights(Color.YELLOW, 3000, 3000);
                    }
//                    NotificationManager notificationManager = (NotificationManager)
//                            getSystemService(NOTIFICATION_SERVICE);
//                    notificationManager.notify(0, builder.build());
                }


            }else{
                Intent intent = new Intent(this,AppActivity.class);
                intent.putExtra(ApplicationConstants.INTENT_DESKTOP_APP_PUBLIC_KEY, fermatStructure.getPublicKey());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pi = PendingIntent
                        .getActivity(this, 0, intent, 0);
                builder = new Notification.Builder(this)
                        .setTicker("Something arrive")
                        .setSmallIcon(R.drawable.fermat_logo_310_x_310)
                        .setContentTitle("Fermat: new notification")
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.YELLOW, 3000, 3000);
            }

        } else {
            builder = new Notification.Builder(this)
                    .setTicker("Something arrive")
                    .setSmallIcon(R.drawable.fermat_logo_310_x_310)
                    .setContentTitle("Fermat: new notification")
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.YELLOW, 3000, 3000);
        }
        if(builder!=null) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(/*(fermatStructure!=null)?notificationId:*/0, builder.build());
        }
    }


    public int notificateProgress(FermatBundle bundle) {
        NotificationCompat.Builder mBuilder;
        try {
            int progress = (int) bundle.getSerializable(Broadcaster.PROGRESS_BAR);
            int publishId = (bundle.contains(Broadcaster.PUBLISH_ID)) ? bundle.getInt(Broadcaster.PUBLISH_ID) :0;
            String progressText = (bundle.contains(Broadcaster.PROGRESS_BAR_TEXT)) ? bundle.getString(Broadcaster.PROGRESS_BAR_TEXT):null;

            mNotifyManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            if(progress<0 || progress>100){
                mNotifyManager.cancel(publishId);
            }else {

// Displays the progress bar for the first time.

                if(publishId==0){
                    mBuilder = new NotificationCompat.Builder(this);
                    mBuilder.setContentTitle((progressText!=null)? progressText:"Downloading something...")
                            .setContentText("Download in progress")
                            .setSmallIcon(R.drawable.fermat_logo_310_x_310);
                    Random random = new Random();
                    publishId = random.nextInt();
                    if(publishId<0){
                        publishId = publishId*(-1);
                    }
                    mapNotifications.put(publishId,mBuilder);
                }else {
                    if(mapNotifications.containsKey(publishId))
                        mBuilder = mapNotifications.get(publishId);
                    else {
                        Log.i(LOG_TAG, "Error, Notification id not found");
                        return 0;
                    }
                }
                mBuilder.setProgress(100, progress, false);

                mNotifyManager.notify(publishId, mBuilder.build());
                return publishId;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void notificateProgress(final String code,int progress){
        if(!lstNotifications.containsKey(code)){
            notificationIdCount++;
            lstNotifications.put(code,notificationIdCount);
        }
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Downloading blockchain blocks")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.fermat_logo_310_x_310);
// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
// Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
// Sets the progress indicator to a max value, the
// current completion percentage, and "determinate"
// state
                            mBuilder.setProgress(100, incr, false);
// Displays the progress bar for the first time.
                            mNotifyManager.notify(lstNotifications.get(code), mBuilder.build());
// Sleeps the thread, simulating an operation
// that takes time
                            try {
// Sleep for 5 seconds
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d(LOG_TAG, "sleep failure");
                            }
                        }
// When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
// Removes the progress bar
                                .setProgress(0,0,false);
                        mNotifyManager.notify(lstNotifications.get(code), mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }
}
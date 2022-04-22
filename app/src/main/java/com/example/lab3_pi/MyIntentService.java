package com.example.lab3_pi;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {

    private static final String action_task1 = "com.example.intent_service.action.task1";
    private static final String parameter1 = "com.example.intent_service.extra.parameter1";
    private static final int notification_id = 1;
    private NotificationManager notificationManager;

    public static void startTask(Context context, int parameter) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(action_task1);
        intent.putExtra(parameter1,parameter);
        context.startService(intent);
    }

    /**
     * @param name
     * @deprecated
     */
    public MyIntentService(String name) {
        super(name);
    }

    public MyIntentService(){
        super("DisplayNotification");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        prepareNotification();
        startForeground(notification_id,createNotification());

        if (intent != null) {
            final String action = intent.getAction();
            if (action_task1.equals(action)) {
                final int param1 = intent.getIntExtra(parameter1,0);
                doTask(param1);
            }else{
                Log.e("intent_service","unknown action");
            }
        }
        Log.d("intent_service","Task finished successfully");
    }

    private void doTask(int param) {
        updateNotification();
    }

    private void prepareNotification(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel("1",name,NotificationManager.IMPORTANCE_HIGH); /// 1 to id kanalu
            notificationManager.createNotificationChannel((channel));
        }
    }

    private Notification createNotification() {
        Intent intentNotf = new Intent(this, MainActivity.class);
//        intentNotf.putExtra();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intentNotf);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.notification_title))
                            .setContentText("Trwa pobieranie pliku ...")
//                            .setProgress(100,downloadValue(),false)
//                            .setContentIntet(waitingIntent)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setChannelId("1");
//        if(downloadValue()!=100){
//            notificationBuilder.setOngoing(false);
//        }else{
//            notificationBuilder.setOngoing(true);
//        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationBuilder.setChannelId("1");  /// 1 to id kanalu
        }

        return notificationBuilder.build();
    }

    private void updateNotification() {
        notificationManager.notify(1,createNotification());
    }

}

package com.example.lab3_pi;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class MyIntentService extends IntentService implements DownloadActivity.updateDowloadedData {

    private static final String action_task1 = "com.example.intent_service.action.task2";
    private static final String parameter1 = "com.example.intent_service.extra.parameter2";
    private static final int notification_id = 4;
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.intent_service.notification_channel3";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private int size;
    private int downloadedData;

    public static void startTask(Context context, String parameter) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(action_task1);
        intent.putExtra(parameter1, parameter);
        context.startService(intent);

    }

    public MyIntentService() {
        super("MyIntentService");

    }

    /**
     * Nadpisanie metody obsługującej wykonywanie usługi
     */

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        prepareNotification();
        notificationManager.notify(notification_id, createNotification());

        if (intent != null) {
            final String action = intent.getAction();
            if (action_task1.equals(action)) {
                final String param1 = intent.getStringExtra(parameter1);
                doTask(param1);
            } else {
                Log.e("intent_service", "unknown action");
            }
        }
        Log.d("intent_service", "Task finished successfully");
    }

    /**
     * metoda wykonująca zadanie pobierania i aktualizowania paska postępu
     */
    private void doTask(String param) {
        DownloadActivity download = new DownloadActivity(param, this,this);
        download.execute();
        progressBar();

    }

    /**
     * Utworzenie kanału powiadomień
     */
    private void prepareNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT); /// 1 to id kanalu
            channel.setDescription("Pobieranie w toku");
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setSound(null,null);
            notificationManager.createNotificationChannel(channel);
        }
    }

    int counter = 0;

    /**
     * tworzenie powiadomienia
     */
    private Notification createNotification() {
        Intent intentNotf = new Intent(this, MainActivity.class);
//
        intentNotf.addCategory(Intent.CATEGORY_LAUNCHER);
        intentNotf.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
      //  Log.e("TEST", "WORKs");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intentNotf);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * Ustawienie tutułu i opisu powiadomienia, priorytetu, niektóre chyba ustawienia są niepotrzebne
         * Pamiętać, że każde powiadomienie jest przypisywane do odpowiedniego wcześniej utworzonego kanału
         * W przypadku gdy utworzymy kanał z jakims id o priorytecie HIGH to nie możemy tego zmienic bez usuwania kanału
         * najlepszą opcją jest ustawienie innego id
         */
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Pobieranie pliku")
                .setContentText("Trwa pobieranie pliku ...")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setContentIntent(pendingIntent);



        return notificationBuilder.build();
    }

    private void updateNotification() {
        notificationManager.notify(notification_id, createNotification());
    }

    /**
     * Metoda aktualizująca pasek postępu w powiadomieniach.
     */
    private void progressBar() {

        while(downloadedData <= size) {
            // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
            notificationBuilder.setProgress(size, downloadedData, false);
            // Displays the progress bar for the first time.
            notificationManager.notify(notification_id, notificationBuilder.build());
            // Sleeps the thread, simulating an operation
            try {
                // Sleep for 1 second
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                Log.d("TAG", "sleep failure");
            }
            if(downloadedData==size){
                notificationBuilder.setContentText("Download completed")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                notificationManager.notify(notification_id, notificationBuilder.build());
                break;
            }
        }
    }

    /**
     * Nadpisanie metod interfejsu aby przekazac dane do innej klasy
     */
    @Override
    public void updateBytes(int downloadedBytes) {
        this.downloadedData = downloadedBytes;
    }

    @Override
    public void updateSize(int size) {
        this.size = size;
    }
}


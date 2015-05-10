package project.cs442.team9.georeminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceClass extends Service {
    int k=0,num=1;
    boolean b=true;
    String s,adr;
    public ServiceClass() {
    }
    public void onCreate()
    {

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("---------------->", "Service  is started");
        Bundle b=intent.getExtras();
        //int s=b.getInt("Num");
        s=b.getString("listn");
        adr=b.getString("adr");
       // num =s;//Integer.parseInt(s);
        Context a=getApplicationContext();
        pthread p=new pthread(a);//start Counter Thread
        p.start();
        // Toast.makeText(this, "Service Sta"+num, Toast.LENGTH_SHORT).show();
        return START_STICKY;


    }


    public void onDestroy()  //stop service
    {
        super.onDestroy();
        //Log.d("InDestroy","InDestroy");
        Toast.makeText(this, "My Counter Service Stopped", Toast.LENGTH_LONG).show();
        num=0;
        k=0;
        b=false;


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    private class pthread extends Thread
    {  Intent notification=new Intent();
        Context a;
        // int k;
        private pthread(Context c) {
            a=c;
  //          k=0;
            notification=new Intent(a,ServiceClass.class);

            //  PendingIntent pi=PendingIntent.getActivities(this,0,notification,0);

        }

        @Override
        public void run() {
            super.run();

       //     while(b)
            {

               // try
                {
         //           Thread.sleep(1000);   //Increment Counter
                    Log.d("---------------->", "Service  is in while:"+num);
          //          num++;
         //           k++;
                }
                //catch (InterruptedException e)
                {
                 //   e.printStackTrace();
                }
              //  if(k%10==0)
                {
                    // Notification n = new Notification.Builder(a).setOngoing(true).setContentTitle("Counter:").setContentText(""+num).build();
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //create notification

                    Notification.Builder builder =
                            new Notification.Builder(ServiceClass.this);

                    builder.setSmallIcon(R.drawable.ic_launcher1)
                            .setTicker("Notification")
                            .setContentTitle(s)
                            .setContentText(adr)
                            .setWhen(System.currentTimeMillis())
                            .setDefaults(Notification.DEFAULT_SOUND |
                                    Notification.DEFAULT_VIBRATE)
                            .setSound(
                                    RingtoneManager.getDefaultUri(
                                            RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.RED, 0, 1);

                    Notification notification = builder.getNotification();


                    // Send the notification
                    nm.notify(1, notification);






                    // startForeground(num,n);
                }

            }
        }

    }

}

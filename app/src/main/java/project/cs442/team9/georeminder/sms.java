package project.cs442.team9.georeminder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class sms extends Service {
    int k=0,num=1,d;
    boolean b=true;
    String s,adr;
    Context mycontext;
    AudioManager audiomanage;
    String number,message;

    public sms() {
    }

    public void onCreate()
    {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("---------------->", "Service  is started");
        Bundle b=intent.getExtras();
        number=b.getString("num");
        message=b.getString("msg");
        //int s=b.getInt("Num");
        //    s=b.getString("listn");
        //   adr=b.getString("adr");
        // num =s;//Integer.parseInt(s);
        Context a=getApplicationContext();
        mycontext=a;
        pthread p=new pthread(a);//start Counter Thread
        p.start();
        // Toast.makeText(this, "Service Sta"+num, Toast.LENGTH_SHORT).show();
        return START_STICKY;


    }


    public void onDestroy()  //stop service
    {
        super.onDestroy();
        //Log.d("InDestroy","InDestroy");
        //  Toast.makeText(this, "My Counter Service Stopped", Toast.LENGTH_LONG).show();
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
                    Log.d("---------------->", "Service  is in sms :"+num);
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(number, null, message, null, null);


                    Log.d("phone:","msg sent");

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


                    // startForeground(num,n);
                }

            }
        }

    }
}

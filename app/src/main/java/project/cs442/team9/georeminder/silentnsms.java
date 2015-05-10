package project.cs442.team9.georeminder;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Datta on 4/17/2015.
 */
public class silentnsms extends Activity
{ Context mycontext;
    AudioManager audiomanage;
    int d;

    silentnsms(Context cc)
     {
         mycontext=cc;
     }
    public void silent()
    {
        audiomanage = (AudioManager)getSystemService(mycontext.AUDIO_SERVICE);
        d= audiomanage.getRingerMode();
        audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
     //   Toast.makeText(this, "Phone Turned to Silent", Toast.LENGTH_LONG).show();
    }

    public void revert()
    {
        audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Toast.makeText(this, "Phone Mode is Restored back", Toast.LENGTH_LONG).show();
    }
    public void txtmsg(String number,String message)
    {
        try{

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number, null, message, null, null);
      //      Toast.makeText(this, "Msg Sending", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
//            Toast.makeText(this, "Msg Sending Failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}

package project.cs442.team9.georeminder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Phone extends ActionBarActivity {

    TextView message;
    TextView ph_1;
    TextView ph_2;
    Button btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ph_1= (TextView) findViewById(R.id.editText2);
        message= (TextView) findViewById(R.id.editText5);
        message.setSingleLine(false);

        btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE },
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        String number = c.getString(0);
                        int type = c.getInt(1);
                        showSelectedNumber(type, number);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }
    public void showSelectedNumber(int type, String number) {
          ph_1.setText(number);

        //Toast.makeText(this, type + ": " + number, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void Save() {
        String num = ph_1.getText().toString();
        String text = message.getText().toString();
        if (num.matches("^$")) {
            Toast.makeText(this, " Please Enter Phone Number ", Toast.LENGTH_LONG).show();
            ph_1.setText("");
        } else if (text.matches("^$")) {
            Toast.makeText(this, " Please Select a Location ", Toast.LENGTH_LONG).show();
            message.setText("");
        } else {
            Intent n = new Intent();
            n.putExtra("phn", num);
            n.putExtra("msg",text);
            setResult(2, n);
            finish();
        }
    }
}

package uk.ac.york.tftv.im.catastrophic.catastrophiccoding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    private FileWriter writer;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    public void click(View v) {
        Button b = (Button)v;
        writeLine((String.valueOf(b.getText())).replace("\n"," "));
    }

    private void writeLine(String line) {
        //write timestamp and button to csv based on todays date
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String timestamp = (String)df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        String date = (String)df.format("yyyyMMdd",new java.util.Date());


        if(writer==null){
            try {
                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/CatastrophicLogs/");
                if(!path.exists()) path.mkdirs();
                File out = new File(path,"/"+date+".csv");
                if(!out.exists()) out.createNewFile();

                writer = new FileWriter(out, true);
            }
            catch (IOException e) {
                (Toast.makeText(this,"Error opening output file", Toast.LENGTH_SHORT)).show();
                e.printStackTrace();
                return;
            }
        }
        try {
            writer.append(timestamp+","+line+"\n");
            writer.flush();
            (Toast.makeText(this,"Logged", Toast.LENGTH_SHORT)).show();
            vibrator.vibrate(100);

        } catch (IOException e) {
            (Toast.makeText(this,"Error writing to output file", Toast.LENGTH_SHORT)).show();
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(writer!=null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

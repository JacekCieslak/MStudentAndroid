package pl.edu.prz.mstudent.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.prz.mstudent.R;


public class IntroActivity extends Activity {

    private static ArrayList<Activity> activities=new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);


        TextView tx = (TextView)findViewById(R.id.mstudentLogo);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),
                "fonts/Sketchtica.ttf");
        tx.setTypeface(custom_font);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }


        }.start();
    }
}
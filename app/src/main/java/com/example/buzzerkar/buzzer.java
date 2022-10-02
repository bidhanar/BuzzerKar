package com.example.buzzerkar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class buzzer extends AppCompatActivity {

    Button buzz;
    Button exit;
    Button refresh;
    TextView round;
    String curr_round;
    Intent intent;
    String user;
    String game_name;
    String time;
    String time_utc;
    Calendar calendar;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzzer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.game_color));
        }


        final MediaPlayer sound = MediaPlayer.create(getApplicationContext(), R.raw.buzz);

        buzz = findViewById(R.id.buzz);
        exit = findViewById(R.id.exit);
        refresh = findViewById(R.id.refresh);
        listView = findViewById(R.id.listView);
        curr_round = "Round_1";
        intent = getIntent();
        game_name = intent.getStringExtra("game_name");
        user = intent.getStringExtra("name");


        arrayList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        TimeZone.setDefault(TimeZone.getTimeZone("KolKata"));



        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Query Code
                ParseQuery<ParseObject> query = new ParseQuery<>(game_name);
                query.addAscendingOrder("createdAt");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        arrayList.clear();
                        if(e == null){
                            int i = 1;
                            for(ParseObject object : objects){
                                if(object.getString("user") != null){
                                    arrayList.add(String.valueOf(i) + " " + object.getString("user"));
                                    i++;
                                }

                            }
                            arrayAdapter = new ArrayAdapter(getApplicationContext() , R.layout.text_items_layout, arrayList);
                            listView.setAdapter(arrayAdapter);

                        }
                        else{
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        buzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.start();
                buzz.setEnabled(false);
                buzz.setVisibility(View.INVISIBLE);
//
//                calendar = Calendar.getInstance();
//                Date now = new Date();
//
//                time = sdf.format(calendar.getTimeInMillis());
//                time_utc = sdf.format(now);
//
//                float temp = Integer.parseInt(time.substring(9,12)) + offset;
//                String add;
//                if(temp < 10){
//                    add = "00" + String.valueOf(temp);
//                }
//                else if(temp < 100){
//                    add = "0" + String.valueOf(temp);
//                }
//                else{
//                    add = String.valueOf(temp);
//                }
//
//                String corrected = time.substring(0,9) + add;



                ParseObject parseObject = new ParseObject(game_name);
                parseObject.put("user", user);
//                parseObject.put("time",time);
//                parseObject.put("time_utc" , time_utc);
//                parseObject.put("corrected", corrected);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    buzz.setEnabled(true);
                                    buzz.setVisibility(View.VISIBLE);

                                }
                            },5000);// set time as per your requirement
                        }
                        else{
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        buzz.setEnabled(true);
                        buzz.setVisibility(View.VISIBLE);

                    }
                },5000);
//                int i;
//                for(i = 0; i < 10; i++){
//
//                    ParseQuery<ParseObject> query = new ParseQuery<>(game_name);
//                    query.addAscendingOrder("createdAt");
//                    query.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> objects, ParseException e) {
//                            arrayList.clear();
//                            if(e == null){
//                                int i = 1;
//                                for(ParseObject object : objects){
//                                    if(object.getString("user") != null){
//                                        arrayList.add(String.valueOf(i) + " " + object.getString("user"));
//                                        i++;
//                                    }
//
//                                }
//                                arrayAdapter = new ArrayAdapter(getApplicationContext() , R.layout.text_items_layout, arrayList);
//                                listView.setAdapter(arrayAdapter);
//
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

//                final Handler handler = new Handler();
//                final int delay = 3000; // 1000 milliseconds == 1 second
//
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//
//
//                        ParseQuery<ParseObject> query = new ParseQuery<>(game_name);
//                        query.addAscendingOrder("createdAt");
//                        query.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> objects, ParseException e) {
//                                arrayList.clear();
//                                if(e == null){
//                                    int i = 1;
//                                    for(ParseObject object : objects){
//                                        if(object.getString("user") != null){
//                                            arrayList.add(String.valueOf(i) + " " + object.getString("user"));
//                                            i++;
//                                        }
//
//                                    }
//                                    arrayAdapter = new ArrayAdapter(getApplicationContext() , R.layout.text_items_layout, arrayList);
//                                    listView.setAdapter(arrayAdapter);
//
//                                }
//                                else{
//                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });
//
//                        handler.postDelayed(this, delay);
//                    }
//                }, delay);

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed () {

    }
}
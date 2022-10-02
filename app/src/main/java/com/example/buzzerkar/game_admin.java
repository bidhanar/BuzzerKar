package com.example.buzzerkar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class game_admin extends AppCompatActivity {

    Button next;
    Button refresh;
    Button end;
    Intent intent;
    String game_name;
    ListView listView;
    ArrayList<String> arrayList = null;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_admin);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.game_color));
        }


        next = findViewById(R.id.next);
        refresh = findViewById(R.id.admin_refresh);
        end = findViewById(R.id.end);
        listView = findViewById(R.id.admin_listview);
        arrayList = new ArrayList<>();
        final MediaPlayer sound = MediaPlayer.create(getApplicationContext(), R.raw.buzz);

        intent = getIntent();
        game_name = intent.getStringExtra("game_name");

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(game_name);

                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> results, ParseException e) {

                        if (e == null) {
                            for (ParseObject reply : results) {
                                reply.deleteInBackground();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                ParseObject object = new ParseObject(game_name);
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                ParseQuery<ParseObject> games = ParseQuery.getQuery("password");
                games.whereEqualTo("game",game_name);
                games.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for(ParseObject game: objects){
                            game.deleteInBackground();
                        }
                    }
                });


                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery(game_name);
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> results, ParseException e) {

                        if (e == null) {
                            for (ParseObject reply : results) {
                                reply.deleteInBackground();

                            Toast.makeText(getApplicationContext(),"Done", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });













                ParseObject object = new ParseObject(game_name);
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(getApplicationContext(), "Next Round", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                ParseObject parseObject = new ParseObject(game_name);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(getApplicationContext(), "Object created successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        final Handler handler = new Handler();
        final int delay = 3000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {


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
                            if(arrayList.size() == 1)
                            arrayAdapter = new ArrayAdapter(getApplicationContext() , R.layout.text_items_layout, arrayList);
                            listView.setAdapter(arrayAdapter);

                        }
                        else{
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onBackPressed () {

    }
}
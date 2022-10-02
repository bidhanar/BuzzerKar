package com.example.buzzerkar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;


public class team extends AppCompatActivity {

    EditText name;
    EditText game_name;
    Button start;
    Intent intent;
    Calendar calendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.game_color));
        }

        name = findViewById(R.id.name);
        game_name = findViewById(R.id.game_name);
        start = findViewById(R.id.start);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().replaceAll("\\s", "") != null && game_name.getText().toString().replaceAll("\\s", "") != null){

                    ParseQuery query = ParseQuery.getQuery("password");
                    query.whereEqualTo("game", game_name.getText().toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e == null){
                                if(objects.size() == 1){
                                    intent = new Intent(getApplicationContext(),buzzer.class);
                                    intent.putExtra("game_name", game_name.getText().toString().replaceAll("\\s", ""));
                                    intent.putExtra("name", name.getText().toString().replaceAll("\\s", ""));
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(getApplicationContext(),"Game doesn't exist",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

    }
}
package com.example.buzzerkar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class admin extends AppCompatActivity {

    Button create_game;
    EditText game;
    EditText password;
    Intent intent;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.game_color));
        }

        create_game = findViewById(R.id.create_game);
        game = findViewById(R.id.game);
        password = findViewById(R.id.password);


        create_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag = 0;

                ParseQuery<ParseObject> query = new ParseQuery<>("password");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null){
                            int i = 1;
                            if(objects.size() > 0){
                                for(ParseObject object : objects){
                                    Toast.makeText(getApplicationContext(), object.getString("passphrase").replaceAll("\\s", ""), Toast.LENGTH_SHORT).show();
                                    if(object.getString("game").replaceAll("\\s", "").equals(game.getText().toString().replaceAll("\\s", ""))){
                                        flag = 1;
                                        break;
                                    }
                                }
                            }


                            if(flag == 1){

                                ParseQuery<ParseObject> pass_query = new ParseQuery<>("password");
                                pass_query.whereEqualTo("game", game.getText().toString().replaceAll("\\s", ""));
                                pass_query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e == null){

                                            for(ParseObject object : objects){
                                                if(object.getString("passphrase").replaceAll("\\s", "").equals(password.getText().toString().replaceAll("\\s", ""))){
                                                    intent = new Intent(getApplicationContext(), game_admin.class);
                                                    intent.putExtra("game_name" , game.getText().toString().replaceAll("\\s", ""));
                                                    startActivity(intent);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "Incorrect Password!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

//                                if(password.getText().toString().replaceAll("\\s", "").equals(objects.get(0).getString("passphrase"))){
//                                    intent = new Intent(getApplicationContext(), game_admin.class);
//                                    intent.putExtra("game_name" , game.getText().toString().replaceAll("\\s", ""));
//                                    startActivity(intent);
//                                }

                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Error in password field", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                            else if(flag == 0){
                                ParseObject parseObject = new ParseObject(game.getText().toString().replaceAll("\\s", ""));
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            Toast.makeText(getApplicationContext(), "Game created successfully...", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                                ParseObject passphrase = new ParseObject("password");
                                passphrase.put("game",game.getText().toString().replaceAll("\\s", "") );
                                passphrase.put("passphrase", password.getText().toString().replaceAll("\\s", ""));
                                passphrase.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            Toast.makeText(getApplicationContext(), "Password created successfully...", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });



                                intent = new Intent(getApplicationContext(), game_admin.class);
                                intent.putExtra("game_name" , game.getText().toString().replaceAll("\\s", ""));
                                startActivity(intent);
                            }













                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });
    }
}
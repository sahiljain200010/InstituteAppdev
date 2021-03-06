package com.example.anant.iitbhuvaranasi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.anant.iitbhuvaranasi.BackendResponse.Api_Response;
import com.example.anant.iitbhuvaranasi.Constants;
import com.example.anant.iitbhuvaranasi.Models.CLubFeedData;
import com.example.anant.iitbhuvaranasi.R;
import com.example.anant.iitbhuvaranasi.Interfaces.ServerCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PinActivity extends AppCompatActivity {

    private ArrayList<CLubFeedData> mExampleList;
    private ArrayList<String> ClubNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        Toolbar toolbar = findViewById(R.id.toolbar_pin_activity);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));


        Api_Response.method(this, new ServerCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        String response45678 = pref2.getString(Constants.Response_Feed_Old, "2");



        try {
            JSONObject jsonObject = new JSONObject(response45678);
            int status = jsonObject.getInt("status");
            JSONArray allcouncils = jsonObject.getJSONArray("councils");
//            JSONObject council = allcouncils.getJSONObject(position);
//            JSONArray clubs = council.getJSONArray("clubs");

            for (int i = 0; i < allcouncils.length(); i++) {
                JSONObject council = allcouncils.getJSONObject(i);
                JSONArray clubs = council.getJSONArray("clubs");
                for (int j = 0; j < clubs.length(); j++) {
                    JSONObject club = clubs.getJSONObject(j);
                    String clubName = club.getString("name");
                    ClubNames.add(clubName);
                }

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayout subList = (LinearLayout) findViewById(R.id.sub_list);
        SharedPreferences sharedPrefs = getSharedPreferences("com.example.anant.iitbhuvaranasi", MODE_PRIVATE);

        for (int i = 0; i < ClubNames.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.pin_item, null);
            final Switch subItem = (Switch) view.findViewById(R.id.switch_for_pin);
            String clubName= ClubNames.get(i);
            String[] separated = clubName.split(" ");

            final int temp = i;
            if(sharedPrefs.getBoolean("000" + i, false)) {
                FirebaseMessaging.getInstance().subscribeToTopic(separated[0])
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Succcess";
                                if (!task.isSuccessful()) {
                                    msg = "Failed";
                                }


                            }
                        });
            }
            else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic(separated[0])
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Succcess";
                                if (!task.isSuccessful()) {
                                    msg = "Failed";
                                }


                            }
                        });
            }
            subItem.setText(clubName);
            subItem.setId(i);
            subItem.setChecked(sharedPrefs.getBoolean("000" + i, false));
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subItem.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences("com.example.anant.iitbhuvaranasi", MODE_PRIVATE).edit();
                        editor.putBoolean("000" + temp, true);
                        editor.commit();
                        FirebaseMessaging.getInstance().subscribeToTopic(separated[0])
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Subscribed to "+clubName+" for notifications";
                                        if (!task.isSuccessful()) {
                                            msg = "Failed";
                                        }

                                        Toast.makeText(PinActivity.this, msg, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences("com.example.anant.iitbhuvaranasi", MODE_PRIVATE).edit();
                        editor.putBoolean("000" + temp, false);
                        editor.commit();
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(separated[0])
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Unsubscribed to "+clubName+" for notifications";
                                        if (!task.isSuccessful()) {
                                            msg = "Failed";
                                        }

                                        Toast.makeText(PinActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                }
            });

            subList.addView(view);
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);

        finish();

    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
        return true;
    }
}




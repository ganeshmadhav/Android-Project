package com.example.csci571.homework9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommiteeDetails extends AppCompatActivity {
JSONObject jsonob;
    ImageButton favcom;
    boolean b_isThere = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitee_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        toolbar.setTitle("Committee Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();// this line
        favcom = (ImageButton) findViewById(R.id.commstar);
        String details = (String) getIntent().getSerializableExtra("commString");
        try {
            jsonob = new JSONObject(details);
            try {
                String leg_fav = preferences.getString("committee_data", "null");
                JSONArray initVal;
                initVal = new JSONArray(leg_fav);
                for(int i=0; i<initVal.length();i++){
                    if((jsonob.getString("committee_id").equals(((JSONObject) initVal.get(i)).getString("committee_id")))){
                        favcom.setImageResource(R.mipmap.ic_starison);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView comm_id = (TextView) findViewById(R.id.committeeinfo_id);
            TextView comm_name = (TextView) findViewById(R.id.namecomm_id);
            TextView comm_chamber = (TextView) findViewById(R.id.chamber_id);
            TextView comm_pid = (TextView) findViewById(R.id.parent_id);
            TextView comm_contact = (TextView) findViewById(R.id.contctinfo_id);
            TextView comm_office = (TextView) findViewById(R.id.office_infoid);
            ImageView img = (ImageView) findViewById(R.id.chamber_image);
            String c_id = jsonob.getString("committee_id").toUpperCase();
            if(c_id.equals("null")){
                comm_id.setText("N.A");
            } else {
                comm_id.setText(c_id);
            }
            String name = jsonob.getString("name");
            if(name.equals("null")){
                comm_name.setText("N.A");
            } else {
                comm_name.setText(name);
            }
            String chamber = jsonob.getString("chamber");
            if(chamber.equals("house")){
                Picasso.with(this).load("http://cs-server.usc.edu:45678/hw/hw8/images/h.png").resize(80,80).into(img);
            } else if(chamber.equals("senate")){
                /*Picasso.with(this).load("https://commons.wikimedia.org/wiki/File:US-Senate-Logo.svg").resize(80,80).into(img);*/
            } else {
                /*Picasso.with(this).load("https://commons.wikimedia.org/wiki/File:US-Senate-Logo.svg").resize(80,80).into(img);*/
            }
            chamber =  chamber.substring(0, 1).toUpperCase() + chamber.substring(1);
            comm_chamber.setText(chamber);
            if(jsonob.has("parent_committee_id")){
                String parent_id = jsonob.getString("parent_committee_id");
                if(parent_id.equals("null")){
                    comm_pid.setText("N.A");
                } else {
                    parent_id = parent_id.toUpperCase();
                    comm_pid.setText(parent_id);
                }
            } else {
                comm_pid.setText("N.A");
            }

            if(jsonob.has("phone") == false || jsonob.getString("phone").equals("null")){
                comm_contact.setText("N.A");
            } else {
                comm_contact.setText(jsonob.getString("phone"));
            }
            if(jsonob.has("office") == false || jsonob.getString("office").equals("null")){
                comm_office.setText("N.A");
            } else {
                comm_office.setText(jsonob.getString("office"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        favcom = (ImageButton) findViewById(R.id.commstar);
        favcom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String leg_fav = preferences.getString("committee_data", "null");
                if(leg_fav.equals("null")){
                    JSONArray putVal = new JSONArray();
                    putVal.put(jsonob);
                    favcom.setImageResource(R.mipmap.ic_starison);
                    editor.putString("committee_data",putVal.toString());
                    editor.apply();
                } else {
                    try {
                        JSONArray putVal = new JSONArray(leg_fav);
                        JSONArray list = new JSONArray();
                        for(int i=0; i<putVal.length();i++){
                            if(!(jsonob.getString("committee_id").equals(((JSONObject) putVal.get(i)).getString("committee_id")))){
                                list.put(putVal.get(i));
                            } else {
                                b_isThere = true;
                            }
                        }
                        if(b_isThere == false){
                            putVal.put(jsonob);
                            favcom.setImageResource(R.mipmap.ic_starison);
                            editor.putString("committee_data",putVal.toString());
                            editor.apply();
                        } else{
                            /*if(list.length() == 0){
                                SharedPreferences.Editor edit = preferences.edit();
                                edit.clear().commit();
                            }else{*/
                                editor.putString("committee_data",list.toString());
                                editor.apply();
                           /* }*/
                            b_isThere = false;
                            favcom.setImageResource(R.drawable.ic_star_nofill);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

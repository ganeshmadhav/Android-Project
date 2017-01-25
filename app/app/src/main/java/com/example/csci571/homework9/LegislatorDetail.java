package com.example.csci571.homework9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LegislatorDetail extends AppCompatActivity {
  JSONObject jsonob;
    ImageButton fbButton;
    ImageButton twButton;
    ImageButton webButton;
    ImageButton favButton;
    boolean b_isThere = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String details;
    public static final String MY_PREFS_NAME = "favorite_legislators";
    WebView webview;
    String cham;
    String stDate, enDate, birthdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislator_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Legislator Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);              // this line
        details = (String) getIntent().getSerializableExtra("userProfileString");
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();


        try {
            jsonob = new JSONObject(details);
            String leg_fav = preferences.getString("legislator_data", "null");
            JSONArray initVal;
            favButton = (ImageButton) findViewById(R.id.favoriteleg);
            try {
                initVal = new JSONArray(leg_fav);
                for(int i=0; i<initVal.length();i++){
                    if((jsonob.getString("bioguide_id").equals(((JSONObject) initVal.get(i)).getString("bioguide_id")))){
                        favButton.setImageResource(R.mipmap.ic_starison);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addListenerOnButton();
            ImageButton nm = (ImageButton) findViewById(R.id.facebook);
            ImageView pt = (ImageView) findViewById(R.id.party_icon);
            TextView fullName = (TextView) findViewById(R.id.full_name);
            ImageView img = (ImageView) findViewById(R.id.legislatorphoto);
            TextView email = (TextView) findViewById(R.id.email_id);
            TextView chamber = (TextView) findViewById(R.id.chambr_id);
            TextView contact = (TextView) findViewById(R.id.contact_id);
            TextView stterm = (TextView) findViewById(R.id.startterm_id);
            TextView enterm = (TextView) findViewById(R.id.endterm_id);
            TextView office = (TextView) findViewById(R.id.office_id);
            TextView stateID = (TextView) findViewById(R.id.state_id);
            TextView fax = (TextView) findViewById(R.id.fx_id);
            TextView party = (TextView) findViewById(R.id.party_id);
            TextView birthday = (TextView) findViewById(R.id.birthday_id);
            String displaypic = "https://theunitedstates.io/images/congress/original/"+jsonob.getString("bioguide_id")+".jpg";
            String name = jsonob.getString("title")+ "."+jsonob.get("last_name")+", "+jsonob.getString("first_name");
            fullName.setText(name);
            Picasso.with(getBaseContext()).load(displaypic).resize(100,100).into(img);
            if(jsonob.has("oc_email") == false || jsonob.getString("oc_email").equals("null")){
                email.setText("N.A");
            }else{
                email.setText(jsonob.getString("oc_email"));
            }

            cham = jsonob.getString("chamber");
            cham = cham.substring(0, 1).toUpperCase() + cham.substring(1);
            chamber.setText(cham);
            if(jsonob.has("phone") == false || jsonob.getString("phone").equals("null")){
                contact.setText("N.A");
            }else{
                contact.setText(jsonob.getString("phone"));
            }


            if(jsonob.getString("party").equals("R")){
                party.setText("Republican");
                Picasso.with(getBaseContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/r.png").resize(100,100).into(pt);
            }else if(jsonob.getString("party").equals("D")){
                party.setText("Democrat");
                Picasso.with(getBaseContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/d.png").resize(100,100).into(pt);
            } else{
                party.setText("Independent");
                Picasso.with(getBaseContext()).load("http://independentamericanparty.org/wp-content/themes/v/images/logo-american-heritage-academy.png").resize(100,100).into(pt);
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateStart = df.parse(jsonob.getString("term_start"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,");
            stDate = formatter.format(cal.getTime())+" "+ cal.get(Calendar.YEAR);
            stterm.setText(stDate);
            Date dateEnd =  df.parse(jsonob.getString("term_end"));
            cal.setTime(dateEnd);
            enDate = formatter.format(cal.getTime())+" "+ cal.get(Calendar.YEAR);
            enterm.setText(enDate);
            if(jsonob.has("office") == false || jsonob.getString("office").equals("null")){
                office.setText("N.A");
            } else {
                office.setText(jsonob.getString("office"));
            }

            stateID.setText(jsonob.getString("state"));
            if(jsonob.has("fax") == false || jsonob.getString("fax").equals("null")){
                fax.setText("N.A");
            } else {
                fax.setText(jsonob.getString("fax"));
            }
            if(jsonob.has("birthday") == false || jsonob.getString("birthday").equals("null")){
                birthday.setText("N.A");
            } else {
                Date bdate =  df.parse(jsonob.getString("birthday"));
                cal.setTime(bdate);
                birthdate = formatter.format(cal.getTime())+" "+ cal.get(Calendar.YEAR);
                birthday.setText(birthdate);
            }

            Date currDate = new Date();
           Date st =  df.parse(jsonob.getString("term_start"));
            Date en = df.parse(jsonob.getString("term_end"));
            long d = en.getTime() - st.getTime();
            long c = currDate.getTime() - st.getTime();
            long diff = c*100/d;
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setProgress((int)diff);
            TextView progressTxt = (TextView) findViewById(R.id.myTextProgress);
            progressTxt.setText(Integer.toString((int)diff)+"%");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addListenerOnButton();

    }

    public void addListenerOnButton() {

        fbButton = (ImageButton) findViewById(R.id.facebook);
        twButton = (ImageButton) findViewById(R.id.twitter);
        webButton = (ImageButton) findViewById(R.id.webbt);
        favButton = (ImageButton) findViewById(R.id.favoriteleg);

        fbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String fb="";
                try {
                    fb = "https://www.facebook.com/"+jsonob.getString("facebook_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(!(jsonob.has("facebook_id")) || jsonob.getString("facebook_id").equals("null")){
                        Toast.makeText(getBaseContext(),"No facebook page",Toast.LENGTH_SHORT).show();
                    }else{
                        Uri uri = Uri.parse(fb);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });
        twButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String tw="";
                try {
                    tw = "https://www.twitter.com/"+jsonob.getString("twitter_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(!(jsonob.has("twitter_id")) || jsonob.getString("twitter_id").equals("null")){
                        Toast.makeText(getBaseContext(),"No twitter page",Toast.LENGTH_SHORT).show();
                    }else{
                        Uri uri = Uri.parse(tw);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });
        webButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String wb="";
                try {
                    wb = jsonob.getString("website");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(!(jsonob.has("website")) || jsonob.getString("website").equals("null")){
                        Toast.makeText(getBaseContext(),"No website",Toast.LENGTH_SHORT).show();
                    }else{
                        Uri uri = Uri.parse(wb);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });

        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            String leg_fav = preferences.getString("legislator_data", "null");
                if(leg_fav.equals("null")){
                    JSONArray putVal = new JSONArray();
                    putVal.put(jsonob);
                    favButton.setImageResource(R.mipmap.ic_starison);
                    editor.putString("legislator_data",putVal.toString());
                    editor.apply();
                } else {
                    try {
                        JSONArray putVal = new JSONArray(leg_fav);
                        JSONArray list = new JSONArray();
                        for(int i=0; i<putVal.length();i++){
                            if(!(jsonob.getString("bioguide_id").equals(((JSONObject) putVal.get(i)).getString("bioguide_id")))){
                                list.put(putVal.get(i));
                            } else {
                                b_isThere = true;
                            }
                        }
                        if(b_isThere == false){
                            putVal.put(jsonob);
                            favButton.setImageResource(R.mipmap.ic_starison);
                            editor.putString("legislator_data",putVal.toString());
                            editor.apply();
                        } else{
                            /*if(list.length() == 0){
                                SharedPreferences.Editor edit = preferences.edit();
                                edit.clear().commit();
                            }else{*/
                                editor.putString("legislator_data",list.toString());
                                editor.apply();
                            //}
                            b_isThere = false;
                            favButton.setImageResource(R.drawable.ic_star_nofill);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    @Override
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

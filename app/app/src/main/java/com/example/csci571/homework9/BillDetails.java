package com.example.csci571.homework9;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BillDetails extends AppCompatActivity {
JSONObject jsonob;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean b_isThere = false;
    ImageButton favBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("Bill Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        favBill = (ImageButton) findViewById(R.id.bill_star);
        String details = (String) getIntent().getSerializableExtra("billString");
        try {
            String leg_fav = preferences.getString("bill_data", "null");
            jsonob = new JSONObject(details);
            JSONArray initVal;
            try {
                initVal = new JSONArray(leg_fav);
                for(int i=0; i<initVal.length();i++){
                    if((jsonob.getString("bill_id").equals(((JSONObject) initVal.get(i)).getString("bill_id")))){
                        favBill.setImageResource(R.mipmap.ic_starison);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView billID = (TextView) findViewById(R.id.billdt_id);
            String bid = jsonob.getString("bill_id").toUpperCase();
            billID.setText(bid);
            TextView title = (TextView) findViewById(R.id.title_id);
            if(jsonob.has("official_title") == false || jsonob.getString("official_title").equals("null")){
                title.setText(jsonob.getString("N.A"));
            } else{
                title.setText(jsonob.getString("official_title"));
            }
            TextView billType = (TextView) findViewById(R.id.bill_type);
            if(jsonob.has("bill_type") == false || jsonob.getString("bill_type").equals("null")){
                billType.setText("N.A");
            } else {
                billType.setText(jsonob.getString("bill_type").toUpperCase());
            }
            TextView sponsor = (TextView) findViewById(R.id.sponsor_id);
            String sp = ((JSONObject)jsonob.get("sponsor")).getString("title")+". "+((JSONObject)jsonob.get("sponsor")).getString("last_name")+", "+((JSONObject)jsonob.get("sponsor")).getString("first_name");
            sponsor.setText(sp);
            TextView chamber = (TextView) findViewById(R.id.chamber_id);
            String cham = jsonob.getString("chamber");
            cham = cham.substring(0, 1).toUpperCase() + cham.substring(1);
            chamber.setText(cham);
            TextView status = (TextView) findViewById(R.id.Status_id);
            String stat = ((JSONObject)jsonob.get("history")).getString("active");
            if(stat.equals("true")){
                stat = "Active";
            }else{
                stat = "New";
            }
            status.setText(stat);


            TextView intro_on = (TextView) findViewById(R.id.introducedOn_id);
            String introduced_on = jsonob.getString("introduced_on");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateStart = df.parse(introduced_on);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,");
            String stDate = formatter.format(cal.getTime())+" "+ cal.get(Calendar.YEAR);
            intro_on.setText(stDate);
            TextView congress = (TextView) findViewById(R.id.congress_id);
            String con_url= ((JSONObject) jsonob.get("urls")).getString("congress");
            if(con_url.equals("null")){
                congress.setText("N.A");
            }else{
                congress.setText(con_url);
            }
            if(jsonob.has("last_version")){
                String ver = ((JSONObject)jsonob.get("last_version")).getString("version_name");
                TextView version = (TextView) findViewById(R.id.version_id);
                if(ver.equals("null")){
                    version.setText("N.A");
                } else{
                    version.setText(ver);
                }

            }

            TextView bill_url = (TextView) findViewById(R.id.bill_url_id);
            String billurl = ((JSONObject) ((JSONObject)jsonob.get("last_version")).get("urls")).getString("pdf");
            if(billurl.equals("null")){
                bill_url.setText("N.A");
            }else{
                bill_url.setText(billurl);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        favBill = (ImageButton) findViewById(R.id.bill_star);
        favBill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String leg_fav = preferences.getString("bill_data", "null");
                if(leg_fav.equals("null")){
                    JSONArray putVal = new JSONArray();
                    putVal.put(jsonob);
                    favBill.setImageResource(R.mipmap.ic_starison);
                    editor.putString("bill_data",putVal.toString());
                    editor.apply();
                } else {
                    try {
                        JSONArray putVal = new JSONArray(leg_fav);
                        JSONArray list = new JSONArray();
                        for(int i=0; i<putVal.length();i++){
                            if(!(jsonob.getString("bill_id").equals(((JSONObject) putVal.get(i)).getString("bill_id")))){
                                list.put(putVal.get(i));
                            } else {
                                b_isThere = true;
                            }
                        }
                        if(b_isThere == false){
                            putVal.put(jsonob);
                            favBill.setImageResource(R.mipmap.ic_starison);
                            editor.putString("bill_data",putVal.toString());
                            editor.apply();
                        } else{
                            /*if(list.length() == 0){
                                SharedPreferences.Editor edit = preferences.edit();
                                edit.clear().commit();
                            }else{*/
                                editor.putString("bill_data",list.toString());
                                editor.apply();
                           /* }*/
                            b_isThere = false;
                            favBill.setImageResource(R.drawable.ic_star_nofill);

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

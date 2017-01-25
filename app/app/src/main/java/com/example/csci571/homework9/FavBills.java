package com.example.csci571.homework9;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavBills extends Fragment {
    ListView listViewfb;
    JSONArray jsonBr;
    JSONArray billsarray;

    public FavBills() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fav_bills, container, false);
        listViewfb = (ListView) rootView.findViewById(R.id.fav_bill_list);
        listViewfb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getContext();
                try{

                    Object ob = listViewfb.getItemAtPosition(position);
                    Bundle args=new Bundle();
                    String userProfileString=ob.toString();
                    args.putString("billString", userProfileString);

                    Intent intent = new Intent(getContext(), BillDetails.class);
                    intent.putExtras(args);
                    startActivity(intent);
                }catch(Exception e){

                }

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String leg_bfav = preferences.getString("bill_data", "null");
        try {
            if(leg_bfav != "null"){
                jsonBr = new JSONArray(leg_bfav);
                billsarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < jsonBr.length(); i++) {
                    jsonList.add(jsonBr.getJSONObject(i));
                }

                Collections.sort( jsonList, new Comparator<JSONObject>() {

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = (String) a.get("introduced_on");
                            valB = (String) b.get("introduced_on");
                        }
                        catch (JSONException e) {
                        }

                        return valB.compareTo(valA);
                    }
                });

                for (int i = 0; i < jsonBr.length(); i++) {
                    billsarray.put(jsonList.get(i));
                }
            } else {
                jsonBr = new JSONArray();
            }
            RowBillsAdapter rw = new RowBillsAdapter(getContext(), billsarray);
            listViewfb
                    .setAdapter(rw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

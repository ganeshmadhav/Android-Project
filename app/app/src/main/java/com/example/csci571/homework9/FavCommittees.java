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
public class FavCommittees extends Fragment {
    ListView listViewfc;
    JSONArray jsonCr;
    JSONArray comarray;
    public FavCommittees() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fav_committees, container, false);
         listViewfc = (ListView) rootView.findViewById(R.id.fav_com_list);


        listViewfc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getContext();
                try{

                    Object ob = listViewfc.getItemAtPosition(position);
                    Bundle args=new Bundle();
                    String userProfileString=ob.toString();
                    args.putString("commString", userProfileString);

                    Intent intent = new Intent(getContext(), CommiteeDetails.class);
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
        String leg_cfav = preferences.getString("committee_data", "null");
        try {
            if(leg_cfav != "null"){
                jsonCr = new JSONArray(leg_cfav);
                comarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < jsonCr.length(); i++) {
                    jsonList.add(jsonCr.getJSONObject(i));
                }

                Collections.sort( jsonList, new Comparator<JSONObject>() {

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = (String) a.get("name");
                            valB = (String) b.get("name");
                        }
                        catch (JSONException e) {
                        }

                        return valA.compareTo(valB);
                    }
                });

                for (int i = 0; i < jsonCr.length(); i++) {
                    comarray.put(jsonList.get(i));
                }

            } else {
                jsonCr = new JSONArray();
            }
            RowComAdapter rw = new RowComAdapter(getContext(), comarray);
            listViewfc.setAdapter(rw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

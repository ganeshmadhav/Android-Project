package com.example.csci571.homework9;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavLegislators extends Fragment implements View.OnClickListener{
    Map<String, Integer> mapIndex;
    ListView listView;
    JSONArray jsonAr;
    JSONArray legislatorarray;
    LinearLayout indexLayout;
    public FavLegislators() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fav_legislators, container, false);
         listView = (ListView) rootView.findViewById(R.id.fav_legislators);
        indexLayout = (LinearLayout) rootView.findViewById(R.id.fav_index);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getContext();
                try{

                    Object ob = listView.getItemAtPosition(position);
                    Bundle args=new Bundle();
                    String userProfileString=ob.toString();
                    args.putString("userProfileString", userProfileString);

                    Intent intent = new Intent(getContext(), LegislatorDetail.class);
                    intent.putExtras(args);
                    startActivity(intent);
                }catch(Exception e){

                }

            }
        });



        return rootView;
    }
    private void getIndexList(ArrayList<String> items) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String itemstring = items.get(i);
            String index = itemstring.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }
    private void displayIndex() {

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        if(indexLayout.getChildCount() > 0)
            indexLayout.removeAllViews();
        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }
    @Override
    public void onClick(View v) {
        ListView listView = (ListView) getActivity().findViewById(R.id.fav_legislators);
        TextView selectedIndex = (TextView) v;
        listView.setSelection(mapIndex.get(selectedIndex.getText()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String leg_fav = preferences.getString("legislator_data", "null");
        try {
            if(leg_fav != "null"){
                jsonAr = new JSONArray(leg_fav);
                legislatorarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < jsonAr.length(); i++) {
                    jsonList.add(jsonAr.getJSONObject(i));
                }

                Collections.sort( jsonList, new Comparator<JSONObject>() {

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = (String) a.get("last_name");
                            valB = (String) b.get("last_name");
                        }
                        catch (JSONException e) {
                        }

                        return valA.compareTo(valB);
                    }
                });

                for (int i = 0; i < jsonAr.length(); i++) {
                    legislatorarray.put(jsonList.get(i));
                }
            } else {
                jsonAr = new JSONArray();
            }
            RowAdapter rw = new RowAdapter(getContext(), legislatorarray);
            listView.setAdapter(rw);
            String s_n;
            ArrayList<String> St = new ArrayList<String>();
            for(int i=0; i<legislatorarray.length();i++){
                try {
                    s_n = ((JSONObject) legislatorarray.get(i)).getString("last_name");
                    St.add(s_n);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            getIndexList(St);
            displayIndex();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

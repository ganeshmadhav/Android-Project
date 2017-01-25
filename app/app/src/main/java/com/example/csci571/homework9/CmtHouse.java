package com.example.csci571.homework9;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CmtHouse extends Fragment {


    public CmtHouse() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cmt_house, container, false);
        String url = String.format("http://sample-env.9miahvwcsy.us-west-2.elasticbeanstalk.com/?type=houseCommittee");
        final ListView listView = (ListView) rootView.findViewById(R.id.comhouselist);
        new CmtHouse.CallHouseAPI(listView).execute(url);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getContext();
                try{

                    Object ob = listView.getItemAtPosition(position);
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
    private class CallHouseAPI extends AsyncTask<String, String, String> {
        JSONArray mainHouse;
        private ListView listView;
        JSONArray comarray;
        ArrayAdapter<String> adapter;
        public CallHouseAPI(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) listView.getAdapter();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                JSONObject topLevel = new JSONObject(builder.toString());

                mainHouse = topLevel.getJSONArray("results");
                comarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < mainHouse.length(); i++) {
                    jsonList.add(mainHouse.getJSONObject(i));
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

                for (int i = 0; i < mainHouse.length(); i++) {
                    comarray.put(jsonList.get(i));
                }

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String temp) {
            RowComAdapter rwActive = new RowComAdapter(getContext(), comarray);
            ListView listView = (ListView) getActivity().findViewById(R.id.comhouselist);
            listView.setAdapter(rwActive);
        }
    }
}

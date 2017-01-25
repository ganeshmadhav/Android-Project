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

public class NewBill extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_bill, container, false);
        String url = String.format("http://sample-env.9miahvwcsy.us-west-2.elasticbeanstalk.com/?type=newBill");
        final ListView listView2 = (ListView) rootView.findViewById(R.id.newBillList);
        new NewBill.CallNewAPI(listView2).execute(url);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getContext();
                try{

                    Object ob = listView2.getItemAtPosition(position);
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
    private class CallNewAPI extends AsyncTask<String, String, String> {
        JSONArray mainNew;
        JSONArray billsarray;
        private ListView listView;
        ArrayAdapter<String> adapter;
        public CallNewAPI(ListView listView) {
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

                mainNew = topLevel.getJSONArray("results");
                billsarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < mainNew.length(); i++) {
                    jsonList.add(mainNew.getJSONObject(i));
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

                for (int i = 0; i < mainNew.length(); i++) {
                    billsarray.put(jsonList.get(i));
                }

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String temp) {
            RowBillsAdapter rwActive = new RowBillsAdapter(getContext(), billsarray);
            ListView listView = (ListView) getActivity().findViewById(R.id.newBillList);
            listView.setAdapter(rwActive);
        }
    }
}
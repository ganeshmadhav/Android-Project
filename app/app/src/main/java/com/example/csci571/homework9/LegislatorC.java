package com.example.csci571.homework9;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LegislatorC extends Fragment implements View.OnClickListener{
        Map<String, Integer> mapIndex;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_legislatorc, container, false);
        String url = String.format("http://sample-env.9miahvwcsy.us-west-2.elasticbeanstalk.com/?type=Senate");
        final ListView listView2 = (ListView) rootView.findViewById(R.id.lgListSenate);
        try{
            new LegislatorC.CallSenateAPI(this,listView2).execute(url);
        } catch (Exception e){
            e.printStackTrace();
        }

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getContext();
                try{

                    Object ob = listView2.getItemAtPosition(position);
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
    private class CallSenateAPI extends AsyncTask<String, String, String> {
        View.OnClickListener viewMy;
        JSONArray main2;
        JSONArray legislatorarray;
        private ListView listView;
        ArrayAdapter<String> adapter;
        public CallSenateAPI(View.OnClickListener view, ListView listView) {
            this.listView = listView;
            this.viewMy = view;
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

                main2 = topLevel.getJSONArray("results");
                legislatorarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < main2.length(); i++) {
                    jsonList.add(main2.getJSONObject(i));
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

                for (int i = 0; i < main2.length(); i++) {
                    legislatorarray.put(jsonList.get(i));
                }
                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String temp) {
            RowAdapter rw2 = new RowAdapter(getContext(), legislatorarray);
            ListView listView = (ListView) getActivity().findViewById(R.id.lgListSenate);
            listView.setAdapter(rw2);
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
            LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index_senate);

            TextView textView;
            List<String> indexList = new ArrayList<String>(mapIndex.keySet());
            for (String index : indexList) {
                textView = (TextView) getActivity().getLayoutInflater().inflate(
                        R.layout.side_index_item, null);
                textView.setText(index);
                textView.setOnClickListener(viewMy);
                indexLayout.addView(textView);
            }
        }
    }
        @Override
        public void onClick(View v) {
            ListView listView = (ListView) getActivity().findViewById(R.id.lgListSenate);
            TextView selectedIndex = (TextView) v;
            listView.setSelection(mapIndex.get(selectedIndex.getText()));
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.main, menu);
        }
}
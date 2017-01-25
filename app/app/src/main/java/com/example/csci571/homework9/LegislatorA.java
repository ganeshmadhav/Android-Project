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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class LegislatorA extends Fragment implements View.OnClickListener{
    Map<String, Integer> mapIndex;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_legislatora, container, false);
        String url = String.format("http://sample-env.9miahvwcsy.us-west-2.elasticbeanstalk.com/?states=");
        final ListView listView = (ListView) rootView.findViewById(R.id.lgliststate);
        try{
            new CallAPI(this,listView).execute(url);
        } catch (Exception e){
            e.printStackTrace();
        }
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




    private class CallAPI extends AsyncTask<String, String, String>{

        View.OnClickListener viewMy;
        JSONArray main;
        JSONArray legislatorarray;
        private ListView listView;
        ArrayAdapter<String> adapter;
        public CallAPI(View.OnClickListener view,ListView listView) {
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

                main = topLevel.getJSONArray("results");
                legislatorarray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < main.length(); i++) {
                    jsonList.add(main.getJSONObject(i));
                }

                Collections.sort( jsonList, new Comparator<JSONObject>() {

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = (String) a.get("state_name");
                            valB = (String) b.get("state_name");
                        }
                        catch (JSONException e) {
                        }

                        return valA.compareTo(valB);
                    }
                });

                for (int i = 0; i < main.length(); i++) {
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
            RowAdapter rw = new RowAdapter(getContext(), legislatorarray);
            ListView listView = (ListView) getActivity().findViewById(R.id.lgliststate);
            try{
                listView.setAdapter(rw);
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            String s_n;
            ArrayList<String> St = new ArrayList<String>();
            for(int i=0; i<main.length();i++){
                try {
                    s_n = ((JSONObject) legislatorarray.get(i)).getString("state_name");
                    St.add(s_n);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            getIndexList(St);
            displayView();
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
        private void displayView() {
            LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.side_index);

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
        ListView listView = (ListView) getActivity().findViewById(R.id.lgliststate);
        TextView selectedIndex = (TextView) v;
        listView.setSelection(mapIndex.get(selectedIndex.getText()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
}

class RowAdapter extends BaseAdapter implements ListAdapter {
    private final JSONArray jsonArray;
    Context context;
    String[] data;
    private static LayoutInflater inflater = null;

    public RowAdapter(Context context, JSONArray data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.jsonArray = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(null==jsonArray)
            return 0;
        else
            return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        // TODO Auto-generated method stub
        if(null==jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.legislator_row, null);



        TextView text =(TextView)convertView.findViewById(R.id.LgName);
        TextView text2 =(TextView)convertView.findViewById(R.id.LgState);
        TextView text3 =(TextView)convertView.findViewById(R.id.LgDistrict);
        TextView text4 = (TextView)convertView.findViewById(R.id.LgParty);

        ImageView img = (ImageView) convertView.findViewById(R.id.imageView2);
        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String jj= null;
            String nm = null;
            String dt = null;
            String pt = null;
            String image = null;
            try {
                jj = json_data.getString("last_name")+", "+json_data.getString("first_name");
                nm = json_data.getString("state_name");
                dt = json_data.getString("district");
                image = "https://theunitedstates.io/images/congress/original/"+json_data.getString("bioguide_id")+".jpg";
                pt = "("+json_data.getString("party")+")";
                if(dt.equals("null")){
                    dt = "District 0";
                } else {
                    dt = "District " + dt;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            text.setText(jj);
            text2.setText(nm);
            text3.setText(dt);
            text4.setText(pt);
            Picasso.with(context).load(image)
                    .resize(80,80)
                    .into(img);
        }

        return convertView;
    }
}
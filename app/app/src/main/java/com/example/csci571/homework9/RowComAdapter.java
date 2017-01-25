package com.example.csci571.homework9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ganesh on 19-11-2016.
 */

public class RowComAdapter extends BaseAdapter{
    private final JSONArray jsonArray;
    Context context;
    String[] data;
    private static LayoutInflater inflater = null;

    public RowComAdapter(Context context, JSONArray data) {
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
            convertView = inflater.inflate(R.layout.committee_row, null);



        TextView text =(TextView)convertView.findViewById(R.id.comId);
        TextView text2 =(TextView)convertView.findViewById(R.id.comName);
        TextView text3 =(TextView)convertView.findViewById(R.id.comChamber);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String jj= null;
            String nm = null;
            String dt = null;
            try {
                jj = json_data.getString("committee_id").toUpperCase();
                nm = json_data.getString("name");
                dt = json_data.getString("chamber");
                dt = dt.substring(0, 1).toUpperCase() + dt.substring(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            text.setText(jj);
            text2.setText(nm);
            text3.setText(dt);
        }

        return convertView;
    }
}

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ganesh on 19-11-2016.
 */

public class RowBillsAdapter extends BaseAdapter{
    private final JSONArray jsonArray;
    Context context;
    String[] data;
    private static LayoutInflater inflater = null;

    public RowBillsAdapter(Context context, JSONArray data) {
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
            convertView = inflater.inflate(R.layout.bills_row, null);



        TextView text =(TextView)convertView.findViewById(R.id.billsID);
        TextView text2 =(TextView)convertView.findViewById(R.id.bills_title);
        TextView text3 =(TextView)convertView.findViewById(R.id.bills_creationdate);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String jj= null;
            String nm = null;
            String dt = null;
            try {
                jj = json_data.getString("bill_id").toUpperCase();
                nm = json_data.getString("short_title");
                dt = json_data.getString("introduced_on");
                if(nm.equals("null")){
                    nm = json_data.getString("official_title");
                    if(nm.equals("null")){
                        nm = "N.A";
                    }
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(dt);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,");
                dt = formatter.format(cal.getTime())+" "+ cal.get(Calendar.YEAR);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            text.setText(jj);
            text2.setText(nm);
            text3.setText(dt);
        }

        return convertView;
    }
}

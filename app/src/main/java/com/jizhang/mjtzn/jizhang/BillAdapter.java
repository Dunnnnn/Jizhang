package com.jizhang.mjtzn.jizhang;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class BillAdapter extends BaseAdapter {
    private LinkedList<RecordBean> mData;
    private Context mContext;
    public BillAdapter(LinkedList<RecordBean> mData,Context mContext){
        this.mData = mData;
        this.mContext= mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.bill_layout,parent,false);
        TextView txt_Name = (TextView) convertView.findViewById(R.id.txt_Name);
        TextView txt_Num = (TextView) convertView.findViewById(R.id.txt_Num);
        TextView txt_Date = (TextView) convertView.findViewById(R.id.txt_Date);
        txt_Name.setText(mData.get(position).getCategory());
        if(mData.get(position).getType()==2){
            txt_Num.setText("+"+String.format("%.2f",mData.get(position).getAmount()));
            txt_Num.setTextColor(Color.RED);
        }else{
            txt_Num.setText("-"+String.format("%.2f",mData.get(position).getAmount()));
            txt_Num.setTextColor(Color.GREEN);
        }
        txt_Date.setText(mData.get(position).getDate());
        return convertView;
    }
}

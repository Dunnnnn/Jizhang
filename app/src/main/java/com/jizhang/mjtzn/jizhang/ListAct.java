package com.jizhang.mjtzn.jizhang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.security.AccessController;
import java.util.LinkedList;
import java.util.List;

public class ListAct extends AppCompatActivity{
    private LinkedList<RecordBean> mData;
    private Context mContext;
    private BillAdapter mAdapter = null;
    private ListView list_bill;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        mContext = ListAct.this;
        list_bill = (ListView)findViewById(R.id.billist);
        mData = Util.getInstance().databaseHelper.getAllRecords();

        mAdapter = new BillAdapter(mData,mContext);
        list_bill.setAdapter(mAdapter);
        registerForContextMenu(list_bill);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mData = Util.getInstance().databaseHelper.getAllRecords();
        mAdapter = new BillAdapter(mData,mContext);
        list_bill.setAdapter(mAdapter);
        registerForContextMenu(list_bill);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1, Menu.NONE,"删除");
        menu.add(0,2,Menu.NONE,"修改");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenu.ContextMenuInfo menuInfo = (ContextMenu.ContextMenuInfo) item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo Info = ( AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int num = (int) Info.id;
        switch(item.getItemId()){
            case 1://实现删除
                Util.getInstance().databaseHelper.removeRecord(mData.get(num).getUuid());
                onResume();
                Toast.makeText(this, "记录已删除", Toast.LENGTH_SHORT).show();
                break;
            case 2://实现详情
                Intent it2 = new Intent(this,AddAct.class);
                Bundle extra = new Bundle();
                extra.putSerializable("record",mData.get(num));
                it2.putExtras(extra);
                startActivity(it2);
                break;
            default:
                break;
        }
     return true;
    }
}

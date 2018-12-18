package com.jizhang.mjtzn.jizhang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.widget.Toast.LENGTH_SHORT;

public class Set_app extends AppCompatActivity{
private Button bu1;
private ToggleButton bu_swi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_layout);
        bu1 =(Button)findViewById(R.id.out_data);
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.getInstance().databaseHelper.clearRecord();
            }
        });
    }
}

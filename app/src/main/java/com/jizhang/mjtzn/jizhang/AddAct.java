package com.jizhang.mjtzn.jizhang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AddAct extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private TextView text_typ;
    private EditText text_name;
    private EditText text_num;
    private ToggleButton toggleButton;
    private RecordBean record;
    private int editing;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        text_typ = findViewById(R.id.out_typ);

        text_name=findViewById(R.id.out_name);
        text_num =findViewById(R.id.out_num);

        text_num.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        toggleButton=findViewById(R.id.typ_swi);


        record=(RecordBean) getIntent().getSerializableExtra("record");
        if(record != null) {
            editing=1;
            text_name.setText(record.getCategory());
            text_num.setText(String.format("%.2f",record.getAmount()));
            if(record.getType()==1){
                toggleButton.setChecked(true);
            }else{
                toggleButton.setChecked(false);
            }
        }else{
            editing=0;
        }
        Button bu1 = findViewById(R.id.cancel);
        Button bu2 = findViewById(R.id.ok);
        ToggleButton bu3 = findViewById(R.id.typ_swi);
        bu3.setOnCheckedChangeListener(this);

        bu1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bu2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(editing==1){
                    record.setCategory(text_name.getText().toString());
                    record.setAmount(Util.getInstance().str2float(text_num.getText().toString()));
                    if(toggleButton.isChecked()){
                        record.setType(1);
                    }else{
                        record.setType(2);
                    }
                    Util.getInstance().databaseHelper.removeRecord(record.getUuid());
                    Util.getInstance().databaseHelper.addRecord(record);
                    Toast.makeText(AddAct.this, "修改成功", Toast.LENGTH_SHORT).show();
                }else{
                    record=new RecordBean();
                    record.setCategory(text_name.getText().toString());
                    record.setAmount(Util.getInstance().str2float(text_num.getText().toString()));
                    if(toggleButton.isChecked()){
                        record.setType(1);
                    }else{
                        record.setType(2);
                    }
                    Util.getInstance().databaseHelper.addRecord(record);
                    Toast.makeText(AddAct.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.typ_swi){
            if(buttonView.isChecked()) {
                Toast.makeText(this, "支出", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"收入",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

package com.example.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private RadioGroup radiogroup;
    private EditText phone, passwd;
    private CheckBox ckbx1, ckbx2, ckbx3;
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = (EditText)findViewById(R.id.editTextPhone);
        passwd = (EditText)findViewById(R.id.editTextTextPassword);
        radiogroup = (RadioGroup)findViewById(R.id.radioGroup);
        ckbx1 = (CheckBox)findViewById(R.id.checkBox);
        ckbx2 = (CheckBox)findViewById(R.id.checkBox2);
        ckbx3 = (CheckBox)findViewById(R.id.checkBox3);
        checkBoxList.add(ckbx1);
        checkBoxList.add(ckbx2);
        checkBoxList.add(ckbx3);
        spinner = (Spinner)findViewById(R.id.spinner);
        String[] city = new String[]{"北京", "上海", "武汉", "南京", "南昌"};
        button = (Button) findViewById(R.id.button);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,city);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
                String phoneNumber = phone.getText().toString();
                String pswd = passwd.getText().toString();
                String sex = ((RadioButton) findViewById(radiogroup.getCheckedRadioButtonId())).getText().toString();
                String city = spinner.getSelectedItem().toString();
                StringBuilder hobbies = new StringBuilder();
                for (CheckBox cb : checkBoxList) {
                    if (cb.isChecked()) {
                        hobbies.append(cb.getText().toString()).append(" ");
                    }
                }

                String text = "手机号：" + phoneNumber + "\n密码：" + pswd + "\n性别：" + sex + "\n爱好：" + hobbies + "\n城市：" + city;
                Bundle bundle = new Bundle();
                bundle.putString("text", text);

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, post.class);
                intent.putExtras(bundle);

                startActivity(intent);
        }
    });
}
}
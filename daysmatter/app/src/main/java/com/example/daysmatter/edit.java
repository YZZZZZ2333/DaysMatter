package com.example.daysmatter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class edit extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    private Context context;
    private EditText edittitle, editdsc;
    private Spinner spinnertype;
    private Switch switchstar;
    private Button buttonc, buttons, buttondate, buttontime;
    private int year, month, day, hour, minute;
    private StringBuffer date, time;
    int usrid=-1;
    int rid=0;
    String token="";
    String uname="";
    String result="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        date = new StringBuffer();
        time = new StringBuffer();
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        DatabaseHelper databaseHelper = new DatabaseHelper(this,"daysmatter",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        usrid=sharedPreferences.getInt("usrid",-1);
        token=sharedPreferences.getString("token","");
        uname=sharedPreferences.getString("uname","");
        setContentView(R.layout.edit);
        edittitle=(EditText)findViewById(R.id.edittitle);
        editdsc=(EditText)findViewById(R.id.editdsc);
        spinnertype=(Spinner)findViewById(R.id.spinnertype);
        String[] typs=new String[]{"纪念日","计划日程"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, typs);
        spinnertype.setAdapter(adapter1);
        switchstar=(Switch)findViewById(R.id.switchstar);
        buttonc=(Button)findViewById(R.id.buttonc);
        buttons=(Button)findViewById(R.id.buttons);
        buttondate=(Button)findViewById(R.id.buttondate);
        buttontime=(Button)findViewById(R.id.buttontime);
        Intent intent=getIntent(); // 取得从上一个Activity当中传递过来的Intent对象
        if (intent!=null) {
            rid=intent.getIntExtra("rid",0);
            if (rid!=0){
                Cursor cursor = db.query("data",new String[]{"title","dsc","typ","star","tim"},"rid=?",new String[]{String.valueOf(rid)},null,null,null);
                while(cursor.moveToNext()) {
                    String title=cursor.getString(cursor.getColumnIndex("title"));
                    edittitle.setText(title);
                    String dsc=cursor.getString(cursor.getColumnIndex("dsc"));
                    editdsc.setText(dsc);
                    int typ=Integer.parseInt(cursor.getString(cursor.getColumnIndex("typ")));
                    spinnertype.setSelection(typ);
                    int star=Integer.parseInt(cursor.getString(cursor.getColumnIndex("star")));
                    switchstar.setChecked(star==0?false:true);
                    long tim=Integer.parseInt(cursor.getString(cursor.getColumnIndex("tim")));
                    initDateTime(tim*1000);
                }
            }
            else {
                setTitle("新建");
                initDateTime(System.currentTimeMillis());
            }
        }
        buttonc.setOnClickListener(this);
        buttons.setOnClickListener(this);
        buttondate.setOnClickListener(this);
        buttontime.setOnClickListener(this);
    }
    private void initDateTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    private long toMill() throws ParseException {
        String buffer=year+"-"+month+"-"+day+" "+hour+":"+minute;
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(buffer);
        long time=date.getTime()/1000;
        return time;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttondate:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("设置", (dialog, which) -> {
                    if (date.length() > 0) { //清除上次记录的日期
                        date.delete(0, date.length());
                    }
                    buttondate.setText(date.append(year).append("-").append(month).append("-").append(day));
                    dialog.dismiss();
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month-1, day, this);
                break;
            case R.id.buttontime:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("设置", (dialog1, which) -> {
                    if (time.length() > 0) { //清除上次记录的日期
                        time.delete(0, time.length());
                    }
                    buttontime.setText(time+String.valueOf(hour)+":"+(minute<10?"0":"")+String.valueOf(minute));
                    dialog1.dismiss();
                });
                builder2.setNegativeButton("取消", (dialog12, which) -> dialog12.dismiss());
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);
                dialog2.setTitle("设置时间");
                dialog2.setView(dialogView2);
                dialog2.show();
                break;
            case R.id.buttons:
                String title = edittitle.getText().toString();
                String dsc = editdsc.getText().toString();
                //String tim = editDate.getText().toString();
                String typ = String.valueOf(spinnertype.getSelectedItemPosition());
                String star = String.valueOf(switchstar.isChecked()?1:0);
                long tim = 0;
                try {
                    tim = toMill();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List datalist = new ArrayList<>();
                datalist.add("usrid=" + usrid);
                datalist.add("token=" + token);
                datalist.add("rid=" + rid);
                datalist.add("title=" + title);
                datalist.add("dsc=" + dsc);
                datalist.add("typ=" + typ);
                datalist.add("star=" + star);
                datalist.add("tim=" + tim);
                try {
                    result = getData.getdata(datalist, "edit.php");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject json = null;
                int stat = 1;
                String msg = "";
                try {
                    json = new JSONObject(result);
                    stat = json.getInt("status");//获取json数组中的data项
                    msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (stat == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case R.id.buttonc:
                finish();
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }
}

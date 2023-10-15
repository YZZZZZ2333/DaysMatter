package com.example.daysmatter.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.daysmatter.DatabaseHelper;
import com.example.daysmatter.R;
import com.example.daysmatter.detail;

public class HomeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View cFragment = inflater.inflate(R.layout.fragment_list, container, false);
        cFragment = course(cFragment);
        return cFragment;
    }

    public View course(View cFragment){
        LinearLayout course_linearLayout  = (LinearLayout)cFragment.findViewById(R.id.ll0);
        long now=System.currentTimeMillis()/1000;
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext(),"daysmatter",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        //星标条目
        TextView t1=new TextView(course_linearLayout.getContext());
        t1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        t1.setText("星标：");
        ViewGroup.MarginLayoutParams margin1 = new ViewGroup.MarginLayoutParams(t1.getLayoutParams());
        margin1.setMargins(margin1.leftMargin + 100, 50, margin1.rightMargin + 100, 50);
        LinearLayout.LayoutParams layoutParamst = new LinearLayout.LayoutParams(margin1);
        t1.setLayoutParams(layoutParamst);
        t1.setTextSize(18);
        course_linearLayout.addView(t1);
        Cursor cursor = db.query("data",new String[]{"rid","title","dsc","star","tim"},"star=1",null,null,null,"tim");
        while (cursor.moveToNext()) {
            Button b = new Button(course_linearLayout.getContext());
            String title=cursor.getString(cursor.getColumnIndex("title"));
            int rid=cursor.getInt(cursor.getColumnIndex("rid"));
            long tim=cursor.getLong(cursor.getColumnIndex("tim"));
            b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //设置文字 内容与颜色
            int day=(int)(Math.abs(now-tim)/86400);
            if (tim>now)
                b.setText(title+"\n"+day+"天后");
            if (tim<now)
                b.setText(title+"\n"+day+"天前");
            if (day==0)
                b.setText(title+"\n今天");
            b.setId(rid);
            b.setTextColor(Color.parseColor("#535353"));
            //设置背景色
            b.setBackgroundColor(Color.parseColor("#EEEEEE"));
            b.setTextSize(18);
            //设置button所属的位置
            ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(b.getLayoutParams());
            margin.setMargins(margin.leftMargin + 100, 0, margin.rightMargin + 100, 50);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
            b.setLayoutParams(layoutParams);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rid=b.getId();
                    Intent intent = new Intent(getActivity(),detail.class);
                    intent.putExtra("rid",rid);
                    intent.putExtra("share",0);
                    startActivity(intent);
                }
            });
            //添加到线性布局
            course_linearLayout.addView(b);
        }
        //即将到来的日程
        TextView t2=new TextView(course_linearLayout.getContext());
        t2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        t2.setText("即将到来的日程：");
        ViewGroup.MarginLayoutParams margin2 = new ViewGroup.MarginLayoutParams(t2.getLayoutParams());
        margin2.setMargins(margin2.leftMargin + 100, 0, margin2.rightMargin + 100, 50);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(margin2);
        t2.setLayoutParams(layoutParams2);
        t2.setTextSize(18);
        course_linearLayout.addView(t2);
        cursor = db.query("data",new String[]{"rid","title","dsc","star","tim"},"typ=1",null,null,null,"tim");
        while (cursor.moveToNext()) {
            long tim=cursor.getLong(cursor.getColumnIndex("tim"));
            //设置文字 内容与颜色
            int day=(int)(Math.abs(now-tim)/86400);
            if (tim>=now&&day<=20) {
                int rid=cursor.getInt(cursor.getColumnIndex("rid"));
                Button b = new Button(course_linearLayout.getContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                b.setText(title+"\n"+day+"天后");
                if (day==0)
                    b.setText(title+"\n今天");
                b.setId(rid);
                b.setTextColor(Color.parseColor("#535353"));
                //设置背景色
                b.setBackgroundColor(Color.parseColor("#EEEEEE"));
                b.setTextSize(18);
                //设置button所属的位置
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(b.getLayoutParams());
                margin.setMargins(margin.leftMargin + 100, 0, margin.rightMargin + 100, 50);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
                b.setLayoutParams(layoutParams);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rid = b.getId();
                        Intent intent = new Intent(getActivity(), detail.class);
                        intent.putExtra("rid", rid);
                        intent.putExtra("share", 0);
                        startActivity(intent);
                    }
                });
                //添加到线性布局
                course_linearLayout.addView(b);
            }
        }
        //一些纪念日
        TextView t3=new TextView(course_linearLayout.getContext());
        t3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        t3.setText("一些纪念日：");
        ViewGroup.MarginLayoutParams margin3 = new ViewGroup.MarginLayoutParams(t3.getLayoutParams());
        margin3.setMargins(margin3.leftMargin + 100, 0, margin3.rightMargin + 100, 50);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(margin3);
        t3.setLayoutParams(layoutParams3);
        t3.setTextSize(18);
        course_linearLayout.addView(t3);
        cursor = db.query("data",new String[]{"rid","title","dsc","star","tim"},"typ=0",null,null,null,"tim");
        while (cursor.moveToNext()) {
            long tim=cursor.getLong(cursor.getColumnIndex("tim"));
            //设置文字 内容与颜色
            int day=(int)(Math.abs(now-tim)/86400);
            if (day%100==0||day%365==0||day==99) {
                int rid=cursor.getInt(cursor.getColumnIndex("rid"));
                Button b = new Button(course_linearLayout.getContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                if (tim>now)
                    b.setText(title+"\n"+day+"天后");
                if (tim<now)
                    b.setText(title+"\n"+day+"天前");
                if (day==0)
                    b.setText(title+"\n今天");
                b.setId(rid);
                b.setTextColor(Color.parseColor("#535353"));
                //设置背景色
                b.setBackgroundColor(Color.parseColor("#EEEEEE"));
                b.setTextSize(18);
                //设置button所属的位置
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(b.getLayoutParams());
                margin.setMargins(margin.leftMargin + 100, 0, margin.rightMargin + 100, 50);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
                b.setLayoutParams(layoutParams);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rid = b.getId();
                        Intent intent = new Intent(getActivity(), detail.class);
                        intent.putExtra("rid", rid);
                        intent.putExtra("share", 0);
                        startActivity(intent);
                    }
                });
                //添加到线性布局
                course_linearLayout.addView(b);
            }
        }
        //近期的共享内容
        TextView t4=new TextView(course_linearLayout.getContext());
        t4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        t4.setText("近期的共享内容：");
        ViewGroup.MarginLayoutParams margin4 = new ViewGroup.MarginLayoutParams(t4.getLayoutParams());
        margin4.setMargins(margin4.leftMargin + 100, 0, margin4.rightMargin + 100, 50);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(margin4);
        t4.setLayoutParams(layoutParams4);
        t4.setTextSize(18);
        course_linearLayout.addView(t4);
        cursor = db.query("share",new String[]{"rid","title","dsc","typ","tim"},null,null,null,null,"tim");
        while (cursor.moveToNext()) {
            long tim=cursor.getLong(cursor.getColumnIndex("tim"));
            //设置文字 内容与颜色
            int day=(int)(Math.abs(now-tim)/86400);
            if (day<=5) {
                int rid=cursor.getInt(cursor.getColumnIndex("rid"));
                Button b = new Button(course_linearLayout.getContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                if (tim>now)
                    b.setText(title+"\n"+day+"天后");
                if (tim<now)
                    b.setText(title+"\n"+day+"天前");
                if (day==0)
                    b.setText(title+"\n今天");
                b.setId(rid);
                b.setTextColor(Color.parseColor("#535353"));
                //设置背景色
                b.setBackgroundColor(Color.parseColor("#EEEEEE"));
                b.setTextSize(18);
                //设置button所属的位置
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(b.getLayoutParams());
                margin.setMargins(margin.leftMargin + 100, 0, margin.rightMargin + 100, 50);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
                b.setLayoutParams(layoutParams);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rid = b.getId();
                        Intent intent = new Intent(getActivity(), detail.class);
                        intent.putExtra("rid", rid);
                        intent.putExtra("share", 1);
                        startActivity(intent);
                    }
                });
                //添加到线性布局
                course_linearLayout.addView(b);
            }
        }
        return cFragment;
    }
}
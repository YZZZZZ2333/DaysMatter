package com.example.daysmatter.ui.memorial;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.daysmatter.DatabaseHelper;
import com.example.daysmatter.R;
import com.example.daysmatter.detail;

public class MemorialFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View cFragment = inflater.inflate(R.layout.fragment_list, container, false);
        cFragment = course(cFragment);
        return cFragment;
    }

    public View course(View cFragment){
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext(),"daysmatter",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("data",new String[]{"rid","title","dsc","star","tim"},"typ=0",null,null,null,"tim");
        LinearLayout course_linearLayout  = (LinearLayout)cFragment.findViewById(R.id.ll0);
        //创建button
        //设置高度
        long now=System.currentTimeMillis()/1000;
        while (cursor.moveToNext()) {
            Button b = new Button(course_linearLayout.getContext());
            String title=cursor.getString(cursor.getColumnIndex("title"));
            int rid=cursor.getInt(cursor.getColumnIndex("rid"));
            long tim=cursor.getLong(cursor.getColumnIndex("tim"));
            b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
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
            margin.setMargins(margin.leftMargin + 100, 50, margin.rightMargin + 100, 50);
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
        return cFragment;
    }
}

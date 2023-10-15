package com.example.timetable.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.timetable.DatabaseHelper;
import com.example.timetable.R;
import com.example.timetable.databinding.FragmentHomeBinding;

import java.util.Calendar;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper helper;
    SQLiteDatabase db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textHome;
        Calendar calendar=Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)-1);
        helper = new DatabaseHelper(getActivity(),"classdata",null,1);
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("data",new String[]{"subj","sttime","lstime","clroom","teac"},"dayow=?",new String[]{day},null,null,"sttime");
        String textview_data = "";
        while(cursor.moveToNext()){
            String subj = cursor.getString(cursor.getColumnIndex("subj"));
            int sttime = Integer.parseInt(cursor.getString(cursor.getColumnIndex("sttime")));
            int lstime = Integer.parseInt(cursor.getString(cursor.getColumnIndex("lstime")));
            String tim = "第"+String.valueOf(sttime)+"～"+String.valueOf(sttime+lstime-1)+"节：";
            String clroom = cursor.getString(cursor.getColumnIndex("clroom"));
            String teac = cursor.getString(cursor.getColumnIndex("teac"));
            textview_data = textview_data + "\n" + tim + "\n" + subj + "\n" + clroom + "\n" + teac + "\n";
        }
        if (textview_data=="")
            textview_data="你今天没有课程";
        textView.setText(textview_data);
        db.close();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.timetable.ui.detail;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.timetable.DatabaseHelper;
import com.example.timetable.MainActivity;
import com.example.timetable.R;
import com.example.timetable.databinding.FragmentDetailBinding;

import java.util.Objects;

public class DetailFragment extends Fragment {

    private DetailViewModel detailViewModel;
    private DatabaseHelper helper;
    SQLiteDatabase db;
    private FragmentDetailBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailViewModel =
                new ViewModelProvider(this).get(DetailViewModel.class);

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        String days=String.valueOf(actionBar.getTitle());
        final TextView textView = binding.textDetail;
        String dayi="0";
        if (days.contains("一")) dayi="1";
        if (days.contains("二")) dayi="2";
        if (days.contains("三")) dayi="3";
        if (days.contains("四")) dayi="4";
        if (days.contains("五")) dayi="5";
        if (days.contains("六")) dayi="6";
        helper = new DatabaseHelper(getActivity(),"classdata",null,1);
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("data",new String[]{"subj","sttime","lstime","clroom","teac"},"dayow=?",new String[]{dayi},null,null,"sttime");
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
            textview_data="你当天没有课程";
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
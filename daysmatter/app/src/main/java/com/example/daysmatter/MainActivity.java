package com.example.daysmatter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.daysmatter.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String token="",uname="";
    int usrid=-1;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        usrid=sharedPreferences.getInt("usrid",-1);
        token=sharedPreferences.getString("token","");
        uname=sharedPreferences.getString("uname","");
        if (token.equals("")){
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
            finish();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,edit.class);
                intent.putExtra("rid","0");
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_star, R.id.nav_memorial, R.id.nav_agenda, R.id.nav_sharewm, R.id.nav_search)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    protected void onResume() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this,"daysmatter",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List datalist = new ArrayList<>();
        datalist.add("usrid=" + usrid);
        datalist.add("token=" + token);
        String result="null";
        try {
            result = getData.getdata(datalist, "fetchsql.php");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String sql="DELETE FROM data";
        db.execSQL(sql);
        JSONObject json = null;
        int stat = 1;
        String msg = "";
        try {
            json = new JSONObject(result);
            stat = json.getInt("status");//获取json数组中的data项
            msg = json.getString("msg");
            JSONArray paths = json.getJSONArray("data");
            for (int i=0;i<paths.length();i++) {
                int rid = paths.getJSONObject(i).getInt("id");
                String title = paths.getJSONObject(i).getString("title");
                String dsc = paths.getJSONObject(i).getString("dsc");
                int typ = paths.getJSONObject(i).getInt("typ");
                int star = paths.getJSONObject(i).getInt("star");
                long tim = paths.getJSONObject(i).getLong("tim");
                sql = "INSERT INTO data VALUES (?,?,?,?,?,?)";
                db.execSQL(sql,new Object[]{rid,title,dsc,typ,star,tim});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List sharelist = new ArrayList<>();
        sharelist.add("usrid=" + usrid);
        sharelist.add("token=" + token);
        result="null";
        try {
            result = getData.getdata(sharelist, "sharewm.php");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sql="DELETE FROM share";
        db.execSQL(sql);
        JSONObject json2 = null;
        stat = 1;
        msg = "";
        try {
            json2 = new JSONObject(result);
            stat = json2.getInt("status");//获取json数组中的data项
            msg = json2.getString("msg");
            JSONArray paths2 = json2.getJSONArray("data");
            for (int i=0;i<paths2.length();i++) {
                int rid = paths2.getJSONObject(i).getInt("rid");
                String title = paths2.getJSONObject(i).getString("title");
                String name = paths2.getJSONObject(i).getString("name");
                String dsc = paths2.getJSONObject(i).getString("dsc");
                int typ = paths2.getJSONObject(i).getInt("typ");
                long tim = paths2.getJSONObject(i).getLong("tim");
                sql = "INSERT INTO share VALUES (?,?,?,?,?,?)";
                db.execSQL(sql,new Object[]{rid,name,title,dsc,typ,tim});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //super.onOptionsItemSelected(item);
        if (item.getItemId()==2131230784) {
            String url = "logout.php";
            String result = "null";
            List loginlist = new ArrayList<>();
            loginlist.add("usrid=" + usrid);
            loginlist.add("token=" + token);
            try {
                result = getData.getdata(loginlist, url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                int stat = json.getInt("status");//获取json数组中的data项
                String msg = json.getString("msg");
                if (stat == 0) {
                    editor.remove("usrid");
                    editor.remove("token");
                    editor.remove("uname");
                    editor.commit();
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
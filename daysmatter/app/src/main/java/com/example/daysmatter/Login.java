package com.example.daysmatter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Login extends AppCompatActivity {
    private EditText editTextemail, editTextpwd;
    private Button btn_login, btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_reg=(Button) findViewById(R.id.btn_reg);
        editTextemail=(EditText) findViewById(R.id.editTextemail);
        editTextpwd=(EditText) findViewById(R.id.editTextpwd);
        String finalUrl = "loginauth.php";
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result="";
                String email=editTextemail.getText().toString();
                String pwd=editTextpwd.getText().toString();
                pwd=getMD5(pwd);
                List loginlist = new ArrayList<>();
                loginlist.add("email="+email);
                loginlist.add("pwd="+pwd);
                try {
                    result=getData.getdata(loginlist, finalUrl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    int stat = json.getInt("status");//获取json数组中的data项
                    String msg = json.getString("msg");
                    int usrid = json.getInt("usrid");
                    String uname = json.getString("uname");
                    String token = json.getString("token");
                    if (stat == 0) {
                        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("usrid", usrid);
                        editor.putString("uname", uname);
                        editor.putString("token", token);
                        editor.commit();
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e) {
            return "";
        }
        catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

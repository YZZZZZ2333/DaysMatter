package com.example.daysmatter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;

public class getData {
    private static String addr="http://120.55.43.217";
    public static String getdata(List<String> params, String site) throws InterruptedException {
        final String[] res0 = {""};
        String res;
        String url=addr+"/"+site;
        Vector<Thread> vectors=new Vector<Thread>();
        Thread doPost = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    //设置请求方式,请求超时信息
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    //设置运行输入,输出:
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //Post方式不能缓存,需手动设置为false
                    conn.setUseCaches(false);
                    //我们请求的数据:
                    String data = "";
                    for (int i=0;i<params.size()-1;i++){
                        data=data + params.get(i) + "&";
                    }
                    data=data + params.get(params.size()-1);
                    //res0[0]=site;
                    //这里可以写一些请求头的东东...
                    //获取输出流
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    if (conn.getResponseCode() == 200) {
                        // 获取响应的输入流对象
                        InputStream is = conn.getInputStream();
                        // 创建字节输出流对象
                        ByteArrayOutputStream message = new ByteArrayOutputStream();
                        // 定义读取的长度
                        int len = 0;
                        // 定义缓冲区
                        byte[] buffer = new byte[1024];
                        // 按照缓冲区的大小，循环读取
                        while ((len = is.read(buffer)) != -1) {
                            // 根据读取的长度写入到os对象中
                            message.write(buffer, 0, len);
                        }
                        // 释放资源
                        is.close();
                        message.close();
                        // 返回字符串
                        res0[0] = new String(message.toByteArray());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    res0[0]=String.valueOf(e);
                }
            }
        });
        vectors.add(doPost);
        doPost.start();
        for(Thread thread : vectors){
            thread.join();
        }
        res=res0[0];
        return res;
    }
}

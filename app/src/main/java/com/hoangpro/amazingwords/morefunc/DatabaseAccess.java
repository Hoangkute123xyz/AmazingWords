package com.hoangpro.amazingwords.morefunc;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseAccess {
    private String DB_PATH="";
    private String DB_NAME="word.db";
    private Context context;

    public DatabaseAccess(Context context) {
        this.context = context;
    }

    private boolean checkDataBase(){
        if (android.os.Build.VERSION.SDK_INT>=17){
            DB_PATH = context.getApplicationInfo().dataDir+"/databases/";
        } else {
            DB_PATH="/data/data/"+context.getPackageName()+"/databases/";
        }
        File dir = new File(DB_PATH);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(DB_PATH+DB_NAME);
        Log.e("isExists", DB_PATH);
        return file.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);
        String fileName = DB_PATH+DB_NAME;
        OutputStream outputStream= new FileOutputStream(fileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length=inputStream.read(buffer))>0){
            outputStream.write(buffer,0,length);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    public void createDatabase(){
        if (!checkDataBase()){
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

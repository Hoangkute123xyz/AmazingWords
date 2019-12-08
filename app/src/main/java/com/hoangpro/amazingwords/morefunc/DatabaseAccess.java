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
    //Tên các thuộc tính hằng sô input
    private String DB_PATH="";
    private String DB_NAME="word.db";
    private Context context;

    //Hàm tạo
    public DatabaseAccess(Context context) {
        this.context = context;
    }

    //Kiểm tra dataBase
    private boolean checkDataBase(){
        //Nếu version >=17 thì đổi vị trí lưu database
        if (android.os.Build.VERSION.SDK_INT>=17){
            DB_PATH = context.getApplicationInfo().dataDir+"/databases/";
        } else {
            DB_PATH="/data/data/"+context.getPackageName()+"/databases/";
        }
        File dir = new File(DB_PATH);
        //Nếu không tồn tại thư mục dataBase thì tiến hành tạo thư mục chứa database
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(DB_PATH+DB_NAME);
        Log.e("isExists", DB_PATH);
        return file.exists();
    }

    //Ghi database vào bộ nhớ
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

    //Tạo database
    public void createDatabase(){
        //Nếu database không tồn tại thì tiến hành ghi
        if (!checkDataBase()){
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.curry.bhk.bhk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Curry on 2016/8/19.
 */
public class SavePicture {
    public Context mContext;

    public SavePicture(Context context) {
        this.mContext = context;
    }

    /**
     * save picture
     */
    public String saveFile(Bitmap bmp) {
        String sdStatue = Environment.getExternalStorageState();
        if (!sdStatue.endsWith(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mContext, "No SD card.", Toast.LENGTH_LONG).show();
            return null;
        }

        String sd_root_path = android.os.Environment.getExternalStorageDirectory().getPath();
        FileOutputStream b = null;
        File dirFile = new File(sd_root_path + "/myImage");
        if (!dirFile.isDirectory()) {
            dirFile.mkdir();
        }
        SimpleDateFormat sDattFormat = new SimpleDateFormat("yyyMMddhhmmss");
        String data = sDattFormat.format(new Date());
        String pictureName = sd_root_path + "/myImage/" + data + ".jpg";

//        String pictureName = sd_root_path + "/myImage/" + email + ".jpg";
        try {
            b = new FileOutputStream(pictureName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sd_root_path;
    }
}

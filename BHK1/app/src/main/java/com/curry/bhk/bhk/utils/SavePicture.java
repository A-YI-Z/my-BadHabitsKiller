package com.curry.bhk.bhk.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

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
        FileOutputStream fileOutputStream = null;
        File dirFile = new File(sd_root_path + "/BHK");
        if (!dirFile.isDirectory()) {
            dirFile.mkdir();
        }
        SimpleDateFormat sDattFormat = new SimpleDateFormat("yyyMMddhhmmss");
        String data = sDattFormat.format(new Date());
        String pictureName = sd_root_path + "/BHK/" + data + ".jpg";

        try {
            fileOutputStream = new FileOutputStream(pictureName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pictureName;
    }

    public String pictureResult(int requestCode, int resultCode, Intent data, ImageView head_img) {
        String headUrl = "";
        if (requestCode == 0) {// album
            if (resultCode == Activity.RESULT_OK) {
                Uri myuri = data.getData();
                ContentResolver resolver = mContext.getContentResolver();
                Cursor mycursor = resolver.query(myuri, null, null, null, null);
                if (mycursor != null) {
                    mycursor.moveToNext();
                    headUrl = mycursor.getString(mycursor.getColumnIndex("_data"));
//                    Bitmap bm = BitmapFactory.decodeFile(headUrl);
//                    bm = CheckBitmapDegree.rotateBitmapByDegree(bm, CheckBitmapDegree.getBitmapDegree(headUrl));
//                    head_img.setImageBitmap(bm);

                }
                mycursor.close();
            }
        } else if (requestCode == 1) {// take a photo
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap mybitmap = (Bitmap) bundle.get("data");

                    mybitmap = CheckBitmapDegree.rotateBitmapByDegree(mybitmap, CheckBitmapDegree.getBitmapDegree(headUrl));

                    headUrl = saveFile(mybitmap);

//                    head_img.setImageBitmap(mybitmap);


                }
            }
        }
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(headUrl);
        ImageLoader.getInstance().displayImage(imageUrl, head_img);
        return headUrl;
    }
}

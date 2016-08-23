package com.curry.bhk.bhk.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.curry.bhk.bhk.bean.ImageBucket;
import com.curry.bhk.bhk.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * fetcher picture
 */

public class ImageFetcher {
    private static ImageFetcher mImageFetcher;
    private Context mContext;
    private HashMap<String, ImageBucket> mBucketList = new HashMap<String, ImageBucket>();
    private HashMap<String, String> mThumbnailList = new HashMap<String, String>();


    private ImageFetcher(Context context) {
        this.mContext = context;
    }

    public static ImageFetcher getInstance(Context context) {
        // if(context==null)
        // context = MyApplication.getMyApplicationContext();

        if (mImageFetcher == null) {
            synchronized (ImageFetcher.class) {
                mImageFetcher = new ImageFetcher(context);
            }
        }
        return mImageFetcher;
    }

    /**
     * is or not upload the album
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * get the album
     *
     * @param refresh
     * @return
     */
    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
        Iterator<Entry<String, ImageBucket>> itr = mBucketList.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr
                    .next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    private void buildImagesBucketList() {
        Cursor cursor = null;
        try {
            long startTime = System.currentTimeMillis();

            // Construct a thumbnail index
            getThumbnail();

            // Tectonic album index
            String columns[] = new String[]{Media._ID, Media.BUCKET_ID,
                    Media.DATA, Media.BUCKET_DISPLAY_NAME};
            // get a cursor
            cursor = mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
            if (cursor.moveToFirst()) {

                int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
                int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);

                do {
                    String _id = cursor.getString(photoIDIndex);
                    String path = cursor.getString(photoPathIndex);
                    String bucketName = cursor.getString(bucketDisplayNameIndex);
                    String bucketId = cursor.getString(bucketIdIndex);

                    ImageBucket bucket = mBucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        mBucketList.put(bucketId, bucket);
                        bucket.mImageList = new ArrayList<ImageItem>();
                        bucket.mBucketName = bucketName;
                    }

                    bucket.mCount++;

                    ImageItem imageItem = new ImageItem();
                    imageItem.imageId = _id;
                    imageItem.sourcePath = path;
                    imageItem.thumbnailPath = mThumbnailList.get(_id);
                    bucket.mImageList.add(imageItem);

                }
                while (cursor.moveToNext());
            }

            hasBuildImagesBucketList = true;
            long endTime = System.currentTimeMillis();
            Log.d(ImageFetcher.class.getName(), "use time: "
                    + (endTime - startTime) + " ms");
        } finally {
            cursor.close();
        }
    }

    /**
     * Get a thumbnail
     */
    private void getThumbnail() {
        Cursor cursor = null;
        try {
            String[] projection = {Thumbnails.IMAGE_ID, Thumbnails.DATA};
            cursor = mContext.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null,
                    null);
            getThumbnailColumnData(cursor);
        } finally {
            cursor.close();
        }
    }

    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);

                mThumbnailList.put("" + image_id, image_path);
            }
            while (cur.moveToNext());
        }
    }

}

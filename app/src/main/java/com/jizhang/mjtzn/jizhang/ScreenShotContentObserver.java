package com.jizhang.mjtzn.jizhang;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

public class ScreenShotContentObserver extends ContentObserver{
    private Context context;
    private static String previousPath="";

    public ScreenShotContentObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{
                    //MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA
            }, null, null, null);
            if (cursor != null && cursor.moveToLast()) {
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String path = cursor.getString(dataColumnIndex);
                if(!previousPath.equals(path) && isScreenshot(path)){
                    //Log.i("scrnshot", path);

                    Util.getInstance().ocrwork.getRecognitionResultByImage(path);

                    previousPath=path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        super.onChange(selfChange, uri);
    }

    private boolean isScreenshot(String path) {
        if(path == null || path.equals("")){
            return false;
        }
        String Lpath=path.toLowerCase();
        String screenshots[]={"screenshot","screen_shot","screen-shot","screen shot",
                                "screencapture","screen_capture","screen-capture","screen capture",
                                "screencap","screen_cap","screen-cap","screen cap"};
        for(String keyword:screenshots){
            if(Lpath.contains(keyword)){
                return true;
            }
        }
        return false;
    }
}

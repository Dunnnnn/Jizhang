package com.jizhang.mjtzn.jizhang;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OCRwork {
    private BaiduOCRService baiduOCRService;
    private static final String ACCESS_TOKEN = "24.a4e769f75463d19e6f9348a15e1df85d.2592000.1547286740.282335-15152730";

    public OCRwork() {
        //Log.d("OCR","init");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://aip.baidubce.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        baiduOCRService = retrofit.create(BaiduOCRService.class);

    }

    public void getRecognitionResultByImage(final String path) {
        new Thread(){
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                String encodeResult = bitmapToString(bitmap);
                baiduOCRService.getRecognitionResultByImage(ACCESS_TOKEN, encodeResult)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RecognitionResultBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(RecognitionResultBean recognitionResultBean) {
                                RecordBean record=Util.getInstance().imageInfo2Record(recognitionResultBean);
                                if(record.getAmount()<0.00001 && record.getAmount()>-0.00001){
                                    return;
                                }
                                record.setRemark("来自截图"+path2filename(path));
                                Util.getInstance().databaseHelper.addRecord(record);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("onerror", e.toString());
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }
        }.start();
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private String path2filename(String path){
        String strs[]=path.split("/");
        if(strs.length>=1){
            return strs[strs.length-1];
        }else{
            return path;
        }
    }
}

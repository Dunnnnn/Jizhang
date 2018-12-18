package com.jizhang.mjtzn.jizhang;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static Util instance;
    public RecordDatabaseHelper databaseHelper;
    public Context context;
    public static OCRwork ocrwork;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        databaseHelper = new RecordDatabaseHelper(context, RecordDatabaseHelper.DB_NAME, null, 1);
        ocrwork = new OCRwork();
    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }


    //unix time -> 11:11
    public static String long2HM(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(timestamp));
    }

    //unix time -> 2018-12-12
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    private boolean supercontains(LinkedList<String> words,String keywords[]){
        int i,k=0;
        for(i=0;i<words.size();i++){
            if(words.get(i).contains(keywords[k])){
                k++;
                if(k==keywords.length){
                    return true;
                }
            }
        }
        return false;
    }

    public RecordBean imageInfo2Record(RecognitionResultBean resultbean) {
        RecordBean res = new RecordBean();
        LinkedList<String> words = resultbean.getWords_result();
        Log.i("words",words.toString());

        //支付宝收款
        if( supercontains(words,new String[]{"账单详情","交易成功","收款方式"}) ){
            for(int i=0;i<words.size();i++){
                if(words.get(i).contains("交易成功")){
                    res.setAmount(str2float(words.get(i-1)));
                    break;
                }
            }
            res.setType(2);//收入
            res.setCategory("支付宝收款");
            return res;
        }
        //支付宝消费
        if( supercontains(words,new String[]{"完成","支付成功","付款方式"}) ){
            for(int i=0;i<words.size();i++){
                if(words.get(i).contains("支付成功")){
                    res.setAmount(str2float(words.get(i+1)));
                    break;
                }
            }
            res.setType(1);//支出
            res.setCategory("支付宝消费 "+words.get(words.size()-1));
            return res;
        }
        //微信钱包转账
        if( supercontains(words,new String[]{"支付成功","确认收钱","完成"}) ){
            for(int i=0;i<words.size();i++){
                if(words.get(i).contains("确认收钱")){
                    res.setAmount(str2float(words.get(i+1)));
                    res.setType(1);//支出
                    res.setCategory("微信转账 "+words.get(i));
                    break;
                }
            }
            return res;
        }
        //微信钱包收款
        if( supercontains(words,new String[]{"交易详情","已收钱","转账时间"}) ){
            for(int i=0;i<words.size();i++){
                if(words.get(i).contains("已收钱")){
                    res.setAmount(str2float(words.get(i+1)));
                    break;
                }
            }
            res.setType(2);//收入
            res.setCategory("微信收款");
            return res;
        }
        //微信钱包消费
        if( supercontains(words,new String[]{"支付成功","完成"}) ){
            for(int i=0;i<words.size();i++){
                if(words.get(i).contains("支付成功")){
                    res.setAmount(str2float(words.get(i+2)));
                    res.setType(1);//支出
                    res.setCategory("微信消费 "+words.get(i+1));
                    break;
                }
            }
            return res;
        }
        return res;
    }

    public static double str2float(String str) {
        double res = 0;
        String tempstr;
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        Matcher m = p.matcher(str);
        if (m.find()) {
            tempstr = m.group(1) == null ? "" : m.group(1);
            res = Double.parseDouble(tempstr);
            return res;
        } else {
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                tempstr = m.group(1) == null ? "" : m.group(1);
                res = Double.parseDouble(tempstr);
                return res;
            } else {
                return res;
            }
        }
    }

    public RecordBean notification2record(String title, String content) {
        RecordBean res = new RecordBean();

        if (title.equals("交易提醒")) {
            res.setAmount(str2float(content));
            res.setType(1);//支出
            res.setCategory("支付宝交易");
        }
        if (title.equals("退款提醒")) {
            res.setAmount(str2float(content));
            res.setType(2);//收入
            res.setCategory("支付宝退款");
        }
        if (title.equals("通知测试")) {
            res.setAmount(str2float(content));
            res.setType(1);//支出
            res.setCategory("测试交易");
        }
        res.setRemark("来自通知" + title + ": " + content);
        return res;
    }

    private boolean isSameMonth(String d1,String d2){
        if(d1.charAt(5)==d2.charAt(5) && d1.charAt(6)==d2.charAt(6)){
            return true;
        }
        else return false;
    }

    public String getMonthexpense(){
        double result=0;

        LinkedList<RecordBean> records=databaseHelper.getAllRecords();
        String date=getDate();

        for(int i=records.size()-1;i>=0;i--){
            if(isSameMonth(records.get(i).getDate(),date)){
                if(records.get(i).getType()==1){
                    result+=records.get(i).getAmount();
                }
            }
        }

        return String.format("%.2f",result);
    }

    public String getTodayexpense(){
        double result=0;

        LinkedList<RecordBean> records=databaseHelper.readRecords(getDate());

        for(int i=0;i<records.size();i++){
            if(records.get(i).getType()==1){
                result+=records.get(i).getAmount();
            }
        }

        return String.format("%.2f",result);
    }

    public double getexpense(String date){
        double result=0;

        LinkedList<RecordBean> records=databaseHelper.readRecords(date);

        for(int i=0;i<records.size();i++){
            if(records.get(i).getType()==1){
                result+=records.get(i).getAmount();
            }
        }

        return result;
    }

    public String getMonthincome(){
        double result=0;

        LinkedList<RecordBean> records=databaseHelper.getAllRecords();
        String date=getDate();

        for(int i=records.size()-1;i>=0;i--){
            if(isSameMonth(records.get(i).getDate(),date)){
                if(records.get(i).getType()==2){
                    result+=records.get(i).getAmount();
                }
            }
        }

        return String.format("+%.2f",result);
    }

    public double array2MaxY(){
        double result=0;

        return result;
    }

    private String date2rizi(String date){
        String res[]=date.split("-");
        if(res[res.length-1].charAt(0)=='0'){
            return res[res.length-1].charAt(1)+"日";
        }
        else return res[res.length-1]+"日";
    }

    public void setTable(HistogramView table){
        LinkedList<String> dates=databaseHelper.getAvaliableDate();
        if(dates.size()>7){
            for(int i=7;i<dates.size();i++){
                dates.remove(i);
                i--;
            }
        }
        int length=dates.size();
        double datas[]=new double[length];
        double max=0;
        for(int i=0;i<length;i++){
            datas[i]=getexpense(dates.get(length-1-i));
            if(max<datas[i])max=datas[i];
        }
        table.setData(datas);
        double maxy=((int)max/100+1)*100.0;
        table.setMaxY(maxy);

        int part=((int)maxy+1)/4;
        String y[]=new String[5];
        for (int i = 4; i >=0; i--) {
            y[i]=String.format("%d",part*(4-i));
        }
        table.setySteps(y);

        String x[]=new String[length];
        int k=0;
        for (int i = 0; i < length ; i++) {
            x[i]=date2rizi(dates.get(length-1-i));
            k++;
        }
        table.setxWeeks(x);

    }

}

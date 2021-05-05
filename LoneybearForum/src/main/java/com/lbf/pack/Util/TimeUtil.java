package com.lbf.pack.Util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.auth.callback.Callback;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@Data
public class TimeUtil {


    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat df3 = new SimpleDateFormat("yyyyMMdd");
    private String cuerrent_time = df1.format(new Date());
    private String cuerrent_date = df2.format(new Date());
    private String cuerrent_date_no_shortline = df3.format(new Date());

    public String getCuerrent_time_String() {
        return cuerrent_time;
    }

    public String getCuerrent_date_String() {
        return cuerrent_date;
    }

    public String getCuerrent_date_no_line_String() {
        return cuerrent_date_no_shortline;
    }

//    public Date getCurrentTime(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        return df.format(new Date());// new Date()为获取当前系统时间
//    }
    public String getExpireTime(int second){
         Calendar cal = Calendar.getInstance();
         cal.add(Calendar.SECOND,second);
         String expire_time = df1.format(cal.getTime());
         return expire_time;
    }

    public long secondToMinute(long second){
        return second/60;
    }

    public String secondToDay(long second){
        String day = "";
        if(second / (24 * 60 * 60)!=0){
            day=day.concat( second / (24 * 60 * 60) + "天");
        }
        if(second % (24 * 60 * 60) / (60 * 60)!=0){
            day=day.concat( second % (24 * 60 * 60) / (60 * 60) + "小时");
        }
        if(second % (24 * 60 * 60) % (60 * 60) / 60!=0){
            day=day.concat( second % (24 * 60 * 60) % (60 * 60) / 60 + "分钟");
        }
        day=day.concat( second % (24 * 60 * 60) % (60 * 60) % 60 + "秒");

       return day;
    }
}

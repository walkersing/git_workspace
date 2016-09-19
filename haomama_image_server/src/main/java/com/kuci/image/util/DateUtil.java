package com.kuci.image.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


	
	/**
	 * 得到目标时间与当前时间的时间差
	 * 如 1分钟前 10分钟前等
	 * @param date
	 * @return
	 */
	public static String getTimeDiff(Date date){
		if(date == null){
			return "未知";
		}
		Date d = new Date();
		String meg = "";
		long diff = d.getTime() - date.getTime();
		long l = diff/(1000*60);
		if(l >= 1){
			if(l/60 >= 1){
				l=l/60;
				if(l/24 >= 1){
					l=l/24;
					if(l/30 >= 1){
						l=l/30;
						if( l/12>=1){
							l=l/12;
							meg = l+"年前";
						}
						else{
							meg = l+"月前";
						}
					}else{
						meg = l+"天前";
					}
				}else{
					meg = l+"小时前";
				}
			}else{
				meg = l+"分钟前";
			}
		}else{
			meg = "1分钟前";
		}
		return meg;	
	}
	
	
	/**
	 * 得到目标时间与当前时间的时间差
	 * 如 1分钟前 10分钟前等
	 * @param date
	 * @return
	 */
	public static String getTimeDiffEng(Date date){
		if(date == null){
			return "";
		}
		Date d = new Date();
		String meg = "";
		long diff = d.getTime() - date.getTime();
		long l = diff/(1000*60);
		if(l >= 1){
			if(l/60 >= 1){
				l=l/60;
				if(l/24 >= 1){
					l=l/24;
					if(l/30 >= 1){
						l=l/30;
						if( l/12>=1){
							l=l/12;
							meg = l+" years ago";
						}
						else{
							meg = l+" months ago";
						}
					}else{
						meg = l+" days ago";
					}
				}else{
					meg = l+" hours ago";
				}
			}else{
				meg = l+" minutes ago";
			}
		}else{
			meg = "one minute ago";
		}
		
		if(l==1)
			meg = meg.replaceAll("s", "");
		return meg;	
	}
	
	public static String getTimeDiffOrTime(Date date){
		if(date == null){
			return "未知";
		}
		Date d = new Date();
		String meg = "";
		long diff = d.getTime() - date.getTime();
		long l = diff/(1000*60*60);
		if(l>=1){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
			if(sdf.format(d).equals(sdf.format(date))){
				sdf = new SimpleDateFormat("HH:mm"); 
				meg = "今天 " +sdf.format(date);
			}else{
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
				meg = sdf.format(date);
			}
		}else{
			l = diff/(1000*60);
			if(l < 1){
				l = 1;
			}
			meg = l+"分钟前";
		}
		return meg;
	}
	
	/**
	 * 判断是否为当天
	 * @param date
	 * @return  true为当天  false 不是当天
	 */
	public static boolean isToady(Date date){
		if(date == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(sdf.format(new Date()).equals(sdf.format(date))){
			return true;
		}
		return false;
	}
	
	public static String getDateChStr() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
    }

    public static String getDateStr() {
        Calendar calendar = new GregorianCalendar();
        int mon = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return (calendar.get(Calendar.YEAR) + "-" + (mon <= 9 ? ("0" + mon) : mon) + "-" + (day <= 9 ? ("0" + day) : day));
    }

    public static long paseDateLong(String date, String pattern) {
        long result = -1;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            result = format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String formatToStr(long date, String pattern) {
        Date d = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(d);
    }
    
    public static String getPreDay() {
        Date d = new Date(System.currentTimeMillis()-24*2600*1000);
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
        return format.format(d);
    }


   
    /**
	 * 得到当前日期
	 * @param format
	 *            默认 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static final String getDate(String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_PATTERN;
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String cdate = sdf.format(cal.getTime());
		return cdate;
	}
	public static final String getDate(long time,String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_PATTERN;
		}
		Date d = new Date(time);
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(d);
	}
	
	public static final String getDate(Date d,String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_PATTERN;
		}
		if(d==null){
			d = new Date(System.currentTimeMillis());
		}
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(d);
	}

	public static final int DAY_SECONDS = 86400;
    
    public static final DateFormat DATE_YMD = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat DATE_YMDH = new SimpleDateFormat("yyyy-MM-dd HH");

    public static final DateFormat DATE_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
    public static final DateFormat DATETIME_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateFormat DATETIME_YMDHMSS_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    
    public static final DateFormat TIME_HMS = new SimpleDateFormat("HH:mm:ss");
    
    public static final DateFormat DIGIT_YMDHMS=new SimpleDateFormat("yyyyMMddHHmmss");
    
    //Thu Mar 12 15:57:22 CST 2009 or Wed Mar 11 00:00:00 UTC 2009
    public static final DateFormat CST=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
    
    //12 Mar 2009 09:09:47 GMT
    public static final DateFormat GMT=new SimpleDateFormat("dd MMM yyyy HH:mm:ss zzz",Locale.US);
    
    
    public static final String[] SHORT_CN_WEEK={"日","一","二","三","四","五","六"};
    
    public static final String[] SHORT_EN_WEEK={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    
    public static final String[] EN_WEEK={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    
    public static final String[] SHORT_CN_MONTH={"一","二","三","四","五","六","七","八","九","十","十一","十二"};
    
    public static final String[] SHORT_EN_MONTH={"Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec"};
    
    public static final String[] EN_MONTH={"January","February","March","April","May","June","July","Aguest","September","October","November","December"};
    
    public static final Calendar CALENDAR = Calendar.getInstance();
    
    public static String getDate(){
            return new Timestamp(System.currentTimeMillis()).toString().substring(0,10);
    }
    
    public static String getDateTime(){
            return new Timestamp(System.currentTimeMillis()).toString().substring(0,19);
    }
    
    /**
     * @param format
     * @param date
     * @return
     */
    public static String format(String format,Date date){
            return new SimpleDateFormat(format).format(date);
//          CALENDAR.setTime(date);
//          int year = CALENDAR.get(Calendar.YEAR);
//          int month = CALENDAR.get(Calendar.MONTH);
//          int day = CALENDAR.get(Calendar.DAY_OF_MONTH);
//          int hour = CALENDAR.get(Calendar.HOUR_OF_DAY);
//          int min = CALENDAR.get(Calendar.MINUTE);
//          int sec = CALENDAR.get(Calendar.SECOND);
//          return year + "-" + (month < 10 ? "0" + month : "" + month) + "-"
//                  + (day < 10 ? "0" + day : "" + day)
//                  + (hour < 10 ? " 0" + hour : " " + hour) + ":"
//                  + (min < 10 ? "0" + min : "" + min) + ":"
//                  + (sec < 10 ? "0" + sec : "" + sec);
    }
            
    public static Date parse(DateFormat dateFormat,String str){
            try {
                    return dateFormat.parse(str);
            } catch (ParseException e) {
                   
            }
            return null;
    }
    
    /**
     * TODO
     * @param str
     * @return
     */
    public static Date parse(String str) {
            try {
                    if(str.indexOf("CST")>-1){
                            return CST.parse(str);
                    }else if(str.indexOf("GMT")>-1){//12 Mar 2009 09:09:47 GMT
                            return GMT.parse(str);
                    }else if (str.length() > 10){
                            return DATETIME_YMDHMS.parse(str);
                    }else if (str.indexOf(':') == -1){
                            return DATE_YMD.parse(str);
                    }else{
                            return TIME_HMS.parse(str);
                    }
            } catch (ParseException e) {
            }
            return null;
    }
            
    /**
     * 在当前时间上加上指定偏移的秒数(seconds为负数则减去相应秒数),得到新的时间
     * @param seconds
     * @return java.util.Date
     */
    public static Date getDateByOffsetSeconds(long seconds){
            Date date = new Date();
            long time = (date.getTime() / 1000) + seconds;
            date.setTime(time * 1000);
            return date;
    }       
    
    public static List<Date> getDateList(Date begin,Date end){
            int differ=getDifferDays(begin,end);
            List<Date> temp=new ArrayList<Date>(differ);
            for(int i=0;i<=differ;i++){
                    CALENDAR.setTime(begin);
                    CALENDAR.add(Calendar.DATE,i);
                    temp.add(CALENDAR.getTime());
            }
            return temp;
    }
    
    public static int getDifferDays(Date begin,Date end){
            Long d=((begin.getTime()-end.getTime())/1000/DAY_SECONDS);
            return d.intValue();
    }

    public static int getDayOfWeek(Date date){
            CALENDAR.setTime(date);  
            return CALENDAR.get(Calendar.DAY_OF_WEEK)-1;
    }
    
    
    public static Date getFirstDateOfWeek(){
            return getDateOfWeek(Calendar.SUNDAY);
    }
    
    public static Date getLastDateOfWeek(){
            return getDateOfWeek(Calendar.SATURDAY);
    }
            
    public static Date getDateOfWeek(int week){
            return getDateOfWeek(new Date(),week);
    }
    
    public static Date getDateOfWeek(Date date,int week){
            CALENDAR.setTime(date);  
    CALENDAR.set(Calendar.DAY_OF_WEEK,week);
            CALENDAR.set(Calendar.HOUR_OF_DAY,0);
            CALENDAR.set(Calendar.MINUTE, 0);
            CALENDAR.set(Calendar.SECOND, 0);
    return CALENDAR.getTime();  
    }

    public static Date getFirstDateOfMonth(){
            try {
				CALENDAR.setTime(DATE_YMD.parse(CALENDAR.get(Calendar.YEAR)+"-"+(CALENDAR.get(Calendar.MONTH)+1)+"-01"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
            return CALENDAR.getTime();
    }
    
    public static Date getLastDateOfMonth(){
            try {
                    CALENDAR.setTime(DATE_YMD.parse(CALENDAR.get(Calendar.YEAR)+"-"+(CALENDAR.get(Calendar.MONTH)+1)+"-"+CALENDAR.getActualMaximum(Calendar.DAY_OF_MONTH)));
                   
            } catch (ParseException e) {
            }
            return CALENDAR.getTime();
    }
}

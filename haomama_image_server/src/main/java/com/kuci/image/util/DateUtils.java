package com.kuci.image.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 得到目标时间与当前时间的时间差 如 1分钟前 10分钟前等
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeDiff(Date date) {
		if (date == null) {
			return "未知";
		}
		Date d = new Date();
		String meg = "";
		long diff = d.getTime() - date.getTime();
		long l = diff / (1000 * 60);
		if (l >= 1) {
			if (l / 60 >= 1) {
				l = l / 60;
				if (l / 24 >= 1) {
					l = l / 24;
					if (l / 30 >= 1) {
						l = l / 30;
						if (l / 12 >= 1) {
							l = l / 12;
							meg = l + "年前";
						} else {
							meg = l + "月前";
						}
					} else {
						meg = l + "天前";
					}
				} else {
					meg = l + "小时前";
				}
			} else {
				meg = l + "分钟前";
			}
		} else {
			meg = "1分钟前";
		}
		return meg;
	}

	public static String getTimeDiffOrTime(Date date) {
		if (date == null) {
			return "未知";
		}
		Date d = new Date();
		String meg = "";
		long diff = d.getTime() - date.getTime();
		long l = diff / (1000 * 60 * 60);
		if (l >= 1) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (sdf.format(d).equals(sdf.format(date))) {
				sdf = new SimpleDateFormat("HH:mm");
				meg = "今天 " + sdf.format(date);
			} else {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				meg = sdf.format(date);
			}
		} else {
			l = diff / (1000 * 60);
			if (l < 1) {
				l = 1;
			}
			meg = l + "分钟前";
		}
		return meg;
	}

	/**
	 * 判断是否为当天
	 * 
	 * @param date
	 * @return true为当天 false 不是当天
	 */
	public static boolean isToady(Date date) {
		if (date == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (sdf.format(new Date()).equals(sdf.format(date))) {
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
		Date d = new Date(System.currentTimeMillis() - 24 * 2600 * 1000);
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
		return format.format(d);
	}

	/**
	 * 得到当前日期
	 * 
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

	public static final String getDate(long time, String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_PATTERN;
		}
		Date d = new Date(time);
		SimpleDateFormat fm = new SimpleDateFormat(format);
		return fm.format(d);
	}

	public static final String getDate(String date, String format)
			throws ParseException {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_PATTERN;
		}
		SimpleDateFormat fm = new SimpleDateFormat(format);
		Date d = fm.parse(date);
		return fm.format(d);
	}
	
	public static final String getDate(Date d, String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_PATTERN;
		}
		if (d == null) {
			d = new Date(System.currentTimeMillis());
		}
		SimpleDateFormat fm = new SimpleDateFormat(format);
		return fm.format(d);
	}

	public static long getNow() {
		return System.currentTimeMillis();
	}

}

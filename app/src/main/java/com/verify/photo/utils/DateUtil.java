package com.verify.photo.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	// 默认显示日期的格式
	public static final String DATAFORMAT_STR = "yyyy-MM-dd";

	// 默认显示日期的格式
	public static final String YYYY_MM_DATAFORMAT_STR = "yyyy-MM";

	// 默认显示日期时间的格式
	public static final String DATATIMEF_STR = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	// 默认显示日期时间的格式
	public static final String ACTION_DATATIMEF_STR = "yyyy/MM/dd HH:mm:ss";

	// 默认显示简体中文日期的格式
	public static final String ZHCN_DATAFORMAT_STR = "yyyy年MM月dd日";
	
	// 默认显示简体中文日期的格式
	public static final String ZHCN_DATA_STR = "MM月dd日";

	// 默认显示简体中文日期时间的格式
	public static final String ZHCN_DATATIMEF_STR = "yyyy年MM月dd日HH时mm分ss秒";

	// 默认显示简体中文日期时间的格式
	public static final String ZHCN_DATATIMEF_STR_4yMMddHHmm = "yyyy年MM月dd日HH时mm分";
	//时间格式毫秒
	public static final String YYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	//下拉刷新头显示时间
	public static final String DATATIME_REFRESH = "MM-dd HH:mm";

	private static SimpleDateFormat getDateFormat(String formatStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr, Locale.CHINA);
		return dateFormat;
	}

	/**
	 * 
	 * 按照默认显示日期时间的格式"yyyy-MM-dd HH:mm:ss"，转化dateTimeStr为Date类型
	 * 
	 * dateTimeStr必须是"yyyy-MM-dd HH:mm:ss"的形式
	 * 
	 * @param dateTimeStr
	 * 
	 * @return
	 */
	public static Date getDate(String dateTimeStr) {
		return getDate(dateTimeStr, DATATIMEF_STR);
	}

	/**
	 * 
	 * 按照默认formatStr的格式，转化dateTimeStr为Date类型
	 * 
	 * dateTimeStr必须是formatStr的形式
	 * 
	 * @param dateTimeStr
	 * 
	 * @param formatStr
	 * 
	 * @return
	 */
	public static Date getDate(String dateTimeStr, String formatStr){
		try{
			if (dateTimeStr == null || dateTimeStr.equals("")){
				return null;
			}
			SimpleDateFormat sdf = getDateFormat(formatStr);
			Date d = sdf.parse(dateTimeStr);
			return d;
		}catch (ParseException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * 将YYYYMMDD转换成Date日期
	 *
	 * @param date
	 *
	 * @return
	 *
	 * @throws BusinessException
	 */
	public static Date transferDate(String date) throws Exception {
		if (date == null || date.length() < 1)
			return null;

		if (date.length() != 8)
			throw new Exception("日期格式错误");

		String con = "-";
		String yyyy = date.substring(0, 4);
		String mm = date.substring(4, 6);
		String dd = date.substring(6, 8);
		int month = Integer.parseInt(mm);
		int day = Integer.parseInt(dd);
		if (month < 1 || month > 12 || day < 1 || day > 31)
			throw new Exception("日期格式错误");

		String str = yyyy + con + mm + con + dd;
		return DateUtil.getDate(str, DateUtil.DATAFORMAT_STR);
	}

	/**
	 *
	 * 将Date转换成字符串“yyyy-mm-dd hh:mm:ss”的字符串
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String dateToDateString(Date date){
		return dateToDateString(date, DATATIMEF_STR);
	}


	/**
	 *
	 * 将Date转换成formatStr格式的字符串
	 *
	 * @param date
	 *
	 * @param formatStr
	 *
	 * @return
	 */
	public static String dateToDateString(Date date, String formatStr){
		SimpleDateFormat df = getDateFormat(formatStr);
		return df.format(date);
	}

	/**
	 *
	 * 返回一个yyyy-MM-dd HH:mm:ss 形式的日期时间字符串中的HH:mm:ss
	 *
	 * @param dateTime
	 *
	 * @return
	 */
	public static String getTimeString(String dateTime){
		return getTimeString(dateTime, DATATIMEF_STR);
	}

	/**
	 *
	 * 返回一个formatStr格式的日期时间字符串中的HH:mm:ss
	 *
	 * @param dateTime
	 *
	 * @param formatStr
	 *
	 * @return
	 */
	public static String getTimeString(String dateTime, String formatStr){
		Date d = getDate(dateTime, formatStr);
		String s = dateToDateString(d);
		return s.substring(DATATIMEF_STR.indexOf('H'));

	}


	/**
	 *
	 * 获取当前日期
	 *
	 * @return
	 */
	public static Date getCurrentDate(){

		return new Date(System.currentTimeMillis());

	}

	/**
	 *
	 * 获取当前日期yyyy-MM-dd的形式
	 *
	 * @return
	 */
	public static String getDateByDate(Date date){

		return dateToDateString(date,DATAFORMAT_STR);

	}
	/**
	 *
	 * 获取当前毫秒130708184205047的形式
	 *
	 * @return
	 */
	public static String getCurMsecDate(){
		return dateToDateString(getCurrentDate(),YYMMDDHHMMSSSSS);

	}

	/**
	 *
	 * 获取当前日期yyyy/MM/dd的形式
	 *
	 * @return
	 */
	public static String getDate(){
		Calendar c = Calendar.getInstance();// 获取系统默认是日期
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String date=new StringBuffer().append(year).append("/").append(month).append("/").append(day).toString();
		return date;

	}

	/**
	 *
	 * 获取明天的日期yyyy-MM-dd的形式
	 *
	 * @return
	 */
	public static String getTomorrowDate(){
		Date date=getCurrentDate();
	    Calendar calendar = new GregorianCalendar();
	    calendar.add(calendar.DATE, 1);
	    date=calendar.getTime();
	    SimpleDateFormat formatter = new SimpleDateFormat(DATAFORMAT_STR);
	    String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 *
	 * 获取当前日期yyyy年MM月dd日的形式
	 *
	 * @return
	 */
	public static String getCurZhCNDate(){
		return dateToDateString(getCurrentDate(), ZHCN_DATAFORMAT_STR);
	}

	/**
	 *
	 * 获取当前日期时间yyyy-MM-dd HH:mm:ss的形式
	 *
	 * @return
	 */
	public static String getCurDateTime(){
		return dateToDateString(getCurrentDate(), DATATIMEF_STR);
	}

	/**
	 *
	 * 获取当前日期yyyy-MM-dd的形式
	 *
	 * @return
	 */
	public static String getCurDate(){

		return dateToDateString(getCurrentDate(),DATAFORMAT_STR);

	}


	/**
	 *
	 * 获取当前日期时间yyyy-MM-dd HH:mm的形式
	 *
	 * @return
	 */
	public static String getDateTime(){
		return dateToDateString(getCurrentDate(), YYYY_MM_DD_HH_MM);
	}

	/**
	 *
	 * 获取当前日期时间yyyy年MM月dd日HH时mm分ss秒的形式
	 *
	 * @return
	 */
	public static String getCurZhCNDateTime(){
		return dateToDateString(getCurrentDate(), ZHCN_DATATIMEF_STR);
	}

	/**
	 *
	 * 获取日期d的days天后的一个Date
	 *
	 * @param d
	 *
	 * @param days
	 *
	 * @return
	 */
	public static Date getInternalDateByDay(Date d, int days){

		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.DATE, days);
		return now.getTime();
	}

	public static Date getInternalDateByMon(Date d, int months){

		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.MONTH, months);
		return now.getTime();
	}

	public static Date getInternalDateByYear(Date d, int years){

		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.YEAR, years);
		return now.getTime();

	}

	public static Date getInternalDateBySec(Date d, int sec){

		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.SECOND, sec);
		return now.getTime();
	}

	public static Date getInternalDateByMin(Date d, int min){

		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.MINUTE, min);
		return now.getTime();
	}

	public static Date getInternalDateByHour(Date d, int hours){

		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.HOUR_OF_DAY, hours);
		return now.getTime();
	}

	/**
	 *
	 * 根据一个日期字符串，返回日期格式，目前支持4种
	 *
	 * 如果都不是，则返回null
	 *
	 * @param DateString
	 *
	 * @return
	 */
	public static String getFormateStr(String DateString){

		String patternStr1 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"; // "yyyy-MM-dd"
		String patternStr2 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"; // "yyyy-MM-dd HH:mm:ss";
		String patternStr3 = "[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日";// "yyyy年MM月dd日"
		String patternStr4 = "[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日[0-9]{1,2}时[0-9]{1,2}分[0-9]{1,2}秒";// "yyyy年MM月dd日HH时mm分ss秒"
		Pattern p = Pattern.compile(patternStr1);
		Matcher m = p.matcher(DateString);
		boolean b = m.matches();
		if(b) return DATAFORMAT_STR;

		p = Pattern.compile(patternStr2);
		m = p.matcher(DateString);
		b = m.matches();
		if(b) return DATATIMEF_STR;

		p = Pattern.compile(patternStr3);
		m = p.matcher(DateString);
		b = m.matches();
		if(b) return ZHCN_DATAFORMAT_STR;

		p = Pattern.compile(patternStr4);
		m = p.matcher(DateString);
		b = m.matches();
		if(b) return ZHCN_DATATIMEF_STR;

		return null;
	}

	/**
	 *
	 * 将一个"yyyy-MM-dd HH:mm:ss"字符串，转换成"yyyy年MM月dd日HH时mm分ss秒"的字符串
	 *
	 * @param dateStr
	 *
	 * @return
	 */
	public static String getZhCNDateTime(String dateStr){

		Date d = getDate(dateStr);
		return dateToDateString(d, ZHCN_DATATIMEF_STR);
	}

	/**
	 *
	 * 将一个"yyyy-MM-dd"字符串，转换成"yyyy年MM月dd日"的字符串
	 *
	 * @param dateStr
	 *
	 * @return
	 */
	public static String getZhCNDate(String dateStr){

		Date d = getDate(dateStr, DATAFORMAT_STR);
		return dateToDateString(d, ZHCN_DATAFORMAT_STR);
	}

	/**
	 *
	 * 将dateStr从fmtFrom转换到fmtTo的格式
	 *
	 * @param dateStr
	 *
	 * @param fmtFrom
	 *
	 * @param fmtTo
	 *
	 * @return
	 */
	public static String getDateStr(String dateStr, String fmtFrom, String fmtTo){

		Date d = getDate(dateStr, fmtFrom);
		return dateToDateString(d, fmtTo);
	}

	/**
	 *
	 * 比较两个"yyyy-MM-dd HH:mm:ss"格式的日期，之间相差多少毫秒,time2-time1
	 *
	 * @param time1
	 *
	 * @param time2
	 *
	 * @return
	 */
	public static long compareDateStr(String time1, String time2){

		Date d1 = getDate(time1);
		Date d2 = getDate(time2);
		return d2.getTime() - d1.getTime();
	}

	/**
	 *
	 * 比较于当前时间之间相差多少毫秒, "yyyy-MM-dd HH:mm:ss"格式的日期
	 *
	 * @param time
	 *
	 *
	 * @return
	 */
	public static long compareCurrentDateStr(String time){

		Date d1 = getDate(time);
		Date d2 = getCurrentDate();
		return d2.getTime() - d1.getTime();
	}

	/**
	 *
	 * 比较两个时间是否为同一天
	 *
	 * @param time
	 *
	 * @return
	 */
	public static boolean compareTwoDateForSameDay(Date firstDate, Date secondDate) {
		if(firstDate == null || secondDate==null)return false;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(firstDate);
		c2.setTime(secondDate);
		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& c1.get(Calendar.DAY_OF_MONTH) == c2
						.get(Calendar.DAY_OF_MONTH)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * cookie时间比较
	 * @param firstDate 当前时间
	 * @param secondDate 过期时间
     * @return 是否过期
     */
	public static boolean compareTwoDateBefore(Date firstDate, Date secondDate) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(firstDate);
		c2.setTime(secondDate);
		long t1 = c1.getTimeInMillis();
		long t2 = c2.getTimeInMillis();
		if (t1 > t2 - 3600 * 24){
			return true;
		}else{
			return false;
		}
	}

	/**
	 *
	 * 将小时数换算成返回以毫秒为单位的时间
	 *
	 * @param hours
	 *
	 * @return
	 */
	public static long getMicroSec(BigDecimal hours){
		BigDecimal bd;
		bd = hours.multiply(new BigDecimal(3600 * 1000));
		return bd.longValue();
	}


	/**
	 *
	 * 获取Date中的分钟
	 *
	 * @param d
	 *
	 * @return
	 */
	public static int getMin(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.MINUTE);
	}

	/**
	 *
	 * 获取Date中的小时(24小时)
	 *
	 * @param d
	 *
	 * @return
	 */
	public static int getHour(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 *
	 * 获取Date中的小时和分钟(24小时)
	 *
	 * @param d
	 *
	 * @return
	 */
	public static String getTime(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		return hour+":"+min;
	}

	/**
	 *
	 * 获取Date中的秒
	 *
	 * @param d
	 *
	 * @return
	 */
	public static int getSecond(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.SECOND);
	}

	/**
	 *
	 * 获取xxxx-xx-xx的日
	 *
	 * @param d
	 *
	 * @return
	 */
	public static int getDay(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 *
	 * 获取月份，1-12月
	 *
	 * @param d
	 *
	 * @return
	 */
	public static int getMonth(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.MONTH) + 1;
	}

	/**
	 *
	 * 获取19xx,20xx形式的年
	 *
	 * @param d
	 *
	 * @return
	 */
	public static int getYear(Date d){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.YEAR);
	}

	/**
	 * 获取星期
	 * @param d
	 * @return
	 */
	public static String getDayOfWeek(Date d){
		SimpleDateFormat formatD = getDateFormat("EEEE");
		return formatD.format(d);
	}

	/**
	 *
	 * 得到d的上个月的年份+月份,如200505
	 *
	 * @return
	 */
	public static String getYearMonthOfLastMon(Date d){

		Date newdate = getInternalDateByMon(d, -1);
		String year = String.valueOf(getYear(newdate));
		String month = String.valueOf(getMonth(newdate));
		return year + month;
	}

	/**
	 *
	 * 得到当前日期的年和月如200509
	 *
	 * @return String
	 */
	public static String getCurYearMonth(){
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "yyyyMM";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		return (sdf.format(now.getTime()));
	}

	public static Date getNextMonth(String year, String month){
		String datestr = year + "-" + month + "-01";
		Date date = getDate(datestr, DATAFORMAT_STR);
		return getInternalDateByMon(date, 1);
	}

	public static Date getLastMonth(String year, String month){
		String datestr = year + "-" + month + "-01";
		Date date = getDate(datestr, DATAFORMAT_STR);
		return getInternalDateByMon(date, -1);
	}

	/**
	 * 
	 * 得到日期d，按照页面日期控件格式，如"2001-3-16"
	 * 
	 * @return
	 */
	public static String getSingleNumDate(){
		return dateToDateString(new Date(System.currentTimeMillis()), DATAFORMAT_STR);
	}

	/**
	 * 
	 * 得到d半年前的日期,"yyyy-MM-dd"
	 * 
	 * @param d
	 * 
	 * @return
	 */
	public static String getHalfYearBeforeStr(Date d){
		return dateToDateString(getInternalDateByMon(d, -6), DATAFORMAT_STR);
	}

	/**
	 * 
	 * 得到当前日期D的月底的前/后若干天的时间,<0表示之前，>0表示之后
	 * 
	 * @param d
	 * 
	 * @param days
	 * 
	 * @return
	 */
	public static String getInternalDateByLastDay(Date d, int days){
		return dateToDateString(getInternalDateByDay(d, days), DATAFORMAT_STR);
	}

	/**
	 * 
	 * 日期中的年月日相加
	 * 
	 * @param field int 需要加的字段 年 月 日
	 * 
	 * @param amount int 加多少
	 * 
	 * @return String
	 */
	public static String addDate(int field, int amount){
		String Time = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			cal.add(field, amount);
			Time = sdf.format(cal.getTime());
			return Time;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 日期中的年月日时分秒相加
	 * 
	 * @param field int 需要加的字段 年 月 日时分秒
	 * 
	 * @param amount int 加多少
	 * 
	 * @return String
	 */
	public static String addDate(Date date, int field, int amount){
		String Time = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(DATATIMEF_STR);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(field, amount);
			Time = sdf.format(cal.getTime());
			return Time;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 获得系统当前月份的天数
	 * 
	 * @return
	 */
	public static int getCurentMonthDay(){
		Date date = getCurrentDate();
		return getMonthDay(date);
	}

	/**
	 * 
	 * 获得指定日期月份的天数
	 * 
	 * @return
	 */
	public static int getMonthDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * 获得指定日期月份的天数 yyyy-mm-dd
	 * 
	 * @return
	 */
	public static int getMonthDay(String date){
		Date strDate = getDate(date, DATAFORMAT_STR);
		return getMonthDay(strDate);
	}

	public static String getStringDate(Calendar cal){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(cal.getTime());
	}
	
	/**
	 * 给不月份与日期不足两位的日期,补足两位
	 * @param date 为补之前的
	 * @return 补零之后
	 */
	public static String dateZeroize(String date){
		String[] oldDate = date.split("-");
		String month = ((Integer.valueOf(oldDate[1])+100)+"").substring(1);
		String day = ((Integer.valueOf(oldDate[2])+100)+"").substring(1);
		return oldDate[0]+"-"+month+"-"+day;
	}

	/**
	 * 时间戳转化为日期
	 * @param mill
	 * @return
	 */
	public static String timestampConvertToDate(long mill){
		Timestamp timestamp=new Timestamp(mill);
		Date date=new Date(mill);
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat(DATATIMEF_STR);
		return format.format(date);
	}
	
	/**
	 * 判断今天是否是工作日 true 是 false 不是
	 * @return
	 */
	public static boolean todayIsWorkDay(){
		boolean flag=true;
		Calendar c = Calendar.getInstance();
		int dayforweek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayforweek = 7;
		} else {
			dayforweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		if(dayforweek>5){
			flag=false;
		}
		return flag;
	}
	
	public static String getFirstDateByWeek(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal = getFirstDateByWeekCorrect(cal);
//		//cal.setFirstDayOfWeek(Calendar.MONDAY);
//		if(cal.get(Calendar.DAY_OF_WEEK)-1 == 0){
//			cal.add(Calendar.DAY_OF_YEAR, -7);
//		}	
//      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        return dateToDateString(cal.getTime(), DATAFORMAT_STR);
		
	}
	
	public static Calendar getFirstDateByWeekCorrect(Calendar date){
        int dayOfWeek=date.get(Calendar.DAY_OF_WEEK)- Calendar.MONDAY;
        if(dayOfWeek<0){
            dayOfWeek+=7;
        }
        Calendar result= Calendar.getInstance();
        result.setTime(date.getTime());
        result.add(Calendar.DAY_OF_MONTH,(-1)*dayOfWeek);

        return result;
    }
	
	public static String getLastDateByWeek(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return dateToDateString(cal.getTime(), DATAFORMAT_STR);
		
//		Calendar cd = Calendar.getInstance();
//		cd.setTime(date);
//		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1; // 因为按中国礼拜一作为第一天所以这里减1
//		Date lastday=null;
//		int number=0;//本周剩余的天数
//		if (dayOfWeek!=0) {//等于0的话说明是周日，表示是本周最后
//			number=7-dayOfWeek;//本周已还剩多少天
//		}
//		lastday = DateUtil.getInternalDateByDay(date, number);
//		System.out.println("dayOfWeek:"+dayOfWeek);
//		return DateUtil.getDateByDate(lastday);
		
	}
	
	/**
	 * 取得当周的第一天
	 * 
	 * @return
	 */
	public static String getFirst(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		Date monday = null;
		int number;// 本周过去的天数
		if (dayOfWeek == 0) {// 周日
			number = 6;
		} else {
			number = dayOfWeek - 1;// 本周已经过去多少天
		}
		monday = DateUtil.getInternalDateByDay(date, -number);
		return DateUtil.getDateByDate(monday);
	}

	/**
	 * 取得当周的最后一天
	 * 
	 * @return
	 */
	public static String getLast(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		Date lastday = null;
		int number = 0;// 本周剩余的天数
		if (dayOfWeek != 0) {// 等于0的话说明是周日，表示是本周最后
			number = 7 - dayOfWeek;// 本周已还剩多少天
		}
		lastday = DateUtil.getInternalDateByDay(date, number);
		System.out.println("dayOfWeek:" + dayOfWeek);
		return DateUtil.getDateByDate(lastday);
	}
	
	
	/**
	 * 比较两个日期之间的天数
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
       return Integer.parseInt(String.valueOf(between_days));
    }  
	
	/**
	 * 比较两个日期相差的月数
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthSpace(String startDate, String endDate)
			throws ParseException {

		int result = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(sdf.parse(startDate));
		c2.setTime(sdf.parse(endDate));

		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		
		int yearResult = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR))==0?0: Math.abs(c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR))*12;
		result = yearResult + result;
		
		return result;

	}

	public static String getHourAndSecond(String time){
		long longTime = Long.parseLong(time);
		SimpleDateFormat format =new SimpleDateFormat("HH:mm");
		Date date=new Date(longTime*1000);
		return  format.format(date);
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		

		// //System.out.print(DateUtil.getDate("04:04:04","HH:mm:ss"));

		// System.out.print("\n"+DateUtil.getCurZhCNDateTime());

		// System.out.print("\n"+getFormateStr(DateUtil.getCurDate()));

		// System.out.print("\n"+compareDateStr("1900-1-1 1:1:2","1900-1-1 1:1:3"));

		// System.out.print("\n"+getDay(getCurrentDate()));

		// System.out.print("\n"+getMonth(getCurrentDate()));

		// System.out.print("\n"+getYear(getCurrentDate()));

		// System.out.print("\n"+getMin(getCurrentDate()));

		// // System.out.print("\n"+getCurrentDate().getSeconds());

		/*
		 * Date d1 = new Date(2007,11,30);
		 * 
		 * Date d2 = new Date(2007,12,1);
		 * 
		 * if(d2.compareTo(d1)>0){
		 * 
		 * System.out.println("d2大于d1");
		 * 
		 * }else{
		 * 
		 * System.out.println("d2小于d1");
		 * 
		 * }
		 */

		//System.out.println(addDate(1, 1));

		//System.out.println(addDate(2, 1));

		//System.out.println(addDate(3, 1));

		//System.out.println(getCurentMonthDay());
//		System.out.println(getTomorrowDate());
	}

	/**
	 * 格式08月08日  08:18
	 */
	public static String getMouthDay (String time){
		SimpleDateFormat format=new SimpleDateFormat("MM月dd日  HH:mm");
		return format.format(Long.parseLong(time)*1000);
	}

}

package com.verify.photo.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 计算订单倒计时工具类
 * Created by licong on 2018/10/23.
 */

public class CountDownUtils {

    private long day = 0;
    private long hour = 0;
    private long minute = 0;
    private long second = 0;
    private boolean dayNotAlready = false;
    private boolean hourNotAlready = false;
    private boolean minuteNotAlready = false;
    private boolean secondNotAlready = false;
    private Timer timer;
    private Handler handler;

    public CountDownUtils(long totalSeconds, Handler handler) {
        this.handler = handler;
        timer = new Timer();
        initData(totalSeconds);
        timer.schedule(new MyTask(), 0, 1000);
    }

    private boolean getSecondNotAlready() {
        return secondNotAlready;
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (getSecondNotAlready()) {
                startCount();
            } else {
                cancel();
            }
        }
    }

    /**
     * 初始化赋值
     *
     * @param totalSeconds
     */
    private void initData(long totalSeconds) {
        resetData();
        if (totalSeconds > 0) {
            secondNotAlready = true;
            second = totalSeconds;
            if (second >= 60) {
                minuteNotAlready = true;
                minute = second / 60;
                second = second % 60;
                if (minute >= 60) {
                    hourNotAlready = true;
                    hour = minute / 60;
                    minute = minute % 60;
                    if (hour > 24) {
                        dayNotAlready = true;
                        day = hour / 24;
                        hour = hour % 24;
                    }
                }
            }
            if (handler != null) {
                String minuteStr = minute >= 10 ? minute + "" : "0" + minute;
                String secondStr = second >= 10 ? second + "" : "0" + second;
                String text = minuteStr + ":" + secondStr;
                Message message = new Message();
                message.what = 1;
                message.obj = text;
                handler.sendMessage(message);
            }
        } else {
            secondNotAlready = false;
        }

    }

    private void resetData() {
        day = 0;
        hour = 0;
        minute = 0;
        second = 0;
        dayNotAlready = false;
        hourNotAlready = false;
        minuteNotAlready = false;
        secondNotAlready = false;
    }

    /**
     * 计算各个值的变动
     */
    public void startCount() {
        if (secondNotAlready) {
            if (second > 0) {
                second--;
                if (second == 0 && !minuteNotAlready) {
                    secondNotAlready = false;
                }
            } else {
                if (minuteNotAlready) {
                    if (minute > 0) {
                        minute--;
                        second = 59;
                        if (minute == 0 && !hourNotAlready) {
                            minuteNotAlready = false;
                        }
                    } else {
                        if (hourNotAlready) {
                            if (hour > 0) {
                                hour--;
                                minute = 59;
                                second = 59;
                                if (hour == 0 && !dayNotAlready) {
                                    hourNotAlready = false;
                                }
                            } else {
                                if (dayNotAlready) {
                                    day--;
                                    hour = 23;
                                    minute = 59;
                                    second = 59;
                                    if (day == 0) {
                                        dayNotAlready = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (handler != null) {
            String minuteStr = minute >= 10 ? minute + "" : "0" + minute;
            String secondStr = second >= 10 ? second + "" : "0" + second;
            String text = minuteStr + ":" + secondStr;
            Message message = new Message();
            if (minute == 0 && second == 0) {
                message.what = 0;
            } else {
                message.what = 1;
                message.obj = text;
            }
            handler.sendMessage(message);
        }
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}

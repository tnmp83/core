package sscrm.io.phuongdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Debug logs. Using handle error and debug code
 *
 * @author PhuongTNM
 * @version 2.1
 * @since Mar 17, 2014
 */
public class DebugLog {

    public static final String TAG = "demo";
    private static final int DOT_LEVEL = 5; //3 package name dots + 2

    private static boolean hasDebuggable() {
        return true;
    }

    /**
     * <pre>
     * Show log with Debug able in android
     *
     * @param logType includes Log.DEBUG, INFO, VERBOSE, WARN, ERROR
     * @param tag     name for search
     * @param msg     message when show log
     */
    public static void show(int logType, String tag, String msg) {
        if (!hasDebuggable()) {
            return;
        }
        if (null == tag) {
            tag = TAG;
        }

        switch (logType) {
            case Log.ERROR:
                Log.e(tag, log(msg, DOT_LEVEL));
                break;

            case Log.DEBUG:
                Log.d(tag, log(msg, DOT_LEVEL));
                break;

            case Log.INFO:
                Log.i(tag, log(msg, DOT_LEVEL));
                break;

            case Log.VERBOSE:
                Log.v(tag, log(msg, DOT_LEVEL));
                break;

            case Log.WARN:
                Log.w(tag, log(msg, DOT_LEVEL));
                break;

            default:
                Log.d(tag, log(msg, DOT_LEVEL));
                break;
        }
    }

    /**
     * Show log with log type and message
     *
     * @param logType includes Log.DEBUG, INFO, VERBOSE, WARN, ERROR
     * @param msg     message when show log
     */
    public static void show(int logType, String msg) {
        show(logType, null, msg);
    }

    /**
     * Show log with log type
     *
     * @param logType includes Log.DEBUG, INFO, VERBOSE, WARN, ERROR
     */
    public static void show(int logType) {
        show(logType, null, null);
    }

    /**
     * Show log (Log.DEBUG) with tag and message
     *
     * @param tag filter tag
     * @param msg message when show log
     */
    public static void show(String tag, String msg) {
        show(0, tag, msg);
    }

    /**
     * Show log (Log.DEBUG) with message
     *
     * @param msg message when show log
     */
    public static void show(String msg) {
        show(0, null, msg);
    }

    /**
     * Show log (Log.DEBUG)
     */
    public static void show() {
        show(Log.VERBOSE, null, "*** DEBUG LOG ***");
    }

    /**
     * General message
     *
     * @param msg
     * @param level
     * @return string with
     * <pre>
     * ClassName.methodName():lineNumber
     * => message more
     * </pre>
     */
    private static String log(String msg, int level) {

        String fullClassName = Thread.currentThread().getStackTrace()[level].getClassName(); 
        /*
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        if(CLASS_NAME.equals(className)){
        	fullClassName = Thread.currentThread().getStackTrace()[(level - 1)].getClassName(); 
        	className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        }
        */
        String className = fullClassName;

        String methodName = Thread.currentThread().getStackTrace()[level].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[level].getLineNumber();

        String result = className + "." + methodName + "():" + lineNumber;
        if (null != msg && !"".equals(msg)) {
            result = result + "\n=> " + msg;
        }
        return result;
    }

    /**
     * Show log (Log.DEBUG)
     */
    public static void showError(String msg) {
        show(Log.ERROR, null, msg);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SimpleDateFormat")
    private static String getCurrentDate(String msg) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();

        if (null == msg) {
            msg = "";
        }
        msg = msg + "\n" + dateFormat.format(cal.getTime());

        return msg;
    }

    public static void showTime(String tag, String msg) {
        msg = getCurrentDate(msg);
        show(Log.INFO, tag, msg);
    }

    public static void showTime(String msg) {
        msg = getCurrentDate(msg);
        show(Log.INFO, msg);
    }

    public static void parseJsonString(String tag, String jsonString) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        if (null == jsonString) {
            //Show Log
            show(Log.ERROR, tag, "NULL");
        }
        try {
            JSONObject json = new JSONObject(jsonString);
            //Show Log
            show(tag, "Total length: " + json.length());
            String jsonKey = "";
            String result = "";
            int index = 0;
            Iterator<?> keys = json.keys();
            while (keys.hasNext()) {
                index++;
                jsonKey = String.valueOf(keys.next());
                result = index + ". " + jsonKey + " => " + json.getString(jsonKey);
                //Show Log
                Log.i(tag, result);
                //show(Log.INFO, tag, result);
            }
        } catch (JSONException e) {
            show(Log.ERROR, tag, e.getMessage());
        }
    }

    public static void memoryUsed(String message) {
        if (null == message) {
            message = "";
        }

        Runtime rt = Runtime.getRuntime();

        long kb = 1024;
        long maxMemory = rt.maxMemory() / kb;
        long totalMemory = rt.totalMemory() / kb;
        long freeMemory = rt.freeMemory() / kb;
        long usedMemory = totalMemory - freeMemory;

        String msg = "MEMORY: " + message +
                "\n Used Memory: " + usedMemory + "(kb) => (total - free = " + totalMemory + " - " + freeMemory + ")" +
                "\n Max Memory(Heap Size): " + maxMemory + "(kb)";

        show(Log.INFO, msg);
    }

    /**
     * DebugLog.virtualSleep(1000);
     * while (DebugLog.timerSleep()) {
     * // TODO Do something
     * }
     *
     * @param delay is milliseconds and repeat 10
     */
    public static void virtualSleep(long delay) {
        countDown = 0;

        final Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (10 > countDown) {
                    countDown++;
                    show(0, null, "Count " + countDown);
                } else {
                    timer.cancel();
                    show(0, null, "Cancel");
                }
            }
        }, 0, delay);
    }

    private static int countDown = 0;

    public static boolean timerSleep() {
        if (10 <= countDown) {
            return false;
        }
        return true;
    }


    /**
     * Get device density
     */
    public static void deviceDensity(Context context) {

        if (null != context) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            float density = metrics.density;

            String msg = "---- -- ----\n";
            msg += "Density: " + density + " => ";
            if (density >= 4.0) {
                msg += "xxxhdpi";
            } else if (density >= 3.0) {
                msg += "xxhdpi";
            } else if (density >= 2.0) {
                msg += "xhdpi";
            } else if (density >= 1.5) {
                msg += "hdpi";
            } else if (density >= 1.0) {
                msg += "mdpi";
            } else {
                msg += "ldpi";
            }
            msg += " : " + metrics.widthPixels + "x" + metrics.heightPixels;
            msg += "\n--- --- --- ---";

            show(Log.INFO, msg);
        }
    }


    public static void showSplit(String jsonString) {
        int length = jsonString.length();
        int count = (int) Math.floor(length / 500);
        count = count + 1;

        int j = 0;
        show(Log.INFO, "FULL DEBUG: " + count + " x " + length);
        for(int i = 1; i <= count; i++){
            int temp = i * 500;
            if(temp > length){
                temp = length;
            }
            String result = jsonString.substring(j, temp);
            Log.d(TAG, result);
            j = temp;
        }
    }
}

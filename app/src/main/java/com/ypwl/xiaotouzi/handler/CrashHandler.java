package com.ypwl.xiaotouzi.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.PackageManagerUtil;
import com.ypwl.xiaotouzi.utils.StorageUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * function: 截获（记录）崩溃. : 当程序产生未捕获异常则有此类接管并将异常记录在SD卡应用根目录或应用缓存目录的.crashLog文件夹下面.
 *
 * <p>Created by lzj on 2015/12/31.</p>
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class CrashHandler implements UncaughtExceptionHandler {
    /** 记录标志. */
    private static final String TAG = CrashHandler.class.getSimpleName();
    /** CrashHandler实例. */
    private volatile static CrashHandler instance;
    /** 错误日志文件夹名称 . */
    private static final String DIRNAME_CRASHLOG = ".xtzLog";

    /** 初始化. */
    public static void init(Context context) {
        if (null == instance) {
            synchronized (CrashHandler.class) {
                if (null == instance) {
                    instance = new CrashHandler(context);
                }
            }
        }
    }

    /** 程序的Context对象. */
    private Context mContext;
    /** 用于格式化日期,作为日志文件名的一部分. */
    private DateFormat formatter = new SimpleDateFormat("MMdd-HH:mm:ss", Locale.getDefault());

    /** 进程名字. 默认主进程名是包名 */
    private String mProcessName;
    /** 系统默认的UncaughtException处理类. */
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /** 保证只有一个CrashHandler实例. */
    private CrashHandler(Context context) {
        mContext = context.getApplicationContext();
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mProcessName = PackageManagerUtil.getProcessNameByPid(mContext, Process.myPid());
    }

    /** 当UncaughtException发生时会转入该函数来处理. */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "---------------uncaughtException start---------------\r\n");
        Log.e(TAG, "process [" + mProcessName + "],is abnormal!\r\n");
        try {
            handleException(thread, throwable);
        } catch (Exception ex) {
            Log.e(TAG, "uncaughtException,ex:" + ex.getMessage());
            ex.printStackTrace();
//            CrashReport.postCatchedException(ex);
        }
        if (PackageManagerUtil.isMainProcess(mContext, mProcessName)) {
            ComponentName componentName = PackageManagerUtil.getTheProcessBaseActivity(mContext);
            if (componentName != null) {
                Intent intent = new Intent();
                intent.setComponent(componentName);
                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 100毫秒钟后重启应用activit
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "error : ", e);
            }
        }
        Process.killProcess(Process.myPid());
        Log.d(TAG, "---------------uncaughtException end---------------\r\n");
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private boolean handleException(Thread thread, Throwable rhrowable) throws IOException {
        //记录数量达到10个就清理数据
        String logdirPath = StorageUtil.getXtzCacheParent(mContext).getAbsolutePath() + File.separator + DIRNAME_CRASHLOG + File.separator;
        File logDir = new File(logdirPath);
        if (logDir.exists()) {
            clearLogexMax(logDir, 10);
        } else {
            logDir.mkdirs();
        }

        //写入错误信息到文件

        // 本次记录文件名
        Date date = new Date(); // 当前时间
        String logFileName = formatter.format(date) + String.format("[%s-%d]", thread.getName(), thread.getId()) + ".txt";
        File logex = new File(logDir, logFileName);
        logex.createNewFile();
        // 写入异常到文件中
        FileWriter fw = new FileWriter(logex, true);
        fw.write("\r\nProcess[" + mProcessName + "," + Process.myPid() + "]"); // 进程信息，线程信息
        fw.write("\r\n" + thread + "(" + thread.getId() + ")"); // 进程信息，线程信息
        fw.write("\r\nTime stamp：" + date); // 日期
        // 打印调用栈
        PrintWriter printWriter = new PrintWriter(fw);
        rhrowable.printStackTrace(printWriter);
        Throwable cause = rhrowable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        fw.write("\r\n");
        fw.flush();
        printWriter.close();
        fw.close();
        return true;
    }

    /**
     * 清理日志,限制日志数量.
     *
     * @param logdir 日志目录
     * @param max    自多保存的日志数量
     */
    private void clearLogexMax(File logdir, int max) {
        File[] logList = logdir.listFiles();
        if (logList == null || logList.length == 0) {
            return;
        }
        int length = logList.length;
        if (length >= max) {
            for (File aLogList : logList) {
                try {
                    if (aLogList.delete()) {
                        Log.d(TAG, "clearLogexMax delete:" + aLogList.getName());
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "clearLogexMax,ex:" + ex);
                }
            }
        }
    }

}

package com.ypwl.xiaotouzi.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.io.IOException;

/**
 * function :消息提醒帮助类,提示震动及声音
 *
 * <p><uses-permission android:name="android.permission.VIBRATE"/></p>
 *
 * Created by lzj on 2016/3/27
 */
public class MsgHintHelper {
    protected final String TAG = MsgHintHelper.class.getSimpleName();
    private static MsgHintHelper instance;

    private static MsgHintHelper getInstance() {
        if (null == instance) {
            synchronized (MsgHintHelper.class) {
                if (null == instance) {
                    instance = new MsgHintHelper();
                }
            }
        }
        return instance;
    }

    private MsgHintHelper() {
    }

    public static void hintMsgCome(Activity activity) {
        getInstance().hint(activity);
    }

    private void hint(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        switch (audioService.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL://普通模式
                beep(activity);
                vibrate(activity);
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动模式
                vibrate(activity);
                break;
            case AudioManager.RINGER_MODE_SILENT://静音模式
                break;
        }
    }

    /** 提示声音 */
    private void beep(Activity activity) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });
        AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.msg_come);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ioe) {
            LogUtil.w(TAG, ioe);
        }
    }

    /** 提示震动 */
    private void vibrate(Activity activity) {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

}

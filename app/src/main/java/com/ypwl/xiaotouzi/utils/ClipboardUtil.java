package com.ypwl.xiaotouzi.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * function :剪切板工具类
 *
 * Created by lzj on 2016/3/27
 */
@SuppressWarnings({"deprecation", "unused"})
public class ClipboardUtil {

    private static ClipboardManager getClipboardManager() {
        return (ClipboardManager) UIUtil.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 实现复制字符串文本功能
     */
    public static void copyString(String content) {
        getClipboardManager().setText(content.trim());
    }

    /**
     * 实现粘贴字符串文本功能
     */
    public static String pasteString() {
        return getClipboardManager().getText().toString().trim();
    }
}

package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 九宫格工具
 */
public class LockPatternUtils {

	// private static final String TAG = "LockPatternUtils";
	private final static String KEY_LOCK_PWD = "lock_pwd";

	private static Context mContext;

	private static SharedPreferences preference;

	// private final ContentResolver mContentResolver;

	public LockPatternUtils(Context context) {
		mContext = context;
		preference = PreferenceManager.getDefaultSharedPreferences(mContext);
		// mContentResolver = context.getContentResolver();
	}

	/**
	 * Deserialize a pattern.
	 * 
	 * @param string
	 *            The pattern serialized with {@link #patternToString}
	 * @return The pattern.
	 */
	public static List<LockPatternView.Cell> stringToPattern(String string) {
		List<LockPatternView.Cell> result = new ArrayList<LockPatternView.Cell>();

		final byte[] bytes = string.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			result.add(com.ypwl.xiaotouzi.view.LockPatternView.Cell.of(b / 3, b % 3));
		}
		return result;
	}

	/**
	 * Serialize a pattern.
	 * 
	 * @param pattern
	 *            The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<com.ypwl.xiaotouzi.view.LockPatternView.Cell> pattern) {
		if (pattern == null || pattern.size()<4) {//如果少于4个点
			return "";
		}
		final int patternSize = pattern.size();

		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			com.ypwl.xiaotouzi.view.LockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}

		return Arrays.toString(res);
	}

	public void saveLockPattern(List<com.ypwl.xiaotouzi.view.LockPatternView.Cell> pattern) {
		Editor editor = preference.edit();
			editor.putString(KEY_LOCK_PWD, patternToString(pattern));
		editor.commit();
	}

	public String getLockPaternString() {
		return preference.getString(KEY_LOCK_PWD, "");
	}

	public int checkPattern(List<com.ypwl.xiaotouzi.view.LockPatternView.Cell> pattern) {
		String stored = getLockPaternString();
		if (!stored.equals("")) {
			return stored.equals(patternToString(pattern)) ? 1 : 0;
		}
		return -1;
	}

	public void clearLock() {
		saveLockPattern(null);
	}

}

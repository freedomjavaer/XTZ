package com.ypwl.xiaotouzi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * <p>function : 图片处理工具类.</p>
 * Modify by lzj on 2015/11/3.
 */
@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class ImageUtils {

    private static final String TAG = "ImageUtils";
    private static final boolean DEBUG = false;

    /**
     * 旋转图片
     *
     * @param bitmap 要旋转的图片
     * @param degree 旋转的角度
     * @return 这是旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        /**
         * 这是旋转后返回的bitmap对象
         */
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }


    /**
     * 获取指定路径图片
     *
     * @param srcPath 路径
     * @return Bitmap
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap;//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;
        float ww = 480f;
        //缩放比，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = compressImage(BitmapFactory.decodeFile(srcPath, newOpts));
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {    //循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    // 获得圆形图片
    public static Bitmap circleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    // 获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // 获得带倒影的图片方法
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 获取图片
     */
    public static Bitmap getBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 初始化照片保存地址
     */
    public static File savaImagePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String imgName = "xiaotouImagurl.JPEG";
            String thumPicture = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/download";
            File pictureParent = new File(thumPicture);
            File pictureFile = new File(pictureParent, imgName);

            if (!pictureParent.exists()) {
                pictureParent.mkdirs();
            }
            try {
                if (!pictureFile.exists()) {
                    pictureFile.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pictureFile;
        } else {
            return null;//没有sd卡返回空
        }
    }

    /**
     * 保存图片成JPEG格式
     */
    public static boolean saveBitmap(Context context, String path, Bitmap bmp) {
        return saveBitmap(context, new File(path), bmp);
    }

    public static boolean saveBitmap(Context context, File file, Bitmap bmp) {
        try {
            if (!file.exists()) {
                File p = file.getParentFile();
                if (p != null) {
                    p.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            // 100 means no compression
            boolean isCompress = bmp.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            context.getContentResolver().notifyChange(Uri.fromFile(file), null);

            return isCompress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	/*
    public static void notifySdMounted(Context context) {
		Uri sdCardUri = Uri.fromFile(Environment.getExternalStorageDirectory()); 
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, sdCardUri));
	}
	*/

    // 添加到gallery
    /*
     * When you create a photo through an intent,
	 * you should know where your image is located, 
	 * because you said where to save it in the first place. 
	 * For everyone else, perhaps the easiest way to make 
	 * your photo accessible is to make it accessible 
	 * from the system's Media Provider.
	 * The following example method demonstrates 
	 * how to invoke the system's media scanner to add 
	 * your photo to the Media Provider's database, 
	 * making it available in the Android Gallery application and to other apps.
	 */
    /*
    public static void galleryAddPic(Context context, String photoPath) {
	   galleryAddPic(context, new File(photoPath));
	}
	
	public static void galleryAddPic(Context context, File photoFile) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(photoFile);
	    mediaScanIntent.setData(contentUri);
	    context.sendBroadcast(mediaScanIntent);
	}
	*/

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFilePath(Context context, String path, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        computeSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap decodeSampleBitmapFromUri(Context context, Uri uri, int reqWidth, int reqHeight) throws IOException {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }

        InputStream input = context.getContentResolver().openInputStream(uri);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        if (input != null) {
            input.close();
        }

        computeSize(options, reqWidth, reqHeight);

        input = context.getContentResolver().openInputStream(uri);
        options.inJustDecodeBounds = false;
        Bitmap sampleBimap = BitmapFactory.decodeStream(input, null, options);
        if (input != null) {
            input.close();
        }
        return sampleBimap;
    }

    private static void computeSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int newWidth = reqWidth;
        int newHeight = reqHeight;
        if (reqWidth != reqHeight) {
            final int bitmapHeight = options.outHeight;
            final int bitmapWidth = options.outWidth;

            if (reqHeight == 0) {
                float scaleWidht = ((float) reqWidth / bitmapWidth);
                newHeight = (int) (bitmapHeight * scaleWidht);
            }

            if (reqWidth == 0) {
                float scaleHeight = ((float) reqHeight / bitmapHeight);
                newWidth = (int) (bitmapWidth * scaleHeight);
            }
        }
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
        if (DEBUG) Log.i(TAG, "原宽高:" + options.outWidth + "*" + options.outHeight
                + " 压缩比例:" + options.inSampleSize
                + " 压缩后宽高:" + newWidth + "*" + newHeight);
    }

    public static Bitmap resizeBitmap(Context context, Uri uri, int reqWidth, int reqHeight) throws IOException {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }

        Bitmap bmp = decodeSampleBitmapFromUri(context, uri, reqWidth, reqHeight);

        int bitmapWidth = 0;
        if (bmp != null) {
            bitmapWidth = bmp.getWidth();
        }
        int bitmapHeight = 0;
        if (bmp != null) {
            bitmapHeight = bmp.getHeight();
        }

        float scaleWidth;
        float scaleHeight;

        scaleWidth = ((float) reqWidth) / bitmapWidth;
        scaleHeight = ((float) reqHeight) / bitmapHeight;

        if (reqHeight == 0) {
            reqHeight = (int) (bitmapHeight * scaleWidth);
            scaleHeight = ((float) reqHeight) / bitmapHeight;
        }

        if (reqWidth == 0) {
            reqWidth = (int) (bitmapWidth * scaleHeight);
            scaleWidth = ((float) reqWidth) / bitmapWidth;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizeBmp = null;
        if (bmp != null) {
            resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            bmp.recycle();
        }
        if (DEBUG) Log.d(TAG, "原大小:" + bitmapWidth + "*" + bitmapHeight +
                " 改变大小后宽高:" + resizeBmp.getWidth() + "*" + resizeBmp.getHeight());

        return resizeBmp;
    }

    /**
     * 获取ImageView的图片原大小
     *
     * @return int[0] = height, int[1] = width
     */
    public static int[] getImageViewBitmapHeightAndWidth(ImageView imageView) {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bd.getBitmap();
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();

        return new int[]{height, width};
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees) {
        return rotateAndMirror(b, degrees, false);
    }

    // Rotates and/or mirrors the bitmap. If a new bitmap is created, the
    // original bitmap is recycled.
    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror) {
        if ((degrees != 0 || mirror) && b != null) {
            Matrix m = new Matrix();
            // Mirror first.
            // horizontal flip + rotation = -rotation + horizontal flip
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate(b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate(b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees=" + degrees);
                }
            }
            if (degrees != 0) {
                // clockwise
                m.postRotate(degrees,
                        (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            }

            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     * videoThumbnail.setImageBitmap(getVideoThumbnail(videoPath, 100, 100,
     * MediaStore.Images.Thumbnails.MICRO_KIND));
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图  
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /** 裁剪图片 */
    public static Uri startImageCrop(Activity context, Uri uri, int aspectX, int aspectY, int outputX, int outputY,
                                     int requestCode) {
        String filename = DateTimeUtil.formatDateTime(new Date(), "yyyyMMddHHmmss") + ".jpg";
        Uri outputUri = Uri.fromFile(new File(StorageUtil.getTempDir(context), filename));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // face detection
        context.startActivityForResult(intent, requestCode);
        return outputUri;
    }

    /** 保存图片到目录 */
    public static String saveImageFromUri(Activity context, Uri uri, String dirPath, boolean needRoundPic) {
        String path = null;
        if (uri != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
            if (bitmap != null) {
                if (needRoundPic) {
                    bitmap = ImageUtils.toRoundBitmap(bitmap);
                }
                String filename = DateTimeUtil.formatDateTime(new Date(), "yyyyMMddHHmmss") + ".jpg";
                path = dirPath + filename;
                try {
                    ImageUtils.saveBitmap(dirPath, filename, bitmap, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
        LogUtil.e(TAG, "--->path : " + path);
        return path;
    }

    /** 保存图片到目录 */
    public static String saveImageFromIntentData(Activity context, Intent data, String dirPath) {
        Bundle extras = data.getExtras();
        String path = null;
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            if (bitmap != null) {
                bitmap = ImageUtils.toRoundBitmap(bitmap);
                String filename = DateTimeUtil.formatDateTime(new Date(), "yyyyMMddHHmmss") + ".jpg";
                path = dirPath + filename;
                try {
                    ImageUtils.saveBitmap(dirPath, filename, bitmap, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
        LogUtil.e(TAG, "--->path : " + path);
        return path;
    }

    /**
     * 获取指定路径下的图片的指定大小的缩略图 getImageThumbnail
     *
     * @return Bitmap
     * @throws
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 保存图片到指定目录下
     *
     * @param dirpath
     * @param filename
     * @param bitmap
     * @param isDelete
     */
    public static void saveBitmap(String dirpath, String filename, Bitmap bitmap, boolean isDelete) throws IOException {
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirpath, filename);
        if (isDelete) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;

    }

    /**
     * 旋转图片一定角度 rotaingImageView
     *
     * @return Bitmap
     * @throws
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 将图片转化为圆形头像
     *
     * @throws
     * @Title: toRoundBitmap
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * 图片合成
     *
     * @param circleBmp 边框图片
     * @param targetBmp 内容图片
     * @return 合成后的图片
     */
    public static Bitmap compositeBitMap(Bitmap circleBmp, Bitmap targetBmp) {
        int circleBmpWidth = circleBmp.getWidth();
        int circleBmpHeight = circleBmp.getHeight();
        int targetBmpWidth = targetBmp.getWidth();
        int targetBmpHeight = targetBmp.getHeight();
        // 创建一张空白边框大小的图
        Bitmap drawBitmap = Bitmap.createBitmap(circleBmpWidth, circleBmpHeight, circleBmp.getConfig());
        Canvas canvas = new Canvas(drawBitmap);
        Paint paint = new Paint();
        // 画边框
        canvas.drawBitmap(circleBmp, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN));
        // 对边框进行缩放
        float scaleX = circleBmp.getWidth() * 1F / targetBmpWidth;
        float scaleY = (circleBmp.getHeight() - 30) * 1F / targetBmpHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY); // 缩放图片
        Bitmap copyBitmap = Bitmap.createBitmap(targetBmp, 0, 0, targetBmpWidth, targetBmpHeight, matrix, true);
        canvas.drawBitmap(copyBitmap, 0, 0, paint);
        return drawBitmap;
    }

    /**
     * 压缩图片
     */
    public static String compressImage(String path, String newPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int inSampleSize = 1;
        int maxSize = 3000;
        if (options.outWidth > maxSize || options.outHeight > maxSize) {
            int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
            int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
            inSampleSize = Math.max(widthScale, heightScale);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newW = w;
        int newH = h;
        if (w > maxSize || h > maxSize) {
            if (w > h) {
                newW = maxSize;
                newH = (int) (newW * h * 1.0 / w);
            } else {
                newH = maxSize;
                newW = (int) (newH * w * 1.0 / h);
            }
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(newPath);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        recycle(newBitmap);
        recycle(bitmap);
        return newPath;
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();
    }

    /**
     * 根据InputStream获取图片实际的宽度和高度
     */
    public static ImageSize getImageSizeByInputStream(InputStream imageStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    /**
     * iamge大小
     */
    public static class ImageSize {
        int width;
        int height;

        public ImageSize() {
        }

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "ImageSize{" + "width=" + width + ", height=" + height + '}';
        }
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
        // 源图片的宽度
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;

        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 根据ImageView获适当的压缩的宽和高
     */
    public static ImageSize getImageViewSize(View view) {

        ImageSize imageSize = new ImageSize();

        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);

        return imageSize;
    }

    /**
     * 根据view获得期望的高度
     */
    private static int getExpectHeight(View view) {

        int height = 0;
        if (view == null)
            return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        // 如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = view.getWidth(); // 获得实际的宽度
        }
        if (height <= 0 && params != null) {
            height = params.height; // 获得布局文件中的声明的宽度
        }

        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
        }

        // 如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (height <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }

        return height;
    }

    /**
     * 根据view获得期望的宽度
     */
    private static int getExpectWidth(View view) {
        int width = 0;
        if (view == null)
            return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        // 如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = view.getWidth(); // 获得实际的宽度
        }
        if (width <= 0 && params != null) {
            width = params.width; // 获得布局文件中的声明的宽度
        }

        if (width <= 0)

        {
            width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
        }
        // 如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (width <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    /**
     * 通过反射获取imageview的某个属性值
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}

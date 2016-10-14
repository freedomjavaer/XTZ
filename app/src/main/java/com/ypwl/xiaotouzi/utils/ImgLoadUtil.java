package com.ypwl.xiaotouzi.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.common.URLConstant;

/**
 * <p>function : 图片加载工具类.<p/>
 * Created by lzj on 2016/1/28.
 */
@SuppressWarnings("unused")
public class ImgLoadUtil {
    private static final String TAG = ImgLoadUtil.class.getSimpleName();

    private static Picasso getPicasso() {
        return Picasso.with(UIUtil.getContext());
    }


    /**
     * 加载平台logo(需要拼接地址)
     *
     * @param pid              平台id 即 :bean.getPid()
     * @param logoFileName     logo文件名 即:bean.getP_logo()
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片资源id
     */
    public static void loadLogo(String pid, String logoFileName, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(pid) || StringUtil.isEmptyOrNull(logoFileName)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C + "up_files/platform_image/" + pid + "/" + logoFileName;
//        LogUtil.e(TAG, "loadLogo : img=" + imgUrl);
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
    }

    /**
     * 加载平台logo(需要拼接地址)
     *
     * @param logoFileName     logo文件名 即:bean.getP_logo()
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片资源id
     */
    public static void loadImgLogo(String logoFileName, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(logoFileName)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C + logoFileName;
//        LogUtil.e(TAG, "loadLogo : img=" + imgUrl);
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
    }


    /**
     * 加载平台logo自定义宽高工具类(需要拼接地址)
     *
     * @param pid              平台id
     * @param logoFileName     logo文件名称
     * @param targetImageView  目标ImageView
     * @param width            图片显示宽度
     * @param height           显示高度
     * @param placeholderResId 默认图片id
     */
    public static void loadLogo(String pid, String logoFileName, ImageView targetImageView, int width, int height, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(pid) || StringUtil.isEmptyOrNull(logoFileName)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).resize(width, height).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C + "up_files/platform_image/" + pid + "/" + logoFileName;
//        LogUtil.e(TAG, "loadLogo : img=" + imgUrl);
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021)
                .resize(width, height).into(targetImageView);
    }


    /**
     * 加载平台logo(加载完整地址包括网络地址和本地地址)
     *
     * @param imgPath          图片地址 eg: http://www.ypwl.com/up_files/cht.jpg , /storage0/DCIM/cht.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片资源id
     */
    public static void loadLogoByPath(String imgPath, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(imgPath)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
            return;
        }
        if (!imgPath.startsWith("http") && !imgPath.startsWith("file://")) {
            imgPath = "file://" + imgPath;
        }
//        LogUtil.e(TAG, "loadLogoByPath : img=" + imgPath);
        getPicasso().load(imgPath).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
    }

    /**
     * 加载图片(需要拼接host)
     *
     * @param imgFileNameStr   图片文件名称 eg: up_files/img.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片id
     */
    public static void loadImgBySplice(String imgFileNameStr, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(imgFileNameStr)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C +  imgFileNameStr;
//        LogUtil.e(TAG, "loadImgBySplice : img=" + imgUrl);
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
    }


    /**
     * 加载图片(加载完整地址包括网络地址和本地地址)
     *
     * @param imgPath          图片地址 eg: http://www.ypwl.com/up_files/picture.jpg , /storage0/DCIM/picture.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片资源id
     */
    public static void loadImgByPath(String imgPath, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(imgPath)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
            return;
        }
        if (!imgPath.startsWith("http") && !imgPath.startsWith("file://")) {
            imgPath = "file://" + imgPath;
        }
//        LogUtil.e(TAG, "loadImgByPath : img=" + imgPath);
        getPicasso().load(imgPath).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_021).into(targetImageView);
    }

    /**
     * 加载头像(需要拼接host)
     *
     * @param avatarFileNameStr avatar文件名称 eg: up_files/user_avatar.jpg
     * @param targetImageView   目标ImageView
     * @param placeholderResId  默认图片id
     */
    public static void loadAvatarBySplice(String avatarFileNameStr, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(avatarFileNameStr)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_027).into(targetImageView);
            return;
        }
        String imgUrl = URLConstant.URL_BASE_C + avatarFileNameStr;
//        LogUtil.e(TAG, "loadAvatarBySplice : img=" + imgUrl);
        getPicasso().load(imgUrl).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_027).into(targetImageView);
    }

    /**
     * 加载头像(加载完整地址包括网络地址和本地地址)
     *
     * @param avatarPath       avatar图片地址 eg: http://www.ypwl.com/up_files/user_avatar.jpg , /storage0/DCIM/avatar.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片id
     */
    public static void loadAvatarByPath(String avatarPath, ImageView targetImageView, int placeholderResId) {
        if (StringUtil.isEmptyOrNull(avatarPath)) {
            getPicasso().load(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_027).into(targetImageView);
            return;
        }
        if (!avatarPath.startsWith("http") && !avatarPath.startsWith("file://")) {
            avatarPath = "file://" + avatarPath;
        }
//        LogUtil.e(TAG, "loadAvatarByPath : img=" + avatarPath);
        getPicasso().load(avatarPath).placeholder(placeholderResId > 0 ? placeholderResId : R.mipmap.pic_027).into(targetImageView);
    }
}

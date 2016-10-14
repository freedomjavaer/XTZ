package com.ypwl.xiaotouzi.event;

/**
 * function : 图片删除事件.
 * <p/>
 * Created by lzj on 2016/1/23.
 */
public class ImageRemoveEvent {
    public String imgPath;

    public ImageRemoveEvent(String imgPath) {
        this.imgPath = imgPath;
    }
}

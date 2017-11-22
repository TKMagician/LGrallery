package grallary.lee.com.grallery.inter;

import android.app.Activity;
import android.content.Context;

import java.io.Serializable;

import grallary.lee.com.grallery.widget.LGalleryImageView;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description 自定义图片加载框架
 * @Version
 */
public interface ImageLoader extends Serializable {
    void displayImage(Activity activity, Context context, String path, LGalleryImageView LGalleryImageView, int width, int height);

    void clearMemoryCache();
}
/*
 *   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 *     ┃　　　┃
 *     ┃　　　┃
 *     ┃　　　┗━━━┓
 *     ┃　　　　　　　┣┓
 *     ┃　　　　　　　┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 *        神兽保佑
 *        代码无BUG!
 */
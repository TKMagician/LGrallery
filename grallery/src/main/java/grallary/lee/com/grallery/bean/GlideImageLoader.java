package grallary.lee.com.grallery.bean;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;

import grallary.lee.com.grallery.R;
import grallary.lee.com.grallery.inter.ImageLoader;
import grallary.lee.com.grallery.widget.LGalleryImageView;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description GlideImageLoader网络上下载的Glide布局
 * @Version
 */
public class GlideImageLoader implements ImageLoader {
    private final static String TAG = "GlideImageLoader";

    @Override
    public void displayImage(Activity activity, Context context, String path, LGalleryImageView LGalleryImageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.gallery_pick_photo)
                .centerCrop()
                .into(LGalleryImageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}

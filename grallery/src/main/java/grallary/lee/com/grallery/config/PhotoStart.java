package grallary.lee.com.grallery.config;


import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import grallary.lee.com.grallery.activity.PhotoOperateActivity;
import grallary.lee.com.grallery.control.SelectPopupWindow;
import grallary.lee.com.grallery.utils.AssertUtil;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/16 0016
 * @Description 启动类
 * @Version
 */
public class PhotoStart {

    private String TAG = "LEE--PhotoStart--:";
    private SelectPopupWindow SelectModeWindow;
    private PhotoConfig PhotoConfig;
    private static PhotoStart PhotoStart;

    public static PhotoStart getInstance() {
        if (AssertUtil.isEmpty(PhotoStart)) {
            PhotoStart = new PhotoStart();
        }
        return PhotoStart;
    }

    /**
     *
     * @param activity  启动的activity
     * @param requestId 请求及返回的ID
     * @param bottomView popWindows 弹出位于哪里
     */
    public void open(final Activity activity, final int requestId, View bottomView) {

        if (AssertUtil.isEmpty(PhotoStart.PhotoConfig)) {
            Log.e(TAG, "请配置 PhotoConfig");
            return;
        }
        if (AssertUtil.isEmpty(PhotoStart.PhotoConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }

        SelectModeWindow = new SelectPopupWindow(activity);
        SelectModeWindow.setStartPhotoListener(new SelectPopupWindow.StartPhoto() {
            @Override
            public void startCamera() {
                PhotoOperateActivity.startPhoto(activity, PhotoOperateActivity.CODE_CAMERA_REQUEST, requestId);
            }

            @Override
            public void startGallery() {
                PhotoOperateActivity.startPhoto(activity, PhotoOperateActivity.CODE_GALLERY_REQUEST, requestId);
            }
        });

        SelectModeWindow.showAtLocation(bottomView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public PhotoStart setPhotoConfig(PhotoConfig pPhotoConfig) {
        this.PhotoConfig = pPhotoConfig;
        return this;
    }

    public PhotoConfig getPhotoConfig() {
        return this.PhotoConfig;
    }


}

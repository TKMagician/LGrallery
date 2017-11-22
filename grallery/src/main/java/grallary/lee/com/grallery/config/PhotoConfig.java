package grallary.lee.com.grallery.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import grallary.lee.com.grallery.bean.GlideImageLoader;
import grallary.lee.com.grallery.inter.IHandlerCallBack;
import grallary.lee.com.grallery.inter.ImageLoader;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description 图片配置类
 * @Version
 */
public class PhotoConfig {

    private ImageLoader imageLoader;    // 图片加载器
    private IHandlerCallBack iHandlerCallBack;   // GalleryPick 生命周期接口

    private boolean multiSelect;        // 是否开启多选  默认 ： false
    private int maxSize;                // 配置开启多选时 最大可选择的图片数量。   默认：9
    private boolean isShowCamera;       // 是否开启相机 默认：true
    private String provider;            // 兼容android 7.0 设置
    private String filePath;            // 拍照以及截图后 存放的位置。    默认：/ltemp
    private ArrayList<String> pathList;      // 已选择照片的路径
    private boolean isOpenCamera;             // 是否直接开启相机    默认：false

    private boolean isNeedCrop;                 // 是否开启裁剪   默认关闭
    private float aspectRatioX;             // 裁剪比         默认   1：1
    private float aspectRatioY;             // 裁剪比         默认   1：1
    private int corpX;                   // 最大的裁剪值   默认    500
    private int corpY;                  // 最大的裁剪值   默认    500

    private Builder builder;

    private PhotoConfig(Builder builder) {
        setBuilder(builder);
    }

    private void setBuilder(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.multiSelect = builder.multiSelect;
        this.maxSize = builder.maxSize;
        this.isShowCamera = builder.isShowCamera;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.isOpenCamera = builder.isOpenCamera;
        this.isNeedCrop = builder.isCrop;
        this.aspectRatioX = builder.aspectRatioX;
        this.aspectRatioY = builder.aspectRatioY;
        this.corpX = builder.cropX;
        this.corpY = builder.cropY;
        this.provider = builder.provider;
        this.builder = builder;
    }

    public static class Builder implements Serializable {

        private static PhotoConfig galleryConfig;

        private ImageLoader imageLoader = new GlideImageLoader();
        private IHandlerCallBack iHandlerCallBack;

        private boolean multiSelect = false;
        private int maxSize = 1;
        private boolean isShowCamera = false;
        private String filePath = "/ltemp";

        private boolean isCrop = false;
        private float aspectRatioX = 1;
        private float aspectRatioY = 1;
        private int cropX = 500;
        private int cropY = 500;

        private String provider;

        private ArrayList<String> pathList = new ArrayList<>();

        private boolean isOpenCamera = false;

        public PhotoConfig.Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public PhotoConfig.Builder crop(boolean isCrop) {
            this.isCrop = isCrop;
            return this;
        }

        public PhotoConfig.Builder crop(boolean isCrop, float aspectRatioX, float aspectRatioY, int cropx, int cropY) {
            this.isCrop = isCrop;
            this.aspectRatioX = aspectRatioX;
            this.aspectRatioY = aspectRatioY;
            this.cropX = cropx;
            this.cropY = cropY;
            return this;
        }


        public PhotoConfig.Builder imageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public PhotoConfig.Builder iHandlerCallBack(IHandlerCallBack iHandlerCallBack) {
            this.iHandlerCallBack = iHandlerCallBack;
            return this;
        }


        public PhotoConfig.Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public PhotoConfig.Builder multiSelect(boolean multiSelect, int maxSize) {
            this.multiSelect = multiSelect;
            this.maxSize = maxSize;
            return this;
        }

        public PhotoConfig.Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
        /*
        public PhotoConfig.Builder isShowCamera(boolean isShowCamera) {
            this.isShowCamera = isShowCamera;
            return this;
        }
        */
        public PhotoConfig.Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public PhotoConfig.Builder isOpenCamera(boolean isOpenCamera) {
            this.isOpenCamera = isOpenCamera;
            return this;
        }


        public PhotoConfig.Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public PhotoConfig build() {
            if (galleryConfig == null) {
                galleryConfig = new PhotoConfig(this);
            } else {
                galleryConfig.setBuilder(this);
            }
            return galleryConfig;
        }

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public String getFilePath() {
        return filePath;
    }

    public IHandlerCallBack getIHandlerCallBack() {
        return iHandlerCallBack;
    }

    public PhotoConfig.Builder getBuilder() {
        return builder;
    }

    public boolean isCrop() {
        return isNeedCrop;
    }

    public float getAspectRatioX() {
        return aspectRatioX;
    }

    public float getAspectRatioY() {
        return aspectRatioY;
    }

    public int getMaxWidth() {
        return corpY;
    }

    public String getProvider() {
        return provider;
    }

    public int getCorpX() {
        return corpX;
    }

    public int getCorpY() {
        return corpY;
    }
}

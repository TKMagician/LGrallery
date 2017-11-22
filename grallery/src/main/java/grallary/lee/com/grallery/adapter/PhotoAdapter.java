package grallary.lee.com.grallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import grallary.lee.com.grallery.R;
import grallary.lee.com.grallery.bean.PhotoInfo;
import grallary.lee.com.grallery.config.PhotoConfig;
import grallary.lee.com.grallery.config.PhotoStart;
import grallary.lee.com.grallery.utils.ScreenUtils;
import grallary.lee.com.grallery.widget.LGalleryImageView;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description 列表中图片的适配器
 * @Version
 */
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<PhotoInfo> photoInfoList;                      // 本地照片数据
    private List<String> selectPhoto = new ArrayList<>();                   // 选择的图片数据
    private OnCallBack onCallBack;
    private final static String TAG = "PhotoAdapter";

    private PhotoConfig galleryConfig = PhotoStart.getInstance().getPhotoConfig();

    private final static int HEAD = 0;    // 开启相机时需要显示的布局
    private final static int ITEM = 1;    // 照片布局

    public PhotoAdapter(Activity mActivity, Context mContext, List<PhotoInfo> photoInfoList) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.photoInfoList = photoInfoList;
        this.mActivity = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.lgallery_item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // 设置 每个imageView 的大小
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = ScreenUtils.getScreenWidth(mContext) / 3;
        params.width = ScreenUtils.getScreenWidth(mContext) / 3;
        holder.itemView.setLayoutParams(params);

        final PhotoInfo photoInfo;
        photoInfo = photoInfoList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        galleryConfig.getImageLoader().displayImage(mActivity, mContext, photoInfo.path, viewHolder.ivPhotoImage,
                ScreenUtils.getScreenWidth(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);

        if (selectPhoto.contains(photoInfo.path)) {
            viewHolder.chkPhotoSelector.setChecked(true);
            viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_checked);
            viewHolder.vPhotoMask.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.chkPhotoSelector.setChecked(false);
            viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_unchecked);
            viewHolder.vPhotoMask.setVisibility(View.GONE);
        }

        if (!galleryConfig.isMultiSelect()) {
            viewHolder.chkPhotoSelector.setVisibility(View.GONE);
            viewHolder.vPhotoMask.setVisibility(View.GONE);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!galleryConfig.isMultiSelect()) {
                    selectPhoto.clear();
                    selectPhoto.add(photoInfo.path);
                    onCallBack.OnClickPhoto(selectPhoto);
                    return;
                }

                if (selectPhoto.contains(photoInfo.path)) {
                    selectPhoto.remove(photoInfo.path);
                    viewHolder.chkPhotoSelector.setChecked(false);
                    viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_unchecked);
                    viewHolder.vPhotoMask.setVisibility(View.GONE);
                }
                else {
                    if (galleryConfig.getMaxSize() <= selectPhoto.size()) {        // 当选择图片达到上限时， 禁止继续添加
                        return;
                    }
                    selectPhoto.add(photoInfo.path);
                    viewHolder.chkPhotoSelector.setChecked(true);
                    viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_checked);
                    viewHolder.vPhotoMask.setVisibility(View.VISIBLE);
                }
                onCallBack.OnClickPhoto(selectPhoto);
            }
        });


    }


    /**
     * 照片的 Holder
     */
    private class ViewHolder extends RecyclerView.ViewHolder {
        private LGalleryImageView ivPhotoImage;
        private View vPhotoMask;
        private CheckBox chkPhotoSelector;

        private ViewHolder(View itemView) {
            super(itemView);
            ivPhotoImage = (LGalleryImageView) itemView.findViewById(R.id.ivGalleryPhotoImage);
            vPhotoMask = itemView.findViewById(R.id.vGalleryPhotoMask);
            chkPhotoSelector = (CheckBox) itemView.findViewById(R.id.chkGalleryPhotoSelector);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    @Override
    public int getItemCount() {
        return photoInfoList.size();
    }

    public interface OnCallBack {
        void OnClickPhoto(List<String> selectPhoto);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    /**
     * 传入已选的图片
     *
     * @param selectPhoto 已选的图片路径
     */
    public void setSelectPhoto(List<String> selectPhoto) {
        this.selectPhoto.addAll(selectPhoto);
    }
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
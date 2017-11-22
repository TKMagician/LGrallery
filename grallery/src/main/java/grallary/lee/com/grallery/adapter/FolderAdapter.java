package grallary.lee.com.grallery.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import grallary.lee.com.grallery.R;
import grallary.lee.com.grallery.bean.FolderInfo;
import grallary.lee.com.grallery.config.PhotoConfig;
import grallary.lee.com.grallery.config.PhotoStart;
import grallary.lee.com.grallery.utils.ScreenUtils;
import grallary.lee.com.grallery.widget.LGalleryImageView;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description 列表适配类
 * @Version
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<FolderInfo> result;
    private final static String TAG = "FolderAdapter";

    private PhotoConfig galleryConfig = PhotoStart.getInstance().getPhotoConfig();
    private int mSelector = 0;
    private OnClickListener onClickListener;

    public FolderAdapter(Activity mActivity, Context mContext, List<FolderInfo> result) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.lgallery_item_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == 0) {
            holder.tvGalleryFolderName.setText(mContext.getString(R.string.gallery_all_folder));
            holder.tvGalleryPhotoNum.setText(mContext.getString(R.string.gallery_photo_num, getTotalImageSize()));
            if (result.size() > 0) {
                galleryConfig.getImageLoader().displayImage(mActivity, mContext, result.get(0).photoInfo.path, holder.ivGalleryFolderImage, ScreenUtils.getScreenWidth(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelector = 0;
                    onClickListener.onClick(null);
                }
            });

            if (mSelector == 0) {
                holder.ivGalleryIndicator.setVisibility(View.VISIBLE);
            } else {
                holder.ivGalleryIndicator.setVisibility(View.GONE);
            }
            return;
        }
        final FolderInfo folderInfo = result.get(position - 1);
        holder.tvGalleryFolderName.setText(folderInfo.name);
        holder.tvGalleryPhotoNum.setText(mContext.getString(R.string.gallery_photo_num, folderInfo.photoInfoList.size()));
        galleryConfig.getImageLoader().displayImage(mActivity, mContext, folderInfo.photoInfo.path, holder.ivGalleryFolderImage, ScreenUtils.getScreenWidth(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelector = holder.getAdapterPosition() + 1;
                onClickListener.onClick(folderInfo);
            }
        });

        if (mSelector == holder.getAdapterPosition() + 1) {
            holder.ivGalleryIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.ivGalleryIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return result.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LGalleryImageView ivGalleryFolderImage;
        private TextView tvGalleryFolderName;
        private TextView tvGalleryPhotoNum;
        private ImageView ivGalleryIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            ivGalleryFolderImage = (LGalleryImageView) itemView.findViewById(R.id.ivGalleryFolderImage);
            tvGalleryFolderName = (TextView) itemView.findViewById(R.id.tvGalleryFolderName);
            tvGalleryPhotoNum = (TextView) itemView.findViewById(R.id.tvGalleryPhotoNum);
            ivGalleryIndicator = (ImageView) itemView.findViewById(R.id.ivGalleryIndicator);
        }

    }


    public interface OnClickListener {
        void onClick(FolderInfo folderInfo);
    }

    /**
     * @return 所有图片数量
     */
    private int getTotalImageSize() {
        int result = 0;
        if (this.result != null && this.result.size() > 0) {
            for (FolderInfo folderInfo : this.result) {
                result += folderInfo.photoInfoList.size();
            }
        }
        return result;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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
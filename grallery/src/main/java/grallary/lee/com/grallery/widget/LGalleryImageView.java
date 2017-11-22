package grallary.lee.com.grallery.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 自定义 Image 用来兼容 fresco
 * Created by  on 2016/10/28.
 */
public class LGalleryImageView extends AppCompatImageView {


    private OnImageViewListener onImageViewListener;

    public LGalleryImageView(Context context) {
        super(context);
    }

    public LGalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LGalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public LGalleryImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
    }

    public interface OnImageViewListener {
        void onDraw(Canvas canvas);

        boolean verifyDrawable(Drawable dr);

        void onDetach();

        void onAttach();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        onImageViewListener.verifyDrawable(dr);
        return super.verifyDrawable(dr);
    }

    public void setOnImageViewListener(OnImageViewListener onImageViewListener) {
        this.onImageViewListener = onImageViewListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onImageViewListener != null) {
            onImageViewListener.onDraw(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (onImageViewListener != null) {
            onImageViewListener.onDetach();
        }
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if (onImageViewListener != null) {
            onImageViewListener.onDetach();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (onImageViewListener != null) {
            onImageViewListener.onAttach();
        }
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if (onImageViewListener != null) {
            onImageViewListener.onAttach();
        }
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
package grallary.lee.com.grallery.control;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import grallary.lee.com.grallery.R;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/17 0017
 * @Description 仿ios弹出选择框
 * @Version 1.0
 */
public class SelectPopupWindow extends PopupWindow {

    private Button TakePhotoBtn, PickPhotoBtn, CancelBtn;
    private View MenuView;

    private StartPhoto StartPhotoLinstener;

    public SelectPopupWindow(Activity comeActivity) {
        LayoutInflater inflater = (LayoutInflater) comeActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MenuView = inflater.inflate(R.layout.activity_select_pic_popup_window, null);

        initControl();
        initSetting();
    }

    private void initControl() {
        TakePhotoBtn = (Button) MenuView.findViewById(R.id.btn_take_photo);
        PickPhotoBtn = (Button) MenuView.findViewById(R.id.btn_pick_photo);
        CancelBtn = (Button) MenuView.findViewById(R.id.btn_cancel);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        MenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = MenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        PickPhotoBtn.setOnClickListener(itemsOnClick);
        TakePhotoBtn.setOnClickListener(itemsOnClick);
    }

    private void initSetting() {
        this.setContentView(MenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        this.setBackgroundDrawable(dw);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SelectPopupWindow.this.dismiss();
            if (v.getId() == R.id.btn_take_photo) {
                StartPhotoLinstener.startCamera();
            }
            else {
                StartPhotoLinstener.startGallery();
            }
        }
    };

    public void setStartPhotoListener(StartPhoto startPhotoListener) {
        StartPhotoLinstener = startPhotoListener;
    }

    public interface StartPhoto {
        void startCamera();
        void startGallery();
    }
}

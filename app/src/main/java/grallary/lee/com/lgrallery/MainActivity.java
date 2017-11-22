package grallary.lee.com.lgrallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import grallary.lee.com.grallery.config.PhotoConfig;
import grallary.lee.com.grallery.config.PhotoStart;
import grallary.lee.com.grallery.inter.IHandlerCallBack;

public class MainActivity extends AppCompatActivity {

    private Button btn_clickBtn;
    private ImageView iv_showImage;
    private PhotoConfig photoConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPhotoConfig();
        btn_clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoStart.getInstance().setPhotoConfig(photoConfig).open(MainActivity.this, R.id.iv_showImage, iv_showImage);
            }
        });
    }

    private void initPhotoConfig() {
        photoConfig = new PhotoConfig.Builder()
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.jpgoodbuy.fileprovider")   // provider(必填)
                .pathList(new ArrayList<String>())                         // 记录已选的图片
                .multiSelect(false, 1)                   // 是否多选   默认：false 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(1)                             // 配置多选时 的多选数量。    默认：9
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .filePath("/lTemp")          // 图片存放路径
                .build();
    }

    IHandlerCallBack iHandlerCallBack = new IHandlerCallBack(){

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(List<String> photoList, int requestId) {
            Toast.makeText(MainActivity.this, "嗖", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onError() {

        }
    };

    private void initView() {
        btn_clickBtn = (Button) findViewById(R.id.btn_clickBtn);
        iv_showImage = (ImageView) findViewById(R.id.iv_showImage);
    }
}

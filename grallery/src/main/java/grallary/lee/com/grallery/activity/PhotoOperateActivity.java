package grallary.lee.com.grallery.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import grallary.lee.com.grallery.R;
import grallary.lee.com.grallery.adapter.FolderAdapter;
import grallary.lee.com.grallery.adapter.PhotoAdapter;
import grallary.lee.com.grallery.bean.FolderInfo;
import grallary.lee.com.grallery.bean.PhotoInfo;
import grallary.lee.com.grallery.config.PhotoConfig;
import grallary.lee.com.grallery.config.PhotoStart;
import grallary.lee.com.grallery.inter.IHandlerCallBack;
import grallary.lee.com.grallery.utils.PhotoOperateUtils;
import grallary.lee.com.grallery.utils.UIUtils;
import grallary.lee.com.grallery.utils.Utils;
import grallary.lee.com.grallery.widget.FolderListPopupWindow;

/**
 * @Author Lee
 * @Date 创建时间 2017/10/26 0026
 * @Description
 * @Version
 */
public class PhotoOperateActivity extends BaseActivity {

    private Context mContext = null;
    private Activity mActivity = null;

    private ArrayList<String> resultPhoto;

    private TextView tvFinish;                  // 完成按钮
    private TextView tvGalleryFolder;           // 文件夹按钮
    private LinearLayout btnGalleryPickBack;    // 返回按钮
    private RecyclerView rvGalleryImage;        // 图片列表

    private PhotoAdapter photoAdapter;              // 图片适配器
    private FolderAdapter folderAdapter;            // 文件夹适配器


    private List<FolderInfo> folderInfoList = new ArrayList<>();    // 本地文件夹信息List
    private List<PhotoInfo> photoInfoList = new ArrayList<>();      // 本地图片信息List

    private static final int LOADER_ALL = 0;         // 获取所有图片
    private static final int LOADER_CATEGORY = 1;    // 获取某个文件夹中的所有图片

    private boolean hasFolderScan = false;           // 是否扫描过

    private PhotoConfig galleryConfig;   // GalleryPick 配置器

    private IHandlerCallBack mHandlerCallBack;   // GalleryPick 生命周期接口

    private FolderListPopupWindow folderListPopupWindow;   // 文件夹选择弹出框

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback;

    public static int CODE_GALLERY_REQUEST = 0;   // 设置拍摄照片的 REQUEST_CODE
    public static int CODE_CAMERA_REQUEST = 1;   // 设置拍摄照片的 REQUEST_CODE
    public static int CODE_RESULT_REQUEST = 2;   // 设置拍摄照片的 REQUEST_CODE
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    private File TmpDir;
    private File FileUri;
    private File FileCropUri;
    private Uri ImageUri;
    private Uri CropImageUri;
    private int requestControlId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lgallery_main);
        mContext = this;
        mActivity = this;

        UIUtils.hideTitleBar(mActivity, R.id.ll_gallery_pick_main);

        galleryConfig = PhotoStart.getInstance().getPhotoConfig();
        if (galleryConfig == null) {
            exit();
            return;
        }

        initView();
        init();
        initFileUri();

        Intent intent = getIntent();
        int modeCode = intent.getIntExtra("modeCode", CODE_GALLERY_REQUEST);
        requestControlId = intent.getIntExtra("requestControlId", 0);

        if (modeCode == CODE_CAMERA_REQUEST) {
            autoObtainCameraPermission();
        }
        else {
            autoObtainStoragePermission();
        }
    }

    private void initFileUri() {
        TmpDir = new File(Utils.getExternalStorageDir() + galleryConfig.getFilePath());
        if(!TmpDir.exists()) {
            TmpDir.mkdirs();
        }
        FileUri = new File(TmpDir.getAbsolutePath() + "/photo.jpg");
        FileCropUri = new File(TmpDir.getAbsolutePath() + "/crop_photo.jpg");
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvFinish = (TextView) super.findViewById(R.id.tvFinish);
        tvGalleryFolder = (TextView) super.findViewById(R.id.tvGalleryFolder);
        btnGalleryPickBack = (LinearLayout) super.findViewById(R.id.btnGalleryPickBack);
        rvGalleryImage = (RecyclerView) super.findViewById(R.id.rvGalleryImage);
    }
    /**
     * 初始化
     */
    private void init() {
        mHandlerCallBack = galleryConfig.getIHandlerCallBack();
        mHandlerCallBack.onStart();

        resultPhoto = galleryConfig.getPathList();

        tvFinish.setText(getString(R.string.gallery_finish, resultPhoto.size(), galleryConfig.getMaxSize()));

        btnGalleryPickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandlerCallBack.onCancel();
                exit();
            }
        });

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvGalleryImage.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(mActivity, mContext, photoInfoList);
        photoAdapter.setOnCallBack(new PhotoAdapter.OnCallBack() {

            @Override
            public void OnClickPhoto(List<String> selectPhotoList) {
                tvFinish.setText(getString(R.string.gallery_finish, selectPhotoList.size(), galleryConfig.getMaxSize()));

                resultPhoto.clear();
                resultPhoto.addAll(selectPhotoList);

                if (!galleryConfig.isMultiSelect() && resultPhoto != null && resultPhoto.size() > 0) {
                    dealResult(Uri.fromFile(new File(resultPhoto.get(resultPhoto.size() - 1))));
                }
            }
        });
        photoAdapter.setSelectPhoto(resultPhoto);
        rvGalleryImage.setAdapter(photoAdapter);

        if (!galleryConfig.isMultiSelect()) {
            tvFinish.setVisibility(View.GONE);
        }

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultPhoto != null && resultPhoto.size() > 0) {
                    ArrayList<String> imgList = new ArrayList<String>();
                    for (String imgPath: resultPhoto) {
                        imgList.add(compressPhoto(Uri.fromFile(new File(imgPath))));
                    }
                    mHandlerCallBack.onSuccess(imgList, requestControlId);
                    exit();
                }
            }
        });

        tvGalleryFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderListPopupWindow != null && folderListPopupWindow.isShowing()) {
                    folderListPopupWindow.dismiss();
                    return;
                }
                folderListPopupWindow = new FolderListPopupWindow(mActivity, mContext, folderAdapter);
                folderListPopupWindow.showAsDropDown(tvGalleryFolder);
            }
        });

        folderAdapter = new FolderAdapter(mActivity, mContext, folderInfoList);
        folderAdapter.setOnClickListener(new FolderAdapter.OnClickListener() {
            @Override
            public void onClick(FolderInfo folderInfo) {
                if (folderInfo == null) {
                    getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    tvGalleryFolder.setText(R.string.gallery_all_folder);
                } else {
                    photoInfoList.clear();
                    photoInfoList.addAll(folderInfo.photoInfoList);
                    photoAdapter.notifyDataSetChanged();
                    tvGalleryFolder.setText(folderInfo.name);
                }
                folderListPopupWindow.dismiss();
                gridLayoutManager.scrollToPosition(0);
            }
        });
    }


    /**
     * 初始化配置
     */
    private void initPhoto() {

        mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.SIZE
            };

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == LOADER_ALL) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == LOADER_CATEGORY) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                }
                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    int count = data.getCount();
                    if (count > 0) {
                        List<PhotoInfo> tempPhotoList = new ArrayList<>();
                        data.moveToFirst();
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                            boolean showFlag = size > 1024 * 5;                           //是否大于5K
                            PhotoInfo photoInfo = new PhotoInfo(path, name, dateTime);
                            if (showFlag) {
                                tempPhotoList.add(photoInfo);
                            }
                            if (!hasFolderScan && showFlag) {
                                File photoFile = new File(path);                  // 获取图片文件
                                File folderFile = photoFile.getParentFile();      // 获取图片上一级文件夹

                                FolderInfo folderInfo = new FolderInfo();
                                folderInfo.name = folderFile.getName();
                                folderInfo.path = folderFile.getAbsolutePath();
                                folderInfo.photoInfo = photoInfo;
                                if (!folderInfoList.contains(folderInfo)) {      // 判断是否是已经扫描到的图片文件夹
                                    List<PhotoInfo> photoInfoList = new ArrayList<>();
                                    photoInfoList.add(photoInfo);
                                    folderInfo.photoInfoList = photoInfoList;
                                    folderInfoList.add(folderInfo);
                                } else {
                                    FolderInfo f = folderInfoList.get(folderInfoList.indexOf(folderInfo));
                                    f.photoInfoList.add(photoInfo);
                                }
                            }

                        } while (data.moveToNext());

                        photoInfoList.clear();
                        photoInfoList.addAll(tempPhotoList);

                        List<String> tempPhotoPathList = new ArrayList<>();
                        for (PhotoInfo photoInfo : photoInfoList) {
                            tempPhotoPathList.add(photoInfo.path);
                        }
                        for (String mPhotoPath : galleryConfig.getPathList()) {
                            if (!tempPhotoPathList.contains(mPhotoPath)) {
                                PhotoInfo photoInfo = new PhotoInfo(mPhotoPath, null, 0L);
                                photoInfoList.add(0, photoInfo);
                            }
                        }
                        photoAdapter.notifyDataSetChanged();
                        hasFolderScan = true;
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);   // 扫描手机中的图片
    }

    /**
     * 相机权限
     */
    private void autoObtainCameraPermission() {

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "你已经拒绝过一次", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        }
        else {//有权限直接调用系统相机拍照
            ImageUri = Uri.fromFile(FileUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ImageUri = FileProvider.getUriForFile(PhotoOperateActivity.this, galleryConfig.getProvider(), FileUri);//通过FileProvider创建一个content类型的Uri
            }
            PhotoOperateUtils.takePicture(this, ImageUri, CODE_CAMERA_REQUEST);
        }
    }

    /**
     * 相册权限
     */
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        }
        else {
            initPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: //调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageUri = Uri.fromFile(FileUri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        ImageUri = FileProvider.getUriForFile(PhotoOperateActivity.this, galleryConfig.getProvider(), FileUri);//通过FileProvider创建一个content类型的Uri
                    PhotoOperateUtils.takePicture(this, ImageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(PhotoOperateActivity.this, "请允许打开相机", Toast.LENGTH_SHORT).show();
                }
                break;

            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //开启相册
                    initPhoto();
                } else {
                    Toast.makeText(PhotoOperateActivity.this, "请允许操作文件", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            mHandlerCallBack.onCancel();
            exit();
            return;
        }

        if (requestCode == CODE_CAMERA_REQUEST) {
            Uri imageSaveUri = sendBordcase();
            dealResult(imageSaveUri);
        }
        else {
            resultPhoto.clear();
            resultPhoto.add(compressPhoto(CropImageUri));
            mHandlerCallBack.onSuccess(resultPhoto, requestControlId);
            exit();
        }
    }

    //处理回调
    private void dealResult(Uri imageSaveUri) {
        if (!galleryConfig.isCrop()) {
            resultPhoto.clear();
            resultPhoto.add(compressPhoto(imageSaveUri));
            mHandlerCallBack.onSuccess(resultPhoto, requestControlId);
            exit();
            return;
        }

        CropImageUri = Uri.fromFile(FileCropUri);

        Uri newUri = Uri.parse(PhotoOperateUtils.getPath(PhotoOperateActivity.this, imageSaveUri));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newUri = FileProvider.getUriForFile(this, galleryConfig.getProvider(), new File(newUri.getPath()));
        }

        PhotoOperateUtils.cropImageUri(this, newUri, CropImageUri, galleryConfig.getAspectRatioX(),
                galleryConfig.getAspectRatioY(), galleryConfig.getCorpX(), galleryConfig.getCorpY(), CODE_RESULT_REQUEST);
    }

    private String compressPhoto(Uri imageSaveUri) {
        Bitmap saveBitmap = PhotoOperateUtils.getSmallBitmap(imageSaveUri.getPath(), 1280,720);
        saveBitmap = PhotoOperateUtils.reviewPicRotate(saveBitmap, imageSaveUri.getPath());
        Uri saveUri = PhotoOperateUtils.saveBitmap(saveBitmap, "uploadImage");
        return saveUri.toString();
    }

    //发送广播通知相册
    private Uri sendBordcase()  {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(FileUri);
        intent.setData(uri);
        sendBroadcast(intent);
        return uri;
    }

    private void exit() {
        if (mHandlerCallBack != null) {
            mHandlerCallBack.onFinish();
        }
        finish();
    }

    /**
     * 回退键监听
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (folderListPopupWindow != null && folderListPopupWindow.isShowing()) {
                folderListPopupWindow.dismiss();
                return true;
            }
            mHandlerCallBack.onCancel();
            exit();
        }
        return true;
    }

    public static void startPhoto (Activity activity, int modeCode, int requestControlId) {
        Intent intent = new Intent(activity, PhotoOperateActivity.class);
        intent.putExtra("modeCode", modeCode);
        intent.putExtra("requestControlId", requestControlId);
        activity.startActivityForResult(intent, modeCode);
    }
}

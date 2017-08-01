package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.net.NetUtil;
import com.ttxg.fruitday.net.request.FormFile;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 修改个人资料
 * Created by lilijun on 2016/9/13.
 */
public class ModifyPersonageInfoActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 修改个人资料
     */
    private final String MODIFY_PERSIONAGE_INFO = "user/setUser";
    /**
     * 查询用户是否绑定了手机号
     */
    private final String QURERY_BINDING_PHONE = "safe/bind_phone";

    /**
     * 修改个人信息成功的resultCode
     */
    public static final int MODIFY_USER_INFO_SUCCESS_RESULT_CODE = 100018;
    private ImageView backImg;
    private Button saveBtn;
    private SimpleDraweeView userIcon;
    private EditText nickNameInput;
    //    private RadioGroup sexRadioGroup;
    private TextView bindingPhoneText;
    private Button bindingBtn;
    private EditText nameInput;
    private EditText companyNameInput;
    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 是否是 “去绑定”  true=去绑定，false=重试
     */
    private boolean isGoBinding = true;

    private String modifyName;
    private String modifyCompanyName;
    private String modifyNickName;


    /**
     * 更改头像时，拍照后保存到本地的图片路径
     */
    private String CAMERA_PIC_PATH = Environment.getExternalStorageDirectory()
            + "/camera.jpg";
    /**
     * 裁剪后的图片保存位置(文件夹)
     */
    public static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    /**
     * 调用相册标识
     */
    private static final int GET_LOCAL_PHOTO_PIC = 1;
    /**
     * 裁剪图片标识
     */
    private static final int CROP_PHOTO_PIC = 2;
    /**
     * 用来标识请求照相功能的activity
     */
    private static final int CAMERA_WITH_DATA = 3;
    /**
     * 相机裁剪图片标识
     */
    private static final int CROP_CAMER_PCI = 4;

    /**
     * 标识finish时 是否需要返回Result结果
     */
    private boolean isForResult = false;

    private Uri imageUri;

    @Override
    protected void initView() {
        setTitleVisible(false);
        setCenterView(R.layout.activity_modiy_personage_info);

        backImg = (ImageView) findViewById(R.id.modify_personage_title_back_img);
        backImg.setOnClickListener(this);
        saveBtn = (Button) findViewById(R.id.modify_personage_save_btn);
        saveBtn.setOnClickListener(this);
        userIcon = (SimpleDraweeView) findViewById(R.id.modify_personage_icon);
        userIcon.setOnClickListener(this);
        nickNameInput = (EditText) findViewById(R.id.modify_personage_nick_name_input);
//        sexRadioGroup = (RadioGroup) findViewById(R.id.modify_personage_radioGroup);
        bindingPhoneText = (TextView) findViewById(R.id.modify_personage_binding_text);
        bindingBtn = (Button) findViewById(R.id.modify_personage_binding_phone);
        bindingBtn.setOnClickListener(this);
        bindingBtn.setVisibility(View.GONE);
        nameInput = (EditText) findViewById(R.id.modify_personage_name_input);
        companyNameInput = (EditText) findViewById(R.id.modify_personage_company_name_input);

        initData();
        showCenterView();
    }

    private void initData() {
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        userIcon.setImageURI(userInfo.getIcon());
        nickNameInput.setText(userInfo.getNickName());
//        sexRadioGroup.check(userInfo.getSex() == 1 ? R.id.modify_personage_man_check : R.id
//                .modify_personage_woman_check);
        bindingPhoneText.setText(!TextUtils.isEmpty(userInfo.getPhone()) ? String.format
                (getResources().getString(R.string.format_phone), userInfo.getPhone()) :
                getResources().getString(R.string.phone_check));
        nameInput.setText(userInfo.getName());
        companyNameInput.setText(userInfo.getCompanyName());

        if (TextUtils.isEmpty(userInfo.getPhone())) {
            // 如果用户绑定的手机号是空的
            // 则需要手动的去查询用户是否绑定了手机
            loadDataGet(QURERY_BINDING_PHONE, null);
        }
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, MODIFY_PERSIONAGE_INFO)) {
            progressDialog.dismiss();
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            userInfo.setNickName(modifyNickName);
            userInfo.setName(modifyName);
            userInfo.setCompanyName(modifyCompanyName);
            Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R.string
                    .modify_success));
            isForResult = true;
            finish();
        } else if (TextUtils.equals(tag, QURERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机成功
            String bindingPhone = "";
            try {
                bindingPhone = resultObject.getString("validateMobile");
                UserInfoManager.getInstance().getUserInfo().setPhone(bindingPhone);
            } catch (Exception e) {
                DLog.e("ModifyPersonageInfoActivity", "查询绑定手机解析出现异常#exception：\n", e);
            }
            if (TextUtils.isEmpty(bindingPhone)) {
                // 没有绑定手机号
                bindingPhoneText.setText(getResources().getString(R.string.phone));
                bindingBtn.setVisibility(View.VISIBLE);
                bindingBtn.setText(getResources().getString(R.string.binding));
                isGoBinding = true;
            } else {
                // 有绑定手机号
                bindingPhoneText.setText(String.format
                        (getResources().getString(R.string.format_phone), bindingPhone));
                bindingBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, MODIFY_PERSIONAGE_INFO)) {
            progressDialog.dismiss();
            Util.showErrorMessage(ModifyPersonageInfoActivity.this, msg, getResources().getString
                    (R.string
                            .modify_failed));
        } else if (TextUtils.equals(tag, QURERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机失败
            // 点击重新查询是否绑定手机号
            bindingPhoneText.setText(getResources().getString(R.string.query_failed));
            bindingBtn.setVisibility(View.VISIBLE);
            bindingBtn.setText(getResources().getString(R.string.re_try));
            isGoBinding = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        DLog.i("onActivityResult-----requestCode--->>" + requestCode + "resultCode------>>>" +
                resultCode);
        if (resultCode == BindingPhoneActivity.BINDING_SUCCESS_RESULT) {
            // 绑定成功
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            bindingPhoneText.setText(!TextUtils.isEmpty(userInfo.getPhone()) ? String.format
                    (getResources().getString(R.string.format_phone), userInfo.getPhone()) :
                    getResources().getString(R.string.phone));
            bindingBtn.setText(getResources().getString(R.string.binding));
            bindingBtn.setVisibility(View.VISIBLE);
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == GET_LOCAL_PHOTO_PIC) {
            // 调用相册返回
            DLog.i("调用相册返回！！！");
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // 裁剪所选择的图片
                    startPhotoZoom(uri, CROP_PHOTO_PIC);
                } else {
                    Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R
                            .string.get_pic_failed));
                }
            }
        } else if (requestCode == CAMERA_WITH_DATA) {
            // 调用相机拍照返回之后，再去裁剪相机所拍摄的照片
            DLog.i("调用相机拍照返回！！！");
            cropPhotoPic(data);
        } else if (requestCode == CROP_CAMER_PCI) {
            // 相机裁剪返回 上传图片
            DLog.i("相机裁剪返回 上传图片！！！");
            Bitmap returnCameraBitmap = data.getParcelableExtra("data");
            if (returnCameraBitmap != null) {
                uploadPic(getBitmapBytes(returnCameraBitmap));
            } else {
                // 裁剪失败
                Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R
                        .string.crop_pic_failed));
            }
        } else if (requestCode == CROP_PHOTO_PIC) {
            // 裁剪图片返回
//            Uri uri = data.getData();
//            DLog.i("lilijun", "裁剪返回uri----->>>" + uri);
//            if (uri != null) {
//                getFileByUri(uri);
////                uploadPic(getBitmapFromUri(uri, ModifyPersonageInfoActivity.this));
//                try {
//                    byte[] bytes = readStream(getContentResolver().openInputStream(Uri.parse(uri
//                            .toString())));
//                    uploadPic(bytes);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // 裁剪失败
//                Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString
//                        (R.string.crop_pic_failed));
//            }

            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if (photo != null) {
                    uploadPic(getBitmapBytes(photo));
                } else {
                    // 裁剪失败
                    Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R
                            .string.crop_pic_failed));
                }
            }
        }
    }


    @Override
    public void finish() {
        if (isForResult) setResult(MODIFY_USER_INFO_SUCCESS_RESULT_CODE);
        super.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_personage_title_back_img:
                // 标题返回按钮
                finish();
                break;
            case R.id.modify_personage_save_btn:
                // 保存按钮
                HashMap<String, String> params = new HashMap<>();
                modifyName = nameInput.getText().toString().trim();
                modifyNickName = nickNameInput.getText().toString().trim();
                modifyCompanyName = companyNameInput.getText().toString().trim();
                params.put("contactName", modifyName);
                params.put("nickName", modifyNickName);
                params.put("companyName", modifyCompanyName);
                progressDialog = Util.showLoadingDialog(ModifyPersonageInfoActivity.this,
                        progressDialog);
                loadDataPost(MODIFY_PERSIONAGE_INFO, params);
                break;
            case R.id.modify_personage_binding_phone:
                // 去绑定按钮
                if (isGoBinding) {
                    // 去绑定
                    BindingPhoneActivity.startActivity(ModifyPersonageInfoActivity.this);
                } else {
                    // 重新查询用户是否有绑定手机号
                    bindingPhoneText.setText(getResources().getString(R.string.phone_check));
                    loadDataGet(QURERY_BINDING_PHONE, null);
                }
                break;
            case R.id.modify_personage_icon:
                // 头像
                Util.showAlertDialog(ModifyPersonageInfoActivity.this, getResources().getString(R
                                .string.warm_prompt), getResources().getString(R.string.choice_pic),
                        getResources().getString(R.string.camera), getResources().getString(R
                                .string.pics), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 相机
                                intentCameraActivity();
                                dialogInterface.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 图库
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");
                                startActivityForResult(intent, GET_LOCAL_PHOTO_PIC);
                                dialogInterface.dismiss();
                            }
                        });
                break;
        }
    }

    /**
     * 跳转到拍照界面
     */
    private void intentCameraActivity() {
        try {
            Intent intent = new Intent();
            intent.putExtra("return-data", true);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(CAMERA_PIC_PATH)));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R.string
                    .get_camera_failed));
            DLog.e("ModifyPersonageInfoActivity", "intentCameraActivity()#exception:", e);
        }
    }

    /**
     * 照片裁剪收缩
     * @param uri
     * @param REQUE_CODE_CROP
     */
    private void startPhotoZoom(Uri uri, int REQUE_CODE_CROP) {
//        int dp = 200;
//        DLog.i("lilijun", "开始裁剪！！！@！@");
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        // intent.putExtra("outputFormat",
//        // Bitmap.CompressFormat.PNG.toString());
//        intent.putExtra("return-data", false);
//        startActivityForResult(intent, REQUE_CODE_CROP);

        DLog.i("lilijun", "开始裁剪！！！@！@");
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUE_CODE_CROP);
    }

    /**
     * 裁剪图片
     *
     * @param data
     */
    private void cropPhotoPic(Intent data) {
        Uri currImageURI = null;
        if (data != null) {
            if (data.getExtras() != null) {
                File file = getFile((Bitmap) data.getExtras().get("data"));
                DLog.i("lilijun", "相机图片的保存地址----->>>" + file.getAbsolutePath());
                // File mCurrentPhotoFile = new File(PHOTO_DIR,
                // getPhotoFileName());//
                // 给新照的照片文件命名
                currImageURI = Uri.fromFile(file);
            } else {
                currImageURI = data.getData();
            }
        } else {
            currImageURI = Uri.fromFile(new File(CAMERA_PIC_PATH));
        }

        try {
            // 启动gallery去剪辑这个照片
            final Intent intent = getCropImageIntent(currImageURI);

            // startActivityForResult(intent, GET_LOCAL_PHOTO_PIC);
            startActivityForResult(intent, CROP_CAMER_PCI);
        } catch (Exception e) {
            Toast.makeText(this, "获取照片错误", Toast.LENGTH_LONG).show();
        }
    }

    private File getFile(Bitmap bitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用

            return null;
        }
        String name = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".jpg";

        FileOutputStream b = null;

        if (!PHOTO_DIR.isDirectory()) {
            PHOTO_DIR.mkdirs();// 创建文件夹
        }
        File fileName = new File(PHOTO_DIR, name);

        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }

    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序
     */
    private static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        return intent;
    }

//    /**
//     * 获取bitmap的大小
//     *
//     * @param bitmap
//     * @return
//     */
//    private long getBitmapSize(Bitmap bitmap) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//            return bitmap.getByteCount();
//        }
//        // Pre HC-MR1
//        return bitmap.getRowBytes() * bitmap.getHeight();
//    }

//    private Bitmap getBitmapFromUri(Uri uri, Context mContext) {
//        try {
//            // 读取uri所在的图片
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//                    mContext.getContentResolver(), uri);
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    private File getFileByUri(Uri uri) {
//        if (uri == null) {
//            return null;
//        }
//        DLog.i("获取文件by URi");
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images
//                .Media.DATA);
//        actualimagecursor.moveToFirst();
//        String img_path = actualimagecursor.getString(actual_image_column_index);
//        DLog.i("图片路径---img_path--->>>" + img_path);
//        return new File(img_path);
//    }

//    private byte[] readStream(InputStream inStream) throws Exception {
//        byte[] buffer = new byte[1024];
//        int len = -1;
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        while ((len = inStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, len);
//        }
//        byte[] data = outStream.toByteArray();
//        outStream.close();
//        inStream.close();
//        return data;
//    }

    /**
     * 将Bitmap转换为数组
     * @param bmp
     * @return
     */
    private byte[] getBitmapBytes(Bitmap bmp) {
        if (bmp == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

//    private void uploadPic(File file) {
//        if (!file.exists()) {
//            Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R.string
//                    .can_not_find_pic));
//            return;
//        }
//        progressDialog = Util.showLoadingDialog(ModifyPersonageInfoActivity.this, progressDialog);
//        List<FormFile> formFiles = new ArrayList<>();
//
//        FormFile formFile = new FormFile("icon.png", "icon.png","image/png", file);
//        formFiles.add(formFile);
//        NetUtil.requestPostUploadRequest("user/uploadHeadImg", formFiles, null,
//                new Response.Listener() {
//                    @Override
//                    public void onResponse(Object response) {
//                        DLog.i("上传成功！@！！！！");
//                        doRequestSuccess((String) response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        DLog.i("上传失败！@！！！！");
//                        progressDialog.dismiss();
//                        Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString
//                                (R.string.upload_failed));
//                    }
//                });
//    }

    private void uploadPic(byte[] bytes) {
        if (bytes == null) {
            Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R.string
                    .can_not_find_pic));
            return;
        }
        progressDialog = Util.showLoadingDialog(ModifyPersonageInfoActivity.this, progressDialog);
        List<FormFile> formFiles = new ArrayList<>();
//        FormFile formFile = new FormFile("icon.jpg", "icon.jpg", "image/*", bitmap);
        FormFile formFile = new FormFile("icon.jpg", "icon.jpg", "image/*", bytes);
        formFiles.add(formFile);
        Map<String,String> params = new HashMap<>();
        params.put("appId",Constants.YB_APP_ID);
        NetUtil.requestPostUploadRequest("user/uploadHeadImg", formFiles, params,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        DLog.i("上传成功！@！！！！");
                        doRequestSuccess((String) response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DLog.i("上传失败！@！！！！");
                        Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString
                                (R.string.upload_failed));
                    }
                });
    }


    /**
     * 处理网络请求成功的事情(仅仅只是和服务器通信成功，但还是会出现其它功能逻辑的错误的)
     */
    private void doRequestSuccess(String response) {
        DLog.i(TAG, "response-------->>>" + response);
        int code = -1;
        String errorMsg = "";
        JSONObject baseObject = null;
        try {
            JSONObject dataObject = new JSONObject(response);
            code = dataObject.getInt("code");
            if (code == 0) {
                // 获取数据成功
                // 返回需要解析的JsonObject对象
                if (!dataObject.isNull("map")) {
                    baseObject = dataObject.getJSONObject("map");
                    UserInfoManager.getInstance().getUserInfo().setIcon(baseObject.getString
                            ("url"));
                    // 设置头像路径
                    userIcon.setImageURI(UserInfoManager.getInstance().getUserInfo().getIcon());
                    isForResult = true;
                }
            } else {
                errorMsg = dataObject.getString("message").trim();
            }
        } catch (Exception e) {
            DLog.e(TAG, "getResponseJson()#exception:\n", e);
            code = -2;
        }
        if (code == 0) {
            // 成功
            Util.showToast(ModifyPersonageInfoActivity.this, getResources().getString(R.string
                    .upload_success));
        } else {
            // 失败
            if (code == Constants.LOGIN_TIME_OUT) {
                // 设置用户为未登录状态
                UserInfoManager.getInstance().clearUserInfo();
                LoginActivity.startActivity(ModifyPersonageInfoActivity.this);
                Util.showErrorMessage(ModifyPersonageInfoActivity.this, errorMsg, getResources()
                        .getString(R.string.upload_failed));
                finish();
            } else {
                Util.showErrorMessage(ModifyPersonageInfoActivity.this, errorMsg, getResources()
                        .getString(R.string.upload_failed));
            }
        }
        progressDialog.dismiss();
    }


    public static void startActivity(Activity activity) {
        if (UserInfoManager.getInstance().isValidUserInfo()) {
            Intent intent = new Intent(activity, ModifyPersonageInfoActivity.class);
            activity.startActivityForResult(intent, 10018);
        }
    }
}

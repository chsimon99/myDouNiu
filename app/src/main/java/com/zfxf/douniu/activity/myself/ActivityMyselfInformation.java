package com.zfxf.douniu.activity.myself;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.address.City;
import com.zfxf.douniu.bean.address.CityPickerDialog;
import com.zfxf.douniu.bean.address.County;
import com.zfxf.douniu.bean.address.Province;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.SelectPicPopupWindow;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:21
 * @des    个人中心
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfInformation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_myself_information_date)
    LinearLayout date;
    @BindView(R.id.ll_myself_information_address)
    LinearLayout address;
    @BindView(R.id.ll_myself_information_name)
    LinearLayout name;
    @BindView(R.id.ll_myself_information_info)
    LinearLayout info;


    @BindView(R.id.tv_myself_information_date)
    TextView textDate;
    @BindView(R.id.tv_myself_information_address)
    TextView textAddress;
    @BindView(R.id.tv_myself_information_name)
    TextView textName;
    @BindView(R.id.tv_myself_information_info)
    TextView textInfo;
    @BindView(R.id.et_myself_information_name)
    EditText editName;
    @BindView(R.id.et_myself_information_info)
    EditText editInfo;

    @BindView(R.id.iv_myself_information_img)
    ImageView img;
    @BindView(R.id.ll_myself_information_content)
    LinearLayout content;
    private Intent mIntent;
    private SelectPicPopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myself_information);
        ButterKnife.bind(this);

        title.setText("个人信息");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {
        CommonUtils.showProgressDialog(this,"加载中……");
        LoginInternetRequest.getUserInformation(new LoginInternetRequest.ForResultInfoListener() {
            @Override
            public void onResponseMessage(Map<String, String> map) {
                if(map.isEmpty()){
                    CommonUtils.dismissProgressDialog();
                    return;
                }else {
                    String nickname = map.get("ud_nickname");
                    String address = map.get("ud_addr");
                    String borth = map.get("ud_borth");
                    String photoUrl = map.get("ud_photo_fileid");
                    String memo = map.get("ud_memo");
                    if(!TextUtils.isEmpty(nickname)){
                        textName.setText(nickname);
                    }
                    if(!TextUtils.isEmpty(address)){
                        textAddress.setText(address);
                    }
                    if(!TextUtils.isEmpty(borth)){
                        textDate.setText(borth);
                    }
                    if(!TextUtils.isEmpty(memo)){
                        textInfo.setText(memo);
                    }

                    if(TextUtils.isEmpty(photoUrl)){
                        img.setImageResource(R.drawable.advisor_home_img);
                        CommonUtils.dismissProgressDialog();
                    }else {
                        Bitmap cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
                        if(cacheBitmap == null){
                            Glide.with(ActivityMyselfInformation.this).load(photoUrl)
                                    .placeholder(R.drawable.advisor_home_img)
                                    .bitmapTransform(new CropCircleTransformation(ActivityMyselfInformation.this))
                                    .into(img);
                        }else {
                            byte[] bytes=CommonUtils.getBitMapByteArray(cacheBitmap);
                            Glide.with(ActivityMyselfInformation.this).load(bytes)
                                    .placeholder(R.drawable.advisor_home_img)
                                    .bitmapTransform(new CropCircleTransformation(ActivityMyselfInformation.this))
                                    .into(img);
                        }
                        CommonUtils.dismissProgressDialog();
                    }
                }
            }
        });
    }
    private void initListener() {
        back.setOnClickListener(this);
        date.setOnClickListener(this);
        address.setOnClickListener(this);
        name.setOnClickListener(this);
        info.setOnClickListener(this);
        img.setOnClickListener(this);
        editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        saveText();
                        break;
                }
                return false;
            }
        });
        editInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        saveText();
                        break;
                }
                return false;
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {//设置这个为了点击时光标在最右边
            @Override
            public void onClick(View v) {
                editName.requestFocus();
                editName.setSelection(editName.getText().length());
            }
        });
        editName.setOnTouchListener(new View.OnTouchListener() {//设置这个为了点击时光标在最右边
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editName.requestFocus();
                editName.setSelection(editName.getText().length());
                return false;
            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {//设置这个为了点击时光标在最右边
            @Override
            public void onClick(View v) {
                editInfo.requestFocus();
                editInfo.setSelection(editInfo.getText().length());
            }
        });
        editInfo.setOnTouchListener(new View.OnTouchListener() {//设置这个为了点击时光标在最右边
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editInfo.requestFocus();
                editInfo.setSelection(editInfo.getText().length());
                return false;
            }
        });
    }
    private static final int		TAKE_CODE	= 100;
    private static final int		PICK_CODE	= 101;
    private static final int		ZOOM_CODE	= 102;
    private int number = 0;
    @Override
    public void onClick(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        switch (v.getId()){
            case R.id.iv_base_back:
                saveText();
                if(number>0){
                    confirm();
                }else {
                    finishAll();
                    finish();
                }
                break;
            case R.id.ll_myself_information_date:
                saveText();
                getTime();
                break;
            case R.id.ll_myself_information_address:
                saveText();
                getAddress();
                break;
            case R.id.ll_myself_information_name:
                getUserName(v);
                break;
            case R.id.ll_myself_information_info:
                getInfo(v);
                break;
            case R.id.iv_myself_information_img:
                mPopupWindow = getPicPopupWindow(this, this, content);
                break;
            case R.id.btn_pick_photo://本地
//                MyPhotoUtil.pickPhoto(this,TAKE_CODE);
                PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)//多选 or 单选
                        .enableCrop(true)//是否裁剪
                        .compress(true)//是否压缩
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                        //系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .previewImage(false)// 是否可预览图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.btn_take_photo://拍照
//                MyPhotoUtil.picTyTakePhoto(this,PICK_CODE);
                PictureSelector.create(this).openCamera(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)//多选 or 单选
                        .enableCrop(true)//是否裁剪
                        .compress(true)//是否压缩
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                        .previewImage(false)// 是否可预览图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;

        }
    }
    private boolean isText = false;
    private boolean isInfo = false;
    private void saveText() {
        if(isText){
            isText = false;
            textName.setVisibility(View.VISIBLE);
            editName.setVisibility(View.GONE);
            textName.setText(editName.getText().toString());
            number++;
        }
        if(isInfo){
            isInfo = false;
            textInfo.setVisibility(View.VISIBLE);
            editInfo.setVisibility(View.GONE);
            textInfo.setText(editInfo.getText().toString());
            number++;
        }
    }

    @Override
    public void onBackPressed() {
        CommonUtils.dismissProgressDialog();
        saveText();
        if(number>0){
            confirm();
        }else {
            finishAll();
            finish();
        }
    }

    private void getUserName(View v) {
        isText = true;
        if(textName.isShown()){
            textName.setVisibility(View.GONE);
            editName.setVisibility(View.VISIBLE);
            editName.setText(textName.getText().toString());
            editName.setFocusable(true);
            editName.setFocusableInTouchMode(true);
            editName.requestFocus();
            editName.findFocus();
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        }
    }
    private void getInfo(View v) {
        isInfo = true;
        if(textInfo.isShown()){
            textInfo.setVisibility(View.GONE);
            editInfo.setVisibility(View.VISIBLE);
            editInfo.setText(textInfo.getText().toString());
            editInfo.setFocusable(true);
            editInfo.setFocusableInTouchMode(true);
            editInfo.requestFocus();
            editInfo.findFocus();
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        }
    }

    private void finishAll() {
        mIntent = null;
    }

    private void getTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog pickerDialog =new DatePickerDialog(ActivityMyselfInformation.this
                , R.style.MydateStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                number++;
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }
    private ArrayList<Province> provinces = new ArrayList<Province>();
    public void getAddress() {
        if (provinces.size() > 0) {
            showAddressDialog();
        } else {
            new InitAreaTask(ActivityMyselfInformation.this).execute(0);
        }
    }
    private void showAddressDialog() {
        new CityPickerDialog(ActivityMyselfInformation.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {
                    @Override
                    public void onPicked(Province selectProvince,
                                         City selectCity, County selectCounty) {
                        StringBuilder address = new StringBuilder();
                        String provine = selectProvince != null ? selectProvince.getAreaName() : "";
                        String city = selectCity != null ? selectCity.getAreaName() : "";
                        String country = selectCounty != null ? selectCounty.getAreaName() : "";
                        if(TextUtils.isEmpty(country)){
                            address.append(provine).append("-").append(city);
                        }else{
                            address.append(provine).append("-").append(city).append("-").append(country);
                        }
//                        address.append(selectProvince != null ? selectProvince.getAreaName() : "")
//                                .append(selectCity != null ? selectCity.getAreaName() : "")
//                                .append(selectCounty != null ? selectCounty.getAreaName() : "");
                        textAddress.setText(address);
                        number++;
                    }
                }).show();
    }
    private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {
        Context mContext;
        public InitAreaTask(Context context) {
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (provinces.size()>0) {
                showAddressDialog();
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected Boolean doInBackground(Integer... params) {
            String address;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("address.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = EncodingUtils.getString(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }

    public SelectPicPopupWindow getPicPopupWindow(Context context, View.OnClickListener itemsOnClick
            , View viewAttach) {
        //实例化SelectPicPopupWindow
        SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(context, itemsOnClick);
        //显示窗口,设置layout在PopupWindow中显示的位置
        menuWindow.showAtLocation(viewAttach, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        return menuWindow;
    }
    private String MYICON = "myicon.jpg";
    private int index = 1;
    private String picID = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case TAKE_CODE:// 从相册选择好之后的结果
//                MyPhotoUtil.onPhotoFromPick(this,data,ZOOM_CODE);
//                break;
//            case PICK_CODE:// 拍照完成之后会来到这个地方
//                MyPhotoUtil.onPhotoFromCamera(this,data,ZOOM_CODE);
//                break;
//            case ZOOM_CODE:// 缩放完成之后会来到这个地方
//                Bitmap zoomBitMap = MyPhotoUtil.getZoomBitMap(this);
//                CommonUtils.saveBitmapFile(zoomBitMap,MYICON);//先保存文件到本地
//                LoginInternetRequest.uplodePicture(MYICON,new LoginInternetRequest.ForResultListener() {
//                    @Override
//                    public void onResponseMessage(String code) {
//                        if(TextUtils.isEmpty(code)){
//                           return;
//                        }else {
//                            picID = code;
//                        }
//                    }
//                });
//                if(zoomBitMap!= null){
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    zoomBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                    byte[] bytes=baos.toByteArray();
//                    Glide.with(this).load(bytes)
//                            .placeholder(R.drawable.advisor_home_img)
//                            .bitmapTransform(new CropCircleTransformation(this))
//                            .into(img);
//                    number++;
//                }else{
//                    img.setImageResource(R.drawable.advisor_home_img);
//                }
//                break;
//        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    final Bitmap zoomBitMap = CommonUtils.getBitmap(media.getCompressPath());
                    if(zoomBitMap!=null){
                        CommonUtils.saveBitmapFile(zoomBitMap,MYICON);//先保存文件到本地
                        LoginInternetRequest.uplodePicture(MYICON,new LoginInternetRequest.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {
                                if(TextUtils.isEmpty(code)){
                                    return;
                                }else {
                                    picID = code;
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    zoomBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] bytes=baos.toByteArray();
                                    Glide.with(ActivityMyselfInformation.this).load(bytes)
                                            .placeholder(R.drawable.advisor_home_img)
                                            .bitmapTransform(new CropCircleTransformation(ActivityMyselfInformation.this))
                                            .into(img);
                                    number++;
                                }
                            }
                        });
                    }else {
                        Bitmap cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
                            byte[] bytes=CommonUtils.getBitMapByteArray(cacheBitmap);
                            Glide.with(ActivityMyselfInformation.this).load(bytes)
                                    .placeholder(R.drawable.advisor_home_img)
                                    .bitmapTransform(new CropCircleTransformation(ActivityMyselfInformation.this))
                                    .into(img);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        builder.setTitle("确认提交修改")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //提交服务器
                        String name = textName.getText().toString();
                        String address = textAddress.getText().toString();
                        String date = textDate.getText().toString();
                        String info = textInfo.getText().toString();
                        if(name.equals("请输入您的昵称")){
                            name = "";
                        }
                        if(address.equals("请输入您的所在地")){
                            address = "";
                        }if(date.equals("请输入您的生日")){
                            date = "";
                        }
                        if(info.equals("请输入您的简介")){
                            info = "";
                        }
                        final String finalName = name;
                        LoginInternetRequest.editUserInformation(name, date, address,info, picID, new LoginInternetRequest.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {
                                if(code.equals("成功")){
                                    CommonUtils.toastMessage("修改个人信息成功");
                                    SpTools.setString(CommonUtils.getContext(), Constants.nickname, finalName);
                                    setResult(Constants.resultCodeEditInfor,null);
                                    finishAll();
                                    finish();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAll();
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }
}

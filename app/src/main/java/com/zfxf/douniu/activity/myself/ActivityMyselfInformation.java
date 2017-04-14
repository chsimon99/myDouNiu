package com.zfxf.douniu.activity.myself;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.address.City;
import com.zfxf.douniu.bean.address.CityPickerDialog;
import com.zfxf.douniu.bean.address.County;
import com.zfxf.douniu.bean.address.Province;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.tv_myself_information_date)
    TextView textDate;
    @BindView(R.id.tv_myself_information_address)
    TextView textAddress;
    private Intent mIntent;

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

    }
    private void initListener() {
        back.setOnClickListener(this);
        date.setOnClickListener(this);
        address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_myself_information_date:
                getTime();
                break;
            case R.id.ll_myself_information_address:
                getAddress();
                break;
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
                        if(country == null){
                            address.append(provine).append("-").append(city);
                        }else{
                            address.append(provine).append("-").append(city).append("-").append(country);
                        }
//                        address.append(selectProvince != null ? selectProvince.getAreaName() : "")
//                                .append(selectCity != null ? selectCity.getAreaName() : "")
//                                .append(selectCounty != null ? selectCounty.getAreaName() : "");
                        textAddress.setText(address);
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
}

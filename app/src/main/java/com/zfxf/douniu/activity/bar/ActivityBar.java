package com.zfxf.douniu.activity.bar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.fragment.FragmentBarBar;
import com.zfxf.douniu.view.fragment.FragmentBarGrade;
import com.zfxf.douniu.view.fragment.FragmentBarZhibo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @author IMXU
 * @time   2017/5/3 13:26
 * @des    斗牛吧
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityBar extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;

    @BindView(R.id.tl_activity_bar)
    TabLayout mTabLayout;
    @BindView(R.id.vp_activity_bar)
    ViewPager mViewPager;

    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentBarBar;
    private Fragment mFragmentBarZhibo;
    private Fragment mFragmentBarGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        ButterKnife.bind(this);
        initdata();
        initListener();
    }

    private void initdata() {
        if (mFragmentBarBar == null) {
            mFragmentBarBar = new FragmentBarBar();
        }
        if (mFragmentBarZhibo == null) {
            mFragmentBarZhibo = new FragmentBarZhibo();
        }
        if (mFragmentBarGrade == null) {
            mFragmentBarGrade = new FragmentBarGrade();
        }
        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentBarZhibo);
            list_fragment.add(mFragmentBarGrade);
            list_fragment.add(mFragmentBarBar);
        }
        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.bar_item_titles);
            for(int i = 0; i<titleStrings.length;i++){
                list_title.add(titleStrings[i]);
            }
        }

        mAdapter = new BarItemAdapter(getSupportFragmentManager(),list_fragment,list_title);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.post(new Runnable() {//改变滑动条的长度
            @Override
            public void run() {
                CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),40)
                        ,CommonUtils.px2dip(CommonUtils.getContext(),40));
            }
        });
    }
    private void initListener() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_edit:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                Intent intent = new Intent(this,ActivityPostBar.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {
        list_fragment = null;
        list_title = null;
        mAdapter = null;
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}

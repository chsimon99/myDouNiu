package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.adapter.viewPager.PicPagerAdapter;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.view.InnerView;
import com.zfxf.douniu.view.fragment.FragmentNewTopEvent;
import com.zfxf.douniu.view.fragment.FragmentNewTopPoint;
import com.zfxf.douniu.view.fragment.FragmentNewTopPolicy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:25
 * @des    看头条
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityHeadline extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.inwerview)
    InnerView mViewPage;
    @BindView(R.id.item_home_pic_ll)
    LinearLayout mContainer;

    private PicPagerAdapter mPagerAdapter;
    private MyLunBo mMyLunBO;

    @BindView(R.id.tl_fragment_new_top)
    TabLayout mTabLayout;
    @BindView(R.id.vp_fragment_new_top)
    ViewPager mViewPager;
    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentNewTopPolicy;
    private Fragment mFragmentNewTopEvent;
    private Fragment mFragmentNewTopPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline);
        ButterKnife.bind(this);
        title.setText("看头条");
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        NewsInternetRequest.getHeadListLunboInformation(new NewsInternetRequest.ForResultHeadListLunboInfoListener() {
            @Override
            public void onResponseMessage(List<LunBoListInfo> lunbo_list) {
                if(lunbo_list.size() >0 ){
                    if(mPagerAdapter ==null){
                        mPagerAdapter = new PicPagerAdapter(lunbo_list, CommonUtils.getContext(), new PicPagerAdapter.MyOnClickListener() {
                            @Override
                            public void onItemClick(int positon) {
                                CommonUtils.toastMessage("您点击的是第 " + (++positon) + " 个Item");
                            }
                        });
                    }

                    if (mMyLunBO == null) {
                        mMyLunBO = new MyLunBo(mContainer, mViewPage, lunbo_list.size());
                        mMyLunBO.startLunBO();
                    }
                    mViewPage.setAdapter(mPagerAdapter);
                }
            }
        });


        if(mFragmentNewTopPolicy == null){
            mFragmentNewTopPolicy = new FragmentNewTopPolicy();
        }
        if(mFragmentNewTopEvent == null){
            mFragmentNewTopEvent = new FragmentNewTopEvent();
        }
        if(mFragmentNewTopPoint == null){
            mFragmentNewTopPoint = new FragmentNewTopPoint();
        }
        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentNewTopPolicy);
            list_fragment.add(mFragmentNewTopEvent);
            list_fragment.add(mFragmentNewTopPoint);
        }

        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.fragment_new_top_titles);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
        }
    }

    private void finishAll() {

    }
    private boolean isOnPause = false;
    @Override
    public void onPause() {
        isOnPause = true;
        if (mMyLunBO != null)
            mMyLunBO.stopLunBO();
        super.onPause();
    }
    @Override
    public void onResume() {
        if (isOnPause) {//防止轮播图暂定不动
            if (mMyLunBO != null)
                mMyLunBO.restartLunBO();
            isOnPause = false;
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();//保证没有加载数据，dialog也关闭
    }
}

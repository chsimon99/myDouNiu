package com.zfxf.douniu.activity.myself;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.fragment.FragmentMyselfAttentionAdvisor;
import com.zfxf.douniu.view.fragment.FragmentMyselfAttentionMatador;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:17
 * @des    我的关注
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfAttention extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;


    @BindView(R.id.tl_myself_attention)
    TabLayout mTabLayout;
    @BindView(R.id.vp_myself_attention)
    ViewPager mViewPager;
    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentMyselfAttentionAdvisor;
    private Fragment mFragmentMyselfAttentionMatador;
    private List<String> datas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_attention);
        ButterKnife.bind(this);

        title.setText("我的关注");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

        if(datas.size() == 0){
            datas.add("1");
            datas.add("2");
            datas.add("3");
            datas.add("1");
            datas.add("2");
            datas.add("3");
            datas.add("1");
            datas.add("2");
        }

        if(mFragmentMyselfAttentionAdvisor == null){
            mFragmentMyselfAttentionAdvisor = new FragmentMyselfAttentionAdvisor(2);
        }
        if(mFragmentMyselfAttentionMatador == null){
            mFragmentMyselfAttentionMatador = new FragmentMyselfAttentionMatador();
        }
        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentMyselfAttentionAdvisor);
            list_fragment.add(mFragmentMyselfAttentionMatador);
        }

        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.fragment_myself_attention_titles);
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
                CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),60)
                        ,CommonUtils.px2dip(CommonUtils.getContext(),60));
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
}

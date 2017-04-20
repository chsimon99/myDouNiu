package com.zfxf.douniu.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.view.InnerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentNewTop extends BaseFragment {
    private View view;

    @BindView(R.id.inwerview)
    InnerView mViewPage;
    @BindView(R.id.item_home_pic_ll)
    LinearLayout mContainer;
    private List<Integer> mDatas = new ArrayList<Integer>();
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
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_headline, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
        if (mDatas.size() == 0) {
            mDatas.add(R.drawable.home_banner);
            mDatas.add(R.drawable.home_banner);
            mDatas.add(R.drawable.home_banner);
            mDatas.add(R.drawable.home_banner);
        }
        mViewPage.setAdapter(new myPicAdapter());

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

        mAdapter = new BarItemAdapter(getActivity().getSupportFragmentManager(),list_fragment,list_title);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.post(new Runnable() {//改变滑动条的长度
            @Override
            public void run() {
                CommonUtils.setIndicator(mTabLayout, CommonUtils.dip2px(CommonUtils.getContext(),5)
                        ,CommonUtils.dip2px(CommonUtils.getContext(),5));
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    class myPicAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mDatas != null) {
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int pos = position % mDatas.size();
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(mDatas.get(pos));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position % mDatas.size());
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void itemClick(int pos) {
        CommonUtils.toastMessage("您点击的是第 " + (++pos) + " 个Item");
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
        if (mMyLunBO == null) {
            mMyLunBO = new MyLunBo(mContainer, mViewPage, mDatas);
            mMyLunBO.startLunBO();
        }
        if (isOnPause) {//防止轮播图暂定不动
            if (mMyLunBO != null)
                mMyLunBO.restartLunBO();
            isOnPause = false;
        }
        super.onResume();
    }
}
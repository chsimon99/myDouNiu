package com.zfxf.douniu.adapter.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @time 2017/3/30 9:32
 * @des ${TODO}
 */

public class BarItemAdapter extends FragmentPagerAdapter {

    private List<Fragment> list_fragment = new ArrayList<Fragment>();
    private List<String> list_title = new ArrayList<>();

    public BarItemAdapter(FragmentManager fm , List<Fragment> list, List<String> li) {
        super(fm);
        list_fragment = list;
        list_title = li;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return list_title.get(position % list_title.size());
    }
}

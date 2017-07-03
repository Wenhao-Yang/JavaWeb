package com.william.www.thestock.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by WILLIAM on 2017/6/22.
 */

public class InfoAdapter extends FragmentStatePagerAdapter {
    private String[] tab_title={"实时图","K线图"};
    FragmentManager fm;
    boolean[] fragmentsUpdateFlag;

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    private List<Fragment> fragmentList;

    public InfoAdapter(FragmentManager fm,List<Fragment> fragmentList,boolean[] fragmentsUpdateFlag) {
        super(fm);
        this.fm=fm;
        this.fragmentList=fragmentList;
        this.fragmentsUpdateFlag=fragmentsUpdateFlag;
    }
    public void UpdateFragment(int position, Fragment fragment){
        //getFragmentList().
        fragmentList.set(position,fragment);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return tab_title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();

        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
            //如果这个fragment需要更新

            FragmentTransaction ft = fm.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = fragmentList.get(position % fragmentList.size());
            //添加新fragment时必须用前面获得的tag，这点很重要
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();

            //复位更新标志
            fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
        }

        return fragment;
    }

}

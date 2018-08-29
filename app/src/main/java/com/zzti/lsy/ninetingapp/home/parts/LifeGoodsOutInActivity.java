package com.zzti.lsy.ninetingapp.home.parts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zzti.lsy.ninetingapp.utils.UIUtils.dip2px;

/**
 * 日用品出库采购
 */
public class LifeGoodsOutInActivity extends BaseActivity {
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegood_out_in;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();

        initData();

    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LifeGoodsOutFragment());
        fragments.add(new LifeGoodsInFragment());

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"出库", "采购"});
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {

    }


    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}

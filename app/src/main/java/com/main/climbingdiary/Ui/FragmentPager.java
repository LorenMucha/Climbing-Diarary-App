package com.main.climbingdiary.Ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.main.climbingdiary.R;
import com.main.climbingdiary.adapter.TabAdapter;
import com.main.climbingdiary.adapter.Tabs;

public class FragmentPager implements TabLayout.OnTabSelectedListener {
    private static final int view_layout = R.id.viewPager;
    private static final int tab_layout = R.id.tabLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;
    private FragmentManager fm;;

    public FragmentPager(AppCompatActivity _activity){
        viewPager = (ViewPager) _activity.findViewById(view_layout);
        tabLayout = (TabLayout) _activity.findViewById(tab_layout);
        adapter = new TabAdapter(_activity.getSupportFragmentManager());
    }

    public void setFragmente(){
        adapter.addFragment(Tabs.STATISTIK.getFragment(),Tabs.STATISTIK.getTitle());
        adapter.addFragment(Tabs.ROUTEN.getFragment(),Tabs.ROUTEN.getTitle());
        adapter.addFragment(Tabs.PROJEKTE.getFragment(),Tabs.PROJEKTE.getTitle());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch(tab.getPosition()) {
            case 0:
                Log.d("select tab","0");
                //addRoute.hide();
                break;
            default :
                Log.d("select tab","1");
                //addRoute.show();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
package com.main.climbingdiary.controller.button;

import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.MapController;
import com.main.climbingdiary.controller.dialog.DialogFactory;
import com.main.climbingdiary.fragments.MapFragment;

public class AppFloatingActionButton implements View.OnClickListener{

    @SuppressLint("StaticFieldLeak")
    private static FloatingActionButton floatingActionButtonAdd;
    @SuppressLint("StaticFieldLeak")
    private static FloatingActionButton floatingActionButtonLocate;

    public AppFloatingActionButton(){
        floatingActionButtonAdd.setOnClickListener(this);
        floatingActionButtonLocate.setOnClickListener(this);
    }

    static {
        floatingActionButtonAdd = MainActivity.getMainActivity().findViewById(R.id.floating_action_btn_add);
        floatingActionButtonLocate = MainActivity.getMainActivity().findViewById(R.id.floating_action_btn_locate);
    }

    @Override
    public void onClick(View v) {
        if(FragmentPager.getTabTitle().equals(MapFragment.TITLE)){
            MapController.setUserPosition();
        }else{
            DialogFactory.openAddRouteDialog(FragmentPager.getTabTitle());
        }
    }

    public static void show() {
        hide();
        if(FragmentPager.getTabTitle().equals(MapFragment.TITLE)){
            floatingActionButtonLocate.show();
        }else{
            floatingActionButtonAdd.show();
        }
    }

    public static void hide() {
        floatingActionButtonAdd.hide();
        floatingActionButtonLocate.hide();
    }
}

package com.docker.core.base.basev2;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.docker.core.R;
import com.docker.core.base.BaseActivity;
import com.docker.core.base.basehivs.HivsBaseViewModel;
import com.docker.core.util.Empty;
import com.docker.core.util.ViewEventResouce;
import com.docker.core.widget.dialog.DialogWait;
import com.gyf.barlibrary.ImmersionBar;

import javax.inject.Inject;

public abstract class HivsBaseV2Activity<prvVM extends HivsBaseV2ViewModel,pubVM extends HivsBaseV2ViewModel, VB extends ViewDataBinding> extends BaseActivity<pubVM, VB> {

    public DialogWait dialogWait;
    @Inject
    Empty empty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        initView();
    }


    public void showWaitDialog(String message, boolean cancelable) {
        if (dialogWait == null) {
            dialogWait = new DialogWait(this);
        }
        dialogWait.setMessage(message);
        dialogWait.show();
        dialogWait.setCancelable(cancelable);
    }

    public void hidWaitDialog() {
        if (dialogWait != null) {
            dialogWait.dismiss();
        }
    }


    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.colorPrimary)
                .navigationBarColor("#FFFFFF")
                .fullScreen(true)
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .autoDarkModeEnable(true) //自动状态栏字体和导航栏图标变色，必须指定状态栏颜色和导航栏颜色才可以自动变色哦
                .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .autoNavigationBarDarkModeEnable(true, 0.2f) //自动导航栏图标变色，必须指定导航栏颜色才可以自动变色哦
                .flymeOSStatusBarFontColor("#000000")  //修改flyme OS状态栏字体颜色
                .init();
    }

    public abstract void initView();

}

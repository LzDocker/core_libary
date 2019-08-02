package com.docker.core.widget.refresh.listener;

import android.view.MotionEvent;

import com.docker.core.widget.refresh.constant.RefreshState;

public interface SmartScrollingListener {

    public void onScrollingListener(MotionEvent e, RefreshState mState, float dy);

}

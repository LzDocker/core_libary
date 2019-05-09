package com.docker.core.base.basehivs;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.docker.core.base.BaseViewModel;
import com.docker.core.binding.command.BaseCommand;
import com.docker.core.util.ViewEventResouce;
import com.docker.core.widget.emptylayout.EmptyStatus;

public abstract class HivsBaseViewModel extends BaseViewModel {


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        initCommand();
        mEmptycommand.set(EmptyStatus.BdLoading);
    }

    public abstract void initCommand();

    /*
     * vm 数据源
     * */
    public final MediatorLiveData<Object> mResourceLiveData = new MediatorLiveData<Object>();

    /*
     * vm smatrrefresh 禁止刷新
     * */
    public ObservableBoolean mEnableRefresh = new ObservableBoolean();

    public ObservableBoolean mEnableLoadmore = new ObservableBoolean();

    public ObservableInt mEmptycommand = new ObservableInt();

    public int mPage = 1;
    /*
  smartrefresh 是否完成加载
 * */
    public ObservableBoolean mCompleteCommand = new ObservableBoolean();


    public BaseCommand mCommand = new BaseCommand();

    public boolean mIsfirstLoad = true;

    public void showDialogWait(String message, boolean cancelable) {
        mVmEventSouce.setValue(new ViewEventResouce(101, message, cancelable));
    }

    public void hideDialogWait() {
        mVmEventSouce.setValue(new ViewEventResouce(102, null, null));
    }

}

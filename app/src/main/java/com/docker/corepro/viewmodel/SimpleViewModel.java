package com.docker.corepro.viewmodel;
import com.bfhd.bfsourcelibary.base.HivsBaseViewModel;
import com.docker.common.widget.EmptyStatus;
import com.docker.core.util.ViewEventResouce;
import javax.inject.Inject;

public class SimpleViewModel extends HivsBaseViewModel {

    @Inject
    public SimpleViewModel() { }


    @Override
    public void initCommand() {
        mCommand.OnRefresh(() -> {   getData();});
        mCommand.OnLoadMore(() -> {   mEmptycommand.set(EmptyStatus.BdLoading);});
        mCommand.OnRetryLoad(() -> {  getData(); });
    }


    public void getData() {
        mEmptycommand.set(EmptyStatus.BdHiden);
    }

    @Override
    public void create() {
        super.create();
        mEnableRefresh.set(false);
        mEnableLoadmore.set(true);
        mVmEventSouce.setValue(new ViewEventResouce(1, "11222111", 1333));

        showDialogWait("11111",true);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }


}

package com.docker.corepro.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheMemoryStaticUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.docker.core.base.basehivs.HivsBaseViewModel;
import com.docker.core.base.basehivs.HivsNetBoundObserver;
import com.docker.core.repository.CommonRepository;
import com.docker.core.repository.Resource;
import com.docker.core.util.ViewEventResouce;
import com.docker.core.util.callback.NetBoundCallback;
import com.docker.core.util.callback.NetBoundObserver;
import com.docker.core.util.versioncontrol.vo.UpdateInfo;
import com.docker.core.widget.emptylayout.EmptyStatus;
import com.docker.corepro.api.AccountService;
import com.docker.corepro.repository.AccountRepository;
import com.docker.corepro.vo.LoginParam;
import com.docker.corepro.vo.LoginVo;
import com.docker.corepro.vo.RegisterVo;
import com.docker.corepro.vo.SpecLoginVo;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by zhangxindang on 2018/10/19.
 */

public class AccountViewModel extends HivsBaseViewModel {

    @Inject
    AccountService service;

    @Inject
    AccountRepository accountRepository;

    @Inject
    CommonRepository commonRepository;


    @Inject
    public AccountViewModel() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        showDialogWait("11111", true);
//        hideDialogWait();
    }


    private final MutableLiveData<LoginParam> paramlv = new MutableLiveData();

    public final LiveData<Resource<SpecLoginVo>> loginlv =
            Transformations.switchMap(paramlv, new Function<LoginParam, LiveData<Resource<SpecLoginVo>>>() {
                @Override
                public LiveData<Resource<SpecLoginVo>> apply(LoginParam param) {
                    return accountRepository.Login("https://www.wanandroid.com/user/login", param.name, param.pwd);
                }
            });


    public void Login(String username, String pwd) {
        mVmEventSouce.setValue(new ViewEventResouce(1, "11111111", 1));

        paramlv.setValue(new LoginParam(username, pwd));
    }


    /*
     * 注册
     * */
//    public LiveData<ApiResponse<BaseResponse<LoginVo>>> register(RegisterVo registerVo) {
//
//        return service.register(registerVo.getUsername().toString().trim(),
//                registerVo.getPassword().toString().trim(), registerVo.getRepassword().toString().trim());
//    }

    private final MutableLiveData<RegisterVo> registerParm = new MutableLiveData<>();

    public LiveData<Resource<LoginVo>> register(RegisterVo registerVo) {
        registerParm.setValue(registerVo);


        return registVo;

    }

    public final LiveData<Resource<LoginVo>> registVo = Transformations.switchMap(registerParm, new Function<RegisterVo, LiveData<Resource<LoginVo>>>() {
        @Override
        public LiveData<Resource<LoginVo>> apply(RegisterVo input) {
//            return accountRepository.registe(input.getUsername(), input.getPassword(), input.getRepassword());

            return commonRepository.noneChache(service.register(input.getUsername(), input.getPassword(), input.getRepassword()));
        }
    });

    /*
     *
     * 更新
     * */
    public final LiveData<Resource<UpdateInfo>> checkUpdate() {
        return accountRepository.checkUpData();
    }

    public void registerqq(RegisterVo input) {
        showDialogWait("-------", true);
        mResourceLiveData.addSource(commonRepository.noneChache(service.register(input.getUsername(), input.getPassword(),
                input.getRepassword())), new NetBoundObserver<>(new NetBoundCallback<LoginVo>() {


            @Override
            public void onBusinessError(Resource<LoginVo> resource) {
                hideDialogWait();
            }

            @Override
            public void onNetworkError(Resource<LoginVo> resource) {
                hideDialogWait();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                hideDialogWait();
            }
        }));

    }

    @Override
    public void initCommand() {

    }


    public void Login() {
        showDialogWait("登录中...", false);
        HashMap<String, String> param = new HashMap<>();
        param.put("username", "15210666053");
        param.put("password", EncryptUtils.encryptMD5ToString("zxd1234567"));
        param.put("clientType", "2");
        param.put("version", AppUtils.getAppVersionCode() + "");
//        param.put("udid", "ssssssssssssssssssssssssssssssssssssssss");
//        param.put("area_code", "+86");
//        param.put("source", "1");
        mResourceLiveData.addSource(
                commonRepository.noneChache(
                        service.login(param)), new HivsNetBoundObserver<>(new NetBoundCallback<Object>(this) {
                    @Override
                    public void onComplete(Resource<Object> resource) {
                        super.onComplete(resource);
                        if (resource.data != null) {
                            Log.d("sss", "onComplete: --------------------");
                        }
                        hideDialogWait();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        hideDialogWait();
                    }
                }));
    }

}

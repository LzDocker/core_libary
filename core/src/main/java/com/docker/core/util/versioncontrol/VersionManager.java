package com.docker.core.util.versioncontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.v4.content.FileProvider;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.docker.core.BuildConfig;
import com.docker.core.di.module.httpmodule.progress.ProgressListen;
import com.docker.core.di.module.httpmodule.progress.ProgressManager;
import com.docker.core.repository.Resource;
import com.docker.core.util.callback.NetBoundCallback;
import com.docker.core.util.callback.NetBoundObserver;
import com.docker.core.util.versioncontrol.vo.UpdateInfo;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/*
 * app 更新管理
 *
 * */
@Singleton
public class VersionManager implements LifecycleObserver {

    /*
     * 默认使用dialog的形式*/
    public final static int TYPE_DIALOG = 1003;
    /*
     * 通知栏更新
     * */
    public final static int TYPE_NOTIFYACTION = 1004;

    private int type;

    private Activity owner;
    private LiveData<Resource<UpdateInfo>> updateLiveData;
    private ProgressDialog dialog;
    @Inject
    ProgressManager progressManager;

    @Inject
    public VersionManager() {
    }

    public VersionManager Bind(Context context, LifecycleOwner lifecycleOwner, LiveData<Resource<UpdateInfo>> updateLiveData, int type) {
        this.owner = (Activity) context;
        this.updateLiveData = updateLiveData;
        this.type = type;
        checkVersion(lifecycleOwner);
        return this;
    }

    private void checkVersion(LifecycleOwner lifecycleOwner) {
        updateLiveData.observe(lifecycleOwner, new NetBoundObserver<>(new NetBoundCallback<UpdateInfo>() {
            @Override
            public void onBusinessError(Resource<UpdateInfo> resource) {
                downloadApk();
            }

            @Override
            public void onNetworkError(Resource<UpdateInfo> resource) {
            }

            @Override
            public void onComplete(Resource<UpdateInfo> resource) {
                super.onComplete(resource);
                if (resource.data != null) {
                    downloadApk();
                } else {
                    downloadApk();
                }
            }
        }));
    }


    private void downloadApk() {

        if (this.type == TYPE_DIALOG) {
            progressManager.download(Environment.getExternalStorageDirectory().getPath(), "qq.apk", "https:\\/\\/www.jinxitime.com\\/and\\/app-release.apk", new ProgressListen() {
                @Override
                public void ondownloadStart(Call call) {
                    initDialog();
                }

                @Override
                public Call onProcessUploadMethod(Map<String, RequestBody> params) {
                    return null;
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    ToastUtils.showShort("下载失败...");
                }

                @Override
                public void onProgress(long progress, long total, boolean done) {
                    if (dialog != null) {
                        dialog.setProgress(new Double((progress * 1.0 / total * 100)).intValue());
                    }
                }

                @Override
                public void onComplete(Response<ResponseBody> response) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    install();
//                    AppUtils.installApp(Environment.getExternalStorageDirectory() + "/qq.apk");
                }
            });

        } else {
            if (ServiceUtils.isServiceRunning(UpdateService.class)) {
                return;
            }
            Intent intent = new Intent(owner, UpdateService.class);
            owner.startService(intent);
        }
    }


    public void install() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = owner.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {   //没有权限
                AlertDialog.Builder builder = new AlertDialog.Builder(owner);
                builder.setTitle("安装应用需要打开未知来源权限，请去设置中开启权限");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startInstallPermissionSettingActivity();
                        }
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return;
            }
        }
        installApk();
    }


    public void installApk() {
        File apkfile = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "qq.apk");
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String nam = owner.getPackageName();
            Uri contentUri = FileProvider.getUriForFile(owner.getApplicationContext(), BuildConfig.APPLICATION_ID+ ".fileprovider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                boolean hasInstallPermission = owner.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
//                    ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
                    startInstallPermissionSettingActivity();
                }
            }
        } else {
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
        }
        if (owner.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            owner.startActivity(intent);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + owner.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        owner.startActivityForResult(intent, 10086);
    }


    public void initDialog() {
        dialog = new ProgressDialog(owner);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle(AppUtils.getAppName());
        dialog.setMax(100);
        dialog.setMessage("下载中...");
        dialog.show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void ON_DESTROY() {
//        if (owner != null) {
//            owner.finish();
//        }
        this.owner = null;
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
            System.gc();
        }
    }
}

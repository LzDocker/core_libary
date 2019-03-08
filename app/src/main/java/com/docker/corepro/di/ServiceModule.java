package com.docker.corepro.di;




import com.docker.corepro.api.AccountService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zhangxindang on 2018/11/21.
 */
@Module
public class ServiceModule {

    @Singleton
    @Provides
    AccountService provideUserInfoService(Retrofit retrofit) {
        return retrofit.create(AccountService.class);
    }
}

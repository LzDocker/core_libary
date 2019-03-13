package com.docker.core.di.module.httpmodule;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class CommonServiceModule_ProvideCommonServiceFactory
    implements Factory<CommonService> {
  private final CommonServiceModule module;

  private final Provider<Retrofit> retrofitProvider;

  public CommonServiceModule_ProvideCommonServiceFactory(
      CommonServiceModule module, Provider<Retrofit> retrofitProvider) {
    assert module != null;
    this.module = module;
    assert retrofitProvider != null;
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public CommonService get() {
    return Preconditions.checkNotNull(
        module.provideCommonService(retrofitProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<CommonService> create(
      CommonServiceModule module, Provider<Retrofit> retrofitProvider) {
    return new CommonServiceModule_ProvideCommonServiceFactory(module, retrofitProvider);
  }

  /** Proxies {@link CommonServiceModule#provideCommonService(Retrofit)}. */
  public static CommonService proxyProvideCommonService(
      CommonServiceModule instance, Retrofit retrofit) {
    return instance.provideCommonService(retrofit);
  }
}

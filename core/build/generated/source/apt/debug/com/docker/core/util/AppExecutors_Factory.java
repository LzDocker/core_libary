// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.docker.core.util;

import dagger.internal.Factory;

public final class AppExecutors_Factory implements Factory<AppExecutors> {
  private static final AppExecutors_Factory INSTANCE = new AppExecutors_Factory();

  @Override
  public AppExecutors get() {
    return new AppExecutors();
  }

  public static Factory<AppExecutors> create() {
    return INSTANCE;
  }
}

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.docker.core.di.module.httpmodule.converter;

import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.docker.core.di.module.httpmodule.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String s = value.string();
        Log.d("ResponseBody: ", s);
        Reader reader = new StringReader(s);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } catch (Exception e) {
            Log.d("ResponseBody", "convert: 数据解析异常" + getClass().getGenericSuperclass());
            e.printStackTrace();
            return null;
//            try {
//                return (T) s;
//            } catch (Exception e1) {
//                Log.d("ResponseBody", "修改为String类型来接收" + getClass().getGenericSuperclass());
//                return null;
//            }
        } finally {
            value.close();
        }
    }
}

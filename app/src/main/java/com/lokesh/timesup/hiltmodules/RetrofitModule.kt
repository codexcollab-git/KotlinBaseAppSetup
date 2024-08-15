package com.lokesh.timesup.hiltmodules

import com.lokesh.timesup.BuildConfig
import com.lokesh.timesup.remote.ApiHelper
import com.lokesh.timesup.remote.ApiService
import com.lokesh.timesup.util.ANDROID
import com.lokesh.timesup.util.APP
import com.lokesh.timesup.util.prefs.PrefUtils
import com.lokesh.timesup.util.getDeviceModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun providesDictionaryApi(httpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .client(httpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        pref: PrefUtils
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    val requestBuilder = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("version_code", BuildConfig.VERSION_CODE.toString())
                        .addHeader("build_type", BuildConfig.BUILD_TYPE)
                        .addHeader("application", BuildConfig.APPLICATION_ID)
                        .addHeader("version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-name", getDeviceModel())
                        .addHeader("x-os-type", ANDROID)
                        .addHeader("x-os-version", android.os.Build.VERSION.RELEASE)
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-source", APP)
                    if (pref.authToken != null) {
                        requestBuilder.addHeader("x-access-token", pref.authToken ?: "")
                    }
                    chain.proceed(requestBuilder.build())
                }
            ).addInterceptor(loggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.BUILD_TYPE.equals("debug", true)) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun providesApiHelper(): ApiHelper = ApiHelper()
}

package app.petermiklanek.currency.injection.modules

import app.petermiklanek.currency.BuildConfig
import app.petermiklanek.currency.data.remote.ApiService
import app.petermiklanek.currency.tools.Constants.Api.BASE_URL
import app.petermiklanek.currency.tools.Constants.Api.TIMEOUT_IN_SECONDS
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .validateEagerly(BuildConfig.DEBUG)
        .build()
        .create(ApiService::class.java)
}

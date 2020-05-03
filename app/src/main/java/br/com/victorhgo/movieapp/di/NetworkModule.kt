package br.com.victorhgo.movieapp.di

import br.com.victorhgo.movieapp.BuildConfig
import br.com.victorhgo.movieapp.domain.MovieRepository
import br.com.victorhgo.movieapp.data.remote.MovieService
import br.com.victorhgo.movieapp.data.repository.MovieRepositoryImp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    single { createService(get()) }
    single { createRetrofit(get(), BuildConfig.BASE_URL) }
    single { createOkHttpClient() }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor { chain -> createParametersDefault(chain) }
        .addInterceptor(httpLoggingInterceptor).build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun createParametersDefault(chain: Interceptor.Chain): Response {
    var request = chain.request()
    val builder = request.url().newBuilder()

    builder.addQueryParameter("api_key", BuildConfig.API_PRIVATE)
    request = request.newBuilder().url(builder.build()).build()
    return chain.proceed(request)
}

fun createService(retrofit: Retrofit): MovieService {
    return retrofit.create(MovieService::class.java)
}

fun createMovieRepository(apiService: MovieService): MovieRepository {
    return MovieRepositoryImp(apiService)
}
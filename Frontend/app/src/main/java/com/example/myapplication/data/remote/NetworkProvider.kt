package com.example.myapplication.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

//
//object NetworkProvider {
//    private val retrofit: Retrofit by lazy {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY // Log request/response bodies
//        }
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .build()
//
//        Retrofit.Builder()
//            .baseUrl("http://192.168.127.240:5000") // Ensure server is accessible
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val authApi: AuthApi by lazy {
//        retrofit.create(AuthApi::class.java)
//    }
//}

object NetworkProvider {
    private val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request/response bodies
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("http://192.168.214.131:5000/") // Use HTTP, ensure trailing slash
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    val doctorApi: DoctorApi by lazy {
        retrofit.create(DoctorApi::class.java)
    }
    val staffApi: StaffApi by lazy {
        retrofit.create(StaffApi::class.java)
    }
    val bookingApi: BookingApi by lazy {
        retrofit.create(BookingApi::class.java)
    }
    val triageApi: TriageApi by lazy {
        retrofit.create(TriageApi::class.java)
    }
    val patientApi: PatientApi by lazy {
        retrofit.create(PatientApi::class.java)
    }
}

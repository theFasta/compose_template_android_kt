package com.oimmei.oipharma

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.preference.PreferenceManager
import com.oimmei.oipharma.app.R
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import kotlin.time.ExperimentalTime


/**
 * @author Andrea Fastame
 * *
 * @email andrea@oimmei.com
 * *
 * @since 21/03/2017 - 16:06
 * * Copyright Oimmei Srls 2015-2022 - www.oimmei.com
 */


class OIApplication : Application() {

    @OptIn(ExperimentalTime::class)
    val elapsedMillisFromLastBackground: Long
        get() {
            val now = System.currentTimeMillis()
            val elapsed = now.minus(lastBackgroundTimestamp ?: now)
//            val duration = Duration.convert(
//                elapsed.toDouble(),
//                DurationUnit.MILLISECONDS,
//                DurationUnit.SECONDS
//            )
            return elapsed
        }
    var lastBackgroundTimestamp: Long? = null
    private val TAG: String = OIApplication::class.java.simpleName

    companion object {
        lateinit var _context: WeakReference<Context>

        val context: Context
            get() {
                return _context.get()!!
            }

    }

    inner class ApplicationObserver : DefaultLifecycleObserver, LifecycleEventObserver {
        val TAG: String = ApplicationObserver::class.java.simpleName
        override fun onStart(owner: LifecycleOwner) {
            Log.i(TAG, "Coming to FOREGROUND")
            val last = PreferenceManager.getDefaultSharedPreferences(this@OIApplication)
                .getLong("lastBackgroundTimestamp", -1L)
            if (last != -1L) {
                lastBackgroundTimestamp = last
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            Log.i(TAG, "Going to BACKGROUND")
            lastBackgroundTimestamp = System.currentTimeMillis()
            PreferenceManager.getDefaultSharedPreferences(this@OIApplication)
                .edit()
                .putLong("lastBackgroundTimestamp", lastBackgroundTimestamp!!)
                .apply()
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            Log.i(TAG, "Going ON_PAUSE")
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            Log.w(TAG, "App destroyed!")
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            Log.w(TAG, "App destroyed!")
        }


    }

    override fun onCreate() {
        super.onCreate()
        _context = WeakReference(this)

//        throw RuntimeException("O VEDIAMO SE CRASHLYTICS FUNZIONA!?")


        ProcessLifecycleOwner.get().lifecycle.addObserver(observer = ApplicationObserver())

//        MapsInitializer.initialize(this)
//        CommsHelper.init()
//        OIAnalyticsCommsHelper.client

//        throw RuntimeException("Test del professor Katz (il famoso Test di Katz")

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        BaseRealtimeClass.initParameters()

        initPicasso()

//        PaymentConfiguration.init(
//            applicationContext,
//            BuildConfig.STRIPE_KEY
//        )

//        val nm = OIApplication.context.getSystemService(Context.NOTIFICATION_SERVICE)

//        if (!Constants.DEBUG)
//            Fabric.with(this, Crashlytics())

//        when (Constants.DEBUG){
//            false -> {
//                Firebase
//            }
//        }

    }

    private fun initPicasso() {
        val picassoClient = OkHttpClient.Builder()
//                .addInterceptor { chain ->
//                    //                        Log.d(TAG, "PICASSO INTERCEPTOR CALLED");
//                    val newRequest = chain.request().newBuilder()
//                            .addHeader("Authorization", OIConstants.AUTH_BASE64_ENCODED_HEADER)
//                            .build()
//                    chain.proceed(newRequest)
//                }
            .build()


        //        picassoClient.interceptors().add(new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //                Request newRequest = chain.request().newBuilder()
        //                        .addHeader("Authorization", OIConstants.AUTH_BASE64_ENCODED_HEADER)
        //                        .build();
        //                return chain.proceed(newRequest);
        //            }
        //        });

        try {

            val picasso = Picasso.Builder(this)
                .downloader(com.squareup.picasso.OkHttp3Downloader(picassoClient))
                .listener { _, _, exception ->
                    //                            Log.e(TAG, "Trying to load image from " + uri.toString());
                    exception.printStackTrace()
                }
                .build()
            //            picasso.setIndicatorsEnabled(true);
            Picasso.setSingletonInstance(picasso)
            //            OICommsHelper.picasso = picasso;
        } catch (e: Exception) {
            Log.w(TAG, e.localizedMessage?.toString() ?: getString(R.string.unknown_error))
        }

    }

    override fun onTerminate() {
        super.onTerminate()
//        stopKovenant()
    }


}

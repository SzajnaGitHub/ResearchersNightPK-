package com.esspresso.nocnaukowcwpk.di

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Size
import com.jakewharton.rxrelay3.PublishRelay
import com.jakewharton.rxrelay3.Relay
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.altbeacon.beacon.Beacon
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    @EnterBeaconRange
    fun provideEnterBeaconRangeRelay(): Relay<Boolean> = PublishRelay.create()

    @Provides
    @Singleton
    @BeaconsInRange
    fun provideBeaconsInRangeRelay(): Relay<Collection<Beacon>> = PublishRelay.create()

    @Provides
    @Singleton
    @RemotelyCloseDialogActivity
    fun provideRemoteCloseDialogActivityRelay(): Relay<Unit> = PublishRelay.create()

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context) = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context) = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    @BluetoothState
    fun provideBluetoothStateRelay(): Relay<Boolean> = PublishRelay.create()

    @Provides
    @Singleton
    @LocationState
    fun provideLocationStateRelay(): Relay<Boolean> = PublishRelay.create()

    @Provides
    @Singleton
    @QRCodeImageBitmap
    fun provideQrCodeImageRelay(): Relay<Bitmap> = PublishRelay.create()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("APP_KV_STORE", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideScreenSize(@ApplicationContext context: Context): Size {
        return context.resources.displayMetrics.let {
            Size(it.widthPixels, it.heightPixels)
        }
    }
}

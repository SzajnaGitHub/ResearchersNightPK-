package com.esspresso.nocnaukowcwpk.di

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import com.esspresso.nocnaukowcwpk.core.App
import com.jakewharton.rxrelay3.PublishRelay
import com.jakewharton.rxrelay3.Relay
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.altbeacon.beacon.Beacon
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideContext(): Context = App.appContext

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
    fun provideLocationManager(context: Context) = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context) = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    @BluetoothState
    fun provideBluetoothStateRelay(): Relay<Boolean> = PublishRelay.create()

    @Provides
    @Singleton
    @LocationState
    fun provideLocationStateRelay(): Relay<Boolean> = PublishRelay.create()
}

package com.fleetmatics.networkingworkapp.injection;


import android.content.Context;

import com.fleetmatics.networkingworkapp.Constants;
import com.fleetmatics.networkingworkapp.network.APIService;
import com.fleetmatics.networkingworkapp.network.SearchExecutor;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public final class NetworkModule {

    @Provides
    @Singleton
    NetworkRequestManager provideNetworkRequestManager(@ForApplication Context context) {
        NetworkRequestManager.init(context, Constants.BASE_PATH_API, APIService.class);
        NetworkRequestManager.getInstance().addNetworkRequestExecutor(new SearchExecutor());
        return NetworkRequestManager.getInstance();
    }

}
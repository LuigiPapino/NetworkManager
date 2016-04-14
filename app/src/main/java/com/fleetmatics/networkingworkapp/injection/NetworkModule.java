package com.fleetmatics.networkingworkapp.injection;


import android.content.Context;

import com.fleetmatics.networkingworkapp.Constants;
import com.fleetmatics.networkingworkapp.network.APIService;
import com.fleetmatics.networkingworkapp.network.NetworkApi;
import com.fleetmatics.networkingworkapp.network.SearchExecutor;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public final class NetworkModule {

    @Provides
    @Singleton
    NetworkRequestManager provideNetworkRequestManager(@ForApplication Context context, NetworkApi<APIService> networkApi) {
        NetworkRequestManager.init(context);
        NetworkRequestManager.getInstance().addNetworkRequestExecutor(new SearchExecutor(networkApi));
        return NetworkRequestManager.getInstance();
    }

    @Provides
    @Singleton
    NetworkApi<APIService> provideNetworkApi() {
        return new NetworkApi<>(Constants.BASE_PATH_API, APIService.class);
    }

}
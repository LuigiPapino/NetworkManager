package com.fleetmatics.networkingworkapp.injection;

import android.app.Application;

import com.fleetmatics.networkingworkapp.ui.MainActivity;
import com.fleetmatics.networkingworkapp.MyApplication;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, DataStoreModule.class, NetworkModule.class})
public interface Graph {


    void inject(MyApplication myApplication);

    void inject(MainActivity mainActivity);


    final class Initializer {

        public static Graph init(Application application) {
            return DaggerGraph.builder()
                    .applicationModule(new ApplicationModule(application))

                    .build();
        }

    }
}
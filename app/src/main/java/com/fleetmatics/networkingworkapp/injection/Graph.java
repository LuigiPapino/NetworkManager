package com.fleetmatics.networkingworkapp.injection;

import android.app.Application;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.ui.job_map.JobMapActivity;
import com.fleetmatics.networkingworkapp.ui.jobs_list.MainActivity;
import com.fleetmatics.networkingworkapp.ui.workers_map.WorkersMapActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, DataStoreModule.class, NetworkModule.class})
public interface Graph {


    void inject(MyApplication myApplication);

    void inject(MainActivity mainActivity);

    void inject(JobMapActivity jobMapActivity);

    void inject(WorkersMapActivity workersMapActivity);


    final class Initializer {

        public static Graph init(Application application) {
            return DaggerGraph.builder()
                    .applicationModule(new ApplicationModule(application))

                    .build();
        }

    }
}
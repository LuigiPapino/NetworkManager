package com.fleetmatics.networkmanager.commons;

import android.app.IntentService;
import android.util.Log;


/**
 * Created by luigi.papino on 06/04/16.
 */
public abstract class BaseIntentService extends IntentService {


    private static final boolean showLog = Utility.showLog;
    protected final String TAG = this.getClass().getSimpleName();


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseIntentService(String name) {
        super(name);
    }

    protected void logD(String string) {
        if (showLog)
            Log.d(TAG, string);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getApplicationContext() instanceof RefWatcherProvider)
            ((RefWatcherProvider) getApplicationContext()).getRefWatcher().watch(this);
        else
            throw new IllegalStateException("The application must implement RefWatcherProvider");
    }
}

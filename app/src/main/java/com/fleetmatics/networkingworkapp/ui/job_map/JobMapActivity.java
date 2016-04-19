package com.fleetmatics.networkingworkapp.ui.job_map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.Job;
import com.fleetmatics.networkingworkapp.model.JobList;
import com.fleetmatics.networkingworkapp.model.JobStatus;
import com.fleetmatics.networkingworkapp.model.WorkerLatLng;
import com.fleetmatics.networkingworkapp.network.GetWorkerLocationExecutor;
import com.fleetmatics.networkingworkapp.network.GoogleAPIService;
import com.fleetmatics.networkingworkapp.network.JobsListExecutor;
import com.fleetmatics.networkingworkapp.network.MapsNetworkApi;
import com.fleetmatics.networkingworkapp.ui.jobs_list.JobView;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@EActivity
public class JobMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = JobMapActivity.class.getSimpleName();
    @Extra
    Job job;
    @Inject
    MapsNetworkApi<GoogleAPIService> mapsNetworkApi;
    @ViewById
    TextView eta;
    NetworkRequestManager networkRequestManager = NetworkRequestManager.getInstance();
    LatLng destinationLatLng;
    Marker workerMarker;
    Subscription subscription;
    private GoogleMap mMap;

    @OptionsItem(android.R.id.home)
    void onUp() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().getGraph().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        destinationLatLng = new LatLng(job.getLatitude(), job.getLongitude());
        mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(job.getTitle())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        )
        ;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 8));

        drawWorker();
        drawETA();

        MyApplication.getInstance().getGCMObserver()
                .subscribe(bundle -> {
                    drawWorker();
                    drawETA();
                });


        if (subscription != null)
            subscription.unsubscribe();
        subscription = rx.Observable.interval(1L, TimeUnit.SECONDS)
                .timeInterval()
                .subscribe(longTimeInterval -> {
                    drawWorker();
                    drawETA();
                });


    }

    private void drawETA() {
        networkRequestManager.executeRequest(JobsListExecutor.createRequest(job.getCustomerId()), JobList.class)
                .filter(NetworkRequestStatus::isCompleted)
                .filter(NetworkRequestStatus::isNotError)
                .map(NetworkRequestStatus::getData)
                .map(JobList::getJobs)
                .map(jobs -> {
                    for (int i = 0; i < jobs.size(); i++) {
                        Job j = jobs.get(i);
                        if (job.getId() == j.getId())
                            return j;

                    }
                    return job;

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(job -> {
                    this.job = job;
                    if (job.getStatus() == 1) // EnRoute
                        eta.setText("ETA " + JobView.sdfETA.format(job.getEstimateTime()));
                    else
                        eta.setText(JobStatus.values()[job.getStatus()].toString());

                });
    }

    private void drawWorker() {
        networkRequestManager.executeRequest(GetWorkerLocationExecutor.createRequest(job.getWorkerId()), WorkerLatLng.class)
                .filter(NetworkRequestStatus::isCompleted)
                .filter(NetworkRequestStatus::isNotError)
                .map(NetworkRequestStatus::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workerLatLng -> {
                    if (workerMarker == null) {
                        workerMarker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .position(workerLatLng.getLatLng())
                                .title(job.getWorkerName()));
                        drawPath(workerLatLng);
                    } else {
                        workerMarker.setPosition(workerLatLng.getLatLng());
                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                            LatLngBounds.builder()
                                    .include(workerLatLng.getLatLng())
                                    .include(destinationLatLng)
                                    .build(),
                            200
                            )
                    );


                });
    }

    @Background
    void drawPath(WorkerLatLng workerLatLng) {


        mapsNetworkApi.getApiService().getPath(
                String.format("%f,%f", job.getLatitude(), job.getLongitude()),
                String.format("%f,%f", workerLatLng.getLatitude(), workerLatLng.getLongitude()),
                job.getScheduleTime().getTime() / 1000
        ).subscribe(jsonObject -> {
            String points = jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonObject("overview_polyline").get("points").getAsString();
            Log.d(TAG, "TROIA" + jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonObject("overview_polyline").get("points").getAsString().toString());

            runOnUiThread(() -> mMap.addPolyline(new PolylineOptions()
                    .color(getColor(R.color.colorPrimary))
                    .addAll(PolyUtil.decode(points))));

        });


    }

    @Override
    protected void onDestroy() {
        if (subscription != null)
            subscription.unsubscribe();

        super.onDestroy();
    }
}

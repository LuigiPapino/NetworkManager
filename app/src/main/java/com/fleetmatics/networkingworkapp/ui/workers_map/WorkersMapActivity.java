package com.fleetmatics.networkingworkapp.ui.workers_map;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.Job;
import com.fleetmatics.networkingworkapp.model.JobList;
import com.fleetmatics.networkingworkapp.model.WorkerLatLng;
import com.fleetmatics.networkingworkapp.network.GetWorkerLocationExecutor;
import com.fleetmatics.networkingworkapp.network.GoogleAPIService;
import com.fleetmatics.networkingworkapp.network.JobsListExecutor;
import com.fleetmatics.networkingworkapp.network.MapsNetworkApi;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@EActivity
@OptionsMenu(R.menu.menu_workers_map)
public class WorkersMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = WorkersMapActivity.class.getSimpleName();
    @Extra
    JobList jobList;
    @Inject
    MapsNetworkApi<GoogleAPIService> mapsNetworkApi;
    NetworkRequestManager networkRequestManager = NetworkRequestManager.getInstance();
    HashMap<Integer, Marker> jobMarkerMap = new HashMap<>();
    HashMap<Integer, Marker> workerMarkerMap = new HashMap<>();
    HashMap<Integer, Polyline> polylineHashMap = new HashMap<>();
    boolean firstMovementCamera = true;
    int colorsIndex = 0;
    int[] colors = {R.color.md_blue_500, R.color.md_deep_orange_500, R.color.md_green_500, R.color.md_yellow_700, R.color.md_brown_500};
    private GoogleMap mMap;
    private LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
    private int boundedPoint = 0;

    @OptionsItem(android.R.id.home)
    void onUp() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().getGraph().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OptionsItem(R.id.action_refresh)
    void refresh() {
        mMap.clear();
        jobMarkerMap.clear();
        workerMarkerMap.clear();
        polylineHashMap.clear();
        boundBuilder = new LatLngBounds.Builder();
        drawAll();
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

        drawAll();


        rx.Observable.interval(10L, TimeUnit.SECONDS)
                .timeInterval()
                .subscribe(longTimeInterval -> {
                    //drawAll();
                });


    }

    private void drawAll() {
        networkRequestManager.executeRequest(JobsListExecutor.createRequest(MyApplication.getInstance().getCustomerIdValue()), JobList.class)
                .filter(NetworkRequestStatus::isCompleted)
                .filter(NetworkRequestStatus::isNotError)
                .map(status -> {
                    this.jobList = status.getData();
                    return status.getData();
                })
                .map(JobList::getJobs)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobs -> {
                    resetCamera();
                    for (int i = 0; i < jobs.size(); i++) {
                        Job job = jobs.get(i);
                        drawJob(job);
                        drawWorker(job);

                    }
                });

    }

    private void drawJob(Job job) {
        if (!jobMarkerMap.containsKey(job.getId())) {
            LatLng destinationLatLng = new LatLng(job.getLatitude(), job.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(job.getTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            );
            jobMarkerMap.put(job.getId(), marker);

            addCameraPoint(destinationLatLng);
        }


    }

    private void resetCamera() {
        boundBuilder = new LatLngBounds.Builder();
        boundedPoint = 0;

    }

    private void addCameraPoint(LatLng latLng) {
        boundBuilder.include(latLng);
        if (firstMovementCamera) {
            firstMovementCamera = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 200));


    }

    private void drawWorker(Job job) {
        networkRequestManager.executeRequest(GetWorkerLocationExecutor.createRequest(job.getWorkerId()), WorkerLatLng.class)
                .filter(NetworkRequestStatus::isCompleted)
                .filter(NetworkRequestStatus::isNotError)
                .map(NetworkRequestStatus::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workerLatLng -> {
                    if (!workerMarkerMap.containsKey(job.getWorkerId())) {
                        Marker workerMarker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .position(workerLatLng.getLatLng())
                                .title(job.getWorkerName()));

                        workerMarkerMap.put(job.getWorkerId(), workerMarker);

                    } else {
                        workerMarkerMap.get(job.getWorkerId()).setPosition(workerLatLng.getLatLng());
                    }
                    Log.d("MYLOG", "STATUS" + job.getStatus());
                    if (job.getStatus() == 1)
                        drawPath(job, workerLatLng);
                    else if (job.getStatus() == 1 && polylineHashMap.containsKey(job.getId()))
                        polylineHashMap.get(job.getId()).remove();

                    addCameraPoint(workerLatLng.getLatLng());

                });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Background
    void drawPath(Job job, WorkerLatLng workerLatLng) {
        Log.d(TAG, "drawPath() called with: " + "job = [" + job + "], workerLatLng = [" + workerLatLng + "]");
        if (polylineHashMap.containsKey(job.getId()))
            return;


        mapsNetworkApi.getApiService().getPath(
                String.format("%f,%f", job.getLatitude(), job.getLongitude()),
                String.format("%f,%f", workerLatLng.getLatitude(), workerLatLng.getLongitude()),
                job.getScheduleTime().getTime() / 1000
        ).subscribe(jsonObject -> {
            String points = jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonObject("overview_polyline").get("points").getAsString();
            Log.d(TAG, "TROIA" + jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonObject("overview_polyline").get("points").getAsString().toString());

            runOnUiThread(() -> polylineHashMap.put(job.getId(), mMap.addPolyline(new PolylineOptions()
                    .color(getColor(colors[colorsIndex++ % 5]))
                    .addAll(PolyUtil.decode(points)))));

        });


    }
}

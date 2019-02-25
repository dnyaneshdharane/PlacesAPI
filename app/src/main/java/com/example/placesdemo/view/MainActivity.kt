package com.example.placesdemo.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.placesdemo.R
import com.example.placesdemo.adapter.ResultListAdapter
import com.example.placesdemo.api.APIClient
import com.example.placesdemo.api.ApiService
import com.example.placesdemo.responce.PlacesResponce
import com.example.placesdemo.responce.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.realmkotlinexample.dbservice.PlacesService
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    var permissionsToRequest: ArrayList<String> = ArrayList()
    private val permissionsRejected = ArrayList<String>()
    private val permissions = ArrayList<String>()
    private val ALL_PERMISSIONS_RESULT = 101
    lateinit var latLngString: String
    lateinit var latLng: LatLng
    lateinit var adater: ResultListAdapter
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var mLatitude = 0.0
    var mLongitude = 0.0
    lateinit var api: ApiService
    lateinit var placesService: PlacesService
    lateinit var realm: Realm
    var mLocationPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        placesService = PlacesService()
        realm = Realm.getDefaultInstance()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        api = APIClient.create()
        list_rv.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
        getPlaces()

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//
//            if (permissionsToRequest.size > 0)
//                requestPermissions(permissionsToRequest.toTypedArray(), ALL_PERMISSIONS_RESULT)
//            else {
//                fetchLocation()
//            }
//        } else {
//            fetchLocation()
//        }


    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }
    fun getPlaces() {
        try {
            var results = ArrayList<Result>()
            var placesModel = placesService.getPlaces(realm)
            results.addAll(placesModel)


            adater = ResultListAdapter(this@MainActivity,results )
            list_rv.adapter = adater

        } catch (e: Exception) {
            Log.e("DD", "Exception in creating realm object:", e)
        }
    }

    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()

        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }

        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {

            ALL_PERMISSIONS_RESULT -> {
                for (perms in permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms)
                    }
                }

                if (permissionsRejected.size > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toTypedArray(), ALL_PERMISSIONS_RESULT)
                                    }
                                })
                            return
                        }
                    }

                } else {
                    fetchLocation()
                }
            }
        }

    }


    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    fun fetchLocation() {
        var latLngString = mLatitude.toString() + "," + mLongitude
        var call = api.doPlaces("hotel", latLngString, "hotel", true, "distance", ApiService.GOOGLE_PLACE_API_KEY)
        call.enqueue(object : Callback<PlacesResponce> {
            override fun onResponse(call: Call<PlacesResponce>, response: Response<PlacesResponce>) {
                val root = response.body()

                if (response.isSuccessful()) {
                    for(resulObj in root!!.results!!)
                    placesService.addOrUpdatePlaces(realm, resulObj)
                    getPlaces()
                } else {
                    Toast.makeText(applicationContext, "No matches found near you", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<PlacesResponce>, t: Throwable) {
                // Log error here since request failed
                call.cancel()
            }
        })


    }


    @SuppressLint("MissingPermission")
    fun checkPermissions() {
        permissions.add(ACCESS_FINE_LOCATION)
        permissions.add(ACCESS_COARSE_LOCATION)
        permissionsToRequest = findUnAskedPermissions(permissions)
        var mLastKnownLocation: Location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size > 0)
                requestPermissions(permissionsToRequest.toTypedArray(), ALL_PERMISSIONS_RESULT)
            else {
                if (checkLocationSetting()) {
//                    return
                    val locationResult = mFusedLocationProviderClient.lastLocation
                    locationResult.addOnCompleteListener() {
                        Log.d("DD", "getDeviceLocation addOnCompleteListener")
                        if (it.isSuccessful() && it.getResult() != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = it.getResult()

                            mLatitude = mLastKnownLocation.latitude
                            mLongitude = mLastKnownLocation.longitude
                            fetchLocation()
                        } else {
                            Log.d("DD", "Current location is null. Using defaults.");
                            Log.e("DD", "Exception: %s", it.getException());

                        }
                    }

                }
            }
        } else {
            fetchLocation()
        }

    }


    lateinit var dialog: android.app.AlertDialog
    fun checkLocationSetting(): Boolean {
        Log.d("DD", "checkLocationSetting")
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            var builder = android.app.AlertDialog.Builder(this)
            builder.setTitle(R.string.gps_not_found_title);  // GPS not found
            builder.setMessage(R.string.gps_not_found_message)
            builder.setPositiveButton("YES", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            })
            builder.setNegativeButton("NO", null)
            if (!this::dialog.isInitialized) {
                dialog = builder.create()
                dialog.show()
            } else if (!dialog.isShowing) {
                dialog.show()
            }


        } else
            return true
        return false
    }
}

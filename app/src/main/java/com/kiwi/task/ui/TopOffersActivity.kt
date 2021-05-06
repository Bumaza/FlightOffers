package com.kiwi.task.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.kiwi.task.ui.adapters.FlightViewPagerAdapter
import com.kiwi.task.R
import com.kiwi.task.databinding.ActivityTopOffersBinding
import com.kiwi.task.models.Flight
import com.kiwi.task.utils.MessageBox
import com.kiwi.task.utils.ViewModelFactory
import com.kiwi.task.viewmodels.TopOffersViewModel
import kotlin.collections.ArrayList


class TopOffersActivity : BaseActivity() {

    private val TAG: String = "TopOfffers"

    private var locationManager : LocationManager? = null

    private val binding by binding<ActivityTopOffersBinding>(R.layout.activity_top_offers)
    lateinit var viewModel: TopOffersViewModel

    lateinit var flightAdapter: FlightViewPagerAdapter

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_offers)
        viewModel = ViewModelProviders.of(this,
            ViewModelFactory.viewModelFactory { TopOffersViewModel() }).get(TopOffersViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initAdapter()

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),0)
        }else{
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }

    }


    fun initAdapter(){
        flightAdapter = FlightViewPagerAdapter(this)
        binding.viewPager.adapter = flightAdapter
        binding.dotsInticator.setViewPager(binding.viewPager)
        viewModel.topFlights.observe(this, androidx.lifecycle.Observer {
            flightAdapter.extend(it)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }
    }

    override fun onPermissionsGranted() {
        super.onPermissionsGranted()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
        //locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //Update current location and fetch new offers
           // KiwiApi.defaultQuery[KiwiApi.FlightParams.FLY_FROM.value] =  "${location.longitude}-${location.latitude}-250km"
           // viewModel.fetchFlights()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}
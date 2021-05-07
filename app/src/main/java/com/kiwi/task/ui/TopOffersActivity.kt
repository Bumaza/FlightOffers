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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kiwi.task.App
import com.kiwi.task.ui.adapters.FlightViewPagerAdapter
import com.kiwi.task.R
import com.kiwi.task.data.network.FlightParams
import com.kiwi.task.data.network.KiwiApi
import com.kiwi.task.databinding.ActivityTopOffersBinding
import com.kiwi.task.models.Flight
import com.kiwi.task.utils.MessageBox
import com.kiwi.task.utils.ViewModelFactory
import com.kiwi.task.viewmodels.Status
import com.kiwi.task.viewmodels.TopOffersViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.collections.ArrayList


class TopOffersActivity : BaseActivity() {

    private val TAG: String = "TopOfffers"

    private var locationManager : LocationManager? = null

    private val binding by binding<ActivityTopOffersBinding>(R.layout.activity_top_offers)
    lateinit var viewModel: TopOffersViewModel

    lateinit var flightAdapter: FlightViewPagerAdapter


    private val useCurrentLocation = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this,
            ViewModelFactory.viewModelFactory { TopOffersViewModel() }).get(TopOffersViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initNetworkErrorLayout()
        initAdapter()
        locationInit()

        viewModel.status.observe(this, Observer {
            when(it) {
                Status.ERROR -> {
                    MessageBox.showError(
                        binding.root.parent,
                        getString(R.string.error),
                        getString(R.string.error_text)
                    )
                }
            }
        })
    }

    fun locationInit(){
        if(!useCurrentLocation){
            viewModel.fetchFlights()
            return
        }
        viewModel.status.value = Status.LOADING
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),0)
        }else{
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }
    }

    override fun onRefreshNetwork() {
        super.onRefreshNetwork()
        viewModel.fetchFlights()
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
        }else{
            viewModel.fetchFlights()
        }
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //Update current location and fetch new offers
            KiwiApi.defaultQuery[FlightParams.FLY_FROM.value] =  "${location.longitude}-${location.latitude}-300km"
            viewModel.fetchFlights()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}
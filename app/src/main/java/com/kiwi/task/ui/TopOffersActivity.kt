package com.kiwi.task.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.kiwi.task.models.SearchResult
import com.kiwi.task.repository.api.KiwiApi
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.kiwi.task.ui.adapters.FlightViewPagerAdapter
import com.kiwi.task.R
import com.kiwi.task.databinding.ActivityTopOffersBinding
import com.kiwi.task.models.Flight
import com.kiwi.task.ui.fragments.FilterBottomSheetFragment
import com.kiwi.task.utils.MessageBox
import com.kiwi.task.utils.ViewModelFactory
import com.kiwi.task.viewmodels.TopOffersViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TopOffersActivity : BaseActivity() {

    private val TAG: String = "TopOfffers"

    private var disposable: Disposable? = null
    private var viewPager: ViewPager? = null
    private var dotsIndicator: WormDotsIndicator? = null
    private var dialog: ProgressDialog? = null

    private var formatter:SimpleDateFormat? = null
    private var locationManager : LocationManager? = null

    private var sharedPref: com.kiwi.task.utils.SharedPreferences? = null

    private var KEY_DATE = "date"

    lateinit var binding: ActivityTopOffersBinding
    lateinit var viewModel: TopOffersViewModel
    lateinit var filterFragment: FilterBottomSheetFragment

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_offers)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_top_offers)
        viewModel = ViewModelProviders.of(this,
            ViewModelFactory.viewModelFactory { TopOffersViewModel() }).get(TopOffersViewModel::class.java)

        viewPager = findViewById<ViewPager>(R.id.view_pager)
        dotsIndicator = findViewById<WormDotsIndicator>(R.id.dots_inticator)
        dialog = ProgressDialog(this)
        dialog?.setTitle("Loading...")
        formatter = SimpleDateFormat("dd/MM/yyyy")
        sharedPref = com.kiwi.task.utils.SharedPreferences(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),0)
        }else{
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }

        callPopularFlights()
        MessageBox.showError(binding.root.parent, "Chyba", "NIeco je zle.")

        onFilterOpen(null)
    }

    fun onShareClick(){
        //open share dialog
        Log.i(TAG, "Share click")
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "URL")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
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

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun callPopularFlights(limit: Int = 5, tomorrow: Boolean = false){
        dialog?.show()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        if(tomorrow) calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1)

        KiwiApi.defaultQuery["dateFrom"] = formatter!!.format(calendar.time)
        KiwiApi.defaultQuery["dateTo"] = formatter!!.format(calendar.time)
        KiwiApi.defaultQuery["limit"] = "%d".format(limit)

        disposable = KiwiApi.service.getPopularFlights(KiwiApi.defaultQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> showFlightsResult(result)},
                {error -> error.message?.let { showError(it)}}
            )
    }

    private fun showError(errorMessage: String){
        if(dialog?.isShowing!!) dialog?.dismiss()
//        dialog?.setTitle("ERROR")
//        dialog?.setMessage(errorMessage)
//        dialog?.show()
    }

    private fun showFlightsResult(result: SearchResult){
        if (dialog?.isShowing!!) dialog?.dismiss()
        viewPager?.adapter =
            FlightViewPagerAdapter(
                this,result.flights.toCollection(ArrayList())
            )
        dotsIndicator?.setViewPager(viewPager!!)
    }

    private fun dailyCheck(data: Array<Flight>): ArrayList<Flight>{

        val storedDate: Long = sharedPref?.getLongValue(KEY_DATE)!!

        if(data.isEmpty()){
            callPopularFlights(tomorrow = true)
            return ArrayList()
        }

        if(storedDate >= 0 && !DateUtils.isToday(storedDate)){
            val showedFlights = sharedPref?.getStringSet()
            val dayOffers = ArrayList<Flight>()

            for(flight in data){
                if(!showedFlights?.contains(flight.id)!!){
                    dayOffers.add(flight)
                }
                if(dayOffers.size >= 5) break
            }

            if(dayOffers.size < 5){
                callPopularFlights(showedFlights?.size!! + 5) //call +5(Dirichlet's principle)
                return ArrayList<Flight>()
            }

            sharedPref?.save(dayOffers)
            sharedPref?.save(KEY_DATE, Date().time) //new stored Date.time
            return dayOffers
        }
        return data.toCollection(ArrayList())
    }

    fun onFilterOpen(view: View?){
        supportFragmentManager.let{
            filterFragment = FilterBottomSheetFragment.newInstance(viewModel).apply {
                show(it, tag)
            }
        }
    }
}
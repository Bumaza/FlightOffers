package com.kiwi.task.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kiwi.task.R
import com.kiwi.task.databinding.FlightItemBinding
import com.kiwi.task.models.Flight
import com.kiwi.task.repository.api.KiwiApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.min

class FlightViewPagerAdapter(private val context: Context, data: ArrayList<Flight>) : PagerAdapter() {

    var inflater : LayoutInflater = LayoutInflater.from(context)
    var flights: ArrayList<Flight> = ArrayList<Flight>()

    lateinit var binding: FlightItemBinding

    val cityBitmaps: HashMap<String, Bitmap>? = HashMap<String, Bitmap>()

    init {
        flights = data
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return min(flights.size, 5)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val flight: Flight? = flights[position]

        binding = DataBindingUtil.inflate(inflater, R.layout.flight_item, container, false)
        binding.flight = flight

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }

    private fun downloadImageForDestination(id: String, destination: String, imageView: ImageView){
        KiwiApi.kiwiApiImageService.getImage(destination)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> createAndSaveBitmap(id, result, imageView)},
                {error -> println(error.message)}
            )
    }

    private fun createAndSaveBitmap(id: String, result: ResponseBody, imageView: ImageView) {
        cityBitmaps?.put(id, BitmapFactory.decodeStream(result.byteStream()))
        imageView.setImageBitmap(cityBitmaps?.get(id))
    }
}
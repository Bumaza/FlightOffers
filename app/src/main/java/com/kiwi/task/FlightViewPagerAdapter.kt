package com.kiwi.task

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kiwi.task.api.KiwiApi
import com.kiwi.task.model.FlightData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.min

class FlightViewPagerAdapter(private val context: Context, data: ArrayList<FlightData>) : PagerAdapter() {

    private var layoutInflater : LayoutInflater ? = null
    var flights: ArrayList<FlightData> = ArrayList<FlightData>()

    val cityBitmaps: HashMap<String, Bitmap>? = HashMap<String, Bitmap>()


    init {
        layoutInflater = LayoutInflater.from(context)
        flights = data
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return min(flights.size, 5)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInflater!!.inflate(R.layout.viewpager_flight, container, false)
        val image = view.findViewById<ImageView>(R.id.destination_image)
        val fromLabel = view.findViewById<TextView>(R.id.fly_from_label)
        val toLabel = view.findViewById<TextView>(R.id.fly_to_label)
        val dTimeLabel = view.findViewById<TextView>(R.id.departure_time)
        val aTimeLabel = view.findViewById<TextView>(R.id.arrival_time)
        val durationLabel = view.findViewById<TextView>(R.id.duration)
        val distanceLabel = view.findViewById<TextView>(R.id.distance)
        val priceLabel = view.findViewById<TextView>(R.id.price)

        val flight: FlightData? = flights[position]

        fromLabel.text = flight?.cityFrom
        toLabel.text = flight?.cityTo
        durationLabel.text = flight?.fly_duration
        distanceLabel.text = flight?.distance.toString() + "km"
        priceLabel.text = flight?.price.toString()
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        dTimeLabel.text = simpleDateFormat.format(Date(flight?.dTime!!))
        aTimeLabel.text = simpleDateFormat.format(Date(flight?.aTime!!))


        fromLabel.isSelected = true
        toLabel.isSelected = true

        if(cityBitmaps?.contains(flight?.id)!!){
            image?.setImageBitmap(cityBitmaps.get(flight?.id))
        }else{
            flight?.mapIdto?.let { downloadImageForDestination(flight.id, it, image) }
        }

        container.addView(view)
        return view
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
package com.kiwi.task.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kiwi.task.R
import com.kiwi.task.databinding.FlightItemBinding
import com.kiwi.task.models.Flight
import kotlin.collections.ArrayList
import kotlin.math.min

class FlightViewPagerAdapter(var context: Context, var flights: ArrayList<Flight> = ArrayList()) : PagerAdapter() {

    var inflater : LayoutInflater = LayoutInflater.from(context)

    lateinit var binding: FlightItemBinding

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

    fun extend(data: ArrayList<Flight>){
        flights.addAll(data)
        notifyDataSetChanged()
    }

    fun extend(data: Array<Flight>){
        flights.addAll(data)
        notifyDataSetChanged()
    }
}
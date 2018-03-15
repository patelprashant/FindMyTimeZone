package com.example.findmytimezone

import android.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.findmytimezone.R.layout.activity_time_zone
import kotlinx.android.synthetic.main.activity_time_zone.*
import java.util.*


class TimeZoneActivity : AppCompatActivity() {
    private var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_time_zone)

        listView = this.timeZoneList

        val timezones = ArrayList(Arrays.asList(TimeZone.getAvailableIDs()))
        val adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, R.id.text1, timezones[0])
        listView?.adapter = adapter
    }
}

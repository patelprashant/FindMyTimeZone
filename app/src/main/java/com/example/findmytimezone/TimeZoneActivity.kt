package com.example.findmytimezone

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
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

        val timezones = ArrayList(Arrays.asList(TimeZone.getAvailableIDs()))[0]
        val adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, R.id.text1, timezones)
        listView?.adapter = adapter

        listView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val result = Intent(applicationContext, MainActivity::class.java)
            result.putExtra("timezone", timezones[i])
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }
}

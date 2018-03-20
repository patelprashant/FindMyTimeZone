package com.example.findmytimezone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_select_timezones.*
import java.util.*




class SelectTimezonesActivity : AppCompatActivity() {

    var selectedTimezones: ArrayList<String> = ArrayList()
    var listViewTimeZoneView: ListView? = null
    var showAll = true
    var adapter: ArrayAdapter<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_timezones)

        setTitle("Choose Time Zones");

        val intent = intent
        val bundle = intent.getBundleExtra("selectedTimezonesBundle")
        selectedTimezones = bundle.getStringArrayList("selectedTimezones")

        val timezones = ArrayList(Arrays.asList(TimeZone.getAvailableIDs()))[0]
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, timezones)
        listViewTimeZoneView = this.listViewTimeZone
        listViewTimeZoneView?.adapter = adapter

        listViewTimeZoneView!!.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, i, l ->
            if (listViewTimeZoneView!!.isItemChecked(i)) {
                selectedTimezones.add(adapter!!.getItem(i))
            } else {
                selectedTimezones.remove(adapter!!.getItem(i))
            }
        }
        checkSelectedTimeZones()
    }

    private fun checkSelectedTimeZones() {
        for (j in 0 until adapter!!.getCount()) {
            if (selectedTimezones.contains(adapter!!.getItem(j))) {
                listViewTimeZoneView!!.setItemChecked(j, true)
            } else {
                listViewTimeZoneView!!.setItemChecked(j, false)
            }
        }
    }

    fun uncheckAllData(view: View) {
        selectedTimezones.clear()
        checkSelectedTimeZones()
    }

    fun showCheckedData(view: View) {
        val button = view as Button
        adapter!!.clear()
        if (showAll) {
            for (timezone in selectedTimezones) {
                adapter!!.add(timezone)
            }
            adapter!!.notifyDataSetChanged()

            button.setText("Show All")
            showAll = false
        } else {
            for (timezone in TimeZone.getAvailableIDs()) {
                adapter!!.add(timezone)
            }
            adapter!!.notifyDataSetChanged()

            button.setText("Show Checked")
            showAll = true
        }

        checkSelectedTimeZones()
    }

    fun doneSelection(view: View) {
        val bundle = Bundle()
        bundle.putStringArrayList("selectedTimezones", selectedTimezones)
        val result = Intent(this, MainActivity::class.java)
        result.putExtra("selectedTimezonesBundle", bundle)
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}

package com.example.findmytimezone

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnSeekBarChangeListener, View.OnClickListener {


    private var seekBarView: SeekBar? = null
    private var userTimeView: TextView? = null
    private var dateButtonView: Button? = null
    private var timeZoneButtonView: Button? = null
    private var convertedDateView: TextView? = null
    private var convertedTimeView: TextView? = null
    private var listViewMainView: ListView? = null

    private var localDate: Date = Date()
    private var calendar = Calendar.getInstance()
    private var userTimeZone: TimeZone? = null

    var selectedTimezonesMain = arrayOf("Europe/Bucharest", "Europe/London", "Europe/Paris")
    var selectedTimeZoneMain: TimeZone? = null


    private var CHOOSE_TIME_ZONE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userTimeView = this.userTime
        seekBarView = this.seekBar
        dateButtonView = this.dateButton
        timeZoneButtonView = this.timeZoneButton
        convertedDateView = this.convertedDate
        convertedTimeView = this.convertedTime
        listViewMainView = this.listViewMain

        seekBarView!!.setOnSeekBarChangeListener(this)
        dateButtonView?.setOnClickListener(this)

        val adapterMain = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, selectedTimezonesMain)
        listViewMainView?.adapter = adapterMain
        listViewMainView!!.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            selectedTimeZoneMain = TimeZone.getTimeZone(selectedTimezonesMain[i])
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateButtonView?.text = sdf.format(calendar.time)
    }

    //timezone button click
    fun chooseTimezone(view: View) {
        val intent = Intent(this, TimeZoneActivity::class.java)
//        startActivity(intent)
        startActivityForResult(intent, CHOOSE_TIME_ZONE_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_TIME_ZONE_REQUEST_CODE && resultCode == Activity.RESULT_OK) run {
            val timeZoneData: String = data!!.getStringExtra("timezone")
            timeZoneButtonView!!.text = timeZoneData
            userTimeZone = TimeZone.getTimeZone(timeZoneData)
        }
    }

    //dateButtonView click
    override fun onClick(view: View?) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        DatePickerDialog(this@MainActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    //seekBar interactions
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val progressText: String? = if (progress < 10) {
            "0" + progress.toString()
        } else {
            progress.toString()
        }
        userTimeView?.text = progressText + ":00"

        if (seekBar != null) localDate.hours = seekBar.progress
        if (fromUser) {
            localDate.minutes = 0
        }
        seekBar?.progress = localDate.hours
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


}
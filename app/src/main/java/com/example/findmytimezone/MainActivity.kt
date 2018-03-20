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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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


    private val CHOOSE_TIME_ZONE_REQUEST_CODE = 1
    private val SELECT_TIME_ZONES_REQUEST_CODE = 2

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

        setupListViewAdapter()
        listViewMainView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            selectedTimeZoneMain = TimeZone.getTimeZone(selectedTimezonesMain[i])
            convertTimeZoneData(userTimeZone, selectedTimeZoneMain)
        }
    }

    fun selectTimezones(view: View) {
        val bundle = Bundle()
        bundle.putStringArrayList("selectedTimezones", ArrayList(Arrays.asList(selectedTimezonesMain[0])))
        val intent = Intent(this, SelectTimezonesActivity::class.java)
        intent.putExtra("selectedTimezonesBundle", bundle)
        startActivityForResult(intent, 2)
    }

    private fun setupListViewAdapter() {
        val adapterMain = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, selectedTimezonesMain)
        listViewMainView?.adapter = adapterMain
    }


    private fun convertTimeZoneData(fromTimeZone: TimeZone?, toTimeZone: TimeZone?) {
        if (fromTimeZone != null && toTimeZone != null) {
            val fromOffset = fromTimeZone.getOffset(localDate.time)
            val toOffset = toTimeZone.getOffset(localDate.time)
            val convertedTime = localDate.time - (fromOffset - toOffset)
            val convertedDate = Date(convertedTime)
            val hours = convertedDate.hours
            val minutes = convertedDate.minutes
            val time = ((if (hours < 10) "0" + Integer.toString(hours) else Integer.toString(hours))
                    + ":" + if (minutes < 10) "0" + Integer.toString(minutes) else Integer.toString(minutes))
            convertedTimeView?.text = time
            convertedDateView?.text = (DateFormat.getDateInstance().format(convertedDate))
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateButtonView?.text = sdf.format(calendar.time)
        convertTimeZoneData(userTimeZone, selectedTimeZoneMain)
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

        if (requestCode == SELECT_TIME_ZONES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val bundle = data!!.getBundleExtra("selectedTimezonesBundle")
            val selectedTimezonesArrayList = bundle.getStringArrayList("selectedTimezones")
            selectedTimezonesArrayList.sortWith(Comparator { s, t1 -> s.compareTo(t1, ignoreCase = true) })
            selectedTimezonesMain = Array(selectedTimezonesArrayList.size) { "it = $it" }
            selectedTimezonesArrayList.toArray(selectedTimezonesMain)
            setupListViewAdapter()
        }

        convertTimeZoneData(userTimeZone, selectedTimeZoneMain)
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
        convertTimeZoneData(userTimeZone, selectedTimeZoneMain)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


}
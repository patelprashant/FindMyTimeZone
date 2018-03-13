package com.example.findmytimezone

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnSeekBarChangeListener, View.OnClickListener {


    private var seekBarView: SeekBar? = null
    private var userTimeView: TextView? = null
    private var localDate: Date = Date()
    private var dateBtn: Button? = null
    private var convertedDateView: TextView? = null
    private var calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userTimeView = this.userTime
        seekBarView = this.seekBar
        dateBtn = this.dateButton
        convertedDateView = this.convertedDate

        seekBarView!!.setOnSeekBarChangeListener(this)
        dateBtn?.setOnClickListener(this)
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        convertedDateView?.text = sdf.format(calendar.time)
    }


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
package com.example.findmytimezone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), OnSeekBarChangeListener {

    private var seekBarView: SeekBar? = null
    private var userTimeView: TextView? = null
    private var localDate: Date = Date()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userTimeView = this.userTime
        seekBarView = this.seekBar
        seekBarView!!.setOnSeekBarChangeListener(this)

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val progressText: String? = if (progress < 10) {
            "0" + progress.toString()
        } else {
            progress.toString()
        }
        userTimeView?.text = progressText + ":00"

        if (seekBar != null) localDate.hours= seekBar.progress
        if(fromUser) {
            localDate.minutes = 0
        }
        seekBar?.progress = localDate.hours
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }



}
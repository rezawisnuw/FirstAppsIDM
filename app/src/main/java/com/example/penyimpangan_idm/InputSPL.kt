package com.example.penyimpangan_idm

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_input_spl.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Math.abs
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Period
import java.util.*


class InputSPL : AppCompatActivity() {
    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    var jamInDiff: Date? = null
    var jamInDiffBtm: Date? = null
    var jamInHour: Int? = null
    var jamInMin: Int? = null
    var jamOutDIff:Long? = null
    var DateInput: Any? = null
    var selected: Any? = null
    var shift: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_input_spl)
        buttonJamIn.isEnabled = false
        buttonJamOut.isEnabled = false
        progressBar.visibility = View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        TextAreaTugas.hint = "Isi Tugas"

        val nik = intent.getStringExtra("nik")
        getKaryawanToko(nik)
        SpinnerShift()

        textview_date = this.textDate
        button_date = this.btnCalendar

        textDate.text = "--/--/----"

        var cCal = Calendar.getInstance()
        var cYear = (Calendar.YEAR)
        var cMonth = (Calendar.MONTH)
        var cDay = (Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//            cYear
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
//            cMonth
//            cDay
            updateDateInView()
        },cYear,cMonth,cDay)

        dpd.datePicker.setMinDate(System.currentTimeMillis())
        cCal.add(Calendar.DATE,30)
        var cAdd = cCal.timeInMillis
        dpd.datePicker.setMaxDate(cAdd)

        button_date!!.setOnClickListener {
            dpd.show()
        }

        this.buttonJamIn.setOnClickListener{
            val cal = Calendar.getInstance()
            var compareH = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                println("JAM " + cal.get(Calendar.HOUR))
                println("CAL " + cal.time)
                println("COMP " + compareH.time)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val rightNow = sdf.format(cal.time)

                if(DateInput == rightNow){
                    if(cal.time < compareH.time){
                        JamIn!!.text = SimpleDateFormat("HH:mm").format(compareH.time)
                        if(JamIn.text !== "Jam In"){
                            buttonJamOut.isEnabled = true
                            buttonJamIn.isEnabled = false
                            btnCalendar.isEnabled = false
                        }
                        jamInDiffBtm = compareH.time
                        compareH.add(Calendar.HOUR_OF_DAY,8)
                        jamInDiff = compareH.time
                        jamInHour = compareH.get(Calendar.HOUR_OF_DAY)
                        jamInMin = compareH.get(Calendar.MINUTE)
                    } else {
                        JamIn!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                        if(JamIn.text !== "Jam In"){
                            buttonJamOut.isEnabled = true
                            buttonJamIn.isEnabled = false
                            btnCalendar.isEnabled = false
                        }
                        jamInDiffBtm = cal.time
                        cal.add(Calendar.HOUR_OF_DAY,8)
                        jamInDiff = cal.time
                        jamInHour = cal.get(Calendar.HOUR_OF_DAY)
                        jamInMin = cal.get(Calendar.MINUTE)
                    }
                } else {
                    JamIn!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                    if(JamIn.text !== "Jam In"){
                        buttonJamOut.isEnabled = true
                        buttonJamIn.isEnabled = false
                        btnCalendar.isEnabled = false
                    }
                }


            }

            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        this.buttonJamOut.setOnClickListener{
            val cal = Calendar.getInstance()
            val calDiffUpper = Calendar.getInstance()
            val calDiffLower = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                JamOut!!.text = SimpleDateFormat("HH:mm").format(cal.time)
//                if()
//                println("JAM " + jamInDiff)
//                cal.setTime(jamInDiff)
//                println("CAL " + cal)
//                println("XXX")
//                var jamOutHour = cal.get(Calendar.HOUR_OF_DAY)
//                var jamOutMin = cal.get(Calendar.MINUTE)
////                println((jamOutHour!!*60 + jamOutMin!!)-(jamInHour!!*60+ jamInMin!!))
//                if(jamInHour!! > jamOutHour!!){
//                    if(abs((jamInHour!!*60 + jamInMin!!)-(jamOutHour*60 + jamOutMin))>480){
//                        var hour = jamInHour!!+8
//                        if(hour>24){
//                            hour -= 24
//                        }
//                        var format = "$hour:$jamInMin"
//                        JamOut!!.text = format
//                    }
//                } else {
//                    if(abs((jamOutHour*60 + jamOutMin)-(jamInHour!!*60+ jamInMin!!))>480){
//                        var hour = jamInHour!!+8
//                        if(hour>24){
//                            hour -= 24
//                        }
//                        var format = "$hour:$jamInMin"
//                        JamOut!!.text = format
//                    }
//                }
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        buttonSubmit.setOnClickListener {
            SubmitSPL()
        }

    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.time)
        if(textDate.text !== "--/--/----"){
            buttonJamIn.isEnabled = true
        }
        DateInput = sdf.format(cal.time)
    }

    fun getKaryawanToko(nik:String){
        val url = "https://hrindomaret.com/api/getdata/listkaryawantoko"

        val cred = JSONObject()
        cred.put("nik",nik)

        val formbody = cred.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()


        val client = OkHttpClient()

        client.newCall(post).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }

            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()
                println(resp)
                SpinnerKaryawanToko(resp)
            }
        })
    }

    fun SpinnerKaryawanToko(list:String?){
        val spinnerKaryawan =  findViewById<Spinner>(R.id.spinner)

        val listKaryawanArray = arrayListOf<String?>()

        val jsonobject = JSONObject(list)

        var listKaryawan = jsonobject.getJSONArray("data")

        for(i in 0 until listKaryawan.length()){
            val jsonobjectkaryawan =listKaryawan.getJSONObject(i)
            val karyawan = jsonobjectkaryawan.getString("KaryawanToko")

            listKaryawanArray.add(karyawan)
        }

        println("LIST " + list)
        println("LISTKARYAWANARRAY " + listKaryawanArray[0])
        println("LISTKARYAWAN " + listKaryawan)

        val KaryawanAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item,
            listKaryawanArray// Array
        )

        KaryawanAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread {
            spinnerKaryawan.adapter = KaryawanAdapter
        }

        spinnerKaryawan?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("EMPTY")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = listKaryawanArray[position]
                selected = listKaryawanArray[position]
                textView2.text = selectedItem!!.substringAfter('/')
            }
        }

    }

    fun SpinnerShift(){
        val spinnerShift = findViewById<Spinner>(R.id.spinner2)

        val ShiftArrays = arrayListOf<Int>()
        ShiftArrays.add(1)
        ShiftArrays.add(2)
        ShiftArrays.add(3)

        val ShiftAdapter  = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ShiftArrays
        )

        ShiftAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread {
            spinnerShift.adapter = ShiftAdapter
        }

        spinnerShift?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = ShiftArrays[position]
                shift = ShiftArrays[position]
//                println("SHIFT :" + shift)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                print("Not There")
            }
        }
    }

    fun SubmitSPL(){
        buttonSubmit.isEnabled = false
        progressBar.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        val url = "https://hrindomaret.com/api/postdata/newspl"
        val nik = intent.getStringExtra("nik")
        val cred = JSONObject()
        cred.put("nik",nik)
        cred.put("jammasuk", JamIn.text)
        cred.put("jamkeluar", JamOut.text)
        cred.put("tgllembur", textDate.text)
        cred.put("dtkaryawan", selected)
        cred.put("shift", shift)
        cred.put("kettugas", TextAreaTugas.text)

        val formbody = cred.toString().replace("\\","").toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()


        val client = OkHttpClient()

        client.newCall(post).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
                Toast.makeText(this@InputSPL, "SPL Gagal Di Input", Toast.LENGTH_LONG).show()
                finish()
                startActivity(getIntent())
            }

            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()
                println(resp)
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    buttonSubmit.isEnabled = true
                    Toast.makeText(this@InputSPL, "SPL Berhasil Di Input", Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(getIntent())
                }


            }
        })
    }
}

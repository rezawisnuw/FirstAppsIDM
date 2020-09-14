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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.layout_approval_spl.*
import kotlinx.android.synthetic.main.layout_input_spl_tsm.*
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


class InputSPL_TSM : AppCompatActivity() {
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

    var NikAtasan : String? = ""
    var Kategori : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_input_spl_tsm)
        setTitle("Input Surat Perintah Lembur TSM")

        rv_inputspltsm.setHasFixedSize(true)
        rv_inputspltsm.layoutManager = LinearLayoutManager(this)

        btn_jamIn.isEnabled = false
        btn_jamOut.isEnabled = false
        pb_inputsplTSM.visibility = View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        et_keteranganlembur.hint = "Isi Tugas"

        val nik = intent.getStringExtra("nik")
        getAtasanCabang(nik)
//        SpinnerShift()

        textview_date = this.tv_calendar
        button_date = this.btn_calendar

        tv_calendar.text = "--/--/----"

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

        this.btn_jamIn.setOnClickListener{
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
                        tv_jamIn!!.text = SimpleDateFormat("HH:mm").format(compareH.time)
                        if(tv_jamIn.text !== "Jam In"){
                            btn_jamOut.isEnabled = true
                            btn_jamIn.isEnabled = true
                            btn_calendar.isEnabled = true
                        }
                        jamInDiffBtm = compareH.time
                        compareH.add(Calendar.HOUR_OF_DAY,8)
                        jamInDiff = compareH.time
                        jamInHour = compareH.get(Calendar.HOUR_OF_DAY)
                        jamInMin = compareH.get(Calendar.MINUTE)
                    } else {
                        tv_jamIn!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                        if(tv_jamIn.text !== "Jam In"){
                            btn_jamOut.isEnabled = true
                            btn_jamIn.isEnabled = true
                            btn_calendar.isEnabled = true
                        }
                        jamInDiffBtm = cal.time
                        cal.add(Calendar.HOUR_OF_DAY,8)
                        jamInDiff = cal.time
                        jamInHour = cal.get(Calendar.HOUR_OF_DAY)
                        jamInMin = cal.get(Calendar.MINUTE)
                    }
                } else {
                    tv_jamIn!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                    if(tv_jamIn.text !== "Jam In"){
                        btn_jamOut.isEnabled = true
                        btn_jamIn.isEnabled = true
                        btn_calendar.isEnabled = true
                    }
                }


            }

            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        this.btn_jamOut.setOnClickListener{
            val cal = Calendar.getInstance()
            val calDiffUpper = Calendar.getInstance()
            val calDiffLower = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tv_jamOut!!.text = SimpleDateFormat("HH:mm").format(cal.time)
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

        btn_inputsplTSM.setOnClickListener {
            SubmitSPL()
        }

    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.time)
        if(tv_calendar.text !== "--/--/----"){
            btn_jamIn.isEnabled = true
            getKaryawanCabang()
        }
        DateInput = sdf.format(cal.time)
    }

    fun getAtasanCabang(nik:String){
        val url = "https://hrindomaret.com/api/getdata/listatasancabang"

        val cred = JSONObject()
        cred.put("nik","1997000202")

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
                println("inputspltsm"+resp)
                SpinnerAtasanCabang(resp)
            }
        })
    }

    fun SpinnerAtasanCabang(list:String?){
        val spinnerKaryawan =  findViewById<Spinner>(R.id.sp_atasan)

        val listKaryawanArray = arrayListOf<String?>()
        val listKategoriArray = arrayListOf<String?>()

        val jsonobject = JSONObject(list)

        var listData = jsonobject.getJSONArray("data")

        for(i in 0 until listData.length()){
            val jsonobjectdata = listData.getJSONObject(i)
            val karyawan = jsonobjectdata.getString("AtasanCabang")
            val kategori = jsonobjectdata.getString("Kategori")

            listKaryawanArray.add(karyawan)
            listKategoriArray.add(kategori)
        }

        println("LIST " + list)
        println("LISTATASANARRAY " + listKaryawanArray[0])
        println("LISTDATA " + listData)
        println("LISTKATEGORIARRAY " + listKategoriArray[0])


        val KaryawanAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item,
            listKaryawanArray// Array
        )

        KaryawanAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread {
            spinnerKaryawan.adapter = KaryawanAdapter

            when (listKategoriArray[0]) {
                "manager_cabang" -> tv_atasan.text = "Cabang :"
                "supervisor" -> tv_atasan.text = "Atasan :"
                else -> tv_atasan.text = this.toString()
            }

        }

        spinnerKaryawan?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("EMPTY")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val karyawanselected = listKaryawanArray[position]
                val kategoriselected = listKategoriArray[position]

                selected = listKaryawanArray[position]
                //textView2.text = selectedItem!!.substringAfter('/')

                NikAtasan = karyawanselected
                Kategori = kategoriselected
            }
        }

    }

    fun getKaryawanCabang() {
        val url = "https://hrindomaret.com/api/getdata/listkaryawancabang"
        val param = JSONObject()
//        param.put("nikatasan",  NikAtasan)
//        param.put("kategori",  Kategori)
//        param.put("tgllembur",  tv_calendar.text)
        param.put("nikatasan",  "1997000202")
        param.put("kategori",  "supervisor")
        param.put("tgllembur",  "09/06/2020")

        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body?.string()
                println("listkaryawancabang"+body)
//                val Jobject = JSONObject(body)
//                val Jarray: JSONArray = Jobject.getJSONArray("data")
//
//                println("Jarray"+Jarray)
//
//                for (i in 0 until Jarray.length()) {
//                    val datalist = Jarray.getJSONObject(i)
//                    println("datalist"+datalist)
//                }

                val gson = GsonBuilder().create()
                //val databody = gson.toJson(Jarray)
                //val listkarycbg_spltsm: List<ModelListSPL> = gson.fromJson(body,Array<ModelListSPL>::class.java).toList()
                val listkarycbg_spltsm = gson.fromJson(body, Feed::class.java)
                //println("listkarycbg_spltsm " + listkarycbg_spltsm)

                runOnUiThread {
                    pb_inputsplTSM.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    rv_inputspltsm.adapter = RecyclerInputSPL_TSM(listkarycbg_spltsm)
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })

    }

    fun SubmitSPL(){
        btn_inputsplTSM.isEnabled = false
        pb_inputsplTSM.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        val url = "https://hrindomaret.com/api/postdata/newspl"
        val nik = intent.getStringExtra("nik")
        val cred = JSONObject()
        cred.put("nik",nik)
        cred.put("jammasuk", tv_jamIn.text)
        cred.put("jamkeluar", tv_jamOut.text)
        cred.put("tgllembur", tv_calendar.text)
        cred.put("dtkaryawan", selected)
        cred.put("shift", shift)
        cred.put("kettugas", et_keteranganlembur.text)

        val formbody = cred.toString().replace("\\","").toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()


        val client = OkHttpClient()

        client.newCall(post).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
                Toast.makeText(this@InputSPL_TSM, "SPL Gagal Di Input", Toast.LENGTH_LONG).show()
                finish()
                startActivity(getIntent())
            }

            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()
                println(resp)
                runOnUiThread {
                    pb_inputsplTSM.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    btn_inputsplTSM.isEnabled = true
                    Toast.makeText(this@InputSPL_TSM, "SPL Berhasil Di Input", Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(getIntent())
                }


            }
        })
    }

    data class Feed(
        @SerializedName("data") val data: List<ModelListKaryawanCabang>
    )

    data class ModelListKaryawanCabang(
        @SerializedName("KaryawanCabang") val KaryawanCabang : String?,
        @SerializedName("SPLKe") val SPLKe : String?,
        @SerializedName("JamKerja") val JamKerja : String?,
        @SerializedName("TglLembur") val TglLembur : String?
    )
}

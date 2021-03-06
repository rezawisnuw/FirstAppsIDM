package com.example.penyimpangan_idm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.layout_approval_spl.*
import kotlinx.android.synthetic.main.layout_approval_spl.tv_datarelasi
import kotlinx.android.synthetic.main.layout_history_approval_spl.*
import kotlinx.android.synthetic.main.layout_peminjaman_as.*
import kotlinx.android.synthetic.main.layout_rv_approval_spl.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception


class getApprovalType{
    var listApprovalType: String? = ""
}

object objApprovalType {
    private var spl: String? = ""

    fun getObjApprovalType(): String?{
        return spl
    }

    fun setObjApprovalType(setListApprovalType : getApprovalType){
        spl = setListApprovalType.listApprovalType
    }
}

var ApprovalCode : String? = ""
var KategoriParam : String? = ""
var ListApprovalSPLToko : String? = ""

class ApprovalSPL :  AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_approval_spl)
        setTitle("Approval Surat Perintah Lembur")

        rv_approvalspl.setHasFixedSize(true)
        rv_approvalspl.layoutManager = LinearLayoutManager(this)

        pb_listspl.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        getApprovalList()


        btn_approve.setOnClickListener{
//            if(ck_spl.isChecked){
            println("ahaydeahayde "+ ListApprovalSPLToko)
            if(ListApprovalSPLToko == "[]"){
                Toast.makeText(this@ApprovalSPL, "Data SPL Kosong", Toast.LENGTH_LONG).show()
            }else{
                approveSPL()
            }

//                println("getChecked "+getChecked)
//                println("getJson "+getJson)
//                println("getNoSPL "+ getNoDtlSPL)
//            }else{
//                Toast.makeText(this@ApprovalSPL, "Pilih spl terlebih dahulu", Toast.LENGTH_LONG).show()
//            }


        }

        btn_reject.setOnClickListener {
//            if(ck_spl.isChecked){
            if(ListApprovalSPLToko == "[]"){
                Toast.makeText(this@ApprovalSPL, "Data SPL Kosong", Toast.LENGTH_LONG).show()
            }else{
                rejectSPL()
            }
//            }else{
//                Toast.makeText(this@ApprovalSPL, "Pilih spl terlebih dahulu", Toast.LENGTH_LONG).show()
//            }

        }

        btn_historyspl.setOnClickListener {
            historySPL()
        }


    }

    fun historySPL(){
        pb_listspl.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        val url = "https://hrindomaret.com/api/getdata/historyspl"
        val nik = intent.getStringExtra("nik")
        val param = JSONObject()
        param.put("kategori",  KategoriParam)
        param.put("kodekategori",  ApprovalCode)
        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body?.string()
                println("bodyhistoryspl"+body)

                val gson = GsonBuilder().create()
                val listhistoryspl = gson.fromJson(body,FeedHistory::class.java)
                println("bodygsonhistory " + listhistoryspl.data)

                runOnUiThread {
                    if(listhistoryspl.data.toString() == "[]"){
                        Toast.makeText(
                            this@ApprovalSPL,
                            "Pilihan yang terkait tidak ada data history SPL",
                            Toast.LENGTH_SHORT
                        ).show()
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }else{
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        val intent = Intent(this@ApprovalSPL, HistoryApprovalSPL::class.java)
                        intent.putExtra("nik", nik)
                        startActivity(intent)
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })
    }

    fun approveSPL(){
        pb_listspl.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        val url = "https://hrindomaret.com/api/postdata/submitspl"

        //val req = getChecked
        val nospl = getNoDtlSPL
        val dtkaryawan = getDataKaryawan
        val dtrelasi = getDataRelasi
        val kettugas = getRincianTugas
        val tgllembur = getTglLembur
        val jammasuk = getJamMasuk
        val jamkeluar = getJamKeluar
        val ttldurasi = getTotalDurasi
        // val keterangan = getKeterangan
        val status = "Approve"
        val nik = intent.getStringExtra("nik")

        println("woyowoyowyo "+getJson)


        val param = JSONObject()
        //param.put("request", req)
        param.put("nospl", nospl)
        param.put("dtkaryawan", dtkaryawan)
        param.put("dtrelasi", dtrelasi)
        param.put("kettugas", kettugas)
        param.put("tgllembur", tgllembur)
        param.put("jammasuk", jammasuk)
        param.put("jamkeluar", jamkeluar)
        param.put("ttldurasi", ttldurasi)
        param.put("status", status)
        param.put("nik", nik)


        val formbody = param.toString().replace("\"[","[").replace("]\"","]").replace("\\","").toRequestBody()
        println("parasasaa " + param.toString().replace("\"[","[").replace("]\"","]").replace("\\",""))
        val post2 = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Hasil Success")
                val resp = response.body?.string()
                //val respObject = JSONObject(resp)
                //val respArray = respObject.getJSONArray("data")
                println("responseeee "+ setvalckbox)
                if(resp!!.toString().contains("Sukses") && setvalckbox == true){
                //if(respArray.getJSONObject(0).getString("Pesan").toString() == "Sukses Insert Approve" && setvalckbox == true){
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this@ApprovalSPL, "SPL Berhasil Di Approve", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())

                    }
                } else if (setvalckbox == false) {
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        if(ck_spl.isChecked){
//                            Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Approve", Toast.LENGTH_LONG).show()
//                        }else{
                            Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Approve, Pilih spl terlebih dahulu", Toast.LENGTH_LONG).show()
//                        }
                        finish()
                        startActivity(getIntent())
                    }
                }
                else{
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                          Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Approve, Coba Reboot Aplikasi", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })
    }

    fun rejectSPL(){
        pb_listspl.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );


        val url = "https://hrindomaret.com/api/postdata/submitspl"

        val nospl = getNoDtlSPL
        val dtkaryawan = getDataKaryawan
        val dtrelasi = getDataRelasi
        val kettugas = getRincianTugas
        val tgllembur = getTglLembur
        val jammasuk = getJamMasuk
        val jamkeluar = getJamKeluar
        val ttldurasi = getTotalDurasi
        // val keterangan = getKeterangan
        val status = "Reject"
        val nik = intent.getStringExtra("nik")
        val param = JSONObject()

        param.put("nospl", nospl)
        param.put("dtkaryawan", dtkaryawan)
        param.put("dtrelasi", dtrelasi)
        param.put("kettugas", kettugas)
        param.put("tgllembur", tgllembur)
        param.put("jammasuk", jammasuk)
        param.put("jamkeluar", jamkeluar)
        param.put("ttldurasi", ttldurasi)
        param.put("status", status)
        param.put("nik", nik)

        val formbody = param.toString().replace("\"[","[").replace("]\"","]").replace("\\","").toRequestBody()
        val post2 = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Hasil Success")
                val respspl = response.body?.string()
                val respObject = JSONObject(respspl)
                var respArray: Any
                try {
                    respArray = respObject.getJSONArray("data")
                }
                catch (e : Exception){
                    respArray = "null"
                }
                println("response reject"+ respArray)
                //println("response reject sue"+ respArray.getJSONObject(0).getString("Pesan").toString())
                //if(respArray.getJSONObject(0).getString("Pesan").toString() == "Sukses Insert Reject" && setvalckbox == true){
                if(respspl!!.toString().contains("Sukses") && setvalckbox == true){
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this@ApprovalSPL, "SPL Berhasil Di Reject", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())
                    }
                }
                else if(respObject.getJSONArray("data").getJSONObject(0).getString("Pesan").toString() == "null" && setvalckbox == true)  {
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Reject, Cek Data Presensi", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())
                    }
                }
                else if(setvalckbox == false){
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        if(resp!!.toString()!!.contains("Sukses") && (ck_spl.isChecked()==false)){
                        Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Reject, Pilih spl terlebih dahulu", Toast.LENGTH_LONG).show()
//                        }else{
//                            Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Reject,  Cek Data Presensi", Toast.LENGTH_LONG).show()
//                        }
                        finish()
                        startActivity(getIntent())
                    }
                }
                else{
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Reject, Coba Reboot Aplikasi", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })
    }

    //fun setApprovalList(res: getApprovalType){
    fun setApprovalList(){
        val setApprovalType  = arrayListOf<String?>()
        val setKategoriType = arrayListOf<String?>()

        val setApprovalTypeJson = JSONObject(objApprovalType.getObjApprovalType())
        val jsonArray = JSONArray(setApprovalTypeJson.get("data").toString())
        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)

            val approvalType = jsonObject.getString("JenisApproval")
            val kategoriType = jsonObject.getString("Kategori")

            setApprovalType.add(approvalType)
            setKategoriType.add(kategoriType)

        }

        val spinnerApprovalType = findViewById<Spinner?>(R.id.spin_approvaltype)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, setApprovalType)

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinnerApprovalType?.adapter = adapter

        this.runOnUiThread {
            when (setKategoriType.get(0)) {
                "dataas" -> tv_approvaltype.text = "Pilih AS :"
                "datatoko" -> tv_approvaltype.text = "Pilih Toko :"
                "datadivisi" -> tv_approvaltype.text = "Pilih Divisi :"
                "dataSPV" -> tv_approvaltype.text = "Pilih SPV :"
                else -> tv_approvaltype.text = this.toString()
            }
            tv_datarelasi.text = setKategoriType.get(0)
        }

        spinnerApprovalType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                print("Not There")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedApprovalList = setApprovalType[position]
                val selectedKategoriList = setKategoriType[position]
                //println(selectedApprovalList)
                ApprovalCode = selectedApprovalList
                KategoriParam = selectedKategoriList

                //val string = ApprovalCode
                //val code = string?.substring(0, string.indexOf(" - "))
                //println("testdata" + code)

                pb_listspl.visibility = View.VISIBLE
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );

                getListSPL()


            }
        }

    }

    fun getApprovalList() {
        val url = "https://hrindomaret.com/api/getdata/listapproval"
        val param = JSONObject()
        val nik = intent.getStringExtra("nik")
        param.put("nik",  nik)

        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val respnObject = JSONObject(body)
                //respnObject.getJSONArray("data").getJSONObject(0)
                println("bodygetapproval"+respnObject)


                if (respnObject.getJSONArray("data").toString() == "[]"){
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(
                            this@ApprovalSPL,
                            "Data Pilihan SPL yang bersangkutan Kosong",
                            Toast.LENGTH_SHORT).show()
                        finish()
                        val intent = Intent(this@ApprovalSPL, HomeActivity::class.java)
                        intent.putExtra("nik",nik)
                        startActivity(intent)
                    }
                } else{
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        val resApprovalType = getApprovalType()
                        //listApprovalType = body

                        resApprovalType.listApprovalType = body
                        objApprovalType.setObjApprovalType(resApprovalType)

                        //setApprovalList(resApprovalType)
                        setApprovalList()
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Data Tidak Masuk")
            }
        })
    }

    fun getListSPL() {
        val url = "https://hrindomaret.com/api/getdata/listspl"
        val param = JSONObject()
        param.put("kategori",  KategoriParam)
        param.put("kodekategori",  ApprovalCode)
        //param.put("kategori",  "datatoko")
        //param.put("kodekategori",  "b001 - asd")

        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body?.string()
                println("body"+body)
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
                //val listspl: List<ModelListSPL> = gson.fromJson(body,Array<ModelListSPL>::class.java).toList()
                val listspl = gson.fromJson(body,Feed::class.java)
                println("bodygson " + listspl.data)
                ListApprovalSPLToko = listspl.data.toString()

                runOnUiThread {
                    if(listspl.data.toString() == "[]"){
                        Toast.makeText(
                            this@ApprovalSPL,
                            "Pilihan yang terkait tidak ada data approval SPL",
                            Toast.LENGTH_SHORT
                        ).show()
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }else{
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        rv_approvalspl.adapter = RecyclerApprovalSPL(listspl)

                        setvalckbox = false
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })

    }

    data class Feed(
        @SerializedName("data") val data: List<ModelListSPL>
    )

    data class FeedHistory(
        @SerializedName("data") val data: List<ModelListHistory>
    )

    data class ModelListSPL(
        @SerializedName("NoDtlSPL") val NoDtlSPL : String?,
        @SerializedName("DataKaryawan") val DataKaryawan : String?,
        @SerializedName("DataRelasi") val DataRelasi : String?,
        @SerializedName("RincianTugas") val RincianTugas : String?,
        @SerializedName("TglLembur") val TglLembur : String?,
        @SerializedName("JamMasuk") val JamMasuk : String?,
        @SerializedName("JamKeluar") val JamKeluar : String?,
        @SerializedName("TotalDurasi") val TotalDurasi : String?,
        @SerializedName("Keterangan") val Keterangan : String?
    )

    data class ModelListHistory(
        @SerializedName("NoDtlSPL") val NoDtlSPL : String?,
        @SerializedName("DataKaryawan") val DataKaryawan : String?,
        @SerializedName("DataRelasi") val DataRelasi : String?,
        @SerializedName("RincianTugas") val RincianTugas : String?,
        @SerializedName("TglLembur") val TglLembur : String?,
        @SerializedName("JamMasuk") val JamMasuk : String?,
        @SerializedName("JamKeluar") val JamKeluar : String?,
        @SerializedName("TotalDurasi") val TotalDurasi : String?,
        @SerializedName("Keterangan") val Keterangan : String?
    )


}


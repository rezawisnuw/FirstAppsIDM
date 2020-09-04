package com.example.penyimpangan_idm

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.layout_rv_approval_spl.*
import kotlinx.android.synthetic.main.layout_approval_spl.*
import kotlinx.android.synthetic.main.layout_approval_spl.tv_datarelasi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


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
            approveSPL()
            println("getChecked "+getChecked)
            println("getJson "+getJson)
            println("getNoSPL "+ getNoDtlSPL)
        }

        btn_reject.setOnClickListener {
            rejectSPL()
        }

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

        println("woyowoyowyo "+getNoDtlSPL)


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
                println("responseeee "+ resp)
                if(resp!!.toString().contains("Success") && ck_spl.isChecked){
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this@ApprovalSPL, "SPL Berhasil Di Approve", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())

                    }
                } else {
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Approve", Toast.LENGTH_LONG).show()
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
                val resp = response.body?.string()
                println("response reject"+ resp)
                if(resp!!.toString()!!.contains("Sukses")){
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this@ApprovalSPL, "SPL Berhasil Di Reject", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())
                    }
                } else {
                    runOnUiThread {
                        pb_listspl.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(this@ApprovalSPL, "SPL Gagal Di Reject", Toast.LENGTH_LONG).show()
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
        val setApprovalType  = arrayListOf<String>()
        val setKategoriType = arrayListOf<String>()

        val setApprovalTypeJson = JSONObject(objApprovalType.getObjApprovalType())
        val jsonArray = JSONArray(setApprovalTypeJson.get("data").toString())
        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)

            val approvalType = jsonObject.getString("JenisApproval")
            val kategoriType = jsonObject.getString("Kategori")

            setApprovalType.add(approvalType)
            setKategoriType.add(kategoriType)

        }

        val spinnerApprovalType = findViewById<Spinner>(R.id.spin_approvaltype)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, setApprovalType)

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        this.runOnUiThread {
            spinnerApprovalType.adapter = adapter

            when (setKategoriType.get(1)) {
                "dataas" -> tv_approvaltype.text = "Pilih AS :"
                "datatoko" -> tv_approvaltype.text = "Pilih Toko :"
                "datadivisi" -> tv_approvaltype.text = "Pilih Divisi :"
                "dataSPV" -> tv_approvaltype.text = "Pilih SPV :"
                else -> tv_approvaltype.text = this.toString()
            }
            tv_datarelasi.text = setKategoriType.get(1)
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

                val resApprovalType = getApprovalType()
                //listApprovalType = body

                resApprovalType.listApprovalType = body
                objApprovalType.setObjApprovalType(resApprovalType)
                //setApprovalList(resApprovalType)
                setApprovalList()

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
                //println("asdasd " + listspl)

                runOnUiThread {
                    pb_listspl.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    rv_approvalspl.adapter = RecyclerApprovalSPL(listspl)
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


}


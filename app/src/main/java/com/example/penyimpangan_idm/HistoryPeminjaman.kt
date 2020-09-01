package com.example.penyimpangan_idm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.layout_approval_spl.*
import kotlinx.android.synthetic.main.layout_history_peminjaman.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class HistoryPeminjaman : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_history_peminjaman)
        setTitle("History Peminjaman")

        rv_historypeminjaman.setHasFixedSize(true)
        rv_historypeminjaman.layoutManager = LinearLayoutManager(this)


        getHistory()

        btn_deletehistory.setOnClickListener {
            deleteHistory()
        }
    }

    fun getHistory() {
        val url = "https://hrindomaret.com/api/getpinjam/history"
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
                println(body)

                val gson = GsonBuilder().create()
                val feed: List<ModelHistoryPeminjaman> = gson.fromJson(body,Array<ModelHistoryPeminjaman>::class.java).toList()

                runOnUiThread {
                    rv_historypeminjaman.adapter = RecyclerHistoryPeminjaman(feed)
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })
    }

    fun deleteHistory() {
        val url = "https://hrindomaret.com/api/postpinjam/delete"

        val DataKaryawan = getDataKaryawanHistoryPeminjaman
        val TokoAsal = getTokoAsalHistoryPeminjaman
        val TokoTujuan = getTokoTujuanHistoryPeminjaman
        val TglMulaiPinjam = getTglMulaiDipinjamHistoryPeminjaman
        val TglSelesaiPinjam = getTglSelesaiDipinjamHistoryPeminjaman
        val TglInput = getTglMeminjamHistoryPeminjaman

        val Nik = intent.getStringExtra("nik")
        val Param = JSONObject()

        Param.put("dtkaryawan", DataKaryawan)
        Param.put("tkasal", TokoAsal)
        Param.put("tktujuan", TokoTujuan)
        Param.put("tglmulaipinjam", TglMulaiPinjam)
        Param.put("tglselesaipinjam", TglSelesaiPinjam)
        Param.put("tglinput", TglInput)
        Param.put("nik", Nik)

        val formbody = Param.toString().toRequestBody()
        val post2 = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Hasil Success")
                val resp = response.body?.string()
                println("responsehistory"+ resp)
                if(resp!!.toString()!!.contains("Sukses")){
                    runOnUiThread {
                        Toast.makeText(this@HistoryPeminjaman, "SPL Berhasil Di Reject", Toast.LENGTH_LONG).show()
                        finish()
                        startActivity(getIntent())
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@HistoryPeminjaman, "SPL Gagal Di Reject", Toast.LENGTH_LONG).show()
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

//    data class Feed(
//        @SerializedName("ModelHistoryPeminjaman") val feed: List<ModelHistoryPeminjaman>
//    )


    data class ModelHistoryPeminjaman(
        @SerializedName("DataKaryawan") val DataKaryawan : String?,
        @SerializedName("TokoAsal") val TokoAsal : String?,
        @SerializedName("TokoTujuan") val TokoTujuan : String?,
        @SerializedName("TglMulaiDipinjam") val TglMulaiDipinjam : String?,
        @SerializedName("TglSelesaiDipinjam") val TglSelesaiDipinjam : String?,
        @SerializedName("TglMeminjam") val TglMeminjam : String?
    )
}
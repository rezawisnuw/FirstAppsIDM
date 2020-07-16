package com.example.penyimpangan_idm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
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

        rv_historypeminjaman.setHasFixedSize(true)
        rv_historypeminjaman.layoutManager = LinearLayoutManager(this)


        getHistory()
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

//    data class Feed(
//        @SerializedName("ModelHistoryPeminjaman") val feed: List<ModelHistoryPeminjaman>
//    )


    data class ModelHistoryPeminjaman(
        @SerializedName("DataKaryawan") val DataKaryawan : String?,
        @SerializedName("TokoAsal") val TokoAsal : String?,
        @SerializedName("TokoTujuan") val TokoTujuan : String?,
        @SerializedName("TglMulaiDipinjam") val TglMulaiDipinjam : String?,
        @SerializedName("TglSelesaiDipinjam") val TglSelesaiDipinjam : String?
    )
}
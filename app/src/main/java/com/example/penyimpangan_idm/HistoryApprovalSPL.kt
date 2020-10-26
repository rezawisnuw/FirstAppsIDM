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
import kotlinx.android.synthetic.main.layout_history_approval_spl.*
import kotlinx.android.synthetic.main.layout_history_peminjaman.*
import kotlinx.android.synthetic.main.layout_rv_history_peminjaman.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class HistoryApprovalSPL : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_history_approval_spl)
        setTitle("History Approval Surat Perintah Lembur")

        rv_historyapprovalspl.setHasFixedSize(true)
        rv_historyapprovalspl.layoutManager = LinearLayoutManager(this)

        getHistorySPLToko()

    }

    fun getHistorySPLToko(){
        val url = "https://hrindomaret.com/api/getdata/historyspl"

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
                println("bodyhistoryspltoko"+body)

                val gson = GsonBuilder().create()
                val listhistoryspl = gson.fromJson(body, HistoryApprovalSPL.FeedHistoryApprovalSPL::class.java)
                println("bodygsonhistoryspltoko " + listhistoryspl.data)

                runOnUiThread {
                   rv_historyapprovalspl.adapter = RecyclerHistoryApprovalSPL(listhistoryspl)

                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Hasil Error")
            }
        })
    }

    data class FeedHistoryApprovalSPL(
        @SerializedName("data") val data: List<HistoryApprovalSPL.ModelListHistoryApprovalSPL>
    )

    data class ModelListHistoryApprovalSPL(
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
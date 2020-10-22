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



    }
}
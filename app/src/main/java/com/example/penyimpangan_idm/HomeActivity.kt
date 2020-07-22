package com.example.penyimpangan_idm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.home.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

var ASAM:String? = ""
var JabatanToko:String? = ""

class HomeActivity : AppCompatActivity() {

    var progressBar:ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setTitle("Home")
        progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        runOnUiThread {
            progressBar!!.visibility = View.GONE
        }

        val nik = intent.getStringExtra("nik")

        isASAM(nik)
        isJabatanToko(nik)

        imageButton.setOnClickListener{
            runOnUiThread {
                progressBar!!.visibility = View.VISIBLE
            }
            println("btn1")
            if(ASAM!!.contains("AS")){
//                println("A")
                val intent = Intent(this@HomeActivity, ApprovalpenyimpanganActivity::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)
            } else if(ASAM!!.contains("AM")){
                val intent = Intent(this@HomeActivity, ApprovalPenyimpanganAM::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)
            } else{
                runOnUiThread {
                    Toast.makeText(
                        baseContext,
                        "Anda bukan AS / AM",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
//            if(JSONObject(ASAM).has("AS")){
//                val intent = Intent(this@HomeActivity, ApprovalpenyimpanganActivity::class.java)
//                intent.putExtra("nik",nik)
//                startActivity(intent)
//            }
        }

        imageButton2.setOnClickListener{
            println("btn2")
        }

        imageButton3.setOnClickListener{
            if(ASAM!!.contains("AS")){
                val intent = Intent(this@HomeActivity, Peminjaman_AS::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)
            } else if(ASAM!!.contains("AM")){
                val intent = Intent(this@HomeActivity, Peminjaman_AM::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)
            } else{
                runOnUiThread {
                    Toast.makeText(
                        baseContext,
                        "Anda bukan AS / AM",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        imageButton4.setOnClickListener{
            if(JabatanToko!!.contains("Kepala Toko (Ss)")
                || JabatanToko!!.contains("Kepala Toko")
                || JabatanToko!!.contains("Asisten Kepala Toko (Ss)")
                || JabatanToko!!.contains("Asisten Kepala Toko")
                || JabatanToko!!.contains("Merchandiser (Ss)")
                || JabatanToko!!.contains("Merchandiser")
            ){
                val intent = Intent(this@HomeActivity, InputSPL::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)

            } else{
                val intent = Intent(this@HomeActivity, ApprovalSPL::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        runOnUiThread {
            progressBar!!.visibility = View.GONE
        }
    }

    fun isASAM(nik:String){
        val url = "https://hrindomaret.com/api/isASAM"

        val cred = JSONObject()
            cred.put("EMPLID",nik)

        val formbody2 = cred.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post2 = Request.Builder()
            .url(url)
            .post(formbody2)
            .build()

        val client = OkHttpClient()

        var x = client.newCall(post2).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()
                ASAM = resp.toString()
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun isJabatanToko(nik:String){
        val url = "https://hrindomaret.com/api/getdata/jabatantokospl"

        val cred = JSONObject()
        cred.put("nik",nik)

        val formbody = cred.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()

                val jsonObject = JSONObject(resp).getString("data")
                val jsonArray = JSONArray(jsonObject)
                val dataJabatan = jsonArray.getJSONObject(0).get("JabatanToko")

                JabatanToko = dataJabatan.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}

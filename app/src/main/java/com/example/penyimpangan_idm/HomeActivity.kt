package com.example.penyimpangan_idm

import android.app.AlertDialog
import android.content.DialogInterface
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
var JabatanCabang:String? = ""

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
        isJabatanCabang(nik)

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

                    progressBar!!.visibility = View.GONE
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
            if(JabatanToko!!.contains("N5")
                || JabatanToko!!.contains("N5K")
                || JabatanToko!!.contains("N6")
                || JabatanToko!!.contains("N6K")
                || JabatanToko!!.contains("N7")
                || JabatanToko!!.contains("N7K")
                || JabatanToko!!.contains("HA")
                || JabatanToko!!.contains("HAK")
                || JabatanToko!!.contains("HF")
                || JabatanToko!!.contains("HFK")
                || JabatanToko!!.contains("NH")
                || JabatanToko!!.contains("NHK")
            ){
                val intent = Intent(this@HomeActivity, InputSPL::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)

            }
            else if(ASAM!!.contains("AS") || ASAM!!.contains("AM")){
                val intent = Intent(this@HomeActivity, ApprovalSPL::class.java)
                intent.putExtra("nik",nik)
                startActivity(intent)
            }
            else if(JabatanCabang!!.contains("manager_cabang")
                || JabatanCabang!!.contains("supervisor")
                || JabatanCabang!!.contains("hrapproval")
            ){
                val dialogmenu = AlertDialog.Builder(this@HomeActivity)
                dialogmenu.setTitle("Pilih Menu")
                dialogmenu.setMessage("Silahkan Pilih Menu Selanjtnya, Input SPL atau Approval SPL")
                dialogmenu.setPositiveButton("Input SPL", DialogInterface.OnClickListener{ dialog, which ->
                    val intent = Intent(this@HomeActivity, InputSPL_TSM::class.java)
                    intent.putExtra("nik",nik)
                    startActivity(intent)
                })
                dialogmenu.setNegativeButton("Approval SPL", DialogInterface.OnClickListener{ dialog, which ->
                    val intent = Intent(this@HomeActivity, ApprovalSPL::class.java)
                    intent.putExtra("nik",nik)
                    startActivity(intent)
                })

            }
            else {
                runOnUiThread {
                    Toast.makeText(
                        baseContext,
                        "Anda Tidak Punya Akses",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
        //cred.put("nik","2015020104")

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

    fun isJabatanCabang(nik:String){
        val url = "https://hrindomaret.com/api/getdata/jabatancabangspl"

        val cred = JSONObject()
        //cred.put("nik","1997000202")

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
                val dataJabatan = jsonArray.getJSONObject(0).get("JabatanCabang")

                JabatanCabang = dataJabatan.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}

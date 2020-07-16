package com.example.penyimpangan_idm

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.approvalpenyimpangan.*


class getListAM{
    open var listToko: String? = "ASD"
}

object ListTokoAM{
    private var toko: String? = ""
    fun getToko(): String? {
        return toko
    }

    fun setToko(DaftarToko:getList){
        toko = DaftarToko.listToko
    }
}

var globalAM: String? = "global"
var selectedSpinnerAM: String? = ""
var bawahanAM: String? = ""
var listTokoAM: JSONArray? = null
class ApprovalPenyimpanganAM : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval_penyimpangan_am)
        setTitle("Approval Penyimpangan")
        val nik = intent.getStringExtra("nik")
        println(nik)

        mainAPICall()

        recyclerview.layoutManager = LinearLayoutManager(this)


        button.setOnClickListener{
            acceptPenyimpangan()
        }

        button2.setOnClickListener{
            rejectPenyimpangan()
        }
    }

    fun acceptPenyimpangan(){

        println(jsonChecked)
        val url = "https://hrindomaret.com/api/acceptPenyimpanganKotlin"

        val req = checked

        val param = JSONObject()
        param.put("request", req)

        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val formbod = jsonChecked.toString().toRequestBody()
        val post2 = Request.Builder()
            .url(url)
            .post(formbod)
            .build()

        val client = OkHttpClient()

        client.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful){
                val resp = response.body?.string()
                println("SUKSES")
                println(resp)
                runOnUiThread {
                    getListPenyimpangan()
                }
                if(resp?.toLowerCase()!!.contains("success")){
                    runOnUiThread {
                        Toast.makeText(this@ApprovalPenyimpanganAM, "Penyimpangan Berhasil Di Approve", Toast.LENGTH_LONG).show()
                        mainAPICall()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ApprovalPenyimpanganAM, "Something is Wrong, Please Reboot the App", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }
        })
    }

    fun rejectPenyimpangan(){
        println(jsonChecked)
        val url = "https://hrindomaret.com/api/rejectPenyimpanganKotlin"

        val req = checked

        val param = JSONObject()
        param.put("request", req)

        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val formbod = jsonChecked.toString().toRequestBody()
        val post2 = Request.Builder()
            .url(url)
            .post(formbod)
            .build()

        val client = OkHttpClient()

        client.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful){
                val resp = response.body?.string()
                println("SUKSES")
                println(resp)
                runOnUiThread {
                    getListPenyimpangan()
                }
                if(resp?.toLowerCase()!!.contains("success")){
                    runOnUiThread {
                        Toast.makeText(this@ApprovalPenyimpanganAM, "Penyimpangan Berhasil Di Reject", Toast.LENGTH_LONG).show()
                        mainAPICall()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ApprovalPenyimpanganAM, "Something is Wrong, Please Reboot the App", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }
        })
    }

    fun mainAPICall() = runBlocking{
        //        delay(5000)
        var z = async{getBawahanAM()}
        var y = async{getListPenyimpangan()}
        delay(5000)
        var x = async{getSpinnerList()}

    }

    fun test(res:getList){
        var listAS = arrayListOf<String?>()

        var listASJson = JSONArray(bawahanAM)

        for(i in 0 until listASJson.length()){
            val jsonObject2 = listASJson.getJSONObject(i)
            val bawahan = jsonObject2.getString("NIK_AS")
            listAS.add(bawahan)
        }
        val spinnerbawahan = findViewById<Spinner>(R.id.spinner)
        val bawahanAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listAS
        )

        bawahanAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread {
            spinnerbawahan.adapter = bawahanAdapter
        }

        spinnerbawahan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = listAS[position]?.substring(0,10)
                println("SELECT")
                println(selectedItem)
                tokoPenyimpanganAM(selectedItem)
//                delay(2000L)

            }
        }
//        var listToko = arrayListOf<String?>()
//
//        val listTokoJson = JSONArray(ListToko.getToko())
//
//        for(i in 0 until listTokoJson.length()){
//            val jsonObject1 = listTokoJson.getJSONObject(i)
//
//            val toko = jsonObject1.getString("kodebagian")
//
//            listToko.add(toko)
//        }
//        val spinner = findViewById<Spinner>(R.id.spinner)
//        // Initializing an ArrayAdapter
//        val occupationAdapter = ArrayAdapter(
//            this, // Context
//            android.R.layout.simple_spinner_item,
//            listToko// Array
//        )
//
//        occupationAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//        this.runOnUiThread {
//            spinner.adapter = occupationAdapter
//        }
//
////        spinner.onItemSelectedListener
//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                println("Nothing Selected")
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedString = listToko[position]
//                println("POSITION")
//                println(selectedString)
////                println(global)
////                println(global.toString())
//                var filter:MutableList<Any?> = ArrayList()
//                val gson = GsonBuilder().create()
//                val feed: List<Penyimpangan> = gson.fromJson(global.toString(),Array<Penyimpangan>::class.java).toList()
//                for(list in feed){
//                    if(list.kodebagian == selectedString){
//                        filter.add(list)
//                    }
//                }
//                val reFeed = gson.toJson(filter)
//                val rereFeed = gson.fromJson(reFeed,Array<Penyimpangan>::class.java).toList()
////                val reFeed: List<Penyimpangan> = gson.fromJson(filter,Array<Penyimpangan>::class.java).toList()
//                recyclerview.adapter = RecyclerViewAdapterAM(rereFeed)
//                println("FILTERED")
//                println(filter)
//                println("GLOBAL")
//                println(global)
//                selectedSpinner = listToko[position]
//            }
//
//        }

    }

    fun spinnerAM(){
        var listTokoAS = arrayListOf<String?>()

        var listTokoASJson = (listTokoAM)
//        println("TEST123")
//        println(listTokoASJson?.length())
//        println(listTokoASJson.getJSONObject(0))

        for(i in 0 until listTokoASJson!!.length()){
            val jsonObject2 = listTokoASJson?.getJSONObject(i)
            val bawahan = jsonObject2.getString("kodebagian")
            listTokoAS.add(bawahan)
        }
        val spinnertoko = findViewById<Spinner>(R.id.spinner3)
        val tokoAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listTokoAS
        )

        tokoAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread {
            spinnertoko.adapter = tokoAdapter
        }

        spinnertoko.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = listTokoAS[position]?.substring(0,4)
                println("TOKO")
                println(selectedItem)
                var filter:MutableList<Any?> = ArrayList()
                val gson = GsonBuilder().create()
                val feed: List<Penyimpangan> = gson.fromJson(global.toString(),Array<Penyimpangan>::class.java).toList()
                for(list in feed){
                    if(list.kodebagian == selectedItem){
                        filter.add(list)
                    }
                }
                val reFeed = gson.toJson(filter)
                val rereFeed = gson.fromJson(reFeed,Array<Penyimpangan>::class.java).toList()
//                val reFeed: List<Penyimpangan> = gson.fromJson(filter,Array<Penyimpangan>::class.java).toList()
                recyclerview.adapter = RecyclerViewAdapterAM(rereFeed)
            }
        }
    }

    fun getListPenyimpangan() {
        val url2 = "https://hrindomaret.com/api/getListPenyimpanganKotlin"

        val nik2 = intent.getStringExtra("nik")

        val param2 = JSONObject()
        param2.put("EMPLID",nik2)

        val formbody2 = param2.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post2 = Request.Builder()
            .url(url2)
            .post(formbody2)
            .build()

        val client2 = OkHttpClient()

        val dialogBuilder  = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Tidak ada bawahan yang melakukan penyimpangan presensi")
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener{
                    dialog, which ->  finish()
            })

        val alert = dialogBuilder.create()

        var abc2 = client2.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful){
                val resp = response.body?.string()
                val gson = GsonBuilder().create()
                println("RESP)")
                println(resp)
                val feed: List<Penyimpangan> = gson.fromJson(resp,Array<Penyimpangan>::class.java).toList()
                global = resp
//                println("LISTOKO")
//                println(global)
//                println("LENGTH")
//                println(feed)
//                println("BLANK")
//                println(feed.isEmpty())
                runOnUiThread{
                    if(feed.isEmpty()){
//                        alert.show()
                    }
//                    recyclerview.adapter = RecyclerViewAdapter(feed)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }
        })
    }

    suspend fun getBawahanAM(){
        val url2 = "https://hrindomaret.com/api/getBawahanAMTest"

        val nik2 = intent.getStringExtra("nik")

        val param2 = JSONObject()
        param2.put("EMPLID",nik2)

        val formbody2 = param2.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post2 = Request.Builder()
            .url(url2)
            .post(formbody2)
            .build()

        val client2 = OkHttpClient()

        val dialogBuilder  = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Tidak ada bawahan yang melakukan penyimpangan presensi")
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener{
                    dialog, which ->  finish()
            })

        val alert = dialogBuilder.create()

        var abc = client2.newCall(post2).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val resp = response.body?.string()
                    bawahanAM = resp
                    println("BAWAHAN")
                    println(resp)

                    runOnUiThread{
                        if(bawahanAM!!.isEmpty()){
                            alert.show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }
        })

//        delay(1000L)


    }

    fun tokoPenyimpanganAM(selectedItem: String?) {
        var url3 = "https://hrindomaret.com/api/getTokoPenyimpanganAMTest"

        val gson = GsonBuilder().create()
        val test = gson.toJson(bawahanAM)

        var createdJSON = JSONObject().put("EMPLID",selectedItem?.substring(0,10).toString())

        var param3 = JSONArray(bawahanAM)
//        param3.put("AS", bawahanAM)


        val formbody3 = createdJSON.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val formbod = param3.toString().toRequestBody()
        val post3 = Request.Builder()
            .url(url3)
            .post(formbody3)
            .build()

        val client3 = OkHttpClient()


        var abcd = client3.newCall(post3).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val resp = response.body?.string()
//                    println(bawahanAM)
//                    println(test)
                    println("TOKOBAWAHAN")
                    println(resp)
//                    println(JSONArray(resp))
                    listTokoAM = JSONArray(resp)
                    spinnerAM()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }
        })
    }

    suspend fun getSpinnerList() = runBlocking{


        val url = "https://hrindomaret.com/api/getTokoPenyimpanganKotlinAMTest"

        val nik = intent.getStringExtra("nik")

        val param = JSONObject()
        param.put("EMPLID",nik)

        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()



        var abc = client.newCall(post).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val resp = response.body?.string()
                    val res = getList()
                    println("BEFORE")
                    println(resp)
                    res.listToko = resp
                    ListToko.setToko(res)
                    test(res)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("GAGAL")
            }
        })
    }

    data class Feed(@SerializedName("Penyimpangan") val feed: List<Penyimpangan>)

    data class Penyimpangan(
        @SerializedName("kodenik") val kodenik : String,
        @SerializedName("namakaryawan") val namakaryawan : String,
        @SerializedName("kodebagian") val kodebagian : String,
        @SerializedName("tglupdate") val tglupdate : String,
        @SerializedName("tglabsen") val tglabsen : String,
        @SerializedName("jamin") val jamin : String,
        @SerializedName("jamout") val jamout : String
    )

}

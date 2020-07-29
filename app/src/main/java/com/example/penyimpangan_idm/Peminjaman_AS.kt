package com.example.penyimpangan_idm

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.layout_peminjaman_as.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class getList3 {
    var listToko: String? = "POS"
}

object ListToko3 {

    private var toko: String? = ""
    fun getToko(): String? {
        return toko
    }

    fun setToko(DaftarToko: getList3) {
        toko = DaftarToko.listToko
    }

}
class getList1{
    var listNama :String? ="AWS"
}

object ListNama {
    private var nama: String? = ""
    fun getNama(): String? {
        return nama
    }

    fun setNama(DaftarNama: getList1) {
        nama = DaftarNama.listNama
    }
}
class getList2{
    var listTokotj : String? = "ASW"
}
object ListTokotj{
    private var tokotj: String? =""
    fun getTokotj(): String? {
        return tokotj
    }
    fun setTokotj(DaftarToko2: getList2){
        tokotj = DaftarToko2.listTokotj
    }
}
class postList{
    var listPeminjaman : String? ="LOL"
}
object ListPost{
    private var peminjaman:String?=""
    fun getPost():String?{
        return peminjaman
    }
    fun setPost(DaftarPost:postList){
        peminjaman = DaftarPost.listPeminjaman
    }
}

var TkAsl: String? = ""
var Nikkaryawan: String? = ""
var Tktujuan : String? = ""
var TglAkhir : Int = 0
var TglAwal : Int = 0
var JmlHari : String? = ""
var PostData : String?=""
var TglAkhirminsatu : Int = 0
var Hari4 = arrayOf("")

var getInpTokoTujuanbyAS: String? = ""
var getInpTglMulaiPinjambyAS: String? = ""
var getInpTglSelesaiPinjambyAS: String? = ""

class Peminjaman_AS : AppCompatActivity() {
    var button: Button? = null
    var button3 : Button? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_peminjaman_as)

        //mRecyclerView.layoutManager = LinearLayoutManager(this)

        button = this.button2
        button3 = this.button1

        button!!.text = "Select Date"

        pb_peminjamanAS.visibility = View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
                date()
                //println(date())
                mainApiCall()

            }

        }

        button!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                val dialog = DatePickerDialog(
                    this@Peminjaman_AS, dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                    //cal.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

                )

                val c = Calendar.getInstance()
                c.add(Calendar.DATE, +1) // subtract 2 years from now

                dialog.getDatePicker().setMinDate(c.timeInMillis)
//                c.add(Calendar.DATE, 4) // add 4 years to min date to have 2 years after now
//
//                dialog.getDatePicker().setMaxDate(c.timeInMillis)

//                dialog.datePicker.minDate = Calendar.getInstance().timeInMillis
                dialog.show()
                pb_peminjamanAS.visibility = View.VISIBLE
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );

                //dialog.datePicker.maxDate =
                //Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH).toLong()


                //if (selecte)
                // button!!.text as String?

            }
        })
        button3!!.setOnClickListener(object : View.OnClickListener {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onClick(v: View) {
                pb_peminjamanAS.visibility = View.VISIBLE
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );

                println(Nikkaryawan)
                println(TkAsl)
                println(Tktujuan)
                println(JmlHari)
                println(button!!.text as String?)
                //val kodetk_asl = intent.putExtra("kodetk_asl", TkAsl)
                //val nik_kary =intent.putExtra("nik_kary", Nikkaryawan)
                // val kodetk_tjn =intent.putExtra("kodetk_tjn", Tktujuan)
                // val jml_hari = intent.putExtra("jml_hari", JmlHari)
                // val tgl_pjm = intent.putExtra("tgl_pjm",  button!!.text as String?)
                val url3 = "https://hrindomaret.com/api/postpinjam/submit"
                val nik = intent.getStringExtra("nik")
                val param3 = JSONObject()
                param3.put("nik_kary", Nikkaryawan)
                param3.put("kodetk_asl", TkAsl)
                param3.put("kodetk_tjn", Tktujuan)
                param3.put("jml_hari", JmlHari)
                param3.put("tgl_pjm", button!!.text as String?)
                param3.put("nik_inp", nik)
                val formbody2 = param3.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val post2 = Request.Builder().url(url3).post(formbody2).build()
                val client2 = OkHttpClient()
                client2.newCall(post2).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Maaf Data Tidak Bisa Di Post")

                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        val respn = postList()
                        println("indrabego"+body)
                        PostData = body
                        respn.listPeminjaman = body
                        ListPost.setPost(respn)
                        if (body!!.toString().contains("Sukses")) {
                            runOnUiThread {
                                pb_peminjamanAS.visibility = View.GONE
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                val dialog1 = AlertDialog.Builder(this@Peminjaman_AS)
                                dialog1.setTitle("Submit Berhasil!")
                                dialog1.setMessage("Data Telah Tersubmit")
                                dialog1.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@Peminjaman_AS,"Data Tersubmit",Toast.LENGTH_SHORT).show()
                                    finish()
                                    startActivity(getIntent())
                                })
                                dialog1.show()
                                println(body)
                            }

                        } else if (body!!.toString().contains("Gagal")) {
                            runOnUiThread {
                                messageDialog(nik, Nikkaryawan, "alert")
                                pb_peminjamanAS.visibility = View.GONE
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                val dialog2 = AlertDialog.Builder(this@Peminjaman_AS)
                                dialog2.setTitle("Submit Gagal!")
                                if(getInpTokoTujuanbyAS == ""){
                                    dialog2.setMessage(
                                        "Karyawan yang dipinjam sedang dalam kondisi OFF pada tanggal selanjutnya"
                                    )
                                }
                                else{
                                    dialog2.setMessage(
                                        "Sudah ada data peminjaman di toko $getInpTokoTujuanbyAS pada tanggal $getInpTglMulaiPinjambyAS hingga tanggal $getInpTglSelesaiPinjambyAS"
                                    )
                                }
                                dialog2.setNegativeButton("Kembali", DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(
                                        this@Peminjaman_AS,
                                        "Gagal Tersubmit",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                    startActivity(getIntent())
                                })
                                dialog2.show()
                                println(body)
                            }
                        } else {
                            runOnUiThread {
                                pb_peminjamanAS.visibility = View.GONE
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                val dialog3 = AlertDialog.Builder(this@Peminjaman_AS)
                                dialog3.setTitle("Submit Gagal!")
                                dialog3.setMessage("Data Tidak Lengkap")
                                dialog3.setNegativeButton("Kembali", DialogInterface.OnClickListener{ dialog, which ->
                                    Toast.makeText(
                                        this@Peminjaman_AS,
                                        "Gagal Tersubmit",
                                        Toast.LENGTH_SHORT).show()
//                                    finish()
//                                    startActivity(getIntent())
                                })
                                dialog3.show()
                                println(body)
                            }
                        }
                    }


                })

                //val dialog1 = AlertDialog.Builder(this@MainActivity)
                //dialog1.setMessage("Data Telah Tersubmit!")
                // dialog1.setOnDismissListener(object : DialogInterface.OnDismissListener{
                //    override fun onDismiss(dialog: DialogInterface?) {

                //     }

                // })


                // alert.window!!.attributes.windowAnimations = R.style.MaterialAlertDialog_MaterialComponents_Title_Text_CenterStacked


                //val dialog2 = AlertDialog.Builder(this@MainActivity)
                //  dialog2.setMessage("Maaf Anda Telah Mencapai Batas Maksimal")
                // dialog2.setOnDismissListener(object : DialogInterface.OnDismissListener {
                //    override fun onDismiss(dialog: DialogInterface?) {
                //        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                //    }

                // })


            }
        })

        // fetchJson()

    }
    fun date(){

        // val dateFormat = getDateInstance()
        //val convertedDate = dateFormat.parse(button!!.text as String)



        // val calendar = Calendar.getInstance()
        //calendar.time = convertedDate

        // calendar.get(Calendar.DAY_OF_MONTH)

        // val lastDayOfMonth = calendar.time
        // println(lastDayOfMonth)


        val tglawalstr = button!!.text as String
        TglAwal = tglawalstr.take(2).toInt()
        println(TglAwal)

        val date = button!!.text as String
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val convertedDate = dateFormat.parse(date)
        val c = Calendar.getInstance()
        c.time = convertedDate
        c[Calendar.DAY_OF_MONTH] = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        //println(c[Calendar.DAY_OF_MONTH])

        TglAkhir = c[Calendar.DAY_OF_MONTH]

        TglAkhirminsatu = TglAkhir -1

        println(TglAkhir)
        println(TglAkhirminsatu)


        if (TglAwal == TglAkhir){
            Hari4 = arrayOf("1")
        }
        else if (TglAwal == TglAkhirminsatu){
            Hari4 = arrayOf("1","2")
        }
        else{ Hari4 = arrayOf("1","2","3")}

        val hari = Hari4


        jmlhari.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hari)
        jmlhari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val jmlhari1 = hari[position]
                JmlHari = jmlhari1
                println(JmlHari)


            }

        }
    }



    fun mainApiCall() = runBlocking {
        //        delay(5000)

        // var y = async { getNama() }
        //  var y = async { main(args = ListToko) }

        var x = async { fetchJson() }
        //delay(5000)
        //var y =async {main()  }

    }
    fun test1(respon : getList2){
        val listToko2 = arrayListOf<String>()
        val listTokoJson2 = JSONArray(ListTokotj.getTokotj())
        for (o in 0 until listTokoJson2.length()) {
            val jsonObject2: JSONObject = listTokoJson2.getJSONObject(o)
            val tokotj = jsonObject2.getString("toko")
            listToko2.add(tokotj)
        }
        val spinner2 = findViewById<Spinner>(R.id.tktujuan)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listToko2)
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread {
            spinner2.adapter = adapter2
        }
        spinner2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedList12 =listToko2[position]
                println(selectedList12)
                Tktujuan = selectedList12.take(4)
            }

        }
    }

    fun test(res: getList3) {
        val listToko = arrayListOf<String>()

        val listTokoJson = JSONArray(ListToko3.getToko())



        for (p in 0 until listTokoJson.length()) {
            val jsonObject1: JSONObject = listTokoJson.getJSONObject(p)

            val toko = jsonObject1.getString("toko")


            listToko.add(toko)

        }
        val spinner = findViewById<Spinner>(R.id.Tokoasal)

        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listToko)

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        this.runOnUiThread {
            spinner.adapter = adapter

            // spinner3.adapter = adapter
            //spinner3.adapter = adapter2
        }
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                print("not there")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedList = listToko[position]
                println(selectedList)

                //println(selectedList.take(4))
                TkAsl = selectedList.take(4)
                //println(TkAsl)
                val kodeTk = TkAsl

                if (kodeTk == TkAsl) {
                    //TkAsl.setSelection[position]
                    main()
                    //  spinner.setSelection(value)
                }


                //spinner2.setSelection(position)
                //val posision1 = value
                // tempat = position
                //val filter: MutableList<Any?> = ArrayList()
                //val gson = GsonBuilder().create()
                //val feed: List<ListToko1> =
                //    gson.fromJson(global.toString(), Array<ListToko1>::class.java).toList()
                //for (list in feed) {
                //    if (list.toko == selectedList) {
                //        filter.add(list)
                //    }
                //}
                //val reFeed = gson.toJson(filter)
                //  val rereFeed = gson.fromJson(reFeed, Array<ListToko1>::class.java).toList()
                //     val reFeed : List<ListToko1> = gson.fromJson(filter,Array<ListToko1>::class.java).toList()
                //runOnUiThread {
                //    mRecyclerView.adapter =  RecyclerAdapter(rereFeed)
                // }
                //println("FILTERED")
                //println(filter)
                //println("GLOBAL")
                //println(global)
                //selectedSpinner = listToko[position]
                // println(selectedSpinner)
            }


        }


    }


    fun getNama1(reps : getList1) {
        val listKaryawan = arrayListOf<String>()
        val listKaryawanJson = JSONArray(ListNama.getNama())

        for (l in 0 until listKaryawanJson.length()) {
            val jsonObject2: JSONObject = listKaryawanJson.getJSONObject(l)
            val nama = jsonObject2.getString("karyawan")
            listKaryawan.add(nama)
        }
        val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter1 = ArrayAdapter(this,android.R.layout.simple_spinner_item,listKaryawan)
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        this.runOnUiThread { spinner3.adapter = adapter1 }
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedlist1 = listKaryawan[position]
                println(selectedlist1)
                Nikkaryawan = selectedlist1.take(10)
                println(Nikkaryawan)
                //test1(respon = getList2())
                tktjuan()
                // val nik = Nikkaryawan

            }

        }
    }

    fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        // val convert = sdf.parse(button!!.text.toString())
        // val c = Calendar.getInstance()
        // c.time = convert
        //c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH))
        button!!.text = sdf.format(cal.time)
        //fetchJson(KODE_TOKO = String())

    }

    fun main() = runBlocking {
        val url2 = "https://hrindomaret.com/api/getpinjam/karyawan"
        val param2 = JSONObject()
        param2.put("kodetoko", TkAsl)
        param2.put("tglshift", button!!.text as String?)
        val formbody2 = param2.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val post2 = Request.Builder().url(url2).post(formbody2).build()
        val client2 = OkHttpClient()
        client2.newCall(post2).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val reps = getList1()
                val gson = GsonBuilder().create()
                val feed2 : List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                Nikkaryawan = body
                reps.listNama = body
                ListNama.setNama(reps)
                getNama1(reps)

            }
        })


    }
    fun tktjuan()= runBlocking{
        val url3 = "https://hrindomaret.com/api/getpinjam/tokotujuan"
        // val kodetk = intent.getStringExtra("kodetoko")
        val param4 = JSONObject()
        val nik = intent.getStringExtra("nik")
        param4.put("nik", nik)
        param4.put("kodetoko", TkAsl)
        val formBody3 = param4.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val post3 = Request.Builder().url(url3).post(formBody3).build()
        val client2 = OkHttpClient()

        client2.newCall(post3).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val respon = getList2()
                val gson2 = GsonBuilder().create()
                val feed2: List<ListToko> = gson2.fromJson(body,Array<ListToko>::class.java).toList()
                Tktujuan = body
                respon.listTokotj = body
                ListTokotj.setTokotj(respon)
                test1(respon)
                runOnUiThread {
                    pb_peminjamanAS.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        })
    }


    fun fetchJson() = runBlocking {

        //val client = OkHttpClient()
        val url = "https://hrindomaret.com/api/getpinjam/tokoasal"
        //val kodetk_asl1 = intent.putExtra("kodetoko","kosong")
        val nik = intent.getStringExtra("nik")
        val param = JSONObject()
        param.put("nik",nik)

        //val param3 = JSONObject()
        // param3.put("nik","2015035527")
        //param3.put("kodetoko","")

        //param.put("kodetoko","")


        val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()


        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val res = getList3()
                val gson = GsonBuilder().create()
                val feed: List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                TkAsl = body
                //println(body)
                res.listToko = body
                ListToko3.setToko(res)
                test(res)
                //test1(res)
                //main()
                //getNama(res)

            }

            override fun onFailure(call: Call, e: IOException) {
                println("gak bisa bhambank")
            }
        })


    }

    fun messageDialog(nik:String, nikkary:String?, attribute:String) {
        val url = "https://hrindomaret.com/api/getpinjam/history"

        val cred = JSONObject()
        cred.put("nik",nik)
        cred.put("nikkary",nikkary)
        cred.put("attribute",attribute)
        val formbody = cred.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val post = Request.Builder()
            .url(url)
            .post(formbody)
            .build()

        val client = OkHttpClient()

        client.newCall(post).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string()
                println("santuuy"+resp)
                val jsonArray = JSONArray(resp)
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                val dataTokoTujuan= jsonObject.get("TokoTujuan")
                val dataTglMulaiPinjam= jsonObject.get("TglMulaiDipinjam")
                val dataTglSelesaiPinjam= jsonObject.get("TglSelesaiDipinjam")

                getInpTokoTujuanbyAS = dataTokoTujuan.toString()
                getInpTglMulaiPinjambyAS = dataTglMulaiPinjam.toString()
                getInpTglSelesaiPinjambyAS = dataTglSelesaiPinjam.toString()

                println("asuuue"+nikkary)

            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    data class Feed(@SerializedName("ListToko") val feed: List<ListToko1>,
                    val deef: List<ListNama1>)
    data class ListToko1(
        @SerializedName("toko") val toko: String
    )
    data class ListNama1(
        @SerializedName("karyawan")val nama: String
    )


}

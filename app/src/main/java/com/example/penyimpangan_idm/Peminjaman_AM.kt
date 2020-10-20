package com.example.penyimpangan_idm

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.penyimpangan_idm.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.layout_peminjaman_am.*
import kotlinx.android.synthetic.main.layout_history_peminjaman.*
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
class getAsAsal {
    var listAsAsal: String? = "POS"
}

object ListAsAsal {

    private var toko: String? = ""
    fun getASasal(): String? {
        return toko
    }

    fun setASasal(DaftarASasal: getAsAsal) {
        toko = DaftarASasal.listAsAsal
    }

}
class getTokoAsal {
    var listTokoAsal : String? = "TKO"
}
object ListTokoAsal {
    private var tokoAsal: String? = ""
    fun getTokoasal(): String?{
        return tokoAsal
    }
    fun setTokoasal(DaftarTokoAsal : getTokoAsal){
        tokoAsal = DaftarTokoAsal.listTokoAsal
    }
}
class getKaryawanToko {
    var listKaryawanToko : String? = "KRY"
}
object ListKaryawan{
    private var karyawanToko: String? = ""
    fun getKaryawan(): String?{
        return karyawanToko
    }
    fun setKaryawan(DaftarKaryawan : getKaryawanToko){
        karyawanToko = DaftarKaryawan.listKaryawanToko
    }
}
class getASTujuan {
    var listASTujuan: String? = "TJN"
}
object ListASTujuan{
    private var AsTujuan : String? =""
    fun getAstujuan(): String?{
        return AsTujuan
    }
    fun setAstujuan(DaftarASTujuan : getASTujuan){
        AsTujuan = DaftarASTujuan.listASTujuan
    }
}
class getTokoTujuan{
    var listTokoTujuan : String? = "TKJ"
}
object ListTokoTujuan{
    private var TokoTujuan : String? =""
    fun getTokotujuan(): String?{
        return TokoTujuan
    }
    fun setTokotujuan (DaftarTokoTujuan : getTokoTujuan){
        TokoTujuan = DaftarTokoTujuan.listTokoTujuan
    }
}
class getDataPost{
    var listPostData: String? = "PST"
}
object ListDataPost{
    private var PostDataSubmit : String?=""
    fun getDataPost(): String?{
        return PostDataSubmit
    }
    fun setDataPost(DaftarPostData : getDataPost){
        PostDataSubmit = DaftarPostData.listPostData
    }
}

var ASAsalG : String? = ""
var TokoAsalG : String? = ""
var KaryawanTokoG : String? = ""
var ASTujuanG : String? = ""
var TokoTujuanG : String? = ""
var JumlahHari:String?=""
var HariAm = arrayOf("")
var TglAwalAm : Int = 0
var TglAkhirAm : Int = 0
var TglAkhirminsatuAm: Int = 0
var DataSubmit: String? = ""

var getInpTokoTujuanbyAM: String? = ""
var getInpTglMulaiPinjambyAM: String? = ""
var getInpTglSelesaiPinjambyAM: String? = ""

class Peminjaman_AM : AppCompatActivity(){
    var cal = Calendar.getInstance()
    var date : Button? = null
    var submit : Button? = null
    var history : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_peminjaman_am)
        setTitle("Peminjaman Area Manager")

        date = this.datebutton
        submit = this.SubmitButton
        history = this.Historybutton

        pb_peminjamanAM.visibility = View.GONE
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
                AsAsal()

            }

        }
        date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                val dialog = DatePickerDialog(
                    this@Peminjaman_AM, dateSetListener,
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
//
            }
        })
        submit!!.setOnClickListener(object : View.OnClickListener {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onClick(v: View) {
                if(TokoTujuanG == ""){

                    pb_peminjamanAM.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    val dialog3 = AlertDialog.Builder(this@Peminjaman_AM)
                    dialog3.setTitle("Submit Gagal!")
                    dialog3.setMessage("Data Tidak Lengkap")
                    dialog3.setNegativeButton("Kembali", DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(
                            this@Peminjaman_AM,
                            "Gagal Tersubmit",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        //val intent = Intent(this@Peminjaman_AM, Peminjaman_AM::class.java)
                        startActivity(getIntent())
                    })
                    dialog3.show()

                }else{
                    var ttlhari : Int = 0
                    when (JumlahHari) {
                        "" -> ttlhari = 0
                        "1" -> ttlhari = 0
                        "2" -> ttlhari = 1
                        "3" -> ttlhari = 2
                        else -> ttlhari = 0
                    }
                    //val ttlhari = JumlahHari?.toInt()
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val convertedDate = dateFormat.parse(date!!.text as String)
                    val calendar = Calendar.getInstance()
                    calendar.time = convertedDate
                    calendar.add(Calendar.DAY_OF_YEAR, ttlhari!!)
                    val tglselesaiint = calendar.time
                    val tglselesaistr = dateFormat.format(tglselesaiint)
                    val dialogue = AlertDialog.Builder(this@Peminjaman_AM)
                    dialogue.setTitle("Konfirmasi Informasi Data Peminjaman")
                    dialogue.setMessage("Karyawan $KaryawanTokoG akan dipinjamkan ke toko $TokoTujuanG dari toko $TokoAsalG pada tanggal "+ date!!.text as String? +" hingga tanggal $tglselesaistr")
                    dialogue.setPositiveButton("Setuju", DialogInterface.OnClickListener{ dialog, which ->
                        pb_peminjamanAM.visibility = View.VISIBLE
                        getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )

                        println(KaryawanTokoG)
                        println(TokoAsalG)
                        println(TokoTujuanG)
                        println(JumlahHari)
                        println(date!!.text as String?)
                        //val kodetk_asl = intent.putExtra("kodetk_asl", TkAsl)
                        //val nik_kary =intent.putExtra("nik_kary", Nikkaryawan)
                        // val kodetk_tjn =intent.putExtra("kodetk_tjn", Tktujuan)
                        // val jml_hari = intent.putExtra("jml_hari", JmlHari)
                        // val tgl_pjm = intent.putExtra("tgl_pjm",  button!!.text as String?)
                        val url3 = "https://hrindomaret.com/api/postpinjam/submit"
                        val nik = intent.getStringExtra("nik")
                        val param3 = JSONObject()
                        param3.put("nik_kary", KaryawanTokoG)
                        param3.put("kodetk_asl", TokoAsalG)
                        param3.put("kodetk_tjn", TokoTujuanG)
                        param3.put("jml_hari", JumlahHari)
                        param3.put("tgl_pjm", date!!.text as String?)
                        param3.put("nik_inp", nik)
                        val formbody2 = param3.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                        println("param3 "+param3)
                        val post2 = Request.Builder().url(url3).post(formbody2).build()
                        val client2 = OkHttpClient()
                        client2.newCall(post2).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                println("Maaf Data Tidak Bisa Di Post")

                            }

                            override fun onResponse(call: Call, response: Response) {
                                val body = response.body?.string()
                                val respn = getDataPost()
                                println(body)
                                DataSubmit = body
                                respn.listPostData = body
                                ListDataPost.setDataPost(respn)
                                if (body!!.toString().contains("Sukses")) {
                                    runOnUiThread {
                                        pb_peminjamanAM.visibility = View.GONE
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        val dialog1 = AlertDialog.Builder(this@Peminjaman_AM)
                                        dialog1.setTitle("Submit Berhasil!")
                                        dialog1.setMessage("Data Telah Tersubmit")
                                        dialog1.setPositiveButton("OK", DialogInterface.OnClickListener{ dialog, which ->
                                            Toast.makeText(
                                                this@Peminjaman_AM,
                                                "Data Tersubmit",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                            //val intent = Intent(this@Peminjaman_AM, Peminjaman_AM::class.java)
                                            startActivity(getIntent())
                                        })
                                        dialog1.show()
                                        println(body)
                                    }

                                } else if (body!!.toString().contains("Gagal")) {
                                    runOnUiThread {
                                        pb_peminjamanAM.visibility = View.GONE
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        val dialog2 = AlertDialog.Builder(this@Peminjaman_AM)
                                        dialog2.setTitle("Submit Gagal!")
                                        if(getInpTokoTujuanbyAM == ""){
                                            dialog2.setMessage(
                                                "Karyawan yang dipinjam sedang dalam kondisi OFF pada tanggal selanjutnya"
                                            )
                                        }
                                        else{
                                            dialog2.setMessage(
                                                //"Sudah ada data peminjaman di toko $getInpTokoTujuanbyAM pada tanggal $getInpTglMulaiPinjambyAM sampai tanggal $getInpTglSelesaiPinjambyAM"
                                                "Anda sudah melakukan peminjaman sebanyak 6 kali"
                                            )
                                        }

                                        dialog2.setNegativeButton("Kembali", DialogInterface.OnClickListener { dialog, which ->
                                            Toast.makeText(
                                                this@Peminjaman_AM,
                                                "Gagal Tersubmit",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                            //val intent = Intent(this@Peminjaman_AM, Peminjaman_AM::class.java)
                                            startActivity(getIntent())
                                        })
                                        dialog2.show()
                                        println(body)
                                    }
                                } else {
                                    runOnUiThread {
                                        pb_peminjamanAM.visibility = View.GONE
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        val dialog3 = AlertDialog.Builder(this@Peminjaman_AM)
                                        dialog3.setTitle("Submit Gagal!")
                                        dialog3.setMessage("Data Bermasalah")
                                        dialog3.setNegativeButton("Kembali", DialogInterface.OnClickListener { dialog, which ->
                                            Toast.makeText(
                                                this@Peminjaman_AM,
                                                "Gagal Tersubmit",
                                                Toast.LENGTH_SHORT
                                            ).show()
//                                    finish()
//                                    //val intent = Intent(this@Peminjaman_AM, Peminjaman_AM::class.java)
//                                    startActivity(getIntent())
                                        })
                                        dialog3.show()
                                        println(body)
                                    }
                                }
                            }


                        })
                    })
                    dialogue.setNegativeButton("Kembali", DialogInterface.OnClickListener{ dialog, which ->
                        Toast.makeText(
                            this@Peminjaman_AM,
                            "Silahkan ulangi proses kembali",
                            Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(getIntent())
                    })
                    dialogue.show()

                }

            }
        })

        history!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View) {
                val urlhistory = "https://hrindomaret.com/api/getpinjam/history"
                val nik = intent.getStringExtra("nik")
                val nikkary = intent.getStringExtra(KaryawanTokoG)
                val attribute = intent.getStringExtra("history")
                val param = JSONObject()
                param.put("nik", nik)
                param.put("nikkary", nikkary)
                param.put("attribute", attribute)
                val formbody = param.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val post = Request.Builder().url(urlhistory).post(formbody).build()
                val client = OkHttpClient()

                client.newCall(post).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Hasil Error")
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        println(body)

                        runOnUiThread {
                            if(body == "[]"){
                                Toast.makeText(
                                    this@Peminjaman_AM,
                                    "Belum Ada Data History",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val intent = Intent(this@Peminjaman_AM, HistoryPeminjaman::class.java)
                                intent.putExtra("nik", nik)
                                startActivity(intent)
                            }

                        }

                    }

                })

            }
        })

//        history!!.setOnClickListener{
//
//            val urlhistory = "https://hrindomaret.com/api/getpinjam/history"
//            val nik = intent.getStringExtra("nik")
//            val intent = Intent(this@Peminjaman_AM, HistoryPeminjaman::class.java)
//            intent.putExtra("nik", nik)
//            startActivity(intent)

//            val param = JSONObject()
//            param.put("nik",  nik)
//
//            val formbody = param.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
//            val post = Request.Builder()
//                .url(urlhistory)
//                .post(formbody)
//                .build()
//            val client = OkHttpClient()
//
//            client.newCall(post).enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    val body = response.body?.string()
//                    if (body!!.isEmpty()) {
//                        val dialog = AlertDialog.Builder(this@Peminjaman_AM)
//                        dialog.setMessage("Belum Ada Aktivitas Peminjaman")
//                        dialog.setPositiveButton("OK") { dialog, which ->
//                            Toast.makeText(
//                                this@Peminjaman_AM,
//                                "Data Kosong",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        val alert = dialog.create()
//                        alert.setTitle("Data Kosong")
//                        alert.show()
//                    } else {
//                        val intent = Intent(this@Peminjaman_AM, HistoryPeminjaman::class.java)
//                        intent.putExtra("nik", nik)
//                        startActivity(intent)
//                    }
//
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    println("Hasil Error")
//                }
//
//            })

//        }


    }
    fun SpinnerASAsal(res: getAsAsal) {
        val listASasal = arrayListOf<String>()

        val listASasalJson = JSONArray(ListAsAsal.getASasal())



        for (p in 0 until listASasalJson.length()) {
            val jsonObject1: JSONObject = listASasalJson.getJSONObject(p)

            val asAsal = jsonObject1.getString("supervisor")


            listASasal.add(asAsal)

        }
        val spinner = findViewById<Spinner>(R.id.ASAsl)

        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listASasal)

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
                val selectedList = listASasal[position]
                println(selectedList)

                //println(selectedList.take(4))
                ASAsalG = selectedList.take(10)
                //println(TkAsl)
                val kodeAS = ASAsalG

                if (kodeAS == ASAsalG) {
                    //TkAsl.setSelection[position]
                    TokoAsal()
                    //  spinner.setSelection(value)
                }
            }
        }
    }
    fun SpinnerTokoAsal(resp : getTokoAsal){
        val listTokoAsal = arrayListOf<String>()

        val listTokoAsalJson = JSONArray(ListTokoAsal.getTokoasal())



        for (t in 0 until listTokoAsalJson.length()) {
            val jsonObject1: JSONObject = listTokoAsalJson.getJSONObject(t)

            val tokoAsal1 = jsonObject1.getString("toko")


            listTokoAsal.add(tokoAsal1)

        }
        val spinner = findViewById<Spinner>(R.id.TkAsal)

        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTokoAsal)

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
                val selectedList = listTokoAsal[position]
                println(selectedList)

                //println(selectedList.take(4))
                TokoAsalG = selectedList.take(4)
                //println(TkAsl)
                KaryawanTk()
            }
        }
    }
    fun SpinnerKaryawanToko(respo : getKaryawanToko){
        val listKaryawanToko = arrayListOf<String>()

        val listKaryawanTokoJson = JSONArray(ListKaryawan.getKaryawan())



        for (k in 0 until listKaryawanTokoJson.length()) {
            val jsonObject1: JSONObject = listKaryawanTokoJson.getJSONObject(k)

            val karyawan1 = jsonObject1.getString("karyawan")


            listKaryawanToko.add(karyawan1)

        }
        val spinner = findViewById<Spinner>(R.id.KaryawanToko)

        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listKaryawanToko)

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
                val selectedList = listKaryawanToko[position]
                println(selectedList)

                //println(selectedList.take(4))
                KaryawanTokoG = selectedList.take(10)
                //println(TkAsl)
                ASTujuan()
            }
        }
    }
    fun SpinnerASTjn(respon : getASTujuan){
        val listASTujuan = arrayListOf<String>()

        val listASTujuanJson = JSONArray(ListASTujuan.getAstujuan())



        for (q in 0 until listASTujuanJson.length()) {
            val jsonObject2: JSONObject = listASTujuanJson.getJSONObject(q)

            val astujuan = jsonObject2.getString("supervisor")


            listASTujuan.add(astujuan)

        }
        val spinner = findViewById<Spinner>(R.id.ASTjn)

        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listASTujuan)

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
                val selectedList = listASTujuan[position]
                println(selectedList)

                //println(selectedList.take(4))
                ASTujuanG = selectedList.take(10)
                //println(TkAsl)
                TokoTujuan()
            }
        }
    }
    fun SpinnerTokoTujuan(restjn : getTokoTujuan){
        val listTokoTujuan = arrayListOf<String>()

        val listTokoTujuanjson = JSONArray(ListTokoTujuan.getTokotujuan())



        for (m in 0 until listTokoTujuanjson.length()) {
            val jsonObject1: JSONObject = listTokoTujuanjson.getJSONObject(m)

            val tokoTujuan = jsonObject1.getString("toko")


            listTokoTujuan.add(tokoTujuan)

        }
        val spinner = findViewById<Spinner>(R.id.TKTujuan)

        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        // val spinner3 = findViewById<Spinner>(R.id.karyawan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTokoTujuan)

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
                val selectedList = listTokoTujuan[position]
                println(selectedList)

                //println(selectedList.take(4))
                TokoTujuanG = selectedList.take(4)
                //println(TkAsl)
            }
        }
    }

    fun AsAsal() = runBlocking {
        if(date!!.text as String? != "Select Date"){
            pb_peminjamanAM.visibility = View.VISIBLE
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
        }else{
            pb_peminjamanAM.visibility = View.GONE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        //val client = OkHttpClient()
        val url = "https://hrindomaret.com/api/getpinjam/supervisorasal"
        //val kodetk_asl1 = intent.putExtra("kodetoko","kosong")
        val param = JSONObject()
        val nik = intent.getStringExtra("nik")
        param.put("nik", nik)

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
                println("werwerwer"+body)
                val res = getAsAsal()
                // val gson = GsonBuilder().create()
                //val feed: List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                ASAsalG = body
                //println(body)
                res.listAsAsal = body
                ListAsAsal.setASasal(res)
                SpinnerASAsal(res)
                //test1(res)
                //main()
                //getNama(res)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("gak bisa bhambank")
            }
        })


    }
    fun TokoAsal() = runBlocking{
        val url = "https://hrindomaret.com/api/getpinjam/tokoasal"
        //val kodetk_asl1 = intent.putExtra("kodetoko","kosong")
        val param = JSONObject()
        param.put("nik", ASAsalG)

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
                val resp = getTokoAsal()
                // val gson = GsonBuilder().create()
                //val feed: List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                TokoAsalG = body
                //println(body)
                resp.listTokoAsal = body
                ListTokoAsal.setTokoasal(resp)
                SpinnerTokoAsal(resp)
                //test1(res)
                //main()
                //getNama(res)


            }

            override fun onFailure(call: Call, e: IOException) {
                println("gak bisa bhambank")
            }
        })


    }
    fun KaryawanTk()= runBlocking {
        val url = "https://hrindomaret.com/api/getpinjam/karyawan"
        //val kodetk_asl1 = intent.putExtra("kodetoko","kosong")
        val param = JSONObject()
        param.put("kodetoko", TokoAsalG)
        param.put("tglshift", date!!.text as String?)
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
                val respo = getKaryawanToko()
                // val gson = GsonBuilder().create()
                //val feed: List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                KaryawanTokoG = body
                //println(body)
                respo.listKaryawanToko = body
                ListKaryawan.setKaryawan(respo)
                SpinnerKaryawanToko(respo)
                //test1(res)
                //main()
                //getNama(res)
                if(body.toString() == "[]"){
                    runOnUiThread {
                        pb_peminjamanAM.visibility = View.GONE
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        //messageDialog(nik, Nikkaryawan, "alert")
                        Toast.makeText(
                            this@Peminjaman_AM,
                            "Data Karyawan Kosong, Silahkan Pilih Tanggal, AS, atau Toko Yang Lain",
                            Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("gak bisa bhambank")
            }
        })

    }
    fun ASTujuan()= runBlocking{
        val url = "https://hrindomaret.com/api/getpinjam/supervisortujuan"
        //val kodetk_asl1 = intent.putExtra("kodetoko","kosong")
        val param = JSONObject()
        val nik = intent.getStringExtra("nik")
        param.put("nik", nik)
        param.put("nik_as", ASAsalG )

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
                val respon = getASTujuan()
                // val gson = GsonBuilder().create()
                //val feed: List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                ASTujuanG = body
                //println(body)
                respon.listASTujuan = body
                ListASTujuan.setAstujuan(respon)
                SpinnerASTjn(respon)
                //test1(res)
                //main()
                //getNama(res)
                //println(ASTujuanG)


            }

            override fun onFailure(call: Call, e: IOException) {
                println("gak bisa bhambank")
            }
        })
    }
    fun TokoTujuan()= runBlocking{
        if(date!!.text as String? != "Select Date"){
            pb_peminjamanAM.visibility = View.VISIBLE
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
        }else{
            pb_peminjamanAM.visibility = View.GONE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        val url = "https://hrindomaret.com/api/getpinjam/tokotujuan"
        //val kodetk_asl1 = intent.putExtra("kodetoko","kosong")
        val param = JSONObject()
        param.put("nik", ASTujuanG )
        param.put("kodetoko", TokoAsalG)


//        param.put("nik", "1998000386")
//        param.put("nik_as", "2001006135")
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
                val restjn = getTokoTujuan()
                // val gson = GsonBuilder().create()
                //val feed: List<ListToko> = gson.fromJson(body,Array<ListToko>::class.java).toList()
                TokoTujuanG = body
                //println(body)
                restjn.listTokoTujuan = body
                ListTokoTujuan.setTokotujuan(restjn)
                SpinnerTokoTujuan(restjn)
                //test1(res)
                //main()
                //getNama(res)
                runOnUiThread {
                    pb_peminjamanAM.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    val nik = intent.getStringExtra("nik")
                    messageDialog(nik, KaryawanTokoG, "alert")
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("gak bisa bhambank")
            }
        })
    }
    fun date(){

        // val dateFormat = getDateInstance()
        //val convertedDate = dateFormat.parse(button!!.text as String)



        // val calendar = Calendar.getInstance()
        //calendar.time = convertedDate

        // calendar.get(Calendar.DAY_OF_MONTH)

        // val lastDayOfMonth = calendar.time
        // println(lastDayOfMonth)


        val tglawalstr = date!!.text as String
        TglAwalAm = tglawalstr.take(2).toInt()
        println(TglAwalAm)

        val date = date!!.text as String
        val dateFormat =
            SimpleDateFormat("dd/MM/yyyy")
        val convertedDate = dateFormat.parse(date)
        val c = Calendar.getInstance()
        c.time = convertedDate
        c[Calendar.DAY_OF_MONTH] = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        //println(c[Calendar.DAY_OF_MONTH])

        TglAkhirAm = c[Calendar.DAY_OF_MONTH]

        TglAkhirminsatuAm = TglAkhirAm -1

        println(TglAkhirAm)
        println(TglAkhirminsatuAm)


        if (TglAwalAm == TglAkhirAm){
            HariAm = arrayOf("1")
        }
        else if (TglAwalAm == TglAkhirminsatuAm){
            HariAm = arrayOf("1","2")
        }
        else{ HariAm = arrayOf("1","2","3")}

        val hari = arrayOf("1","2","3") //HariAm


        jumlahhari.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hari)
        jumlahhari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                JumlahHari = jmlhari1
                println(JumlahHari)


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
        date!!.text = sdf.format(cal.time)
        //fetchJson(KODE_TOKO = String())

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

                val jsonArray = JSONArray(resp)
                if(resp != "[]"){
                    val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                    val dataTokoTujuan= jsonObject.get("TokoTujuan")
                    val dataTglMulaiPinjam= jsonObject.get("TglMulaiDipinjam")
                    val dataTglSelesaiPinjam= jsonObject.get("TglSelesaiDipinjam")

                    getInpTokoTujuanbyAM = dataTokoTujuan.toString()
                    getInpTglMulaiPinjambyAM = dataTglMulaiPinjam.toString()
                    getInpTglSelesaiPinjambyAM = dataTglSelesaiPinjam.toString()
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
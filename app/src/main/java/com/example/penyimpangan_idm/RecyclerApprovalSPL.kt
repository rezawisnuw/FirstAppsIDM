package com.example.penyimpangan_idm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.layout_approval_spl.view.*
import kotlinx.android.synthetic.main.layout_rv_approval_spl.view.*

var getNoDtlSPL:MutableList<String?> = ArrayList()
var getDataKaryawan:MutableList<String?> = ArrayList()
var getDataRelasi:MutableList<String?> = ArrayList()
var getRincianTugas:MutableList<String?> = ArrayList()
var getTglLembur:MutableList<String?> = ArrayList()
var getJamMasuk:MutableList<String?> = ArrayList()
var getJamKeluar:MutableList<String?> = ArrayList()
var getTotalDurasi:MutableList<String?> = ArrayList()
var getKeterangan:MutableList<String?> = ArrayList()

var ttlData: Int = 0

var getChecked:MutableList<String?> = ArrayList()
var getJson:MutableList<String?> = ArrayList()
//class RecyclerViewSPL(val feed: List<ApprovalSPL.ModelListSPL>): RecyclerView.Adapter<CustomViewHolder>(){
class RecyclerApprovalSPL(val feed: ApprovalSPL.Feed): RecyclerView.Adapter<CustomViewHolder>(){

    //var sizeData: String = ""

    override fun getItemCount(): Int {
        //sizeData = feed.data.count().toString()
        ttlData = feed.data.count()
        return feed.data.count()
        //return feed.count()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val row = layoutinflater.inflate(R.layout.layout_rv_approval_spl, parent, false)
        return CustomViewHolder(row)

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int){
        //val feed = feed.get(position)
        val feed = feed.data.get(position)

        holder.view.tv_inpnodtlspl.text = feed.NoDtlSPL
        holder.view.tv_inpdatakaryawan.text = feed.DataKaryawan
        holder.view.tv_inpdatarelasi.text = feed.DataRelasi

        if(feed.DataRelasi == null || feed.DataRelasi.isEmpty()){
            holder.view.tv_inpdatarelasi.setVisibility(View.INVISIBLE);
        }else{
            holder.view.tv_inpdatarelasi.setVisibility(View.VISIBLE);
        }

        holder.view.tv_inprinciantugas.text = feed.RincianTugas
        holder.view.tv_inptgllembur.text = feed.TglLembur
        holder.view.tv_inpjammasuk.text = feed.JamMasuk
        holder.view.tv_inpjamkeluar.text = feed.JamKeluar
        holder.view.tv_inptotaldurasi.text = feed.TotalDurasi

        if(feed.TotalDurasi == null || feed.TotalDurasi.isEmpty()){
            holder.view.tv_totaldurasi.setVisibility(View.GONE);
            holder.view.tv_inptotaldurasi.setVisibility(View.GONE);
        }else{
            holder.view.tv_totaldurasi.setVisibility(View.VISIBLE);
            holder.view.tv_inptotaldurasi.setVisibility(View.VISIBLE);
        }

        holder.view.tv_inpketerangan.text = feed.Keterangan

        holder.view.ck_spl.setOnClickListener{
            var gson = GsonBuilder().create().toJson(feed)
            var gsonNoDtlSPL = GsonBuilder().create().toJson(feed.NoDtlSPL)
            var gsonDataKaryawan = GsonBuilder().create().toJson(feed.DataKaryawan)
            var gsonDataRelasi = GsonBuilder().create().toJson(feed.DataRelasi)
            var gsonRincianTugas = GsonBuilder().create().toJson(feed.RincianTugas)
            var gsonTglLembur = GsonBuilder().create().toJson(feed.TglLembur)
            var gsonJamMasuk = GsonBuilder().create().toJson(feed.JamMasuk)
            var gsonJamKeluar = GsonBuilder().create().toJson(feed.JamKeluar)
            var gsonTotalDurasi = GsonBuilder().create().toJson(feed.TotalDurasi)
            var gsonKeterangan = GsonBuilder().create().toJson(feed.Keterangan)

//            var dataFeed = feed.toString().contains(feed.NoDtlSPL.toString())
//            println("dataFeed "+dataFeed)
            println("ApprovalCode " + feed.NoDtlSPL.toString())
            if(holder.view.ck_spl.isChecked){
                holder.view.btn_approve.isEnabled = true

                getChecked.add(feed.toString())
                getJson.add(gson)

                getNoDtlSPL.add(gsonNoDtlSPL)
                getDataKaryawan.add(gsonDataKaryawan)
                getDataRelasi.add(gsonDataRelasi)
                getRincianTugas.add(gsonRincianTugas)
                getTglLembur.add(gsonTglLembur)
                getJamMasuk.add(gsonJamMasuk)
                getJamKeluar.add(gsonJamKeluar)
                getTotalDurasi.add(gsonTotalDurasi)
                getKeterangan.add(gsonKeterangan)

//                getNoDtlSPL.add(feed.NoDtlSPL)
//                getDataKaryawan.add(feed.DataKaryawan)
//                getDataRelasi.add(feed.DataRelasi)
//                getRincianTugas.add(feed.RincianTugas)
//                getTglLembur.add(feed.TglLembur)
//                getJamMasuk.add(feed.JamMasuk)
//                getJamKeluar.add(feed.JamKeluar)
//                getTotalDurasi.add(feed.TotalDurasi)
//                getKeterangan.add(feed.Keterangan)

            } else {
                holder.view.btn_approve.isEnabled = false

                if(getChecked.contains(feed.toString())){
                    getChecked.remove(feed.toString())
                    getJson.remove(gson)

                    getNoDtlSPL.remove(gsonNoDtlSPL)
                    getDataKaryawan.remove(gsonDataKaryawan)
                    getDataRelasi.remove(gsonDataRelasi)
                    getRincianTugas.remove(gsonRincianTugas)
                    getTglLembur.remove(gsonTglLembur)
                    getJamMasuk.remove(gsonJamMasuk)
                    getJamKeluar.remove(gsonJamKeluar)
                    getTotalDurasi.remove(gsonTotalDurasi)
                    getKeterangan.remove(gsonKeterangan)
                }
            }
            println("GSOOOONN "+ GsonBuilder().create().toJson(feed))
            println("JsonCheckeeed" + getJson)
            println("position "+position)
        }
        holder.view.ck_spl.isChecked = getChecked.contains(feed.toString())

    }




}




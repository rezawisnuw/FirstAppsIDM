package com.example.penyimpangan_idm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.layout_approval_spl.view.*
import kotlinx.android.synthetic.main.layout_history_peminjaman.view.*
import kotlinx.android.synthetic.main.layout_rv_history_peminjaman.view.*

var getDataKaryawanHistoryPeminjaman:MutableList<String?> = ArrayList()
var getTokoAsalHistoryPeminjaman:MutableList<String?> = ArrayList()
var getTokoTujuanHistoryPeminjaman:MutableList<String?> = ArrayList()
var getTglMulaiDipinjamHistoryPeminjaman:MutableList<String?> = ArrayList()
var getTglSelesaiDipinjamHistoryPeminjaman:MutableList<String?> = ArrayList()
var getTglMeminjamHistoryPeminjaman:MutableList<String?> = ArrayList()

var ttlDataHistory: Int = 0

var getCheckedHistoryPeminjaman:MutableList<String?> = ArrayList()
var getJsonHistoryPeminjaman:MutableList<String?> = ArrayList()

class RecyclerHistoryPeminjaman(val list: List<HistoryPeminjaman.ModelHistoryPeminjaman>): RecyclerView.Adapter<CustomViewHolder>(){
    //val test = listOf("qqq","www","aaa")

    override fun getItemCount(): Int {
        ttlDataHistory = list.count()
        return list.count()
        //return 10
        //return test.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.layout_rv_history_peminjaman, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val res = list.get(position)

        holder.view.tv_res_karyawan.text = res.DataKaryawan
        holder.view.tv_res_tokoasal.text = res.TokoAsal
        holder.view.tv_res_tokotujuan.text = res.TokoTujuan
        holder.view.tv_res_tglmulaipinjam.text = res.TglMulaiDipinjam
        holder.view.tv_res_tglselesaipinjam.text = res.TglSelesaiDipinjam
        holder.view.tv_res_tglinput.text = res.TglMeminjam
        holder.view.ll_hdtglinput.setVisibility(View.GONE);

        holder.view.ck_history.setOnClickListener {
            var gson = GsonBuilder().create().toJson(res)
            var gsonDataKaryawan = GsonBuilder().create().toJson(res.DataKaryawan)
            var gsonTokoAsal = GsonBuilder().create().toJson(res.TokoAsal)
            var gsonTokoTujuan = GsonBuilder().create().toJson( res.TokoTujuan)
            var gsonTglMulaiDipinjam = GsonBuilder().create().toJson(res.TglMulaiDipinjam)
            var gsonTglSelesaiDipinjam = GsonBuilder().create().toJson(res.TglSelesaiDipinjam)
            var gsonTglMeminjam = GsonBuilder().create().toJson(res.TglMeminjam)

            if(holder.view.ck_history.isChecked) {
                holder.view.btn_deletehistory.isEnabled = true

                getCheckedHistoryPeminjaman.add(res.toString())
                getJsonHistoryPeminjaman.add(gson)

                getDataKaryawanHistoryPeminjaman.add(gsonDataKaryawan)
                getTokoAsalHistoryPeminjaman.add(gsonTokoAsal)
                getTokoTujuanHistoryPeminjaman.add(gsonTokoTujuan)
                getTglMulaiDipinjamHistoryPeminjaman.add(gsonTglMulaiDipinjam)
                getTglSelesaiDipinjamHistoryPeminjaman.add(gsonTglSelesaiDipinjam)
                getTglMeminjamHistoryPeminjaman.add(gsonTglMeminjam)
            } else {
                holder.view.btn_deletehistory.isEnabled = false

                if(getCheckedHistoryPeminjaman.contains(res.toString())){
                    getCheckedHistoryPeminjaman.remove(res.toString())
                    getJsonHistoryPeminjaman.remove(gson)

                    getDataKaryawan.remove(gsonDataKaryawan)
                    getTokoAsalHistoryPeminjaman.remove(gsonTokoAsal)
                    getTokoTujuanHistoryPeminjaman.remove(gsonTokoTujuan)
                    getTglMulaiDipinjamHistoryPeminjaman.remove(gsonTglMulaiDipinjam)
                    getTglSelesaiDipinjamHistoryPeminjaman.remove(gsonTglSelesaiDipinjam)
                    getTglMeminjamHistoryPeminjaman.remove(gsonTglMeminjam)
                }
            }
            println("GSOOOONN "+ GsonBuilder().create().toJson(res))
            println("JsonCheckeeed" + getJsonHistoryPeminjaman)
            println("position "+position)
        }

//        holder.view.tv_res_karyawan.text = "res.DataKaryawan"
//        holder.view.tv_res_tokoasal.text = "res.TokoAsal"
//        holder.view.tv_res_tokotujuan.text = "res.TokoTujuan"
//        holder.view.tv_res_tokotujuan.text = "res.TglMulaiDipinjam"
//        holder.view.tv_res_tglselesaipinjam.text = "res.TglSelesaiDipinjam"

    }

}

//class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){
//
//}




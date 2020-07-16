package com.example.penyimpangan_idm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_rv_history_peminjaman.view.*

class RecyclerHistoryPeminjaman(val list: List<HistoryPeminjaman.ModelHistoryPeminjaman>): RecyclerView.Adapter<CustomViewHolder>(){
    //val test = listOf("qqq","www","aaa")

    override fun getItemCount(): Int {
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




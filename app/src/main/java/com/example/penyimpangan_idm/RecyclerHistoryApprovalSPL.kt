package com.example.penyimpangan_idm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_rv_approval_spl.view.*

class RecyclerHistoryApprovalSPL(val list: HistoryApprovalSPL.FeedHistoryApprovalSPL): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        ttlData = list.data.count()
        return list.data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val row = layoutinflater.inflate(R.layout.layout_rv_history_approval_spl, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int){
        val feed = list.data.get(position)

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
        holder.view.tv_inpketerangan.text = feed.Keterangan

//        holder.view.tv_keterangan.setVisibility(View.GONE);
//        holder.view.tv_inpketerangan.setVisibility(View.GONE);

    }

}
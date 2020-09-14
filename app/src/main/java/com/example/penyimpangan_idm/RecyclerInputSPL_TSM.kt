package com.example.penyimpangan_idm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.layout_rv_input_spl_tsm.view.*
import kotlinx.android.synthetic.main.layout_rv_input_spl_tsm.view.tv_inpdatakaryawan

var getKaryCbgInputSPL_TSM:MutableList<String?> = ArrayList()
var getJmlSPLInputSPL_TSM:MutableList<String?> = ArrayList()
var getJamKerjaInputSPL_TSM:MutableList<String?> = ArrayList()
//var getTglLemburInputSPL_TSM:MutableList<String?> = ArrayList()

var ttlDataInputSPL_TSM: Int = 0

var getCheckedInputSPL_TSM:MutableList<String?> = ArrayList()
var getJsonInputSPL_TSM:MutableList<String?> = ArrayList()

class RecyclerInputSPL_TSM(val feed: InputSPL_TSM.Feed): RecyclerView.Adapter<CustomViewHolder>(){

    override fun getItemCount(): Int {
        ttlDataInputSPL_TSM = feed.data.count()
        return feed.data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.layout_rv_input_spl_tsm, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val res = feed.data.get(position)

        holder.view.tv_inpdatakaryawan.text = res.KaryawanCabang
        holder.view.tv_inpjmlspl.text = res.SPLKe
        holder.view.tv_inpjamkerja.text = res.JamKerja
//        holder.view.tv_inptgllembur.text = res.TglLembur

        holder.view.ck_inputspltsm.setOnClickListener {
            var gson = GsonBuilder().create().toJson(res)

            var gsonDataKaryawan = GsonBuilder().create().toJson(res.KaryawanCabang)
            var gsonJmlSPL = GsonBuilder().create().toJson(res.SPLKe)
            var gsonJamKerja = GsonBuilder().create().toJson(res.JamKerja)
//            var gsonTglLembur = GsonBuilder().create().toJson(res.TglLembur)


            if(holder.view.ck_inputspltsm.isChecked) {

                getCheckedInputSPL_TSM.add(res.toString())
                getJsonInputSPL_TSM.add(gson)

                getKaryCbgInputSPL_TSM.add(gsonDataKaryawan)
                getJmlSPLInputSPL_TSM.add(gsonJmlSPL)
                getJamKerjaInputSPL_TSM.add(gsonJamKerja)
//                getTglLemburInputSPL_TSM.add(gsonTglLembur)

            } else {

                if(getCheckedInputSPL_TSM.contains(res.toString())){
                    getCheckedInputSPL_TSM.remove(res.toString())
                    getJsonInputSPL_TSM.remove(gson)

                    getKaryCbgInputSPL_TSM.remove(gsonDataKaryawan)
                    getJmlSPLInputSPL_TSM.remove(gsonJmlSPL)
                    getJamKerjaInputSPL_TSM.remove(gsonJamKerja)
//                    getTglLemburInputSPL_TSM.remove(gsonTglLembur)

                }
            }
            println("GsonBuilder "+ GsonBuilder().create().toJson(res))
            println("getJsonInputSPL_TSM" + getJsonInputSPL_TSM)
            println("position "+position)
        }

    }

}
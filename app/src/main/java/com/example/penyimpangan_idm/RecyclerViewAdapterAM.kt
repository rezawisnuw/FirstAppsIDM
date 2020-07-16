package com.example.penyimpangan_idm

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.approvalpenyimpangan.view.*
import kotlinx.android.synthetic.main.penyimpanganlayout.view.*
import kotlinx.coroutines.selects.select

var checkedAM:MutableList<String?> = ArrayList()
var jsonCheckedAM:MutableList<String?> = ArrayList()

class RecyclerViewAdapterAM(val feed: List<ApprovalPenyimpanganAM.Penyimpangan>): RecyclerView.Adapter<CustomViewHolder>(){
    override fun getItemCount(): Int {
        return feed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutinflater = LayoutInflater.from(parent?.context)
        val row = layoutinflater.inflate(R.layout.penyimpanganlayout, parent, false)
        return CustomViewHolder(row)


    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int){
        val feed = feed.get(position)

        holder.view.textView5.text = feed.namakaryawan + " - " + feed.kodenik + "\n" +
                feed.jamin + " - " + feed.jamout + "\n" +
                "Tanggal : " + feed.tglabsen

        holder.view.checkBox.setOnClickListener{
            var gson = GsonBuilder().create().toJson(feed)
            var x = feed.toString().contains(selectedSpinner.toString())
            println(x)
            println(selectedSpinner.toString())
            if(holder.view.checkBox.isChecked){
                checked.add(feed.toString())
                jsonChecked.add(gson)
            } else {
                if(checked.contains(feed.toString())){
                    checked.remove(feed.toString())
                    jsonChecked.remove(gson)
                }
            }
            println(GsonBuilder().create().toJson(feed))
            println(jsonChecked)
        }
        holder.view.checkBox.isChecked = checked.contains(feed.toString())
    }

}

//class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){
//
//}
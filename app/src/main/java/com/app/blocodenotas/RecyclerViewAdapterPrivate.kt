package com.app.treino

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.blocodenotas.Anotation
import com.app.blocodenotas.AnotationPrivate
import com.app.blocodenotas.R
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log


class RecyclerViewAdapterPrivate(private val userList: List<AnotationPrivate>):
    RecyclerView.Adapter<RecyclerViewAdapterPrivate.MyViewHolder>(){


 inner class MyViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){

        val itemTitle = ItemView.findViewById<TextView>(R.id.title_private)

        val descricao = ItemView.findViewById<TextView>(R.id.description_private)


     init {



     }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_privete, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currenteItem = userList[position]

        holder.itemTitle.text = currenteItem.titlePrivate
        holder.descricao.text = currenteItem.anotationPrivate


    }

    override fun getItemCount(): Int {
        return userList.size
    }

}
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
import com.app.blocodenotas.R
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log


class RecyclerViewAdapter(private val userList: List<Anotation>):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){


 inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){

        val itemTitle = ItemView.findViewById<TextView>(R.id.title)

        val descricao = ItemView.findViewById<TextView>(R.id.description)


     init {



     }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currenteItem = userList[position]

        holder.itemTitle.text = currenteItem.title
        holder.descricao.text = currenteItem.anotation

    }

    override fun getItemCount(): Int {
        return userList.size
    }

}
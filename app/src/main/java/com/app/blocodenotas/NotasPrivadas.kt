package com.app.blocodenotas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.treino.RecyclerViewAdapterPrivate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class NotasPrivadas : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference

    private lateinit var rvPrivate : RecyclerView

    private lateinit var userArrayListPrivate : ArrayList<AnotationPrivate>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_privadas)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Notas privadas")

        supportActionBar?.setDisplayHomeAsUpEnabled(Boolean)

        rvPrivate = findViewById(R.id.rv_private)

        rvPrivate.layoutManager = LinearLayoutManager(this)
        rvPrivate.setHasFixedSize(true)
        userArrayListPrivate = arrayListOf<AnotationPrivate>()

        getAnotationDataPrivate()


        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener { task ->

                        val newtoken = task.result


                        userArrayListPrivate.removeAt(viewHolder.adapterPosition)

                        rvPrivate.adapter?.notifyItemRemoved(viewHolder.adapterPosition)

                        FirebaseDatabase.getInstance()
                            .getReference("tokenprivete")
                            .removeValue()
                    }

            }


        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvPrivate)



    }


    private fun getAnotationDataPrivate() {

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task->

                val newtoken = task.result

                dbRef = FirebaseDatabase.getInstance().getReference(Firebase.auth.currentUser!!.uid)

                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for (Snapshot in snapshot.children){

                                val anotacoesPrivate = Snapshot.getValue(AnotationPrivate::class.java)

                                userArrayListPrivate.add(anotacoesPrivate!!)
                            }

                            rvPrivate.adapter = RecyclerViewAdapterPrivate(userArrayListPrivate)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }
    }
}

fun Any?.setDisplayHomeAsUpEnabled(boolean: Boolean.Companion) {

}

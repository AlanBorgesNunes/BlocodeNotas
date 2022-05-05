package com.app.blocodenotas

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.blocodenotas.databinding.ActivityCriarNotaBinding
import com.app.treino.RecyclerViewAdapter
import com.app.treino.RecyclerViewAdapterPrivate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import java.util.prefs.AbstractPreferences
import kotlin.collections.ArrayList

class CriarNota : AppCompatActivity() {

    private val idList: MutableList<String> = mutableListOf(
        "iguiytgiutg",
        "gtiuytiuti",
        "rtuyrtuyrtyuyurt",
        "utgiutitguigiu",
        "hkhgkjghkgkg",
        "guiguigiuguig",
        "gyuiuyreuyrfifg",
        "jkhjlkhlkilhjl",
        "uiyuioyoiyguyu",
        "ugiutggiuguig",
        "ygfyiufgyiufyuiyfuuiy",
        "iuyghuioyhoiuo",
        "iguiugiukguughui",
        "igfiuygiuguhguohgo",
        "iutguiguigiugiu",
        "fuyfuyfyuifgyifgyi",
        "fiuyfgygfijy",
        "hyoihyoihyioho",
        "tgiugtiguiguigig",
        "5687r8urfu",
        "ytt",
        "uydud",
        "estesteslj",
        "yfgyufyufyufy",
        "giuguigoiugu",
        "giuygtiuygiuguoi",
        "ewertsdfghj~çlgjkhv",
        "iufhjhplkhgkfjhgbkjlh",
        "fyuuyfufuyyfyu",
        "ikugugikughuohguohgo",
        "hjikhjoihjopihohyoihio",
        "jfcjhfvhkjgvjhfcvjhvjk",
        "iuyguigigiguiiuug",
        "ghkhj.glfhgdc,lkv",
        "fuytfulfuyfudf",
        "uyfluyfluyfluyfluyfg",
        "kuytdkytdxykmdxsy",
        "luyjfcuvcl.fvujcv",
        "kgjufhdgfdlkjghgf",
        "hfdgjhkhgjhdgfdxhgfhgfc",
        "1",
        "lkjhkgjhfgjkhfkhjf"
    )

    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityCriarNotaBinding
    private lateinit var dbRef: DatabaseReference

    private lateinit var rv: RecyclerView

    var idItem: Int? = null

    private lateinit var userArrayList: ArrayList<Anotation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarNotaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Nova anotação")



        binding.btnSalvarAnotacao.setOnClickListener {

            if (binding.titleAnotation.text.toString().isBlank() ||
                binding.anotation.text.toString().isBlank()
            ) {

                Toast.makeText(
                    this,
                    "Campos obrigatórios em branco!",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                dialogPrivete()


            }
        }


    }


    private fun addAnotationToDatabase(title: String, anotation: String) {

        dbRef = FirebaseDatabase.getInstance().getReference()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->

                val token = task.result
                var id = Random()
                var num = id.nextInt(10000) + 1

                idItem = System.currentTimeMillis().toInt()


                val random: Int = (0 until (idList.size)).random()

                var idAnotation = idList[random]

                dbRef.child(Firebase.auth.currentUser!!.uid)
                    .child("id = ${idAnotation}")
                    .setValue(Anotation(title, anotation))

            }
    }

    private fun addAnotationToDatabasePrivate(titlePrivate: String, anotationPrivate: String) {

        dbRef = FirebaseDatabase.getInstance().getReference()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->

                val tokenprivete = task.result


                dbRef.child(Firebase.auth.currentUser!!.uid)
                    .child("anotaçoes privadas")
                    .setValue(AnotationPrivate(titlePrivate, anotationPrivate))

            }
    }

    fun dialogPrivete() {
        val view = View.inflate(this, R.layout.dialg_simple_private, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        val radioButton = view.findViewById<RadioGroup>(R.id.radioButton)

        radioButton.setOnCheckedChangeListener { radioGroup, i ->
            val rbPrivate: RadioButton = view.findViewById(R.id.rb_private)
            val rbSimple: RadioButton = view.findViewById(R.id.rb_simple)


            if (rbPrivate.isChecked) {
                if (addAnotationToDatabasePrivate(
                        binding.titleAnotation.text.toString(),
                        binding.anotation.text.toString()
                    ) != null
                ) {


                    binding.titleAnotation.text?.clear()
                    binding.anotation.text?.clear()

                    Toast.makeText(
                        this,
                        "salvo como privado simples com sucesso",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    dialog.dismiss()
                }

            }

            if (rbSimple.isChecked) {
                if (addAnotationToDatabase(
                        binding.titleAnotation.text.tos(),
                        binding.anotation.text.tos()
                    ) != null
                ) {


                    binding.titleAnotation.text?.clear()
                    binding.anotation.text?.clear()

                    Toast.makeText(
                        this,
                        "salvo como simples com sucesso",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    dialog.dismiss()
                }

            }

        }


    }

    fun Any?.setDisplayHomeAsUpEnabled(boolean: Boolean.Companion) {

    }
}


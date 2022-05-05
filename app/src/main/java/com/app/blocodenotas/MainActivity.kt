package com.app.blocodenotas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC
import android.hardware.biometrics.BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL
import android.icu.lang.UCharacter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import android.widget.*
import androidx.annotation.AnyThread
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AUTHENTICATION_RESULT_TYPE_UNKNOWN
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.blocodenotas.databinding.ActivityCriarNotaBinding
import com.app.blocodenotas.databinding.ActivityMainBinding
import com.app.treino.RecyclerViewAdapter
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import java.text.FieldPosition
import java.util.*
import javax.crypto.Cipher
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()  {

    private var titlesList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()

    lateinit var preferences: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var dbRef: DatabaseReference

    private lateinit var rv : RecyclerView
     var title : String? = null



    private lateinit var userArrayList : ArrayList<Anotation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        auth = Firebase.auth

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Notas simples")

        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        rv = findViewById(R.id.rv_home)

        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        userArrayList = arrayListOf<Anotation>()



        getAnotationData(idAnotation = "")
        closeAnotation()


    }

     private fun closeAnotation(){

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

                         val token = task.result


                         userArrayList.removeAt(viewHolder.adapterPosition)

                         rv.adapter?.notifyItemRemoved(viewHolder.adapterPosition)

                     }

             }


         }

         val itemTouchHelper = ItemTouchHelper(swipeHandler)
         itemTouchHelper.attachToRecyclerView(rv)

     }

    


    private fun getAnotationData(idAnotation: String) {

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task->

                val token = task.result

                var id  = Random()
                var num = id.nextInt(10000) + 1

        dbRef = FirebaseDatabase.getInstance().getReference(Firebase.auth.currentUser!!.uid)

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (Snapshot in snapshot.children){

                        val anotacoes = Snapshot.getValue(Anotation::class.java)

                        userArrayList.add(anotacoes!!)
                    }

                    rv.adapter = RecyclerViewAdapter(userArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
         }
    }



    fun dialogLogin() {
        val view = View.inflate(this, R.layout.dialg_login, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val btnSeguir = view.findViewById<LinearLayout>(R.id.btnSeguir)


        btnSeguir.setOnClickListener {
            promptBiometricChecker(
                "Acesso a notas privadas",
                "Use sua digital",
                "Cancelar",
                confirmationRequired = true,
                null,
                { result ->

                    when (result.authenticationType) {
                        AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> {
                            val intent = Intent(this, NotasPrivadas::class.java)
                            startActivity(intent)
                        }
                        AUTHENTICATION_RESULT_TYPE_UNKNOWN -> {

                        }
                        AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> {

                        }
                    }

                },
                { error, errorMsg ->
                    Toast.makeText(
                        this,
                        "Acesso negado!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                })
            dialog.dismiss()
        }


    }

    private fun promptBiometricChecker(
        title: String,
        message: String? = null, // OPCIONAL - SE QUISER EXIBIR UMA MENSAGEM
        negativeLabel: String,
        confirmationRequired: Boolean = true,
        initializedCipher: Cipher? = null, // OPICIONAL - SE VC MESMO(SUA APP) QUISER MANTER O CONTROLE SOBRE OS ACESSOS
        onAuthenticationSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onAuthenticationError: (Int, String) -> Unit
    ) {

        val executor = ContextCompat.getMainExecutor(applicationContext)
        val prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                Timber.d("Authenticado com sucesso, acesso permitido!")
                onAuthenticationSuccess(result)
            }

            override fun onAuthenticationError(errorCode: Int, errorMessage: CharSequence) {
                Timber.d("Acesso negado! Alguem ta tentando usar teu celular!")
                onAuthenticationError(errorCode, errorMessage.toString())
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .apply { if (message != null) setDescription(message) }
            .setConfirmationRequired(confirmationRequired)
            .setNegativeButtonText(negativeLabel)
            .build()

        initializedCipher?.let {
            val cryptoObject = BiometricPrompt.CryptoObject(initializedCipher)
            prompt.authenticate(promptInfo, cryptoObject)
            return
        }

        prompt.authenticate(promptInfo)

    }

    fun dialogCriar() {
        val view = View.inflate(this, R.layout.dialg_password, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnSeguir = view.findViewById<Button>(R.id.btnSeguirCriar)

        val voltar = view.findViewById<ImageView>(R.id.voltar)
        voltar.setOnClickListener {
            dialogLogin()
            dialog.dismiss()
        }

        btnSeguir.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.edtEmailCriar)
            val password = view.findViewById<EditText>(R.id.edtPasswordCriar)


            if (email.text.toString().isBlank() || password.text.toString().isBlank()) {
                Toast.makeText(
                    this,
                    "campo obrigatório em branco!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                recupereDados(email.text.toString(), password.text.toString())

            }

        }

    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, NotasPrivadas::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("erroFF", task.exception.toString())
                    val erro = task.exception.toString()
                    errosFirebse(erro)

                }
            }


    }

    private fun recupereDados(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth!!.currentUser?.let { addUserToDatabase(email, password) }

                    val intent = Intent(this, NotasPrivadas::class.java)
                    startActivity(intent)

                    Toast.makeText(
                        this, "User Create Success",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.d("erroFF", task.exception.toString())
                    val erro = task.exception.toString()
                    errosFirebse(erro)

                }


            }
    }

    private fun addUserToDatabase(email: String, password: String) {

        dbRef = FirebaseDatabase.getInstance().getReference()

        dbRef.child("user").setValue(User(email, password))

    }

    private fun singOut(){

        if (FirebaseAuth.getInstance().signOut() != null){

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }


    }

    private fun errosFirebse(erro: String) {
        if (erro.contains("The email address is badly formatted.")) {
            Toast.makeText(
                this,
                "Formato de email inválido!",
                Toast.LENGTH_LONG
            ).show()

        }


        if (erro.contains("The password is invalid or the user does not have a password.")) {
            Toast.makeText(
                this,
                "Senha Incorreta!",
                Toast.LENGTH_LONG
            ).show()

        }

        if (erro.contains("The password is invalid or the user does not have a password.")) {
            Toast.makeText(
                this,
                "Senha Incorreta!",
                Toast.LENGTH_LONG
            ).show()

        }



        if (erro.contains("There is no user record corresponding to this identifier")) {
            Toast.makeText(
                this,
                "e-mail não cadastrado na base de dados",
                Toast.LENGTH_LONG
            ).show()

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menuInflater.inflate(R.menu.menu2, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.mais -> {
            val intent = Intent(this@MainActivity, CriarNota::class.java)
            startActivity(intent)
            true
        }



        R.id.anotaca_privada -> {

            dialogLogin()
            true

        }

        R.id.sair -> {

            singOut()
            true

        }


        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}

private fun Any.removeValue(s: String) {

}

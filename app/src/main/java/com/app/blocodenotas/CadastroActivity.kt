package com.app.blocodenotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.blocodenotas.databinding.ActivityCadastroBinding
import com.app.blocodenotas.databinding.ActivityLoginVersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        binding.tvJaTemConta.setOnClickListener {
            val intent = Intent(this, LoginVers::class.java)
            startActivity(intent)
        }

        binding.tvJaTemConta.setColouredSpan(
            "Já tem conta? Entrar.",
            14,21, lazy {
                ContextCompat.getColor(
                    this,
                    R.color.purple_200
                )
            }.value

        )
        binding.btnCadastrar.setOnClickListener {

            if (binding.edtEmail.text.toString().isBlank() || binding.edtSenha.text.toString().isBlank()) {
                Toast.makeText(
                    this,
                    "campo obrigatório em branco!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                recupereDados(binding.edtEmail.text.toString(), binding.edtSenha.text.toString())

            }
        }

    }

    private fun recupereDados(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let { addUserToDatabase(email, password) }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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


        if (erro.contains("There is no user record corresponding to this identifier")) {
            Toast.makeText(
                this,
                "e-mail não cadastrado na base de dados!",
                Toast.LENGTH_LONG
            ).show()

        }

        if (erro.contains("The email address is already in use by another account")) {
            Toast.makeText(
                this,
                "e-mail já está sendo usado!",
                Toast.LENGTH_LONG
            ).show()

        }


    }

}
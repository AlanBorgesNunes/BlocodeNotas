package com.app.blocodenotas

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.blocodenotas.databinding.ActivityLoginBinding
import com.app.blocodenotas.databinding.ActivityLoginVersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginVers : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

     private lateinit var binding: ActivityLoginVersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.mudarSenha.setOnClickListener {
            dialogCriar()
        }

        binding.tvCriarConta.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        binding.tvCriarConta.setColouredSpan(
            "Ainda nao tem conta? Criar conta",
            21, 32, lazy {
                ContextCompat.getColor(
                    this,
                    R.color.purple_200
                )
            }.value
        )


        binding.login.setOnClickListener {

            if (binding.edtEmailLogin.text.toString()
                    .isBlank() || binding.edtSenhaLogin.text.toString().isBlank()
            ) {
                Toast.makeText(
                    this,
                    "campo obrigatório em branco!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                login(
                        binding.edtEmailLogin.text.toString(),
                        binding.edtSenhaLogin.text.toString()
                    )
            }


        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("erroFF", task.exception.toString())
                    val erro = task.exception.toString()
                    errosFirebse(erro)

                }
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

    fun dialogCriar() {
        val view = View.inflate(this, R.layout.dialg_forget_password, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)





    }

}

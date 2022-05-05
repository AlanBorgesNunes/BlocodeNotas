package com.app.blocodenotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.app.blocodenotas.databinding.ActivityLoginBinding
import com.app.blocodenotas.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val user = Firebase.auth.currentUser?.uid
        if (user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewPager2 = findViewById(R.id.viewPager_image)

        val slideItems: MutableList<SliderItem> = ArrayList()
        slideItems.add(SliderItem(R.drawable.ic_slide1))
        slideItems.add(SliderItem(R.drawable.ic_slide2ui))
        slideItems.add(SliderItem(R.drawable.ic_slide3hjjj))

        viewPager2.adapter = SliderAdapter(slideItems, viewPager2)

        binding.entrar.setOnClickListener {
            val intent = Intent(this, LoginVers::class.java)
            startActivity(intent)

        }

        binding.btnCriarConta.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

    }
}
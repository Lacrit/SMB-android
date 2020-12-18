package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.utilities.SharedPrefs
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefs(this);
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance();
        binding.buttonRegister.setOnClickListener{
            auth.createUserWithEmailAndPassword(binding.login.text.toString(), binding.password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registration complete successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Registration failure.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.buttonLogin.setOnClickListener{
            auth.signInWithEmailAndPassword(binding.login.text.toString(), binding.password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Login complete successfully.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Wrong email or password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
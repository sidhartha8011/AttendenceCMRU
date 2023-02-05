package com.example.attendencecmru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.attendencecmru.register.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button:Button=findViewById(R.id.loginButton)

        val auth=FirebaseAuth.getInstance()

        val tf1:EditText=findViewById(R.id.edittext_email)
        val tf2:EditText=findViewById(R.id.edit_textpass)

        val memail="master@gmail.com"
        val mpass="master123"


        button.setOnClickListener {
            val email=tf1.toString()
            val pass=tf2.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                Toast.makeText(this,"credentials cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(email==memail && pass==mpass) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent= Intent(this, MasterLogin::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }


            auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        val database= FirebaseDatabase.getInstance().getReference("Users")
                        database.child(auth.currentUser!!.uid).get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()){
                                Toast.makeText(this,"login success", Toast.LENGTH_SHORT).show()
                                val i=Intent(this,TW::class.java)
                                startActivity(i)
                            }
                            else{
                                val intent= Intent(this, RegistrationActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                    else{
                        Toast.makeText(this,it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}
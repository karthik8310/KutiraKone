package com.example.kutirakone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val etName =
            findViewById<EditText>(R.id.etName)

        val etPhone =
            findViewById<EditText>(R.id.etPhone)

        val etDob =
            findViewById<EditText>(R.id.etDob)

        val etEmail =
            findViewById<EditText>(R.id.etEmail)

        val etPassword =
            findViewById<EditText>(R.id.etPassword)

        val btnRegister =
            findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val name =
                etName.text.toString().trim()

            val phone =
                etPhone.text.toString().trim()

            var dob =
                etDob.text.toString().trim()

            val email =
                etEmail.text.toString().trim()

            val password =
                etPassword.text.toString().trim()

            // FIX DOUBLE // ISSUE
            dob = dob.replace(Regex("/+"), "/")

            // EMPTY CHECK
            if (
                name.isEmpty() ||
                phone.isEmpty() ||
                dob.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }



// AUTO FORMAT DOB
            if (!dob.contains("/") && dob.length == 8) {

                dob =
                    dob.substring(0, 2) + "/" +
                            dob.substring(2, 4) + "/" +
                            dob.substring(4, 8)
            }

// REMOVE EXTRA //
            dob = dob.replace(Regex("/+"), "/")

// FINAL VALIDATION
            if (dob.length != 10 || !dob.contains("/")) {

                Toast.makeText(
                    this,
                    "Use DOB format DD/MM/YYYY",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid =
                        auth.currentUser!!.uid

                    val userMap = hashMapOf(

                        "name" to name,
                        "phone" to phone,
                        "dob" to dob,
                        "email" to email
                    )

                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .set(userMap)

                        .addOnSuccessListener {

                            Toast.makeText(
                                this,
                                "Registration Successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(
                                Intent(
                                    this,
                                    MainActivity::class.java
                                )
                            )

                            finish()
                        }

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
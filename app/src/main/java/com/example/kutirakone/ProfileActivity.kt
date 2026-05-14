package com.example.kutirakone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        val txtUserName =
            findViewById<TextView>(R.id.txtUserName)

        val txtEmail =
            findViewById<TextView>(R.id.txtEmail)

        val txtPhone =
            findViewById<TextView>(R.id.txtPhone)

        val txtDob =
            findViewById<TextView>(R.id.txtDob)

        val txtUploads =
            findViewById<TextView>(R.id.txtUploads)

        val txtExchanges =
            findViewById<TextView>(R.id.txtExchanges)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        val user =
            FirebaseAuth.getInstance().currentUser

        val uid = user?.uid

        if (uid != null) {

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()

                .addOnSuccessListener { document ->

                    txtUserName.text =
                        document.getString("name")

                    txtEmail.text =
                        document.getString("email")

                    txtPhone.text =
                        document.getString("phone")

                    txtDob.text =
                        document.getString("dob")
                }
        }

        FirebaseFirestore.getInstance()
            .collection("fabrics")
            .get()

            .addOnSuccessListener { documents ->

                txtUploads.text =
                    documents.size().toString()
            }

        FirebaseFirestore.getInstance()
            .collection("fabrics")
            .whereEqualTo("exchangeType", "Swap")
            .get()

            .addOnSuccessListener { documents ->

                txtExchanges.text =
                    documents.size().toString()
            }

        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNav
            )

        bottomNav.selectedItemId =
            R.id.nav_profile

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

                    startActivity(
                        Intent(
                            this,
                            HomeActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_map -> {

                    startActivity(
                        Intent(
                            this,
                            MapActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_wishlist -> {

                    startActivity(
                        Intent(
                            this,
                            WishlistActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_upload -> {

                    startActivity(
                        Intent(
                            this,
                            UploadActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_profile -> {

                    true
                }

                else -> false
            }
        }

        btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }
    }
}
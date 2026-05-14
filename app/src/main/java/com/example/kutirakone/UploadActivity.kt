package com.example.kutirakone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.android.material.bottomnavigation.BottomNavigationView

class UploadActivity : AppCompatActivity() {

    private lateinit var imgFabric: ImageView

    private lateinit var btnSelectImage: Button

    private lateinit var etFabricName: EditText
    private lateinit var etFabricType: EditText
    private lateinit var etColor: EditText
    private lateinit var etPrice: EditText
    private lateinit var etQuantity: EditText

    private lateinit var rbBuy: RadioButton
    private lateinit var rbSwap: RadioButton

    private lateinit var btnUploadFabric: Button

    private var imageUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                imageUri = result.data?.data

                imgFabric.setImageURI(imageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_upload)

        imgFabric = findViewById(R.id.imgFabric)

        btnSelectImage =
            findViewById(R.id.btnSelectImage)

        etFabricName =
            findViewById(R.id.etFabricName)

        etFabricType =
            findViewById(R.id.etFabricType)

        etColor =
            findViewById(R.id.etColor)

        etPrice =
            findViewById(R.id.etPrice)

        etQuantity =
            findViewById(R.id.etQuantity)

        rbBuy =
            findViewById(R.id.radioBuy)

        rbSwap =
            findViewById(R.id.radioSwap)

        btnUploadFabric =
            findViewById(R.id.btnUploadFabric)

        btnSelectImage.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"

            galleryLauncher.launch(intent)
        }

        btnUploadFabric.setOnClickListener {

            uploadFabric()
        }

        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNav
            )

        bottomNav.selectedItemId =
            R.id.nav_upload

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

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(
                            this,
                            ProfileActivity::class.java
                        )
                    )

                    true
                }

                else -> false
            }
        }
    }

    private fun uploadFabric() {

        val fabricName =
            etFabricName.text.toString().trim()

        val fabricType =
            etFabricType.text.toString()
                .trim()
                .lowercase()

        val color =
            etColor.text.toString().trim()

        val price =
            etPrice.text.toString().trim()

        val quantity =
            etQuantity.text.toString().trim()

        val exchangeType =
            if (rbBuy.isChecked)
                "Buy"
            else
                "Swap"

        if (
            fabricName.isEmpty() ||
            fabricType.isEmpty() ||
            color.isEmpty() ||
            price.isEmpty() ||
            quantity.isEmpty() ||
            imageUri == null
        ) {

            Snackbar.make(
                btnUploadFabric,
                "Please fill all fields and select image",
                Snackbar.LENGTH_SHORT
            ).show()

            return
        }

        val storageReference =
            FirebaseStorage.getInstance()
                .reference
                .child(
                    "fabric_images/" +
                            System.currentTimeMillis() +
                            ".jpg"
                )

        imageUri?.let { uri ->

            storageReference.putFile(uri)

                .addOnSuccessListener {

                    storageReference.downloadUrl
                        .addOnSuccessListener { downloadUri ->

                            val fabric = Fabric(

                                fabricName = fabricName,

                                fabricType = fabricType,

                                color = color,

                                price = price,

                                quantity = quantity,

                                exchangeType = exchangeType,

                                imageUrl =
                                    downloadUri.toString(),

                                distance = (2..20).random()
                            )

                            FirebaseFirestore.getInstance()
                                .collection("fabrics")
                                .add(fabric)

                                .addOnSuccessListener {

                                    Snackbar.make(
                                        btnUploadFabric,
                                        "Fabric uploaded successfully",
                                        Snackbar.LENGTH_LONG
                                    ).show()

                                    etFabricName.text.clear()

                                    etFabricType.text.clear()

                                    etColor.text.clear()

                                    etPrice.text.clear()

                                    etQuantity.text.clear()

                                    rbBuy.isChecked = true

                                    imgFabric.setImageResource(
                                        android.R.drawable.ic_menu_gallery
                                    )
                                }
                        }
                }

                .addOnFailureListener { e ->

                    Snackbar.make(
                        btnUploadFabric,
                        e.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
        }
    }
}
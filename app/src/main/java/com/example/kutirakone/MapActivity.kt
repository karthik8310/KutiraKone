package com.example.kutirakone

import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : AppCompatActivity() {

    private lateinit var txtDistance: TextView

    private lateinit var txtNearby: TextView

    private val fabricList = ArrayList<Pair<Fabric, Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)

        val seekDistance =
            findViewById<SeekBar>(R.id.seekDistance)

        txtDistance =
            findViewById(R.id.txtDistance)

        txtNearby =
            findViewById(R.id.txtNearby)

        fetchNearbyFabrics()
        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNav
            )

        bottomNav.selectedItemId = R.id.nav_map

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

        seekDistance.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    txtDistance.text =
                        "Showing within $progress km"

                    showNearbyFabrics(progress)
                }

                override fun onStartTrackingTouch(
                    seekBar: SeekBar?
                ) {
                }

                override fun onStopTrackingTouch(
                    seekBar: SeekBar?
                ) {
                }
            }
        )
    }

    private fun fetchNearbyFabrics() {

        FirebaseFirestore.getInstance()
            .collection("fabrics")
            .get()

            .addOnSuccessListener { documents ->

                fabricList.clear()

                for (document in documents) {

                    val fabric =
                        document.toObject(Fabric::class.java)

                    fabricList.add(
                        Pair(fabric, fabric.distance)
                    )
                }

                showNearbyFabrics(5)
            }
    }

    private fun showNearbyFabrics(maxDistance: Int) {

        val nearbyItems = ArrayList<String>()

        for (item in fabricList) {

            val fabric = item.first

            val distance = item.second

            if (distance <= maxDistance) {

                nearbyItems.add(
                    "${fabric.fabricName} - $distance km away"
                )
            }
        }

        if (nearbyItems.isEmpty()) {

            txtNearby.text =
                "No fabrics found nearby"

        } else {

            txtNearby.text =
                nearbyItems.joinToString("\n")
        }
    }
}
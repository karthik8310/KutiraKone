package com.example.kutirakone

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class WishlistActivity : AppCompatActivity() {

    private lateinit var recyclerWishlist: RecyclerView
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: FabricAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wishlist)

        recyclerWishlist =
            findViewById(R.id.recyclerWishlist)

        txtEmpty =
            findViewById(R.id.txtEmpty)

        recyclerWishlist.layoutManager =
            LinearLayoutManager(this)

        adapter = FabricAdapter(WishlistManager.wishlist)

        recyclerWishlist.adapter = adapter

        updateWishlist()

        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNav
            )

        bottomNav.selectedItemId =
            R.id.nav_wishlist

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
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()

        updateWishlist()
    }

    private fun updateWishlist() {

        if (WishlistManager.wishlist.isEmpty()) {

            txtEmpty.visibility = View.VISIBLE
            recyclerWishlist.visibility = View.GONE

        } else {

            txtEmpty.visibility = View.GONE
            recyclerWishlist.visibility = View.VISIBLE
        }
    }
}
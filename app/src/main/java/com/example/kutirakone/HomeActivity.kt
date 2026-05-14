package com.example.kutirakone

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var chipGroupCategories: ChipGroup

    private lateinit var fabricList: ArrayList<Fabric>

    private lateinit var originalFabricList: ArrayList<Fabric>

    private lateinit var fabricAdapter: FabricAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerViewFabrics)

        chipGroupCategories =
            findViewById(R.id.chipGroupCategories)

        recyclerView.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(
                this,
                2
            )

        recyclerView.itemAnimator =
            DefaultItemAnimator()

        fabricList = ArrayList()

        originalFabricList = ArrayList()

        fabricAdapter = FabricAdapter(fabricList)

        recyclerView.adapter = fabricAdapter

        fetchFabrics()



        val etSearch =
            findViewById<EditText>(R.id.etSearch)




        // SEARCH BUTTON ON KEYBOARD

        etSearch.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                searchFabric(
                    etSearch.text.toString()
                )

                etSearch.clearFocus()

                true

            } else {

                false
            }
        }

        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNav
            )

        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

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

                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
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

                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    )

                    true
                }

                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        fetchFabrics()
    }

    private fun fetchFabrics() {

        FirebaseFirestore.getInstance()
            .collection("fabrics")
            .get()

            .addOnSuccessListener { documents ->

                fabricList.clear()

                originalFabricList.clear()

                for (document in documents) {

                    val fabric =
                        document.toObject(Fabric::class.java)

                    fabricList.add(fabric)

                    originalFabricList.add(fabric)
                }

                setupCategoryChips()

                fabricAdapter.notifyDataSetChanged()
            }
    }

    private fun setupCategoryChips() {

        chipGroupCategories.removeAllViews()

        val allChip = Chip(this)

        allChip.text = "All"

        allChip.isCheckable = true

        allChip.isChecked = true

        chipGroupCategories.addView(allChip)

        allChip.setOnClickListener {

            fabricList.clear()

            fabricList.addAll(originalFabricList)

            fabricAdapter.notifyDataSetChanged()
        }

        val categories = mutableSetOf<String>()

        for (fabric in originalFabricList) {

            categories.add(fabric.fabricType)
        }

        for (category in categories) {

            val chip = Chip(this)

            chip.text =
                category.replaceFirstChar {
                    it.uppercase()
                }

            chip.isCheckable = true

            chipGroupCategories.addView(chip)

            chip.setOnClickListener {

                filterCategory(category)
            }
        }
    }

    private fun filterCategory(category: String) {

        fabricList.clear()

        for (fabric in originalFabricList) {

            if (
                fabric.fabricType.lowercase()
                == category.lowercase()
            ) {

                fabricList.add(fabric)
            }
        }

        fabricAdapter.notifyDataSetChanged()
    }

    private fun searchFabric(query: String) {

        fabricList.clear()

        for (fabric in originalFabricList) {

            if (
                fabric.fabricName.lowercase()
                    .contains(query.lowercase())

                ||

                fabric.fabricType.lowercase()
                    .contains(query.lowercase())
            ) {

                fabricList.add(fabric)
            }
        }

        fabricAdapter.notifyDataSetChanged()
    }
}
package com.example.kutirakone

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class FabricDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fabric_detail)

        val detailImage = findViewById<ImageView>(R.id.detailImage)
        val detailName = findViewById<TextView>(R.id.detailName)
        val detailType = findViewById<TextView>(R.id.detailType)
        val detailColor = findViewById<TextView>(R.id.detailColor)
        val detailQuantity = findViewById<TextView>(R.id.detailQuantity)
        val detailPrice = findViewById<TextView>(R.id.detailPrice)

        val btnBuy = findViewById<Button>(R.id.btnBuy)
        val btnExchange = findViewById<Button>(R.id.btnExchange)

        // Get data from intent
        val fabricName = intent.getStringExtra("fabricName")
        val fabricType = intent.getStringExtra("fabricType")
        val fabricColor = intent.getStringExtra("color")
        val fabricQuantity = intent.getStringExtra("quantity")
        val fabricPrice = intent.getStringExtra("price")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set text data
        detailName.text = fabricName
        detailType.text = fabricType
        detailColor.text = "Colors: $fabricColor"
        detailQuantity.text = "$fabricQuantity pieces"
        detailPrice.text = "₹$fabricPrice"

        // Load image using Glide
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.fabric_placeholder)
            .error(R.drawable.fabric_placeholder)
            .into(detailImage)

        // Buy button
        btnBuy.setOnClickListener {

            Toast.makeText(
                this,
                "Fabric Purchase Request Sent",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Exchange button
        btnExchange.setOnClickListener {

            Toast.makeText(
                this,
                "Exchange Request Sent",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
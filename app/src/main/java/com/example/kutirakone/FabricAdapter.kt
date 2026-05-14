package com.example.kutirakone

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.bumptech.glide.Glide

class FabricAdapter(
    private val fabricList: ArrayList<Fabric>
) : RecyclerView.Adapter<FabricAdapter.FabricViewHolder>() {

    class FabricViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgFabric: ImageView =
            itemView.findViewById(R.id.imgFabric)

        val txtFabricName: TextView =
            itemView.findViewById(R.id.txtFabricName)

        val txtFabricType: TextView =
            itemView.findViewById(R.id.txtFabricType)

        val txtPrice: TextView =
            itemView.findViewById(R.id.txtPrice)

        val txtBadge: TextView =
            itemView.findViewById(R.id.txtBadge)

        val txtQuantity: TextView =
            itemView.findViewById(R.id.txtQuantity)

        val btnBuy: Button =
            itemView.findViewById(R.id.btnBuy)

        val btnSwap: Button =
            itemView.findViewById(R.id.btnSwap)

        val btnWishlist: ImageButton =
            itemView.findViewById(R.id.btnWishlist)

        val txtExchangeType: TextView =
            itemView.findViewById(R.id.txtExchangeType)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FabricViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fabric, parent, false)

        return FabricViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FabricViewHolder,
        position: Int
    ) {

        val fabric = fabricList[position]

        holder.txtExchangeType.text =
            fabric.exchangeType.uppercase()

        holder.txtFabricName.text = fabric.fabricName

        holder.txtFabricType.text =
            fabric.fabricType.replaceFirstChar {
                it.uppercase()
            }

        holder.txtPrice.text = "₹${fabric.price}"

        holder.txtBadge.text = "NEAR YOU"

        holder.txtQuantity.text =
            "${fabric.distance} km away"

        Glide.with(holder.itemView.context)
            .load(fabric.imageUrl)
            .placeholder(R.drawable.fabric_placeholder)
            .error(R.drawable.fabric_placeholder)
            .centerInside()
            .into(holder.imgFabric)
        // WISHLIST STATE

        if (WishlistManager.wishlist.contains(fabric)) {

            holder.btnWishlist.setImageResource(
                R.drawable.ic_wishlist_on
            )

        } else {

            holder.btnWishlist.setImageResource(
                R.drawable.ic_wishlist_off
            )
        }

        // WISHLIST CLICK

        holder.btnWishlist.setOnClickListener {

            if (WishlistManager.wishlist.contains(fabric)) {

                WishlistManager.wishlist.remove(fabric)

                holder.btnWishlist.setImageResource(
                    R.drawable.ic_wishlist_off
                )

                Snackbar.make(
                    holder.itemView,
                    "${fabric.fabricName} removed from wishlist",
                    Snackbar.LENGTH_SHORT
                ).show()

                notifyDataSetChanged()

            } else {

                WishlistManager.wishlist.add(fabric)

                holder.btnWishlist.setImageResource(
                    R.drawable.ic_wishlist_on
                )

                Snackbar.make(
                    holder.itemView,
                    "${fabric.fabricName} added to wishlist",
                    Snackbar.LENGTH_SHORT
                ).show()

                notifyDataSetChanged()
            }
        }

        // BUY

        holder.btnBuy.setOnClickListener {

            Snackbar.make(
                holder.itemView,
                "Order placed successfully",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        // EXCHANGE

        holder.btnSwap.setOnClickListener {

            Snackbar.make(
                holder.itemView,
                "Exchange request sent",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        // DETAIL PAGE

        holder.itemView.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                FabricDetailActivity::class.java
            )

            intent.putExtra(
                "fabricName",
                fabric.fabricName
            )

            intent.putExtra(
                "fabricType",
                fabric.fabricType
            )

            intent.putExtra(
                "color",
                fabric.color
            )

            intent.putExtra(
                "quantity",
                fabric.quantity
            )

            intent.putExtra(
                "price",
                fabric.price
            )

            intent.putExtra(
                "imageUrl",
                fabric.imageUrl
            )

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return fabricList.size
    }
}
package com.example.kutirakone

object WishlistManager {

    val wishlist = ArrayList<Fabric>()

    fun addFabric(fabric: Fabric) {

        if (!wishlist.contains(fabric)) {

            wishlist.add(fabric)
        }
    }

    fun removeFabric(fabric: Fabric) {

        wishlist.remove(fabric)
    }
}
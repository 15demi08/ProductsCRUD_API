package com.example.myapplication.model

class ProductRepo {

    companion object {

        private var repo = ArrayList<Product>()

        var selectedItem:Int? = null

        fun add( p:Product ) = repo.add(p)

        fun get( i:Int ):Product = repo.get(i)

        fun remove() = repo.removeAt(selectedItem!!)

        fun size():Int = repo.size

        fun flush() = repo.removeAll(repo)

    }

}
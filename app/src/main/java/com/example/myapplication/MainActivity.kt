package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Product
import com.example.myapplication.model.ProductAdapter
import com.example.myapplication.model.ProductRepo
import com.example.myapplication.model.ProductRepo2
import com.example.myapplication.service.ProductClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), ProductAdapter.PACL {

    companion object {

        val REQ_NEW = 1
        val REQ_EDIT = 2

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProductClient().getAll(

            onSuccess = { products ->

                Log.i("Product API", "onCreate()")

                ProductRepo2.repo = products!!

                var manager = LinearLayoutManager(this)
                var productAdapter = ProductAdapter()

                productAdapter.clickListener = this

                findViewById<RecyclerView>(R.id.productList).apply {
                    setHasFixedSize(true)
                    layoutManager = manager
                    adapter = productAdapter
                }

            },
            onFailure = {
                showSnackbar(it!!)
            }
        )

        findViewById<FloatingActionButton>(R.id.fabNewProduct).setOnClickListener {
            newProduct(it)
        }

    }

    fun loadItems(){

        Log.i("Product API", "loadItems()")

        ProductClient().getAll(
            onSuccess = { products ->
                ProductRepo2.repo = products!!
                findViewById<RecyclerView>(R.id.productList).adapter?.notifyDataSetChanged()
            },
            onFailure = {
                showSnackbar(it!!)
            }
        )

    }

    fun newProduct( v:View ){

        startActivityForResult( Intent(this, ProductForm::class.java).apply { action = "new" }, REQ_NEW )

    }

    override fun onItemClicked(v: View, p: Int) { // Edit item

        val id = ProductRepo2.repo.get(p).id
        startActivityForResult( Intent(this, ProductForm::class.java).apply { action = "edit" }.putExtra("productID", id), REQ_EDIT )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1 ) {

            showSnackbar(R.string.prodList_msg_canceled)

        } else {

            when (requestCode) {

                REQ_NEW -> {

                    when (resultCode) {

                        0 -> showSnackbar(data?.getIntExtra("message", 0)!!)
                        1 -> showSnackbar(R.string.prodList_msg_saved)

                    }

                }
                REQ_EDIT -> {

                    when (resultCode) {

                        0 -> showSnackbar(data?.getIntExtra("message", 0)!!)
                        1 -> showSnackbar(R.string.prodList_msg_modified)
                        2 -> showSnackbar(R.string.prodList_msg_deleted)

                    }

                }

            }

            loadItems()
            findViewById<RecyclerView>(R.id.productList).adapter?.notifyDataSetChanged()

        }

    }

    private fun showSnackbar( msg:Int ) = Snackbar.make(findViewById(R.id.layoutFABContainer), msg, Snackbar.LENGTH_SHORT ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()

}
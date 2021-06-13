package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.model.Product
import com.example.myapplication.service.ProductClient

class ProductForm : AppCompatActivity() {

    var currentProduct:Product? = null

    lateinit var btnDelete:Button
    lateinit var lblProductID:TextView
    lateinit var txtvProductID:TextView
    lateinit var txteProductName:EditText
    lateinit var txteProductPrice:EditText
    lateinit var lblProductFormTitle:TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_form)

        initScreen()

    }

    private fun initScreen() {

        btnDelete = findViewById(R.id.btnDelete)
        lblProductID = findViewById(R.id.lblProductID)
        txtvProductID = findViewById(R.id.txtvProductID)
        txteProductName = findViewById(R.id.txteProductName)
        txteProductPrice = findViewById(R.id.txteProductPrice)
        lblProductFormTitle = findViewById(R.id.lblProductFormTitle)

        when( intent.action ){

            "new" -> {
                btnDelete.visibility = Button.GONE
                lblProductID.visibility = TextView.GONE
                txtvProductID.visibility = TextView.GONE
            }
            "edit" -> {

                ProductClient().getByID(

                    intent.getIntExtra("productID", 0),
                    onSuccess = { product ->

                        currentProduct = product
                        txtvProductID.text = product?.id.toString()
                        txteProductName.setText(product?.name)
                        txteProductPrice.setText(product?.price.toString())

                    },
                    onFailure = {
                        result(0, Intent().putExtra("message", it))
                    }
                )

            }

        }

        lblProductFormTitle.text = getString(R.string.prodForm_title, intent.action?.capitalize()) // String with Placeholder: '%1$s Product'. %1 is the index, $s denotes String

    }

    fun cancel( v:View ){

        this.result(-1)

    }

    fun save( v:View ){

        when( intent.action ){

            "new" -> {

                currentProduct = Product(
                    txteProductName.text.toString(),
                    txteProductPrice.text.toString().toDouble()
                )

            }
            "edit" -> {

                currentProduct?.apply {
                    name = txteProductName.text.toString()
                    price = txteProductPrice.text.toString().toDouble()
                }

            }

        }

        ProductClient().save(
            currentProduct!!,
            onSuccess = {
                result(1)
            },
            onFailure = {
                result(0, Intent().putExtra("message", it!!))
            }
        )

    }

    fun delete( v:View ){

        ProductClient().delete(
            currentProduct!!.id!!,
            onSuccess = {
                result(2)
            },
            onFailure = {
                result(0, Intent().putExtra("message", it!!))
            }
        )

    }

    private fun result( code:Int, data:Intent? = null ){

        if(data is Intent) setResult(code, data)
        else setResult(code)

        finish()

    }

}
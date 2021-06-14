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

        var titlePieceID = if(intent.action == "new") R.string.prodForm_titlePiece_new else R.string.prodForm_titlePiece_edit

        // R.string.prodForm_title is a String with Placeholders: '%1$s %2$s'. %1 and %2 are indexes, $s denotes String. Result is "[New|Edit] Product", localized
        lblProductFormTitle.text = getString(R.string.prodForm_title, getString(titlePieceID), getString(R.string.prodForm_titlePiece_product)  )

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
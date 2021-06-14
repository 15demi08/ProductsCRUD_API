package com.example.myapplication.service

import android.content.res.Resources
import com.example.myapplication.R
import com.example.myapplication.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductClient( val productAPI:ProductAPI? = RetrofitBase().service ) {

    /**
     * Returns all Products currently saved in the database
     */
    fun getAll(
        onSuccess: ( products:ArrayList<Product>? ) -> Unit,
        onFailure: ( message:Int? ) -> Unit
    ){

        productAPI?.getAll()?.enqueue(productsListCallback(onSuccess, onFailure))

    }

    /**
     * Returns a single Product, by its id. Fails with a message on error
     */
    fun getByID(
        id: Int,
        onSuccess: ( product: Product? ) -> Unit,
        onFailure: ( message:Int? ) -> Unit
    ){

        productAPI?.getByID(id)?.enqueue(productCallback(onSuccess, onFailure))

    }

    /**
     * Creates a new Product or Updates an existing one
     */
    fun save(
        product:Product,
        onSuccess: ( p:Product? ) -> Unit,
        onFailure: ( message:Int? ) -> Unit
    ){

        if( product.id == null ){ // Saving a new product to the database

            productAPI?.new(product)?.enqueue(productCallback(onSuccess, onFailure))

        } else { // Updating an existing product

            productAPI?.update(product)?.enqueue(productCallback(onSuccess, onFailure))

        }

    }

    /**
     * Deletes a product from the Database
     */
    fun delete(
        id:Int,
        onSuccess: ( product:Product? ) -> Unit,
        onFailure: ( message:Int? ) -> Unit
    ){

        productAPI?.delete(id)?.enqueue(productCallback(onSuccess, onFailure))

    }

    /**
     * Returns a Callback object based on a Product
     */
    private fun productCallback(
        onSuccess: ( product:Product? ) -> Unit,
        onFailure: ( message:Int? ) -> Unit
    ):Callback<Product>{

        return object:Callback<Product>{

            override fun onResponse( call: Call<Product>, response: Response<Product> ) {
                if(response.isSuccessful)
                    onSuccess( response.body() ) // A "Product" is always returned. The caller method can simply do nothing with it, however
                else
                    onFailure( R.string.prodList_msg_unknownError )

            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                onFailure( R.string.prodList_msg_unknownError )
            }

        }

    }

    /**
     * Returns a Callback object based on an ArrayList of Products
     */
    private fun productsListCallback(
        onSuccess: ( products: ArrayList<Product>? ) -> Unit,
        onFailure: ( msg:Int? ) -> Unit
    ):Callback<ArrayList<Product>>{

        return object:Callback<ArrayList<Product>>{

            override fun onResponse( call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>> ) {
                if(response.isSuccessful)
                    onSuccess( response.body() )
                else
                    onFailure( R.string.prodList_msg_unknownError )

            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                onFailure( R.string.prodList_msg_unknownError )
            }

        }

    }

}
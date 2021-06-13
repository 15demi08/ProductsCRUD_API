package com.example.myapplication.service

import com.example.myapplication.model.Product
import retrofit2.Call
import retrofit2.http.*

interface ProductAPI {

    @GET("/products")
    fun getAll():Call<ArrayList<Product>>

    @GET("/products/{id}")
    fun getByID( @Path("id") id:Int ):Call<Product>

    @POST("/products")
    fun new( @Body p:Product ):Call<Product>

    @PUT("/products/{id}")
    fun update( @Body p:Product, @Path("id") id:Int = p.id!!  ):Call<Product>

    @DELETE("/products/{id}")
    fun delete( @Path("id") id:Int ):Call<Product>

}
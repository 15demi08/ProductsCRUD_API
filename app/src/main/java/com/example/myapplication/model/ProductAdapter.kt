package com.example.myapplication.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ProductAdapter():RecyclerView.Adapter<ProductAdapter.PAVH>() {

    var clickListener:PACL? = null

    class PAVH:RecyclerView.ViewHolder { //ProductAdapterViewHolder

        var txtvName:TextView
        var txtvBarcode:TextView
        var txtvPrice:TextView

        constructor( v:View ):super(v) {

            txtvName = v.findViewById(R.id.itemLabel_Name)
            txtvBarcode = v.findViewById(R.id.itemLabel_id)
            txtvPrice = v.findViewById(R.id.itemLabel_Price)

        }

    }

    override fun onCreateViewHolder( parent:ViewGroup, viewType:Int):PAVH {

        val view = LayoutInflater.from(parent.context).inflate( R.layout.product_list_item, parent, false )
        return PAVH(view)

    }

    override fun onBindViewHolder( holder:PAVH, position:Int) {

        var product = ProductRepo2.repo.get(position)

        holder.txtvName.text = product?.name
        holder.txtvBarcode.text = product?.id.toString()
        holder.txtvPrice.text = "$${product?.price}"
        holder.itemView.setOnClickListener {
            clickListener?.onItemClicked(holder.itemView, position)
        }

    }

    override fun getItemCount(): Int = ProductRepo2.repo.size

    interface PACL { // ProductAdapterClickListener
        fun onItemClicked( v:View, p:Int )
    }

}

package com.example.mobile_smart_pantry_project_iv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: MutableList<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tv_id)
        val tvNazwa: TextView = itemView.findViewById(R.id.tv_nazwa)
        val tvIlosc: TextView = itemView.findViewById(R.id.tv_ilosc)
        val tvJednostka: TextView = itemView.findViewById(R.id.tv_jednostka)
        val tvKategoria: TextView = itemView.findViewById(R.id.tv_kategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.tvId.text = product.id
        holder.tvNazwa.text = product.nazwa
        holder.tvIlosc.text = product.ilosc.toString()
        holder.tvJednostka.text = product.jednostka
        holder.tvKategoria.text = product.kategoria
    }

    override fun getItemCount(): Int = products.size
}
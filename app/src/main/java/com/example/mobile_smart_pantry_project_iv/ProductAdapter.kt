package com.example.mobile_smart_pantry_project_iv

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ProductAdapter(
    private val context: Context,
    private val products: MutableList<Product>
) : ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_product, parent, false)

        val product = products[position]

        val tvId = itemView.findViewById<TextView>(R.id.tvId)
        val tvNazwa = itemView.findViewById<TextView>(R.id.tvNazwa)
        val tvIlosc = itemView.findViewById<TextView>(R.id.tvIlosc)
        val tvKategoria = itemView.findViewById<TextView>(R.id.tvKategoria)
        val ivProductImage = itemView.findViewById<ImageView>(R.id.ivProductImage)

        tvId.text = product.id
        tvNazwa.text = product.nazwa
        tvIlosc.text = "${product.ilosc} ${product.jednostka}"
        tvKategoria.text = product.kategoria

        val imageResId = when (product.kategoria.lowercase()) {
            "food"         -> R.drawable.medicine
            "life support" -> R.drawable.palliative
            "tools"        -> R.drawable.tools
            "medicine"     -> R.drawable.medicine
            "fuel"         -> R.drawable.gas_station
            else           -> R.drawable.application
        }

        ivProductImage.setImageResource(imageResId)

        if (product.ilosc < 5) {
            itemView.setBackgroundColor(Color.parseColor("#FFCDD2"))
            tvIlosc.setTextColor(Color.parseColor("#B71C1C"))
        } else {
            itemView.setBackgroundColor(Color.parseColor("#EEEEEE"))
            tvIlosc.setTextColor(Color.BLACK)
        }

        return itemView
    }
}
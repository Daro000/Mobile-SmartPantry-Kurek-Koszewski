package com.example.mobile_smart_pantry_project_iv

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_smart_pantry_project_iv.databinding.ActivityMainBinding
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = ProductAdapter(products)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerView.adapter = adapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }

        products.addAll(loadProducts())
        adapter.notifyDataSetChanged()

        if (products.isEmpty()) {
            products.addAll(getFallbackProducts())
            saveProducts(products)
            adapter.notifyDataSetChanged()
        }

    }

    private fun getPantryFile(): File = File(filesDir, "pantry.json")

    private fun loadProducts(): List<Product> {
        val file = getPantryFile()
        if (!file.exists()) return emptyList()

        return try {
            val json = file.readText()
            Json.decodeFromString<List<Product>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveProducts(list: List<Product>) {
        val file = getPantryFile()
        val json = Json.encodeToString(list)
        file.writeText(json)
    }

    private fun getFallbackProducts(): List<Product> = listOf(
        Product("0001", "Woda destylowana", 850, "l", "zasoby-podstawowe"),
        Product("0002", "Tlen ciekły", 1240, "l", "zasoby-podstawowe"),
        Product("0003", "Marchew liofilizowana", 42, "kg", "żywność")
    )

}
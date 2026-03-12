package com.example.mobile_smart_pantry_project_iv

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_smart_pantry_project_iv.databinding.ActivityMainBinding
import com.example.mobile_smart_pantry_project_iv.databinding.DialogAddProductBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        adapter = ProductAdapter(this, productList)
        binding.listViewProducts.adapter = adapter

        val inventoryFile = File(filesDir, "inventory.json")
        if (inventoryFile.exists()) {
            productList.addAll(loadFromFile(inventoryFile))
        } else {
            productList.addAll(loadFromRaw())
            saveProducts()
        }
        adapter.notifyDataSetChanged()

        binding.btnDodaj.setOnClickListener {
            showAddProductDialog()
        }

        binding.btnPlus.setOnClickListener {
            val position = binding.listViewProducts.checkedItemPosition
            if (position < 0) {
                Toast.makeText(this, "Wybierz produkt z listy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val staryProdukt = productList[position]
            productList[position] = staryProdukt.copy(ilosc = staryProdukt.ilosc + 1)
            adapter.notifyDataSetChanged()
            saveProducts()
        }

        binding.btnMinus.setOnClickListener {
            val position = binding.listViewProducts.checkedItemPosition
            if (position < 0) {
                Toast.makeText(this, "Wybierz produkt z listy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val staryProdukt = productList[position]
            if (staryProdukt.ilosc > 0) {
                productList[position] = staryProdukt.copy(ilosc = staryProdukt.ilosc - 1)
                adapter.notifyDataSetChanged()
                saveProducts()
            }
        }

        binding.listViewProducts.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this)
                .setTitle("Usuń produkt")
                .setMessage("Czy na pewno chcesz usunąć ${productList[position].nazwa}?")
                .setPositiveButton("Usuń") { _, _ ->
                    productList.removeAt(position)
                    adapter.notifyDataSetChanged()
                    saveProducts()
                    Toast.makeText(this, "Usunięto produkt", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Anuluj", null)
                .show()
            true
        }
    }

    private fun showAddProductDialog() {
        val dialogBinding = DialogAddProductBinding.inflate(layoutInflater)

        val categories = resources.getStringArray(R.array.product_categories)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerKategoria.adapter = spinnerAdapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Nowy produkt")
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnDodajDialog.setOnClickListener {
            val nazwa = dialogBinding.etNazwa.text.toString()
            val iloscStr = dialogBinding.etIlosc.text.toString()
            val jednostka = dialogBinding.etJednostka.text.toString()
            val kategoria = dialogBinding.spinnerKategoria.selectedItem.toString()

            if (nazwa.isBlank() || iloscStr.isBlank() || jednostka.isBlank()) {
                Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nowyProdukt = Product(
                id = UUID.randomUUID().toString(),
                nazwa = nazwa,
                ilosc = iloscStr.toIntOrNull() ?: 0,
                jednostka = jednostka,
                kategoria = kategoria
            )

            productList.add(nowyProdukt)
            adapter.notifyDataSetChanged()
            saveProducts()

            Toast.makeText(this, "Dodano: $nazwa", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveProducts() {
        try {
            val json = Json { prettyPrint = true }
            val file = File(filesDir, "inventory.json")
            file.writeText(json.encodeToString(productList))
        } catch (e: Exception) {
            Toast.makeText(this, "Błąd zapisu!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun loadFromFile(file: File): List<Product> {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<List<Product>>(file.readText())
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun loadFromRaw(): List<Product> {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val inputStream = resources.openRawResource(R.raw.pantry)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            json.decodeFromString<List<Product>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
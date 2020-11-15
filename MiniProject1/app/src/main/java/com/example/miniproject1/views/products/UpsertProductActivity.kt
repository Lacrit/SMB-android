package com.example.miniproject1.views.products

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproject1.databinding.UpsertProductActivityBinding
import com.example.miniproject1.viewModels.ProductViewModel
import com.example.miniproject1.models.Product
import com.example.miniproject1.shared.SharedPrefs

class UpsertProductActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var quantity: EditText
    private var product: Product? = null
    private val productViewModel = ProductViewModel(Application())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefs(this);
        val binding = UpsertProductActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = binding.nameEdit
        price = binding.priceEdit
        quantity = binding.quantityEdit
        var tmpId = -1
        if (intent != null && intent.extras != null) tmpId =
            intent.extras!!.getInt("id", -1)
        if (tmpId != -1) {
            product = productViewModel.getProductById(tmpId)
            if (product != null) {
                name.setText(product!!.name)
                price.setText(product!!.price.toString())
                quantity.setText(product!!.quantity.toString())
            }

        }
    }

    fun save(view: View?) {
        val name = name.text.toString()
        val price = price.text.toString().toFloat()
        val quantity = quantity.text.toString().toFloat()
        if (product != null) {
            product!!.name = name
            product!!.price = price
            product!!.quantity = quantity
            productViewModel.update(product!!)
        } else {
            val p = Product(name, price, false, quantity)
            productViewModel.insert(p)
        }
        setResult(1)
        finish()
    }


}
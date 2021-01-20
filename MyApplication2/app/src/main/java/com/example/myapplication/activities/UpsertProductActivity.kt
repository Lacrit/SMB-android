package com.example.myapplication.activities
import android.content.Intent
import android.icu.number.NumberRangeFormatter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.UpsertProductActivityBinding
import com.example.myapplication.models.Product
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.repos.OnGetDataListener
import com.example.myapplication.utilities.SharedPrefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import java.util.*

class UpsertProductActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var quantity: EditText
    private var product: Product? = null
    private lateinit var repo : FirebaseProductRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = SharedPrefs(this);
        repo = FirebaseProductRepo(sharedPrefs)
        val binding = UpsertProductActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = binding.nameEdit
        price = binding.priceEdit
        quantity = binding.quantityEdit
        var tmpId = ""
        if (intent != null && intent.extras != null) tmpId =
            intent.extras!!.getString("id", "")
        if (tmpId != "") {
            repo.getById(tmpId, object : OnGetDataListener {
                override fun onSuccess(data: DataSnapshot) {
                    product = data.getValue<Product>()
                    if (product != null) {
                        name.setText(product!!.name)
                        price.setText(product!!.price.toString())
                        quantity.setText(product!!.quantity.toString())
                    }
                }
            })
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
            repo.insertOrUpdate(product!!)
        } else {
            val p = Product(UUID.randomUUID().toString(), name, price, false, quantity)
            repo.insertOrUpdate(p)
        }
        val productList = Intent(this, ProductListActivity::class.java)
        startActivity(productList)
        finish()
    }


}
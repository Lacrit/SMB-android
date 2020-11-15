package com.example.miniproject1.views.products

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.miniproject1.R
import com.example.miniproject1.databinding.ProductBinding
import com.example.miniproject1.models.Product
import com.example.miniproject1.shared.SharedPrefs
import com.example.miniproject1.viewModels.ProductViewModel

class ProductsAdapter(private val viewModel: ProductViewModel):
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {
    private var products: MutableList<Product> = mutableListOf()

    class ProductViewHolder(val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root) {
        var product: Product? = null

        init {
            SharedPrefs(binding.root.context)
        }

        fun setAll(it: Product) {
            val bought = binding.bought
            val quantity = binding.quantity
            val price = binding.price
            val name = binding.name

            product = it
            name.text = it.name
            price.text = it.price.toString()
            quantity.text = it.quantity.toString()
            bought.isChecked = it.bought
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ProductBinding.inflate(inflater, viewGroup, false)
        val productViewHolder = ProductViewHolder(binding)

        binding.root.setOnLongClickListener { v: View? ->
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                  viewModel.delete(productViewHolder.product!!)
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                builder.setTitle(R.string.deleteProductTitle)
                val dialog = builder.create().show()
                true
        }

        binding.root.setOnClickListener { v: View ->
            val intent = Intent(v.context, UpsertProductActivity::class.java)
            intent.putExtra("id", productViewHolder.product!!.id)
            (v.context as Activity).startActivityForResult(intent, 0)
        }

        return productViewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setAll(products[position])

        val bought = holder.binding.bought

        bought.setOnCheckedChangeListener { _, isChecked: Boolean ->
            products[position].bought = isChecked
            viewModel.update(products[position])
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setProducts(products: MutableList<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

}



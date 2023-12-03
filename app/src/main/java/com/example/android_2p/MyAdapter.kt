package com.example.android_2p
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_2p.R
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Item(val id: String,val context: String, val title: String, val price: Int, val stock: Boolean, val seller: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["context"].toString(),doc["title"].toString(), doc["price"].toString().toIntOrNull() ?: 0, doc["stock"] as? Boolean ?: false, doc["seller"].toString())
    }

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class MyAdapter(private val context: Context, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.view.findViewById<TextView>(R.id.textTitle).text = item.title
        holder.view.findViewById<TextView>(R.id.textStock).text = item.price.toString()
        holder.view.findViewById<TextView>(R.id.textPrice).text= if (item.stock) "판매함" else "판매안함"

        holder.view.setOnClickListener {
            itemClickListener?.onItemClick(position)

        }
    }

    override fun getItemCount() = items.size

    fun getItem(position: Int): Item? {
        return if (position in items.indices) {
            items[position]
        } else {
            null
        }
    }
}
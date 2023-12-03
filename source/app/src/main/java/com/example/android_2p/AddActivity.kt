package com.example.android_2p

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddActivity : AppCompatActivity()  {
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("item")
    private val signtitle by lazy { findViewById<EditText>(R.id.signTitle)}
    private val signcontent by lazy { findViewById<EditText>(R.id.signContent)}
    private val signprice by lazy { findViewById<EditText>(R.id.signPrice)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add)

        adapter = MyAdapter(this, emptyList())

        val sign=findViewById<Button>(R.id.sign)
        sign.setOnClickListener{
            val title=signtitle.text.toString()
            val content=signcontent.text.toString()
            val price=signprice.text.toString().toInt()
            val seller= Firebase.auth.currentUser?.email
            val stock: Boolean = true
            val itemMap = hashMapOf(
                "title" to title,
                "content" to content,
                "price" to price,
                "seller" to seller,
                "stock" to stock
            )
            itemsCollectionRef.add(itemMap)
                .addOnSuccessListener { updateList() }.addOnFailureListener{}
            finish()
        }

    }
    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }
}
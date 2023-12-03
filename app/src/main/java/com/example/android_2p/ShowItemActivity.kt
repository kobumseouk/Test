package com.example.android_2p

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.android_2p.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShowItemActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("item")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show)

        val itemId = intent.getStringExtra("id")

        val editTitle = findViewById<EditText>(R.id.TextTitle)
        val editContent = findViewById<EditText>(R.id.TextContent)
        val editPrice = findViewById<EditText>(R.id.TextPrice)
        val editStock = findViewById<EditText>(R.id.TextStock)
        val editsell = findViewById<EditText>(R.id.TextSeller)
        val changesButton = findViewById<Button>(R.id.ok)

        // 항목 데이터를 EditText에 표시
        itemsCollectionRef.document(itemId ?: "")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Document found, extract price and stock values
                    val title =document["title"].toString()
                    val content=document["content"].toString()
                    val seller=document["seller"].toString()
                    val price = document["price"].toString()
                    val stock = document["stock"].toString()

                    // Display the values in TextViews
                    editTitle.setText(title)
                    editContent.setText(content)
                    editsell.setText(seller)
                    editPrice.setText(price)
                    if(stock=="true") {
                        editStock.setText("판매가능")
                    }
                    else{
                        editStock.setText("판매불가")
                    }
                } else {
                    // Document not found or does not exist
                    // Handle the case as needed
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures, e.g., network issues
            }
        changesButton.setOnClickListener{
            val seller = editsell.text.toString()
            val intent = Intent(this, SendMessage()::class.java)
            intent.putExtra("seller", seller)
            startActivity(intent)
            finish()
        }
    }
}
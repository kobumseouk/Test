package com.example.android_2p
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("item")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit)

        val itemId = intent.getStringExtra("id").toString()

        val editPrice = findViewById<EditText>(R.id.editTextPrice)
        val editcheck = findViewById<CheckBox>(R.id.checkBox)
        val chageButton = findViewById<Button>(R.id.change)
        // 항목 데이터를 EditText에 표시

        itemsCollectionRef.document(itemId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Document found, extract price and stock values
                    val price = document["price"].toString()
                    val stock = document["stock"].toString()
                    // Display the values in TextViews
                    editPrice.setText(price)
                    if(stock=="true") {
                        editcheck.isChecked=true
                    } else{
                        editcheck.isChecked=false
                    }
                } else {
                    // Document not found or does not exist
                    // Handle the case as needed
                }
            }
            .addOnFailureListener { exception ->
            }
        chageButton.setOnClickListener{
            val editprice=editPrice.text.toString().toInt()
            val editstock=editcheck.isChecked
            updatePrice(itemId,editprice, editstock)
            finish()
    }

}


    private fun updatePrice(itemID : String,price: Int,stock : Boolean) {
        itemsCollectionRef.document(itemID)
            .update("price", price)
            .addOnSuccessListener {
                // 업데이트 성공
            }
        itemsCollectionRef.document(itemID)
            .update("stock", stock)
            .addOnSuccessListener {
                // 업데이트 성공
            }
    }
}

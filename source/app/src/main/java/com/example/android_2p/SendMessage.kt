package com.example.android_2p

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SendMessage : AppCompatActivity() {
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private var fbauth = FirebaseAuth.getInstance()
    private val messagesCollectionRef = db.collection("messages")
    private var snapshotListener: ListenerRegistration? = null

    private var userEmail = fbauth?.currentUser?.email
    private val editMessage by lazy { findViewById<EditText>(R.id.editMessage) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)


        findViewById<Button>(R.id.buttonSend)?.setOnClickListener {
            addMessage()
            finish()

        }

    }


    private fun addMessage() {
        val message = editMessage.text.toString()
        val sellID = intent.getStringExtra("seller").toString()
        var buyID = userEmail.toString()

        if (message.isEmpty()) {
            Snackbar.make(editMessage, "Input Message!", Snackbar.LENGTH_SHORT).show()
            return
        }

        val itemMap = hashMapOf(
            "sellemail" to sellID,
            "buyemail" to buyID,
            "message" to message
        )

        messagesCollectionRef.add(itemMap)
            .addOnSuccessListener { }.addOnFailureListener {

            }}

}
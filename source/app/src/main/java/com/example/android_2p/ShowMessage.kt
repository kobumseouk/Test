package com.example.android_2p

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShowMessage : AppCompatActivity() {
    private var adapter: MessageAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val messagesCollectionRef = db.collection("messages")
    private lateinit var mAuth: FirebaseAuth
    private val recyclerViewItems by lazy { findViewById<RecyclerView>(R.id.recyclerviewMe) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showmessage)

        mAuth = FirebaseAuth.getInstance()
        val currentUserEmail = mAuth.currentUser?.email

        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, emptyList())

        recyclerViewItems.adapter = adapter

        // Firestore에서 현재 사용자의 이메일과 "seller" 필드의 값이 일치하는 데이터 가져오기
        messagesCollectionRef.whereEqualTo("sellemail", currentUserEmail)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // 오류 처리
                    return@addSnapshotListener
                }

                val messageList = mutableListOf<Message>()

                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot) {
                        val message = Message(document)
                        messageList.add(message)
                    }
                }

                adapter?.updateData(messageList)
            }

    }

}

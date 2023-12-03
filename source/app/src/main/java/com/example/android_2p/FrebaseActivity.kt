package com.example.android_2p

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class clickedItem(val id: String, val title: String, val price: Int, val stock: Boolean)

class FrebaseActivity : AppCompatActivity(){
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("item")
    private var snapshotListener: ListenerRegistration? = null
    private lateinit var mAuth: FirebaseAuth


    private val recyclerViewItems by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firebase_main)

        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
        val message = findViewById<Button>(R.id.showMessage)

        mAuth = FirebaseAuth.getInstance()
//        val currentUser = mAuth.currentUser
//        currentUser?.let {
//            val userEmail = it.email
//            textview.text="user $userEmail"
//        }


        val auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        val userUid = user?.uid

        recyclerViewItems.adapter = adapter

        // 아이템 클릭 시 실행될 리스너 설정
        adapter?.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                 val clickedItem = adapter?.getItem(position)
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null && currentUser.email== clickedItem?.seller) {
                    // 같은 경우 EditViewActivity 실행
                    showEditViewActivity(clickedItem)
                } else {
                    // 다른 경우 처리 (예: 다이얼로그 표시)
                   showItemActivity(clickedItem)
                }
            }

        })
        updateList()

        findViewById<Button>(R.id.Add)?.setOnClickListener{
            startActivity(Intent(this, AddActivity::class.java))
        }
        findViewById<Button>(R.id.showMessage)?.setOnClickListener{
            startActivity(Intent(this, ShowMessage::class.java))
        }

        val radioGroupFilter = findViewById<RadioGroup>(R.id.radioGroupFilter)
        radioGroupFilter.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonAll -> updateList()
                R.id.radioButtonStockTrue -> queryWhere(true)
                R.id.radioButtonStockFalse -> queryWhere(false)
            }
        }
        findViewById<RadioButton>(R.id.radioButtonAll).isChecked = true
    }
    override fun onResume() {
        super.onResume()
        // 액티비티가 포그라운드로 돌아올 때 RecyclerView 업데이트
        updateList()
    }


    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener { querySnapshot ->
            val items = mutableListOf<Item>()
            for (doc in querySnapshot) {
                items.add(Item(doc))
            }

            // stock이 true인 필터링된 목록 사용
            //val filteredItems = items.filter { it.stock }

            adapter?.updateList(items)
        }
    }
    private fun queryWhere(filterByStock: Boolean) {
        val query = if (filterByStock) {
            itemsCollectionRef.whereEqualTo("stock", true)
        } else {
            itemsCollectionRef.whereEqualTo("stock", false)
        }

        query.get().addOnSuccessListener { querySnapshot ->
            val items = mutableListOf<Item>()
            for (doc in querySnapshot) {
                items.add(Item(doc))
            }

            adapter?.updateList(items)
        }.addOnFailureListener {
            // Handle failure
        }
    }

    private fun showEditViewActivity(item: Item?) {
        // EditViewActivity로 이동하면서 선택된 아이템 데이터 전달
        val intent = Intent(this, EditActivity()::class.java)
        intent.putExtra("id", item?.id)
        startActivity(intent)
    }
    private fun showItemActivity(item: Item?){
        val intent = Intent(this, ShowItemActivity()::class.java)
        intent.putExtra("id", item?.id)
        startActivity(intent)
    }

}
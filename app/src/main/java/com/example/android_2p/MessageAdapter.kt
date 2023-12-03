package com.example.android_2p

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Message(val id: String,val buyemail: String, val sellemail: String, val message: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["buyemail"].toString(),doc["sellemail"].toString(), doc["message"].toString())
}

class MessageAdapter(private val context: Context, private var messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textSeller: TextView = itemView.findViewById(R.id.TextSeller)
        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = messageList[position]
        holder.textSeller.text = item.buyemail
        holder.textMessage.text = item.message
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

   fun updateData(newList: List<Message>) {
        messageList = newList
        notifyDataSetChanged()
    }

}

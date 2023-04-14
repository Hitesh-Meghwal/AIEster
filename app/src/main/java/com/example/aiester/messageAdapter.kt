package com.example.aiester

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class messageAdapter(private val MessageList:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserMessage(ItemView:View):RecyclerView.ViewHolder(ItemView){
        val usermsg:TextView=itemView.findViewById(R.id.tvuser)
    }
    class BotMessage(ItemView:View):RecyclerView.ViewHolder(ItemView){
        val botmsg:TextView=itemView.findViewById(R.id.botid)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      val view:View
      return if(viewType==0){
          view=LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent,false)
          UserMessage(view)
      }
        else{
          view=LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent,false)
          BotMessage(view)
      }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sender=MessageList.get(position).Sender
        when(sender){
            "user"->(holder as UserMessage).usermsg.setText(MessageList.get(position).message)
            "bot"->(holder as BotMessage).botmsg.setText(MessageList.get(position).message)
        }
    }
    override fun getItemCount(): Int {
        return MessageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(MessageList.get(position).Sender){
            "user"->0
            "bot"->1
            else ->1
    }
}}
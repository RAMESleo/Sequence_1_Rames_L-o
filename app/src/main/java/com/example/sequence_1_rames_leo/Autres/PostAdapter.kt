package com.example.sequence_1_rames_leo.Autres

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sequence_1_rames_leo.R


class postAdapter (
    private val mesListes: List<ListeTodo>,
    private val listener: OnItemClickListener

    ) : RecyclerView.Adapter<postAdapter.PostViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.d("PostAdapter", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        var layoutId = R.layout.affichageliste

        return PostViewHolder(itemView = inflater.inflate(layoutId, parent, false))

    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Log.d("PostAdapter", "onBindViewHolder position $position")
        holder.bind(this.mesListes[position])
    }

    override fun getItemCount(): Int = mesListes.size




     @Suppress("DEPRECATION")
     inner class PostViewHolder(
        itemView: View)
         : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
     View.OnLongClickListener{

        val titleTextView = itemView.findViewById<TextView>(R.id.TitreListe)
         val isTrashed = itemView.findViewById<ImageView>(R.id.TrashList)

        init{
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            isTrashed.isVisible = false
        }
        fun bind(Liste : ListeTodo) {
            titleTextView.text = Liste.GetTitre()


        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position !=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
         override fun onLongClick(v: View) : Boolean {
             val position = adapterPosition
             //val item = v.
             if(position != RecyclerView.NO_POSITION){
                 listener.onItemLongClick(position)
                 val isTrashed = v.findViewById<ImageView>(R.id.TrashList)
                 if (isTrashed.isVisible){
                     isTrashed.isVisible = false
                 } else{
                     isTrashed.isVisible = true
                 }
                 return true
             }
             return false
         }

        }
    interface OnItemClickListener{
        fun onItemClick(position : Int)
        fun onItemLongClick(position : Int)
    }
    companion object {
        private const val LIST_ITEM = 1
        private const val ITEM_ITEM = 2

    }





    }



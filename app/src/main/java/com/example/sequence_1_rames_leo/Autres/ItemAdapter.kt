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

class ItemAdapter (
    private val mesItems: List<ItemToDo>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ItemAdapter.PostViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.d("PostAdapter", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        var layoutId = R.layout.affichageitem


        return PostViewHolder(itemView = inflater.inflate(layoutId, parent, false))

    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Log.d("PostAdapter", "onBindViewHolder position $position")
        holder.bind(this.mesItems[position])
    }

    override fun getItemCount(): Int = mesItems.size




    @Suppress("DEPRECATION")
    inner class PostViewHolder(
        itemView: View
    )
        : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener{

        val TextTitre = itemView.findViewById<TextView>(R.id.TitreItem)
        val isChecked = itemView.findViewById<ImageView>(R.id.imageCheck)
        val isTrashed = itemView.findViewById<ImageView>(R.id.TrashItem)


        init{
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            isTrashed.isVisible = false
        }
        fun bind(item : ItemToDo) {
            TextTitre.setText(item.GetTitre())
            if(item.GetState()){
                isChecked.setImageResource(R.drawable.ic_ischecked)
            }else{
                isChecked.setImageResource(R.drawable.ic_notchecked)
            }


        }

        override fun onClick(v: View) {
            val position = adapterPosition
            val clickedItem = mesItems[position]
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
                val ischecked  = v.findViewById<ImageView>(R.id.imageCheck)

                if(clickedItem.GetState()){
                    isChecked.setImageResource(R.drawable.ic_ischecked)
                }else{
                    isChecked.setImageResource(R.drawable.ic_notchecked)
                }
            }
        }

        /**
         * Called when a view has been clicked and held.
         *
         * @param v The view that was clicked and held.
         *
         * @return true if the callback consumed the long click, false otherwise.
         */
        override fun onLongClick(v: View) : Boolean {
            val position = adapterPosition
            //val item = v.
            if(position != RecyclerView.NO_POSITION){
                listener.onItemLongClick(position)
                val isTrashed = v.findViewById<ImageView>(R.id.TrashItem)
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
        fun onItemClick(position : Int )
        fun onItemLongClick(position : Int)
    }






}
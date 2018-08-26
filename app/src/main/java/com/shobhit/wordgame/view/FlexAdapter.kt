package com.shobhit.wordgame.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shobhit.wordgame.R
import com.shobhit.wordgame.model.IdName

/**
 * Created by shobhit on 26/08/18.
 */
class FlexAdapter(val mContext: Context, val mList: MutableList<IdName>,val blankClickListener: OnBlankClickListener) : RecyclerView.Adapter<FlexAdapter.FlexViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FlexViewHolder {
    val view = LayoutInflater.from(parent?.context).inflate(R.layout.flex_layout_item_row, parent, false)
    return FlexViewHolder(view)
  }

  override fun getItemCount(): Int {
    return mList.size
  }

  override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {

    holder.itemView.setOnClickListener {
      if(mList[holder.adapterPosition].isBlank) {
        blankClickListener.onBlackClick(mList[holder.adapterPosition], holder.adapterPosition)
      }
    }
    if(mList[holder.adapterPosition].isValueInserted) {
      val spannableString = SpannableString(mList[holder.adapterPosition].name)
      spannableString.setSpan(UnderlineSpan(),0,mList[holder.adapterPosition].name.length,0)
      spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.colorPrimary)),0,mList[holder.adapterPosition].name.length,0)
      holder.txtName.setText(spannableString)
    } else if(mList[holder.adapterPosition].isBlank&&mList[holder.adapterPosition].id!=-1){
      holder.txtName.text = "______"
      holder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
    } else {
      holder.txtName.text = mList[holder.adapterPosition].name
      holder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.black))
    }
  }

  class FlexViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtName = view.findViewById<TextView>(R.id.txt_label)
  }
   interface OnBlankClickListener{
    fun onBlackClick(idName:IdName,position: Int)
  }
}
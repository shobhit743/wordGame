package com.shobhit.wordgame.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shobhit.wordgame.R
import com.shobhit.wordgame.model.IdName
import kotlinx.android.synthetic.main.flex_layout_item_row.view.*

/**
 * Created by shobhit on 26/08/18.
 */
class SelectedWordDialogAdapter(val mContent: Context, val mList: MutableList<IdName>, val selectedWordDialogClick: SelectedWordDialogAdapter.SelectedWordDialogClick) : RecyclerView.Adapter<SelectedWordDialogAdapter.SelectedViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.selected_word_dialog_layout, parent, false)
    return SelectedViewHolder(view)
  }

  override fun getItemCount(): Int {
    return mList.size
  }

  override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
    holder.txtLabel.text = mList[holder.adapterPosition].name
    holder.itemView.setOnClickListener {
      selectedWordDialogClick.onSelectedWordDialog(mList[holder.adapterPosition])
    }
  }

  class SelectedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtLabel = view.findViewById<TextView>(R.id.txt_label)
  }

  interface SelectedWordDialogClick {
    fun onSelectedWordDialog(idName: IdName)
  }
}
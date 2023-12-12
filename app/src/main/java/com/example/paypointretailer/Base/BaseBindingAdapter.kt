package com.example.paypointretailer.Base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.paypointretailer.Utils.ScrollEndListener
import java.util.ArrayList

abstract class BaseBindingAdapter <I> : RecyclerView.Adapter<BaseBindingViewHolder>(),
    BaseBindingViewHolder.ClickListener {

    protected var items: MutableList<I>? = ArrayList()
    private var itemClickListener: ItemClickListener<I>? = null
    private var checkedListener: CheckChangeListener<I>? = null
    protected var scrollEndListener: ScrollEndListener? = null
    override fun onViewClick(position: Int) {
        if (itemClickListener != null && items != null) {
            itemClickListener!!.onClick(items!![position], position)
        }
    }

    fun onViewChecked(position: Int, checked: Boolean) {
        if (checkedListener != null && items != null) {
            checkedListener!!.onClick(items!![position], position, checked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bind(inflater, parent, viewType)
        return BaseBindingViewHolder(binding, this)
    }

    protected abstract fun bind(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewDataBinding

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    fun setListItems(items: MutableList<I>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getListItems(): MutableList<I>? {
        return items
    }

    @JvmOverloads
    fun addItem(item: I, position: Int = items!!.size) {
        items!!.add(position, item)
        notifyItemInserted(position)
    }

    @JvmOverloads
    fun addItems(itemsToAdd: List<I>, position: Int = items!!.size) {
        items!!.addAll(position, itemsToAdd)
        notifyItemRangeInserted(position, itemsToAdd.size)
    }

    fun removeItem(position: Int) {
        try {
            items!!.removeAt(position)
            notifyDataSetChanged()
        } catch (e: IndexOutOfBoundsException) {

        }

    }

    fun clear() {
        val size = items!!.size
        if (size > 0) {
            items!!.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener<I>) {
        this.itemClickListener = itemClickListener
    }

    fun setCheckedChangeListener(itemClickListener: CheckChangeListener<I>) {
        this.checkedListener = itemClickListener

    }
    fun setOnScrollEndListener(listener: ScrollEndListener){
        this.scrollEndListener = listener
    }
    interface ItemClickListener<I> {
        fun onClick(item: I, position: Int)
    }


    interface CheckChangeListener<I> {
        fun onClick(item: I, position: Int, checked: Boolean)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



}
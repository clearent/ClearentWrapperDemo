package com.example.clearentwrapperdemo.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clearent.idtech.android.databinding.PairingReaderItemBinding
import com.clearent.idtech.android.wrapper.model.ReaderStatus

class ReadersListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ReaderStatus, ReadersListAdapter.ReaderViewHolder>(READER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReaderViewHolder {
        val binding =
            PairingReaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReaderViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ReaderViewHolder(private val binding: PairingReaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
            }
        }

        fun bind(reader: ReaderStatus) {
            binding.apply {
                pairingDeviceName.text = reader.displayName
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(reader: ReaderStatus)
    }

    companion object {

        private val READER_COMPARATOR = object : DiffUtil.ItemCallback<ReaderStatus>() {
            override fun areItemsTheSame(oldItem: ReaderStatus, newItem: ReaderStatus) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: ReaderStatus, newItem: ReaderStatus) =
                oldItem == newItem
        }
    }
}
package com.example.cryptoapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.databinding.ItemCoinBinding
import com.example.cryptoapp.model.CoinModel


class CoinListAdapter(private val mListener: CoinListAdapterListener) :
    RecyclerView.Adapter<CoinListAdapter.CoinListViewHolder>() {

    private val coinList = mutableListOf<CoinModel>()
    private var filteredCoinList = mutableListOf<CoinModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder {
        return CoinListViewHolder(
            ItemCoinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) {
        holder.bindView(filteredCoinList[position])
    }

    override fun getItemCount(): Int {
        return filteredCoinList.size
    }

    fun filter(query: String) {
        filteredCoinList = coinList.filter { coin ->
            coin.name.contains(query, ignoreCase = true) ||
                    coin.symbol.contains(query, ignoreCase = true) ||
                    coin.id.contains(query, ignoreCase = true)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun setCoinList(coinList: List<CoinModel>) {
        this.coinList.clear()
        this.coinList.addAll(coinList)
        this.filteredCoinList.addAll(coinList)
        notifyDataSetChanged()
    }

    inner class CoinListViewHolder(
        val binding: ItemCoinBinding,
        private val coinListAdapterListener: CoinListAdapterListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {
                coinListAdapterListener.onItemClick(coinList[adapterPosition])
            }
        }

        fun bindView(coin: CoinModel) {

            binding.apply {
                tvSymbol.text = coin.symbol
                tvName.text = coin.name
                tvRank.text = "Rank : ${coin.rank}"
                tvSymbol.backgroundTintList = ColorStateList.valueOf(generateRandomDarkColor())
            }

        }

    }


    fun generateRandomDarkColor(): Int {
        val minValue = 0x00 // Low end of the color range
        val maxValue = 0x80 // High end of the dark color range
        val red = (minValue..maxValue).random()
        val green = (minValue..maxValue).random()
        val blue = (minValue..maxValue).random()
        return Color.argb(255, red, green, blue)
    }
    interface CoinListAdapterListener {
        fun onItemClick(coin: CoinModel)
    }
}
package com.paypay.currencyapp.ui.currency

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyapp.R
import com.paypay.currencyapp.data.model.CountryCode
import com.paypay.currencyapp.ui.common.countrycode.ListViewAdapter
import kotlinx.android.synthetic.main.list_item_currency_rate.view.*

class CurrencyRateListAdapter(private var items: List<CountryCode>, val context:Context) :
    RecyclerView.Adapter<CurrencyRateListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflater.inflate(R.layout.list_item_currency_rate, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CountryCode) {

            val image = ListViewAdapter.GetImage(context, item.isoCode.toLowerCase())
            if(image != null) {
                itemView.iv_item_flag.setBackgroundDrawable(image)
            }

            itemView.tv_item_code.text = item.code
            itemView.tv_item_rate.text = item.rate
        }
    }
}
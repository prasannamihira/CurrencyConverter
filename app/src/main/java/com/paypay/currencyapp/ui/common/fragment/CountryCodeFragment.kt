package com.paypay.currencyapp.ui.common.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import com.paypay.currencyapp.R
import com.paypay.currencyapp.app.App
import com.paypay.currencyapp.data.model.CountryCode
import com.paypay.currencyapp.ui.common.countrycode.IndexableListView
import com.paypay.currencyapp.ui.common.countrycode.ListViewAdapter
import com.paypay.currencyapp.ui.currency.CurrencyConverterActivity
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CountryCodeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
@SuppressLint("ValidFragment")
class CountryCodeFragment constructor(context:Context) : DialogFragment(), SearchView.OnQueryTextListener  {
    private lateinit var list: IndexableListView
    private lateinit var searview: SearchView

    internal var context = context
    var parentActivity = ""

    private var countryCodeList: ArrayList<CountryCode>? = null
    private var listViewAdapter: ListViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var viewCodes = inflater.inflate(R.layout.fragment_country_code, container, false)

        if (countryCodeList == null || countryCodeList!!.size <= 0) {

            countryCodeList = App.instance.getCountryCodeList()
        }

        searview = viewCodes.findViewById<View>(R.id.searchViewBar) as SearchView
        return viewCodes
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (countryCodeList != null && countryCodeList!!.size > 0) {

            list = view!!.findViewById<View>(android.R.id.list) as IndexableListView
            if (listViewAdapter != null) {
                list.adapter = listViewAdapter
            } else {
                listViewAdapter = ListViewAdapter(activity, countryCodeList)
                list.adapter = listViewAdapter
            }
            list.isTextFilterEnabled = true
            list.isFastScrollEnabled = true

            setupSearchView()

            list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val model = list.adapter.getItem(position) as CountryCode
                Log.e("test", "" + model.isoCode)

                if (parentActivity == "register") run {
                    val editProfActivity = activity as CurrencyConverterActivity?
                    editProfActivity!!.onUserSelectValue(model)
                    dismiss()
                }
            }
        }
    }

    private fun setupSearchView() {
        searview.setIconifiedByDefault(false)
        searview.setOnQueryTextListener(this)
        searview.isSubmitButtonEnabled = true
        searview.queryHint = "Search"
    }

    override fun onQueryTextChange(newText: String): Boolean {

        if (TextUtils.isEmpty(newText)) {
            list.clearTextFilter()
        } else {
            list.setFilterText(newText)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

}

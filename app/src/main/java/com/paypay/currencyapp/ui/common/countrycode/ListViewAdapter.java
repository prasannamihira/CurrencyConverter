package com.paypay.currencyapp.ui.common.countrycode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.paypay.currencyapp.R;
import com.paypay.currencyapp.data.model.CountryCode;
import com.paypay.currencyapp.util.StringMatcher;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements Filterable, SectionIndexer {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<CountryCode> countryList;
    ArrayList<CountryCode> originalData;
    private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ListViewAdapter(Context context, ArrayList<CountryCode> countrys) {
        this.context = context;
        countryList = countrys;
        originalData = countrys;
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int position) {

        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView tvCurrencyCode;
        ImageView ivFlag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.country_code_list_item, parent, false);

        tvCurrencyCode = itemView.findViewById(R.id.tvCurrencyCode);
        ivFlag = itemView.findViewById(R.id.ivFlag);

        // Capture position and set to the TextViews
        tvCurrencyCode.setText(countryList.get(position).getCode());

        Drawable image = GetImage(context, countryList.get(position).getIsoCode().toLowerCase());

        if (image != null) {
            ivFlag.setBackgroundDrawable(image);
        }
        return itemView;
    }

    public static Drawable GetImage(Context c, String ImageName) {
        try {
            return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));

        } catch (Exception ex) {
            return null;
        }
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString().toLowerCase();

                FilterResults result = new FilterResults();

                if (constraint != null && constraint.toString().length() > 0) {
                    final ArrayList<CountryCode> list = originalData;

                    int count = list.size();
                    final ArrayList<CountryCode> nlist = new ArrayList<CountryCode>(count);

                    CountryCode filterableString;

                    for (int i = 0; i < count; i++) {
                        filterableString = list.get(i);
                        String countryname = filterableString.getCode();
                        if (countryname.toLowerCase().startsWith(filterString)) {
                            nlist.add(filterableString);
                        }
                    }

                    result.values = nlist;
                    result.count = nlist.size();
                } else {
                    result.values = originalData;
                    result.count = originalData.size();
                }
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                final ArrayList<CountryCode> filteredDate = (ArrayList<CountryCode>) results.values;
                countryList = filteredDate;
                //new ListViewAdapter(context, filteredDate);
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getPositionForSection(int section) {
        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(countryList.get(j).getCode().charAt(0)), String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(countryList.get(j).getCode().charAt(0)), String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }
}

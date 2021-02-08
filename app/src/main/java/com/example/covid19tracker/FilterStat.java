package com.example.covid19tracker;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterStat extends Filter {

    private StatsAdapter adapter;
    private ArrayList<ModelStats> arrayList;

    public FilterStat(StatsAdapter adapter, ArrayList<ModelStats> arrayList) {
        this.adapter = adapter;
        this.arrayList = arrayList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint!=null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelStats> filteredModels = new ArrayList<>();
            for(int i = 0; i < arrayList.size(); i++) {

                if (arrayList.get(i).getCountry().toUpperCase().contains(constraint)) {
                    filteredModels.add(arrayList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        } else {
            results.count = arrayList.size();
            results.values = arrayList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.stateArrayList = (ArrayList<ModelStats>) results.values;

        adapter.notifyDataSetChanged();
    }
}

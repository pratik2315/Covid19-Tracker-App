package com.example.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  StatsAdapter extends RecyclerView.Adapter<StatsAdapter.HolderStat> implements Filterable {

    private Context context;
    public ArrayList<ModelStats> stateArrayList, filterList;
    private FilterStat filterStat;

    public StatsAdapter(Context context, ArrayList<ModelStats> stateArrayList) {
        this.context = context;
        this.stateArrayList = stateArrayList;
        this.filterList = stateArrayList;

    }

    @NonNull
    @Override
    public HolderStat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_row, parent, false);
        return new HolderStat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderStat holder, int position) {
        ModelStats modelStats = stateArrayList.get(position);

        String country = modelStats.getCountry();
        String newConfirmed = modelStats.getNewConfirmed();
        String totalConfirmed = modelStats.getTotalConfirmed();
        String newDeaths = modelStats.getNewDeaths();
        String totalDeaths = modelStats.getTotalDeaths();
        String newRecovered = modelStats.getNewRecovered();
        String totalRecovered = modelStats.getTotalRecovered();

        holder.countryTv.setText(country);
        holder.casesTv.setText(newConfirmed);
        holder.todayCasesTv.setText(totalConfirmed);
        holder.deathsTv.setText(newDeaths);
        holder.todayDeathsTv.setText(totalDeaths);
        holder.recoveredTv.setText(newRecovered);
        holder.todayRecoveredTv.setText(totalRecovered);

    }

    @Override
    public int getItemCount() {
        return stateArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filterStat == null){
            filterStat = new FilterStat(this, filterList);

        }
        return filterStat;
    }

    class HolderStat extends RecyclerView.ViewHolder{

        TextView countryTv, casesTv, todayCasesTv, deathsTv, todayDeathsTv, recoveredTv, todayRecoveredTv;

        public HolderStat(@NonNull View itemView) {
            super(itemView);
            countryTv = itemView.findViewById(R.id.countryTv);
            casesTv = itemView.findViewById(R.id.casesTv);
            todayCasesTv = itemView.findViewById(R.id.todayCasesTv);
            deathsTv = itemView.findViewById(R.id.deathsTv);
            todayDeathsTv = itemView.findViewById(R.id.todayDeathsTv);
            recoveredTv = itemView.findViewById(R.id.recoveredTv);
            todayRecoveredTv = itemView.findViewById(R.id.todayRecoveredTv);
        }
    }
}

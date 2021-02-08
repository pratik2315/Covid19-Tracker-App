package com.example.covid19tracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StatsFragment extends Fragment {

    Context context;
    private static final String STATS_URL = "https://api.covid19api.com/summary";
    private ProgressBar progressBar;
    private ImageButton imageButton;
    private EditText searchEt;
    private RecyclerView recyclerView;

    ArrayList<ModelStats> statsArrayList;
    StatsAdapter statsAdapter;
    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        imageButton = view.findViewById(R.id.sortBtn);
        searchEt = view.findViewById(R.id.searchEt);
        recyclerView = view.findViewById(R.id.statsTv);

        progressBar.setVisibility(View.GONE);
        LoadStatsData();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    statsAdapter.getFilter().filter(s);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final PopupMenu popupMenu = new PopupMenu(context, imageButton);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Ascending");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Descending");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id==0){
                    Collections.sort(statsArrayList, new CountryAsc());
                    statsAdapter.notifyDataSetChanged();
                }
                else if(id==1){
                    Collections.sort(statsArrayList, new CountryDesc());
                    statsAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupMenu.show();

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadStatsData();
    }

    private void LoadStatsData(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleResponse(response);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void handleResponse(String response) {
        statsArrayList = new ArrayList<>();
        statsArrayList.clear();
        try {

            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("Countries");

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/mm/yyyy hh:mm: a");
            Gson gson = gsonBuilder.create();

            for(int i=0; i<jsonArray.length(); i++){
                ModelStats modelStats = gson.fromJson(jsonArray.getJSONObject(i).toString(), ModelStats.class);
                statsArrayList.add(modelStats);
            }

            statsAdapter = new StatsAdapter(context, statsArrayList);
            recyclerView.setAdapter(statsAdapter);

            progressBar.setVisibility(View.GONE);

        }
        catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    public class CountryAsc implements Comparator<ModelStats>{

        @Override
        public int compare(ModelStats left, ModelStats right) {
            return left.getCountry().compareTo(right.getCountry());
        }
    }

    public class CountryDesc implements Comparator<ModelStats>{

        @Override
        public int compare(ModelStats left, ModelStats right) {
            return right.getCountry().compareTo(left.getCountry());
        }
    }

}

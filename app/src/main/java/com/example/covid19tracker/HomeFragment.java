package com.example.covid19tracker;

import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.databinding.FragmentHomeBinding;
import com.google.gson.JsonObject;

import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private static final String STATS_URL = "https://api.covid19api.com/summary";
    private TextView newCases, totalCases, newD, totalD, newR, totalR;
    private ProgressBar progressBar;
    Context context;
    FragmentHomeBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        newCases = view.findViewById(R.id.newCases);
        totalCases = view.findViewById(R.id.totalCases);
        newD = view.findViewById(R.id.newDeaths);
        totalD = view.findViewById(R.id.totalDeaths);
        newR = view.findViewById(R.id.newRecovered);
        totalR = view.findViewById(R.id.totalRecovered);


        progressBar.setVisibility(View.GONE);
        loadHomeData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHomeData();
    }

    private void loadHomeData(){
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
        try {

            JSONObject jsonObject = new JSONObject(response);

            JSONObject globalJo = jsonObject.getJSONObject("Global");

            String newConfirmed = globalJo.getString("NewConfirmed");
            String totalConfirmed = globalJo.getString("TotalConfirmed");
            String newDeaths = globalJo.getString("NewDeaths");
            String totalDeaths = globalJo.getString("TotalDeaths");
            String newRecovered = globalJo.getString("NewRecovered");
            String totalRecovered = globalJo.getString("TotalRecovered");

            newCases.setText(newConfirmed);
            totalCases.setText(totalConfirmed);
            newD.setText(newDeaths);
            totalD.setText(totalDeaths);
            newR.setText(newRecovered);
            totalR.setText(totalRecovered);

            progressBar.setVisibility(View.GONE);
        }
        catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
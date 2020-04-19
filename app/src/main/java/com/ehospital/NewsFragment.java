package com.ehospital;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ehospital.Model.Articles;
import com.ehospital.Model.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    private View mView;

    private RecyclerView mRecyleView;
    private SwipeRefreshLayout mRefresh;
   // private  EditText etQuery;
   // private  Button btnSearch,btnAboutUs;
    private  Dialog dialog;
    private final String API_KEY = "18d114beca1741729dde87ede4469f28";
    private  Adapter adapter;
    private List<Articles> articles = new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_news, container, false);

        mRefresh = mView.findViewById(R.id.swipeRefresh);
        mRecyleView = mView.findViewById(R.id.recyclerView);

      //  etQuery = mView.findViewById(R.id.etQuery);
      //  btnSearch = mView.findViewById(R.id.btnSearch);
      //  btnAboutUs = mView.findViewById(R.id.aboutUs);
        dialog = new Dialog(getActivity());

        mRecyleView.setLayoutManager(new LinearLayoutManager(getContext()));
        final String country = getCountry();


        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("",country,API_KEY);
            }
        });
        retrieveJson("",country,API_KEY);

//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!etQuery.getText().toString().equals("")){
//                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                        @Override
//                        public void onRefresh() {
//                            retrieveJson(etQuery.getText().toString(),country,API_KEY);
//                        }
//                    });
//                    retrieveJson(etQuery.getText().toString(),country,API_KEY);
//                }else{
//                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                        @Override
//                        public void onRefresh() {
//                            retrieveJson("",country,API_KEY);
//                        }
//                    });
//                    retrieveJson("",country,API_KEY);
//                }
//            }
//        });
//
//        btnAboutUs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });




        return mView;


    }

    public void retrieveJson(String query ,String country, String apiKey){


        mRefresh.setRefreshing(true);
            Call<Headlines> call;

            call= ApiClient.getInstance().getApi().getHeadlines(country,apiKey);


        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    mRefresh.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(getContext(),articles);
                    mRecyleView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                mRefresh.setRefreshing(false);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
//
//    @Override
//    public void onCreate(View v){
////        Intent i = new Intent(NewsFragment.this, ActivityNewsMain.class);
////        startActivity(i);
//    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }


}

package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.RemoveShortListAdapter;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentShortListBinding;
import com.ottego.saathidaar.viewmodel.NewMatchViewModel;

import org.json.JSONObject;


public class ShortListFragment extends Fragment  implements ApiListener {
    Context context;
    SessionManager sessionManager;
    DataModelNewMatches data;
    String member_id;
    public String ShortListUrl ="http://103.150.186.33:8080/saathidaar_backend/api/shortlist/get/all/";
    FragmentShortListBinding b;
    NewMatchViewModel viewModel;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
int count=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShortListFragment() {
        // Required empty public constructor
    }

    public static ShortListFragment newInstance(String param1, String param2) {
        ShortListFragment fragment = new ShortListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       b=FragmentShortListBinding.inflate(getLayoutInflater());
       context=getContext();
       sessionManager=new SessionManager(context);
        viewModel = new ViewModelProvider(requireActivity()).get(NewMatchViewModel.class);
        getData();
        listener();
       return  b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewShortList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {

      //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                ShortListUrl+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               b.srlRecycleViewShortList.setRefreshing(false);
               // progressDialog.dismiss();
                Log.e("ShortList response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelNewMatches.class);
                if (data.results == 1) {
                    viewModel._list.postValue(data.data);
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               b.srlRecycleViewShortList.setRefreshing(false);
             //   progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }

    private void setRecyclerView() {
// count++;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvShortList.setLayoutManager(layoutManager);
        b.rvShortList.setHasFixedSize(true);
        b.rvShortList.setNestedScrollingEnabled(true);
        RemoveShortListAdapter adapter = new RemoveShortListAdapter(context,data.data,this);
        b.rvShortList.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataShortList.setVisibility(View.GONE);
            b.rvShortList.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataShortList.setVisibility(View.VISIBLE);
        }
//refresh(1000);
    }

    private void refresh(int millisecond) {

        final Handler handler= new Handler();
        final  Runnable runnable=new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        handler.postDelayed(runnable,millisecond);



    }

    @Override
    public void onSuccess(int position) {
        getData();
    }

    @Override
    public void onFail(int position) {

    }
}
package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.InboxInvitationAdapter;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.databinding.FragmentInvitationBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;

import org.json.JSONObject;


public class InvitationFragment extends Fragment implements ApiListener {
    DataModelInbox data;
    FragmentInvitationBinding b;
    Context context;
    InboxViewModel viewModel;
    String member_Id;
    String id = "";
    SessionManager sessionManager;
    public String InvitationUrl = "http://103.150.186.33:8080/saathidaar_backend/api/request/invitations/get/all/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InvitationFragment() {
        // Required empty public constructor
    }

    public static InvitationFragment newInstance(String param1, String param2) {
        InvitationFragment fragment = new InvitationFragment();
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
        b = FragmentInvitationBinding.inflate(getLayoutInflater());
        context = getContext();
        sessionManager = new SessionManager(context);
        member_Id = sessionManager.getMemberId().trim();
        Log.e("member", member_Id);
        viewModel = new ViewModelProvider(requireActivity()).get(InboxViewModel.class);
        listener();
        getData();
        return b.getRoot();
    }

    void listener() {
        b.srlRecycleViewInvitation.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                InvitationUrl +sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewInvitation.setRefreshing(false);
               // progressDialog.dismiss();
                Log.e("Invitation response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelInbox.class);
                if (data.my_results == 1) {
                    viewModel._list.postValue(data.data);
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewInvitation.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvInvitation.setLayoutManager(layoutManager);
        b.rvInvitation.setHasFixedSize(true);
        b.rvInvitation.setNestedScrollingEnabled(true);
        InboxInvitationAdapter adapter = new InboxInvitationAdapter(context, data.data,this);
        b.rvInvitation.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataInvitation.setVisibility(View.GONE);
            b.rvInvitation.setVisibility(View.VISIBLE);
        } else {
            b.llNoDataInvitation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int position) {
        getData();
    }

    @Override
    public void onFail(int position) {

    }




}

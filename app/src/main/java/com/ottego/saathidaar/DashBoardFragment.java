package com.ottego.saathidaar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.Model.ImageModel;

import org.json.JSONObject;


public class DashBoardFragment extends Fragment {
    SessionManager sessionManager;
    ImageView ivPremiumImage, profilePic;
    TextView tvPremiumText, RequestAccept, tvDashBoardUserId, tvDashBoardUserAccountType, tvDashBoardUploadImage, Visitors, RequestSent, tvDashboardUpgrade, tvDashBoardUserName;
    int position = 0;
    DataModelDashboard model;
    LinearLayout llPremiumMatch, llMyMatch, llPremium, llshare, tvLogout, llRequestSent, llProfileVisi, llAcceptRequest;
    Animation animation;
    CountDownTimer countDownTimer;
    Context context;
    ImageModel imageModel;
    public String url = "http://103.150.186.33:8080/saathidaar_backend/api/request/count/accept-request/";
    int[] images = {R.drawable.smartphone, R.drawable.documents, R.drawable.global};
    String[] text = {"phone Number to Connect Instantly", "100% Verified Biodatas", "Find Common connections"};
    String image;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);

//       if(sessionManager.getKeyProGender().equalsIgnoreCase("female"))
//       {
//           profilePic.setBackgroundResource(R.drawable.woman);;
//
//        }else
//       {
//          // profilePic.setImageResource(R.drawable.woman);
//       }


        Log.e("gender", sessionManager.getKeyProGender());


        ivPremiumImage = view.findViewById(R.id.ivPremiumImage);
        tvPremiumText = view.findViewById(R.id.tvPremiumText);
        tvLogout = view.findViewById(R.id.tvLogout);
        profilePic = view.findViewById(R.id.profilePic);
        llshare = view.findViewById(R.id.llshare);
        tvDashboardUpgrade = view.findViewById(R.id.tvDashboardpgrade);
        RequestAccept = view.findViewById(R.id.RequestAccept);
        Visitors = view.findViewById(R.id.Visitors);
        llMyMatch = view.findViewById(R.id.llMyMatch);
        tvDashBoardUserAccountType = view.findViewById(R.id.tvDashBoardUserAccountType);
        llPremium = view.findViewById(R.id.llPremium);
        tvDashBoardUploadImage = view.findViewById(R.id.tvDashBoardUploadImage);
        tvDashBoardUserId = view.findViewById(R.id.tvDashBoardUserId);
        llPremiumMatch = view.findViewById(R.id.llPremiumMatch);
        RequestSent = view.findViewById(R.id.RequestSent);
        llRequestSent=view.findViewById(R.id.llRequestSent);
        tvDashBoardUserName=view.findViewById(R.id.tvDashBoardUserName);
        llAcceptRequest=view.findViewById(R.id.llAcceptRequest);
        llProfileVisi=view.findViewById(R.id.llProfileVisi);


        context = getContext();
        sessionManager = new SessionManager(context);
        set();
        Log.e("hey_member",sessionManager.getMemberId());

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //  animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = ivPremiumImage.getWidth();
                final float translationX = width * progress;
                ivPremiumImage.setTranslationX(translationX);
                ivPremiumImage.setTranslationX(translationX - width);
                tvPremiumText.setTranslationX(translationX);
                tvPremiumText.setTranslationX(translationX - width);


            }
        });
        animator.start();
        getData();

        tvDashBoardUserId.setText("["+sessionManager.getKeyProfileId()+"]");
        tvDashBoardUserAccountType.setText(sessionManager.getKeyCreatedby());
        //  setData();
        listener();

        tvDashBoardUserName.setText(sessionManager.getName());
        return view;

    }


    private void listener() {

        tvDashBoardUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GalleryActivity.class);
                startActivity(intent);
            }
        });


        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,GalleryActivity.class);
                startActivity(intent);
            }
        });

        llRequestSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
                llRequestSent.startAnimation(bounce);
                Intent intent=new Intent(context,TestActivity.class);
                startActivity(intent);
            }
        });

        tvDashboardUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.vpMatch, new UpgradeFragment());
//        fragmentTransaction.commit();

               // replaceFragment(new UpgradeFragment());
                Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
                tvDashboardUpgrade.startAnimation(bounce);
                Intent intent=new Intent(context,UpgradeOnButtonActivity.class);
                startActivity(intent);
            }
        });

        llPremiumMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://192.168.1.38:9094/account/help"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    intent.setData(Uri.parse("http://192.168.1.38:9094/account/help"));
                }
            }
        });


        llMyMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://192.168.1.38:9094/account/help"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    intent.setData(Uri.parse("http://192.168.1.38:9094/account/help"));
                }
            }
        });


        llPremiumMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("hhttp://192.168.1.38:9094/account/privacy-policy"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    intent.setData(Uri.parse("http://192.168.1.38:9094/account/privacy-policy"));
                }
            }
        });


        llPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://192.168.1.38:9094/account/term-condition"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    intent.setData(Uri.parse("http://192.168.1.38:9094/account/term-condition"));
                }
            }
        });


        llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=app.com.mymealdabba"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=app.com.mymealdabba"));
                }
            }
        });
        llProfileVisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
                llProfileVisi.startAnimation(bounce);
                Intent intent=new Intent(context,ProfileVisitorsActivity.class);
                startActivity(intent);
            }
        });

        llAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
                llAcceptRequest.startAnimation(bounce);
                Intent intent=new Intent(context,RequestAcceptActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelDashboard.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);


    }

    private void setData() {
        if (model.data != null && model.data.size() > 0   && model.data.isEmpty()) {
            RequestAccept.setText(model.data.get(0).accept_request_count);
            RequestSent.setText(model.data.get(0).sent_request_count);
            Visitors.setText(model.data.get(0).recent_visitors_count);
        }
    }

    private void set() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(2 * 3000, 2000) {
            public void onTick(long millisUntilFinished) {
                // Text_t_speech();
                if (position < text.length) {
                    ivPremiumImage.setImageResource(images[position]);
                    tvPremiumText.setText(text[position]);
                    position = position + 1;
                } else {
                    position = 0;
                }
            }

            @Override
            public void onFinish() {
                start();
            }

        }.start();
    }






}
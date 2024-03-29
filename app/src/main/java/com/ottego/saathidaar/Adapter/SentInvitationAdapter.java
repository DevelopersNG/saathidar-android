package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.ottego.saathidaar.InboxPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class SentInvitationAdapter extends RecyclerView.Adapter<SentInvitationAdapter.ViewHolder>{
        SessionManager sessionManager;
        private AdapterView.OnItemClickListener onItemClickListener;
        Context context;
        List<InboxModel> list;
    ApiListener clickListener;
        public SentInvitationAdapter(Context context, List<InboxModel> list,    ApiListener clickListener) {
            this.context = context;
            this.list = list;
            this.clickListener=clickListener;
        }

        @NonNull
        @Override
        public SentInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sent_item, parent, false);
            return new SentInvitationAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull SentInvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            InboxModel item = list.get(position);
            Log.e(" Inbox model", new Gson().toJson(item));
            sessionManager=new SessionManager(context);
            holder.tvInvNewMatchName.setText(item.first_name + " " + item.last_name);
            holder.tvInvNewMatchAge.setText(item.mage+" yrs");
            holder.tvInvNewMatchHeight.setText(item.religion);
            holder.tvInvNewMatchCity.setText(item.maritalStatus);
            holder.tvInvNewMatchWorkAs.setText(item.country);
            holder.tvImageCountSent.setText(item.images_count);

//            holder.llAccept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Utils.acceptRequest(context, item.member_id);
//                    holder.llAccept.setVisibility(View.GONE);
//                    holder.llAccepted.setVisibility(View.VISIBLE);
//                    holder.llDelete.setVisibility(View.GONE);
//                }
//            });


            holder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.deleteRequest(context, item.member_id);
                    holder.llDelete.setVisibility(View.GONE);
                    holder.llDeleted.setVisibility(View.VISIBLE);

                }
            });


            holder.llBlockSent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.blockMember(context, item.member_id,clickListener);
                    holder.llBlockSent.setVisibility(View.GONE);
                    holder.llBlockedSent.setVisibility(View.VISIBLE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("position  sent Inbox", String.valueOf(position));
                    InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

                }
            });


            holder.llPhotoSent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(), MemberGalleryActivity.class);
                    intent.putExtra("Member_id", item.member_id);
                    context.startActivity(intent);
                }
            });

            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(sessionManager.getKeyGender().equalsIgnoreCase("male")?R.drawable.ic_no_image__female_:R.drawable.ic_no_image__male_)
                    .transform(item.premium_status.equalsIgnoreCase("1")?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                    .into(holder.ivSentInvitation);

            if((item.request_message!= null || item.request_status_date!= null) && (!item.request_message.isEmpty() || !item.request_status_date.isEmpty()) )
            {
                holder.tvInvitationMessageSent.setText(item.request_message);
                // holder.tvInvitationDateInbox.setText(item.request_status_date);

            }else {
                holder.llMsgDateSent.setVisibility(View.GONE);
            }

            if ((item.premium_status.equalsIgnoreCase("1")))
            {
                holder.tvLevelPremiumSent.setVisibility(View.VISIBLE);
                holder.flPremiumSent.setVisibility(View.VISIBLE);
                holder.llPremiumMsgSent.setVisibility(View.VISIBLE);
            }

            }


        @Override
        public int getItemCount() {
            return list.size();
        }



        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvInvNewMatchName, tvInvNewMatchAge, tvInvNewMatchHeight,tvLevelPremiumSent, tvInvNewMatchCity, tvInvNewMatchWorkAs,tvInvitationMessageSent,tvImageCountSent;
            LinearLayout llAccept,llDelete,llAccepted,llDeleted,llPremiumMsgSent,llPhotoSent,llBlockSent,llBlockedSent,llNo_imageFemaleSentInvitation,llMsgDateSent;
            ImageView ivNoImageMaleFemaleSentInvitation,ivSentInvitation;
            FrameLayout flNoImageMaleFemaleSentInvitation,flPremiumSent;
            public ViewHolder(@NonNull View itemView) {

                super(itemView);
                tvLevelPremiumSent=itemView.findViewById(R.id.tvLevelPremiumSent);
                flPremiumSent=itemView.findViewById(R.id.flPremiumSent);
                llPremiumMsgSent=itemView.findViewById(R.id.llPremiumMsgSent);
                tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
                tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
                tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
                tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
                tvInvNewMatchWorkAs = itemView.findViewById(R.id.tvInvNewMatchWorkAs);
                llAccepted = itemView.findViewById(R.id.llAccepted);
                llAccept = itemView.findViewById(R.id.llAccept);
                llDelete = itemView.findViewById(R.id.llDelete);
                llDeleted = itemView.findViewById(R.id.llDeleted);
                llPhotoSent=itemView.findViewById(R.id.llPhotoSent);
                llBlockSent=itemView.findViewById(R.id.llBlockSent);
                llBlockedSent=itemView.findViewById(R.id.llBlockedSent);
                llMsgDateSent=itemView.findViewById(R.id.llMsgDateSent);
                tvInvitationMessageSent=itemView.findViewById(R.id.tvInvitationMessageSent);
                llNo_imageFemaleSentInvitation = itemView.findViewById(R.id.llNo_imageFemaleSentInvitation);
                ivNoImageMaleFemaleSentInvitation=itemView.findViewById(R.id.ivNoImageMaleFemaleSentInvitation);
                ivSentInvitation=itemView.findViewById(R.id.ivSentInvitation);
                flNoImageMaleFemaleSentInvitation=itemView.findViewById(R.id.flNoImageMaleFemaleSentInvitation);
                tvImageCountSent=itemView.findViewById(R.id.tvImageCountSent);
            }
        }
    }


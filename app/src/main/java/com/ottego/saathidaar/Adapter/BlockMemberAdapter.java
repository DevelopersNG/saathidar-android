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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.GalleryActivity;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class BlockMemberAdapter extends RecyclerView.Adapter<BlockMemberAdapter.ViewHolder>{
        SessionManager sessionManager;
        Context context;
        List<NewMatchesModel> list;
    ApiListener clickListener;
        public BlockMemberAdapter(Context context, List<NewMatchesModel> list,ApiListener clickListener) {
            this.context = context;
            this.list = list;
            this.clickListener=clickListener;

        }


        @NonNull
        @Override
        public BlockMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.un_block_layout, parent, false);
            return new BlockMemberAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NewMatchesModel item = list.get(position);
            Log.e(" New Matches model", new Gson().toJson(item));
            sessionManager=new SessionManager(context);

            holder.tvBlockName.setText(item.first_name + " " + item.last_name);
            holder.tvBlockAge.setText(item.mage+" yrs");
            holder.tvNewBlockHeight.setText(item.religion);
            holder.tvBlockCity.setText(item.maritalStatus);
            holder.tvBlockWorkAs.setText(item.income);
            holder.tvImageCountUnBlock.setText(item.images_count);


            holder.ivUnblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.move);
                    holder.llUnBlockAnimation.startAnimation(animation);
                    Utils.UnblockMember(context, item.member_id,clickListener);
                    holder.ivUnblock.setVisibility(View.GONE);
                    holder.llConnectBlock.setVisibility(View.VISIBLE);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("position", String.valueOf(position));
//                /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
//                        Pair.create(holder.ivUserMatch, "tnMemberImage"));
//                Intent intent = new Intent(view.getContext(), MatchesDetailsActivity.class);
//                intent.putExtra("data", new Gson().toJson(item));
//                view.getContext().startActivity(intent, options.toBundle());*/

                    MatchPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");

                }
            });


            holder.llPhotoBlock.setOnClickListener(new View.OnClickListener() {
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
                    .into(holder.ivBlockProfileImage);

            if ((item.premium_status.equalsIgnoreCase("1")))
            {
                holder.tvLevelPremiumBlock.setVisibility(View.VISIBLE);
                holder.flPremiumBlock.setVisibility(View.VISIBLE);
                holder.llPremiumMsgUnBlock.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivBlockProfileImage,ivNoImageMaleFemaleBlock;
            TextView tvBlockName, tvBlockAge, tvLevelPremiumBlock,tvNewBlockHeight, tvBlockCity, tvBlockWorkAs,tvImageCountUnBlock;
            LinearLayout llPhotoBlock,ivUnblock,llConnectBlock,llNo_imageFemaleListBlock,llUnBlockAnimation,llPremiumMsgUnBlock;
            FrameLayout flNoImageMaleFemaleListBlock,flPremiumBlock;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvLevelPremiumBlock=itemView.findViewById(R.id.tvLevelPremiumBlock);
                llPremiumMsgUnBlock=itemView.findViewById(R.id.llPremiumMsgUnBlock);
                tvBlockAge = itemView.findViewById(R.id.tvBlockAge);
                tvBlockName = itemView.findViewById(R.id.tvNewBlockName);
                tvNewBlockHeight = itemView.findViewById(R.id.tvNewBlockHeight);
                tvBlockCity = itemView.findViewById(R.id.tvBlockCity);
                tvBlockWorkAs = itemView.findViewById(R.id.tvBlockWorkAs);
                llPhotoBlock = itemView.findViewById(R.id.llPhotoBlock);
                ivUnblock=itemView.findViewById(R.id.ivUnblock);
                llConnectBlock=itemView.findViewById(R.id.llConnectBlock);
                tvImageCountUnBlock=itemView.findViewById(R.id.tvImageCountUnBlock);
                llUnBlockAnimation=itemView.findViewById(R.id.llUnBlockAnimation);
                ivBlockProfileImage=itemView.findViewById(R.id.ivBlockProfileImage);
                ivNoImageMaleFemaleBlock=itemView.findViewById(R.id.ivNoImageMaleFemaleBlock);
                flNoImageMaleFemaleListBlock=itemView.findViewById(R.id.flNoImageMaleFemaleListBlock);
                llNo_imageFemaleListBlock=itemView.findViewById(R.id.llNo_imageFemaleListBlock);
                flPremiumBlock=itemView.findViewById(R.id.flPremiumBlock);
            }
        }
    }

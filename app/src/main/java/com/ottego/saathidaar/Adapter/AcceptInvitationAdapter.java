package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.GalleryActivity;
import com.ottego.saathidaar.InboxPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class AcceptInvitationAdapter extends RecyclerView.Adapter<AcceptInvitationAdapter.ViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    Context context;
    List<InboxModel> list;

    public AcceptInvitationAdapter(Context context, List<InboxModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AcceptInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accepted, parent, false);
        return new AcceptInvitationAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AcceptInvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InboxModel item = list.get(position);
        Log.e(" New Matches model", new Gson().toJson(item));

        holder.tvInvNewMatchName.setText(item.first_name + " " + item.last_name);
        holder.tvInvNewMatchAge.setText(item.mage);
        holder.tvInvNewMatchHeight.setText(item.religion);
        holder.tvInvNewMatchCity.setText(item.maritalStatus);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position Inbox", String.valueOf(position));
                InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

            }
        });

        holder.llPhotoAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MemberGalleryActivity.class);
                intent.putExtra("Member_id", item.member_id);
                context.startActivity(intent);
            }
        });


        holder.llBlockAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.blockMember(context, item.member_id);
                holder.llBlockAccept.setVisibility(View.GONE);
                holder.llBlockedAccept.setVisibility(View.VISIBLE);
                holder.llAcceptCallMsgDecline.setEnabled(false);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvNewMatchName, tvInvNewMatchAge, tvInvNewMatchHeight, tvInvNewMatchCity, tvInvNewMatchWorkAs;
        LinearLayout llCAll,llWhatsApp, llPhotoAccept,llBlockAccept,llBlockedAccept,llAcceptCallMsgDecline;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
            tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
            tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
            tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
            tvInvNewMatchWorkAs = itemView.findViewById(R.id.tvInvNewMatchWorkAs);
            llCAll = itemView.findViewById(R.id.llCAll);
            llPhotoAccept = itemView.findViewById(R.id.llPhotoAccept);
            llWhatsApp = itemView.findViewById(R.id.llWhatsApp);
            llBlockAccept=itemView.findViewById(R.id.llBlockAccept);
            llBlockedAccept=itemView.findViewById(R.id.llBlockedAccept);
            llAcceptCallMsgDecline=itemView.findViewById(R.id.llAcceptCallMsgDecline);
        }
    }
}

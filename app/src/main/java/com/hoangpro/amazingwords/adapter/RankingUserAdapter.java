package com.hoangpro.amazingwords.adapter;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.model.Top;
import com.hoangpro.amazingwords.morefunc.mAnimation;

import java.util.List;

import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimFloatToTop;

class RankHolder extends RecyclerView.ViewHolder {
    ImageView imgAvt;
    ImageView imgRank;
    TextView tvName;
    TextView tvCoin;

    public RankHolder(@NonNull View itemView) {
        super(itemView);
        imgAvt = itemView.findViewById(R.id.imgAvt);
        imgRank = itemView.findViewById(R.id.imgRank);
        tvName = itemView.findViewById(R.id.tvName);
        tvCoin = itemView.findViewById(R.id.tvCoin);

    }
}

public class RankingUserAdapter extends RecyclerView.Adapter<RankHolder> {
    private Context context;
    private List<Top> list;

    public RankingUserAdapter(Context context, List<Top> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RankHolder(LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        Top top = list.get(position);
        holder.imgAvt.setImageResource(top.getImgAvatar());
        holder.tvName.setText(top.getName());
        holder.tvCoin.setText(String.format("%d %s", top.getCoin(),context.getString(R.string.coin)));
        AnimatorSet animatorSet = setAnimFloatToTop(holder.itemView);
        animatorSet.setStartDelay(position*500);
        animatorSet.start();
        switch (position){
            case 0:
                holder.itemView.setBackgroundResource(R.drawable.bg_top1);
                holder.tvName.setTextColor(Color.parseColor("#FFDF65"));
                break;
            case 1:
                holder.imgRank.setImageResource(R.drawable.no2);
                holder.itemView.setBackgroundResource(R.drawable.bg_top2);
                holder.tvName.setTextColor(Color.parseColor("#E6C072"));
                break;
            case 2:
                holder.imgRank.setImageResource(R.drawable.no3);
                holder.itemView.setBackgroundResource(R.drawable.bg_top3);
                holder.tvName.setTextColor(Color.parseColor("#482F22"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

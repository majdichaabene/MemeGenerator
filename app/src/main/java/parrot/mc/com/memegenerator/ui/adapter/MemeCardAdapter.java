package parrot.mc.com.memegenerator.ui.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import parrot.mc.com.memegenerator.R;
import parrot.mc.com.memegenerator.model.data.MemeEntity;
import parrot.mc.com.memegenerator.ui.component.CardOnClickListener;
import parrot.mc.com.memegenerator.utils.Constant;

public class MemeCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MemeEntity> memeEntities;
    CardOnClickListener cardOnClickListener;

    public MemeCardAdapter(List<MemeEntity> memeEntities, CardOnClickListener cardOnClickListener) {
        this.memeEntities = memeEntities;
        this.cardOnClickListener = cardOnClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.VIEW_ITEM)
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_meme, parent, false));
        else if (viewType == Constant.VIEW_LOADING)
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MemeEntity memeEntity = memeEntities.get(position);

        if (memeEntity != null)
            ((ItemViewHolder) holder).bind(memeEntity,cardOnClickListener);
    }

    public void addLoadingView() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                memeEntities.add(null);
                notifyItemInserted(memeEntities.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        memeEntities.removeAll(Collections.singleton(null));
        notifyItemRemoved(memeEntities.size());
    }


    @Override
    public int getItemCount() {
        return memeEntities == null ? 0 : memeEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return memeEntities.get(position) == null ? Constant.VIEW_LOADING : Constant.VIEW_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtTotalVote;
        private ImageView imgMeme;

        private ItemViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.name_txt);
            txtTotalVote = view.findViewById(R.id.total_vote_txt);
            imgMeme = view.findViewById(R.id.meme_img);
        }

        public void bind(final MemeEntity memeEntity, final CardOnClickListener listener){
            txtName.setText(memeEntity.getDisplayName());
            txtTotalVote.setText(memeEntity.getTotalVotesScore()+"");
            Picasso.get()
                    .load(memeEntity.getImageUrl())
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(imgMeme);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(memeEntity);
                }
            });
        }
    }
}
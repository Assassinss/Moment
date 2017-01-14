package me.zsj.moment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import me.zsj.moment.model.Picture;
import me.zsj.moment.utils.CircleTransform;
import me.zsj.moment.widget.RatioImageView;

import static me.zsj.moment.R.id.picture;

/**
 * @author zsj
 */

public class PictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Picture> pictures;


    public PictureAdapter(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_footer) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_footer, parent, false);
            return new FooterHolder(view);
        } else if (viewType == R.layout.item_picture) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_picture, parent, false);
            return new Holder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == R.layout.item_footer) {
            FooterHolder holder = (FooterHolder) viewHolder;
            holder.progressBar.setVisibility(View.VISIBLE);
        } else if (getItemViewType(position) == R.layout.item_picture) {
            Holder holder = (Holder) viewHolder;
            Picture picture = pictures.get(position);
            holder.image.setOriginalSize(16, 9);
            Context context = holder.image.getContext();

            Glide.with(context)
                    .load(context.getString(R.string.picture_host, picture.pictureUrl))
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);

            Glide.with(context)
                    .load(context.getString(R.string.picture_host, picture.avatarCover))
                    .priority(Priority.NORMAL)
                    .transform(new CircleTransform(context))
                    .into(holder.avatar);

            holder.author.setText(picture.avatar);

            holder.card.setOnClickListener(v -> {
                Intent intent = new Intent(context, PictureActivity.class);
                intent.putExtra(PictureActivity.PICTURE, picture);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (pictures.size() > 0) count += pictures.size();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return R.layout.item_footer;
        } else {
            return R.layout.item_picture;
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {

        FrameLayout card;
        RatioImageView image;
        ImageView avatar;
        TextView author;

        public Holder(View itemView) {
            super(itemView);
            card = (FrameLayout) itemView.findViewById(R.id.card_layout);
            image = (RatioImageView) itemView.findViewById(picture);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public FooterHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }

}

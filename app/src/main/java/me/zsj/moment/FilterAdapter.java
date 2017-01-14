package me.zsj.moment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author zsj
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {

    private String[] filters;

    private OnFilterListener onFilterListener;


    public void setOnFilterListener(OnFilterListener onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    public FilterAdapter(Context context) {
        filters = context.getResources().getStringArray(R.array.filters);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.text.setText(filters[position]);
        holder.filterLayout.setOnClickListener(v -> {
            if (onFilterListener != null) {
                onFilterListener.onFilter(filters[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }

    static class Holder extends RecyclerView.ViewHolder {

        FrameLayout filterLayout;
        TextView text;

        public Holder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.filter_text);
            filterLayout = (FrameLayout) itemView.findViewById(R.id.filter_layout);
        }
    }

    public interface OnFilterListener {

        void onFilter(String filterText);

    }

}

package me.zsj.moment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author zsj
 */

public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.Holder> {

    private Context context;

    private String[] sourceLinkList;

    public SourceListAdapter(Context context) {
        this.context = context;
        sourceLinkList = context.getResources()
                .getStringArray(R.array.open_source_libraries_list);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_source_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.sourceText.setText(sourceLinkList[position]);
    }

    @Override
    public int getItemCount() {
        return sourceLinkList.length;
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView sourceText;

        public Holder(View itemView) {
            super(itemView);
            sourceText = (TextView) itemView.findViewById(R.id.source_text);
        }
    }
}

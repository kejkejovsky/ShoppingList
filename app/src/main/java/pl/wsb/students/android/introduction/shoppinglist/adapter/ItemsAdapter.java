package pl.wsb.students.android.introduction.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private final List<Item> data;
    private final LayoutInflater inflater;
    public ItemsAdapter(
            Context context,
            List<Item> data
    ) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = this.inflater.inflate(R.layout.item_shopping_list, parent,
                false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = data.get(position);
        if (item == null) {
            return;
        } //if
        holder.txtItemName.setText(item.getName());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName;

        ViewHolder(View itemView) {
            super(itemView);
            this.txtItemName =
                    itemView.findViewById(R.id.txtItemName);
        }
    }
    public Item getItem(int position) {
        return data.get(position);
    }
}

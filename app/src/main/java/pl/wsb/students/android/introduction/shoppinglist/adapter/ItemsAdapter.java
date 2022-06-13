package pl.wsb.students.android.introduction.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wsb.students.android.introduction.shoppinglist.MainActivity;
import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private final List<Item> data;
    private final LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;

    public ItemsAdapter(
            Context context,
            List<Item> data
    ) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("ShoppingList");
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
        if(item.getDone() == 1){
            holder.itemView.findViewById(R.id.cardItem).setBackgroundColor(0xC0C0C0FF);
            holder.itemView.findViewById(R.id.buttonRemoveItem).setVisibility(View.INVISIBLE);
            holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.itemView.findViewById(R.id.cardItem).setBackgroundColor(0xFFFFFFFF);
            holder.itemView.findViewById(R.id.buttonRemoveItem).setVisibility(View.VISIBLE);
            holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getDone() == 0){
                    item.setDone(1);
                    holder.itemView.findViewById(R.id.cardItem).setBackgroundColor(0xC0C0C0FF);
                    holder.itemView.findViewById(R.id.buttonRemoveItem).setVisibility(View.INVISIBLE);
                    holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    item.setDone(0);
                    holder.itemView.findViewById(R.id.cardItem).setBackgroundColor(0xFFFFFFFF);
                    holder.itemView.findViewById(R.id.buttonRemoveItem).setVisibility(View.VISIBLE);
                    holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                Map<String, Object> itemValues = item.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/items/" + item.getId(), itemValues);
                mDatabaseRef.updateChildren(childUpdates);
            }
        });
        holder.itemView.findViewById(R.id.buttonRemoveItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/items/" + item.getId(), null);
                mDatabaseRef.updateChildren(childUpdates);
            }
        });
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

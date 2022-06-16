package pl.wsb.students.android.introduction.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;
import pl.wsb.students.android.introduction.shoppinglist.model.ShoppingList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private final List<Item> data;
    private final LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;
    private String listId;
    private ShoppingList shoppingList;

    public ItemsAdapter(
            Context context,
            List<Item> data,
            String listId
    ) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.listId = listId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("ShoppingList");
    }

    public void setShoppingList(ShoppingList shoppingList){
        this.shoppingList = shoppingList;
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
        holder.txtItemCategory.setText(item.getCategory());
        if(shoppingList != null && holder.txtScreenTitle != null) {
            holder.txtScreenTitle.setText(shoppingList.getName());
        }

        if(item.getDone() == 1){
            holder.cardItem.setBackgroundColor(0xC0C0C0FF);
            holder.buttonRemoveItem.setVisibility(View.INVISIBLE);
            holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.cardItem.setBackgroundColor(0xFFFFFFFF);
            holder.buttonRemoveItem.setVisibility(View.VISIBLE);
            holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getDone() == 0){
                    item.setDone(1);
                    holder.cardItem.setBackgroundColor(0xC0C0C0FF);
                    holder.buttonRemoveItem.setVisibility(View.INVISIBLE);
                    holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    item.setDone(0);
                    holder.cardItem.setBackgroundColor(0xFFFFFFFF);
                    holder.buttonRemoveItem.setVisibility(View.VISIBLE);
                    holder.txtItemName.setPaintFlags(holder.txtItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                Map<String, Object> itemValues = item.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/lists/" + listId + "/items/" + item.getId(), itemValues);
                mDatabaseRef.updateChildren(childUpdates);
            }
        });
        holder.itemView.findViewById(R.id.buttonRemoveItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/lists/" + listId + "/items/" + item.getId(), null);
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
        TextView txtItemCategory;
        TextView txtScreenTitle;
        Button buttonRemoveItem;
        CardView cardItem;

        ViewHolder(View itemView) {
            super(itemView);
            this.txtItemName =
                    itemView.findViewById(R.id.txtItemName);
            this.txtItemCategory =
                    itemView.findViewById(R.id.txtItemCategory);
            this.buttonRemoveItem =
                    itemView.findViewById(R.id.buttonRemoveItem);
            this.cardItem =
                    itemView.findViewById(R.id.cardItem);
            this.txtScreenTitle =
                    itemView.findViewById(R.id.txtScreenTitle);
        }
    }
    public Item getItem(int position) {
        return data.get(position);
    }


}

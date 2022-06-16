package pl.wsb.students.android.introduction.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.fragments.ItemsList;
import pl.wsb.students.android.introduction.shoppinglist.model.ShoppingList;

public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder> {
    private final List<ShoppingList> data;
    private final LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;
    private onListClickListener onListClickListener;

    public ShoppingListsAdapter(
            Context context,
            List<ShoppingList> data
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
        ShoppingList shoppingList = data.get(position);
        if (shoppingList == null) {
            return;
        } //if

        holder.txtItemName.setText(shoppingList.getName());
        holder.txtItemCategory.setText(shoppingList.getCreateDate());

        holder.buttonRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/lists/" + shoppingList.getId(), null);
                mDatabaseRef.updateChildren(childUpdates);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onListClickListener != null){
                    onListClickListener.onListClick(shoppingList.getId());
                }
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
    public ShoppingList getShoppingList(int position) {
        return data.get(position);
    }

    public interface onListClickListener{
        void onListClick(String id);
    }

    public void setOnListClickListener(onListClickListener onListClickListener){
        this.onListClickListener = onListClickListener;
    }
}

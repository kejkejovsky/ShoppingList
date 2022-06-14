package pl.wsb.students.android.introduction.shoppinglist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.adapter.ItemsAdapter;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class ItemsList extends Fragment {
    private List<Item> data = new ArrayList<Item>();
    private onAddItemElementListener onAddItemElementListener;
    private String maxId;

    public ItemsList(){

    }
    public ItemsList(Item item){
        addItemElement(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.items_list, parent, false);
        getItemsApiCall(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton btnAddItemElement = view.findViewById(R.id.btnAddItemElement);
        if (btnAddItemElement != null) {
            btnAddItemElement.setOnClickListener(v -> {
                handleBtnClick();
            });
        }
    }

    private void initRecyclerView(View view, List<Item> items) {
        RecyclerView recyclerView = view.findViewById(R.id.ItemsList);
        if (recyclerView == null) {
            return;
        } //if
        if (items == null) {
            return;
        } //if
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemsAdapter itemsAdapter = new ItemsAdapter(view.getContext(), items);
        recyclerView.setAdapter(itemsAdapter);
    }

    private void getItemsApiCall(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ShoppingList/items");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Item item = dataSnapshot.getValue(Item.class);
                data.add(item);
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Item item = dataSnapshot.getValue(Item.class);
                String key = item.getId();
                System.out.println("Changed item: " + key);
                data.set(getIndexByProperty(data, key), item);
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);
                String key = item.getId();
                System.out.println("Removed item: " + key);
                data.remove(getIndexByProperty(data, key));
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getMaxId();
        initRecyclerView(view, data);
    }

    private int getIndexByProperty(List<Item> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) !=null && list.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;// not there is list
    }

    private void handleBtnClick() {
        System.out.println("Click");
        if (onAddItemElementListener != null) {
            onAddItemElementListener.onAddItemElement(maxId);
        }
    }

    public void setOnAddItemElementListener(onAddItemElementListener
                                                     onAddItemElementListener) {
        this.onAddItemElementListener = onAddItemElementListener;
    }

    public interface onAddItemElementListener {
        void onAddItemElement(String id);
    }

    private void addItemElement(Item item){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("ShoppingList");

        Map<String, Object> itemValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/items/" + item.getId(), itemValues);
        mDatabaseRef.updateChildren(childUpdates);
    }

    private void getMaxId(){
        Integer max = 0;
        for(Item el: data){
            if(Integer.parseInt(el.getId()) > max){
                max = Integer.parseInt(el.getId());
            }
        }
        this.maxId = max.toString();
    }
}

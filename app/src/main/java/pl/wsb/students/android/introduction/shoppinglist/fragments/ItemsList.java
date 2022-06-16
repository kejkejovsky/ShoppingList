package pl.wsb.students.android.introduction.shoppinglist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.adapter.ItemsAdapter;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;
import pl.wsb.students.android.introduction.shoppinglist.model.ShoppingList;

public class ItemsList extends Fragment {
    private List<Item> data = new ArrayList<Item>();
    private onAddItemElementListener onAddItemElementListener;
    private String maxId;
    private String listId;

    public ItemsList(String listId){
        this.listId = listId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.items_list, parent, false);

        ((TextView) view.findViewById(R.id.txtScreenTitle)).setText("Listy zakupów");
        data.clear();
        getItemsApiCall(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("ShoppingList");
        database.getReference("ShoppingList/lists/" + listId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    ShoppingList shoppingList = task.getResult().getValue(ShoppingList.class);
                    ((TextView) view.findViewById(R.id.txtScreenTitle)).setText(shoppingList.getName());

                }
            }
        });
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
        ItemsAdapter itemsAdapter = new ItemsAdapter(view.getContext(), items, listId);
        recyclerView.setAdapter(itemsAdapter);
    }

    private void getItemsApiCall(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ShoppingList/lists/" + listId + "/items");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Item item = dataSnapshot.getValue(Item.class);
                if(getIndexByProperty(data, item.getId()) == -1) {
                    data.add(item);
                    sortData();
                }
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Item item = dataSnapshot.getValue(Item.class);
                String key = item.getId();
                if(getIndexByProperty(data, key) != -1) {
                    data.set(getIndexByProperty(data, key), item);
                    sortData();
                }
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);
                String key = item.getId();
                if(getIndexByProperty(data, key) != -1) {
                    data.remove(getIndexByProperty(data, key));
                    sortData();
                }
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
        if (onAddItemElementListener != null) {
            onAddItemElementListener.onAddItemElement(listId, maxId);
        }
    }

    public void setOnAddItemElementListener(onAddItemElementListener
                                                     onAddItemElementListener) {
        this.onAddItemElementListener = onAddItemElementListener;
    }

    public interface onAddItemElementListener {
        void onAddItemElement(String listId, String id);
    }

    private void addItemElement(Item item){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("ShoppingList");

        Map<String, Object> itemValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/lists/" + listId + "/items/" + item.getId(), itemValues);
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

    private void sortData(){
        Collections.sort(data, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Integer.parseInt(o1.getId()) > Integer.parseInt(o2.getId()) ? 1 : -1;
            }
        });
    }
}

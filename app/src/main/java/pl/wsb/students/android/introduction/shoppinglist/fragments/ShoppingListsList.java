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
import pl.wsb.students.android.introduction.shoppinglist.adapter.ShoppingListsAdapter;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;
import pl.wsb.students.android.introduction.shoppinglist.model.ShoppingList;

public class ShoppingListsList extends Fragment {
    private List<ShoppingList> data = new ArrayList<ShoppingList>();
    private OnAddShoppingListElementListener onAddShoppingListElementListener;
    private String maxId;

    public ShoppingListsList(){

    }
    public ShoppingListsList(ShoppingList item){
        onAddShoppingListElement(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_lists_list, parent, false);
        data.clear();
        getItemsApiCall(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton btnAddItemElement = view.findViewById(R.id.btnAddShoppingListElement);
        if (btnAddItemElement != null) {
            btnAddItemElement.setOnClickListener(v -> {
                handleBtnClick();
            });
        }
    }

    private void initRecyclerView(View view, List<ShoppingList> items) {
        RecyclerView recyclerView = view.findViewById(R.id.ShoppingLists);
        if (recyclerView == null) {
            return;
        } //if
        if (items == null) {
            return;
        } //if
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ShoppingListsAdapter shoppingListsAdapter = new ShoppingListsAdapter(view.getContext(), items);
        recyclerView.setAdapter(shoppingListsAdapter);
    }

    private void getItemsApiCall(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ShoppingList/lists");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ShoppingList item = dataSnapshot.getValue(ShoppingList.class);
                data.add(item);
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ShoppingList item = dataSnapshot.getValue(ShoppingList.class);
                String key = item.getName();
                System.out.println("Changed item: " + key);
                data.set(getIndexByProperty(data, key), item);
                getMaxId();
                initRecyclerView(view, data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ShoppingList item = dataSnapshot.getValue(ShoppingList.class);
                String key = item.getId();
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

    private int getIndexByProperty(List<ShoppingList> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) !=null && list.get(i).getName().equals(id)) {
                return i;
            }
        }
        return -1;// not there is list
    }

    private void handleBtnClick() {
        System.out.println("Click");
        if (onAddShoppingListElementListener != null) {
            onAddShoppingListElementListener.onAddShoppingListElement(maxId);
        }
    }

    public void setOnAddShoppingListElementListener(OnAddShoppingListElementListener
                                                            onAddShoppingListElementListener) {
        this.onAddShoppingListElementListener = onAddShoppingListElementListener;
    }

    public interface OnAddShoppingListElementListener {
        void onAddShoppingListElement(String id);
    }

    private void onAddShoppingListElement(ShoppingList item){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("ShoppingList");

        Map<String, Object> itemValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/lists/" + item.getId(), itemValues);
        mDatabaseRef.updateChildren(childUpdates);
    }

    private void getMaxId(){
        Integer max = 0;
        for(ShoppingList el: data){
            if(Integer.parseInt(el.getId()) > max){
                max = Integer.parseInt(el.getId());
            }
        }
        this.maxId = max.toString();
    }
}

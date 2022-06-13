package pl.wsb.students.android.introduction.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pl.wsb.students.android.introduction.shoppinglist.adapter.ItemsAdapter;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class MainActivity extends AppCompatActivity {
    private List<Item> data = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getItemsApiCall();
    }

    private void initRecyclerView(List<Item> items) {
        RecyclerView recyclerView = findViewById(R.id.ItemsList);
        if (recyclerView == null) {
            return;
        } //if
        if (items == null) {
            return;
        } //if
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter itemsAdapter = new ItemsAdapter(this, items);
        recyclerView.setAdapter(itemsAdapter);

    }

    private void getItemsApiCall(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ShoppingList/items");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Item item = dataSnapshot.getValue(Item.class);
                data.add(item);
                initRecyclerView(data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Item item = dataSnapshot.getValue(Item.class);
                String key = item.getId();
                System.out.println("Changed item: " + key);
                data.set(getIndexByProperty(data, key), item);
                initRecyclerView(data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);
                String key = item.getId();
                System.out.println("Removed item: " + key);
                data.remove(getIndexByProperty(data, key));
                initRecyclerView(data);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initRecyclerView(data);
    }

    private int getIndexByProperty(List<Item> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) !=null && list.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;// not there is list
    }

}
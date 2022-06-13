package pl.wsb.students.android.introduction.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pl.wsb.students.android.introduction.shoppinglist.adapter.ItemsAdapter;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Item item = new Item();
        item.setId(1);
        item.setName("Pyry");
        item.setCategory("Warzywa");
        List<Item> data = new ArrayList<Item>();
        data.add(item);

        testFirebase();
        initRecyclerView(data);
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

        for(Integer i = 0; i < itemsAdapter.getItemCount(); i++) {
            Item test = itemsAdapter.getItem(i);
            testFirebase(test, i.toString());
        }
    }

    private void testFirebase(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message3");

        myRef.setValue("Test1");

    }

    private void testFirebase(Item item, String itemId){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ShoppingList");

        myRef.child("items").child(itemId).setValue(item);
    }
}
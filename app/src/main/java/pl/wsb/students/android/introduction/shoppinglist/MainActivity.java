package pl.wsb.students.android.introduction.shoppinglist;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wsb.students.android.introduction.shoppinglist.adapter.ShoppingListsAdapter;
import pl.wsb.students.android.introduction.shoppinglist.fragments.AddItem;
import pl.wsb.students.android.introduction.shoppinglist.fragments.AddShoppingList;
import pl.wsb.students.android.introduction.shoppinglist.fragments.ItemsList;
import pl.wsb.students.android.introduction.shoppinglist.fragments.ShoppingListsList;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;
import pl.wsb.students.android.introduction.shoppinglist.model.ShoppingList;

public class MainActivity extends AppCompatActivity
        implements AddItem.onAddItemListener,
        ItemsList.onAddItemElementListener,
        ShoppingListsList.OnAddShoppingListElementListener,
        AddShoppingList.onAddShoppingListener,
        ShoppingListsAdapter.onListClickListener {

    private DatabaseReference mDatabaseRef;

    private void replaceFragment(Fragment fragment, int containerResId) {
        if (fragment == null) {
            return;
        } //if
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerResId, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mDatabaseRef = database.getReference("ShoppingList");
        setContentView(R.layout.activity_main);
        ShoppingListsList shoppingListsList = new ShoppingListsList();
        shoppingListsList.setOnAddShoppingListElementListener(this);
        shoppingListsList.setOnListClickListener(this);
        replaceFragment(shoppingListsList, R.id.fragmentContainer);
    }

    @Override
    public void onAddItem(String listId, Item item) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/lists/" + listId + "/items/" + item.getId(), item.toMap());
        mDatabaseRef.updateChildren(childUpdates);
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onAddItemElement(String listId, String id) {
        AddItem addItem = new AddItem(listId, id);
        addItem.setOnAddItemListener(this);
        replaceFragment(addItem, R.id.fragmentContainer);
    }

    @Override
    public void onAddShoppingListElement(String id, List<ShoppingList> data) {
        AddShoppingList addShoppingList = new AddShoppingList(id, data);
        addShoppingList.setOnAddShoppingListener(this);
        replaceFragment(addShoppingList, R.id.fragmentContainer);
    }

    @Override
    public void onAddShoppingList(ShoppingList item) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/lists/" + item.getId(), item.toMap());
        mDatabaseRef.updateChildren(childUpdates);
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onListClick(String id) {
        ItemsList itemsList = new ItemsList(id);
        itemsList.setOnAddItemElementListener(this);
        replaceFragment(itemsList, R.id.fragmentContainer);
    }
}
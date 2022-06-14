package pl.wsb.students.android.introduction.shoppinglist;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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
        AddShoppingList.onAddShoppingListener {

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
        setContentView(R.layout.activity_main);
//        ItemsList itemsList = new ItemsList();
//        itemsList.setOnAddItemElementListener(this);
//        replaceFragment(itemsList, R.id.fragmentContainer);
        ShoppingListsList shoppingListsList = new ShoppingListsList();
        shoppingListsList.setOnAddShoppingListElementListener(this);
        replaceFragment(shoppingListsList, R.id.fragmentContainer);
    }

    @Override
    public void onAddItem(Item item) {
        ItemsList itemsList = new ItemsList(item);
        itemsList.setOnAddItemElementListener(this);
        replaceFragment(itemsList, R.id.fragmentContainer);
    }

    @Override
    public void onAddItemElement(String id) {
        AddItem addItem = new AddItem(id);
        addItem.setOnAddItemListener(this);
        replaceFragment(addItem, R.id.fragmentContainer);
    }

    @Override
    public void onAddShoppingListElement(String id) {
        AddShoppingList addShoppingList = new AddShoppingList(id);
        addShoppingList.setOnAddShoppingListener(this);
        replaceFragment(addShoppingList, R.id.fragmentContainer);
    }

    @Override
    public void onAddShoppingList(ShoppingList item) {
        ShoppingListsList itemsList = new ShoppingListsList(item);
        itemsList.setOnAddShoppingListElementListener(this);
        replaceFragment(itemsList, R.id.fragmentContainer);
    }
}
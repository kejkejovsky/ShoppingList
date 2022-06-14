package pl.wsb.students.android.introduction.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import pl.wsb.students.android.introduction.shoppinglist.fragments.AddItem;
import pl.wsb.students.android.introduction.shoppinglist.fragments.ItemsList;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class MainActivity extends AppCompatActivity implements AddItem.onAddItemListener, ItemsList.onAddItemElementListener {

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
        ItemsList itemsList = new ItemsList();
        itemsList.setOnAddItemElementListener(this);
        replaceFragment(itemsList, R.id.fragmentContainer);
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
}
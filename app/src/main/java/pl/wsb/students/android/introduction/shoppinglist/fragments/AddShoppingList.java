package pl.wsb.students.android.introduction.shoppinglist.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Date;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;
import pl.wsb.students.android.introduction.shoppinglist.model.ShoppingList;

public class AddShoppingList extends Fragment {
    private onAddShoppingListener onAddShoppingListener;
    private String itemId;

    public AddShoppingList(String itemId){
        this.itemId = itemId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.add_shopping_list, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnAddItem = view.findViewById(R.id.btnAddShoppingList);
        if (btnAddItem != null) {
            btnAddItem.setOnClickListener(v -> {
                handleBtnClick(view);
            });
        } //if
    }
    private void handleBtnClick(@NonNull View view) {
        EditText editTextName = view.findViewById(R.id.editShoppingListName);
        if (editTextName == null) {
            return;
        } //if
        if (TextUtils.isEmpty(editTextName.getText())) {
            Toast
                    .makeText(
                            getContext(),
                            getString(R.string.shopping_list__no_name),
                            Toast.LENGTH_SHORT
                    )
                    .show();
            return;
        }
        ShoppingList item = new ShoppingList();
        String itemName = editTextName.getText().toString();

        Integer newItemId = Integer.parseInt(this.itemId);
        newItemId++;
        item.setId(newItemId.toString());
        item.setName(itemName);
        item.setCreateDate(new Date().toString());

        if (onAddShoppingListener != null) {
            onAddShoppingListener.onAddShoppingList(item);
        } //if
    }

    public void setOnAddShoppingListener(onAddShoppingListener
                                             onAddItemListener) {
        this.onAddShoppingListener = onAddItemListener;
    }

    public interface onAddShoppingListener {
        void onAddShoppingList(ShoppingList item);
    }
}

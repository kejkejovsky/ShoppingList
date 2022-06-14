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

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class AddItem extends Fragment {
    private onAddItemListener onAddItemListener;
    private String itemId;

    public AddItem(String itemId){
        this.itemId = itemId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.add_item, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnAddItem = view.findViewById(R.id.btnAddItem);
        if (btnAddItem != null) {
            btnAddItem.setOnClickListener(v -> {
                handleBtnClick(view);
            });
        } //if
    }
    private void handleBtnClick(@NonNull View view) {
        EditText editTextName = view.findViewById(R.id.editTextName);
        if (editTextName == null) {
            return;
        } //if
        EditText editTextCategory = view.findViewById(R.id.editTextCategory);
        if (editTextCategory == null) {
            return;
        }
        if (TextUtils.isEmpty(editTextName.getText())) {
            Toast
                    .makeText(
                            getContext(),
                            getString(R.string.item__no_name),
                            Toast.LENGTH_SHORT
                    )
                    .show();
            return;
        }else if (TextUtils.isEmpty(editTextCategory.getText())) {
            Toast
                    .makeText(
                            getContext(),
                            getString(R.string.item__no_category),
                            Toast.LENGTH_SHORT
                    )
                    .show();
            return;
        }
        Item item = new Item();
        String itemName = editTextName.getText().toString();
        String itemCategory = editTextCategory.getText().toString();

        Integer newItemId = Integer.parseInt(this.itemId);
        newItemId++;
        item.setId(newItemId.toString());
        item.setName(itemName);
        item.setCategory(itemCategory);
        item.setDone(0);

        if (onAddItemListener != null) {
            onAddItemListener.onAddItem(item);
        } //if
    }

    public void setOnAddItemListener(onAddItemListener
                                                     onAddItemListener) {
        this.onAddItemListener = onAddItemListener;
    }

    public interface onAddItemListener {
        void onAddItem(Item item);
    }
}

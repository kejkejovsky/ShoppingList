package pl.wsb.students.android.introduction.shoppinglist.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import pl.wsb.students.android.introduction.shoppinglist.R;
import pl.wsb.students.android.introduction.shoppinglist.model.Item;

public class AddItem extends Fragment {
    private onAddItemListener onAddItemListener;
    private String itemId;
    private String listId;

    public AddItem(String listId, String itemId){
        this.listId = listId;
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
        Spinner dropdown = view.findViewById(R.id.editTextCategory);
        EditText editTextName = view.findViewById(R.id.editTextName);
        dropdown.setMinimumWidth(editTextName.getWidth());

        String[] items = {"Warzywa", "Owoce", "Napoje", "Słodycze", "Chemia", "Alkohol", "Inne"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
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
        Spinner editTextCategory = view.findViewById(R.id.editTextCategory);
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
        }else if (editTextCategory.getSelectedItem().toString() == "") {
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
        String itemCategory = editTextCategory.getSelectedItem().toString();

        Integer newItemId = Integer.parseInt(this.itemId);
        newItemId++;
        item.setId(newItemId.toString());
        item.setName(itemName);
        item.setCategory(itemCategory);
        item.setDone(0);

        if (onAddItemListener != null) {
            onAddItemListener.onAddItem(listId, item);
        } //if
    }

    public void setOnAddItemListener(onAddItemListener
                                                     onAddItemListener) {
        this.onAddItemListener = onAddItemListener;
    }

    public interface onAddItemListener {
        void onAddItem(String listId, Item item);
    }
}

package com.example.mipt5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText currencyEditText;
    private ArrayAdapter<String> adapter;
    private DataLoader dataLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        currencyEditText = findViewById(R.id.currencyEditText);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        dataLoader = new DataLoader(new DataLoader.DataLoaderCallback() {
            @Override
            public void onDataLoaded(ArrayList<String> data) {
                updateListView(data, currencyEditText.getText().toString());
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });

        currencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidCurrencyCode(charSequence.toString())) {
                    updateListView(dataLoader.getCachedData(), charSequence.toString());
                } else {
                    showToast("Invalid currency code");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isValidCurrencyCode(editable.toString())) {
                    dataLoader.loadData(editable.toString());
                }
            }
        });
    }

    private void updateListView(ArrayList<String> data, String filterCurrency) {
        if (data == null) {
            showToast("Error loading or parsing data");
            return;
        }

        ArrayList<String> filteredData = new ArrayList<>();
        for (String item : data) {
            if (item.toLowerCase().contains(filterCurrency.toLowerCase())) {
                filteredData.add(item);
            }
        }

        adapter.clear();
        adapter.addAll(filteredData);
        adapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidCurrencyCode(String code) {
        return code.length() == 3;
    }
}

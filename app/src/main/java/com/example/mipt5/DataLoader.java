package com.example.mipt5;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataLoader {

    private DataLoaderCallback callback;
    private ArrayList<String> cachedData;

    public interface DataLoaderCallback {
        void onDataLoaded(ArrayList<String> data);
        void onError(String errorMessage);
    }

    public DataLoader(DataLoaderCallback callback) {
        this.callback = callback;
    }

    public void loadData(String currencyCode) {
        new AsyncTask<String, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(String... params) {
                try {
                    URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    return Parser.parseXml(stringBuilder.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<String> data) {
                if (data != null) {
                    cachedData = data;
                    callback.onDataLoaded(data);
                } else {
                    callback.onError("Error loading or parsing data");
                }
            }
        }.execute(currencyCode);
    }

    public ArrayList<String> getCachedData() {
        return cachedData;
    }
}

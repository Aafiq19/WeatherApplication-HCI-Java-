package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerNews;
    private List<NewsItem> newsList = new ArrayList<>();
    private NewsAdapter adapter;
    private final String apiKey = "edb974366f2448efab720a7fc291a19f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerNews = findViewById(R.id.recyclerNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(newsList);
        recyclerNews.setAdapter(adapter);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // safer back

        // DEBUG: placeholder to check RecyclerView
        newsList.add(new NewsItem("Test Title", "Test Description"));
        adapter.notifyDataSetChanged();

        fetchNews();
    }

    private void fetchNews() {
        // Use the search endpoint with q=weather
        String url = "https://gnews.io/api/v4/search" +
                "?q=weather" +
                "&in=title,description" +
                "&lang=en" +
                "&token=f38aeb00a0d53b0eb0198abbadfac0e9";  // replace with your actual API key

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray articles = response.getJSONArray("articles");
                        newsList.clear();
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject art = articles.getJSONObject(i);
                            String title = art.optString("title", "No Title");
                            String desc = art.optString("description", "No Description");
                            if (!title.isEmpty() || !desc.isEmpty()) {
                                newsList.add(new NewsItem(title, desc));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("NewsActivity", "JSON Parsing Error", e);
                    }
                },
                error -> Log.e("NewsActivity", "API Request Error", error)
        );

        queue.add(request);
    }

}

package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    //Import UI properties to the java files
    EditText etCity;
    Button btnGetWeather;
    TextView tvCity, tvTemp, tvCondi;
    ImageView weatherImg;
    private final String apiKey = "08c5765c70b22ac509a75c3420485974"; // API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Assigning UI IDs (Buttons) to java variables
        Button btnNews = findViewById(R.id.btnNews);
        Button btnSettings = findViewById(R.id.btnSettings);

        //Coding the functions to navigation buttons
        btnNews.setOnClickListener(v ->
                startActivity(new Intent(WeatherActivity.this, NewsActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(WeatherActivity.this, SettingsActivity.class)));

        //Assigning the UI IDs to java variables
        etCity = findViewById(R.id.etCity);
        btnGetWeather = findViewById(R.id.btnGetWeather);
        tvCity = findViewById(R.id.txtCity);
        tvTemp = findViewById(R.id.txtTemp);
        tvCondi = findViewById(R.id.txtCondi);
        weatherImg = findViewById(R.id.wethImg);

        //Weather City or country input field
        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = etCity.getText().toString().trim();
                if (city.isEmpty()) {
                    Toast.makeText(WeatherActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                } else {
                    getWeatherData(city);
                }
            }
        });
    }

//    startActivity(new Intent(this, SettingsActivity.class));

    //Get the weather details by using the API and API URL
    private void getWeatherData(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city.replace(" ", "%20") + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        //Creating an instants of JsonObjectRequest to get the data via api
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);

                            double temp = main.getDouble("temp");
                            String description = weather.getString("main"); // e.g. "Clear", "Rain", "Clouds"

                            tvCity.setText(city.toUpperCase()); // this will change the user input to Upper class then only code can check the input with the api data
                            tvTemp.setText(temp + " °C"); // showing the temperature value via getting the data from the api
                            tvCondi.setText(description); // showing the weather condition via getting the data from the api

                            //Set image based on condition
                            if (description.equalsIgnoreCase("Clear") || description.equalsIgnoreCase("Smoke")) {
                                weatherImg.setImageResource(R.drawable.sunny);
                            } else if (description.equalsIgnoreCase("Clouds")) {
                                weatherImg.setImageResource(R.drawable.cloudy);
                            } else if (description.equalsIgnoreCase("Rain")) {
                                weatherImg.setImageResource(R.drawable.rainy);
                            } else if (description.equalsIgnoreCase("Thunderstorm")) {
                                weatherImg.setImageResource(R.drawable.thunder);
                            } else if (description.equalsIgnoreCase("Snow")) {
                                weatherImg.setImageResource(R.drawable.snow);
                            } else {
                                weatherImg.setImageResource(R.drawable.default_weather);
                            }

                            //Exception handler
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(WeatherActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "City not found", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}

package com.example.contacts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contacts.R;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NewsActivity extends AppCompatActivity {
    String myUrl = "http://localhost:8080/api/covidInBg";
    TextView resultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        resultsTextView = (TextView) findViewById(R.id.results);
        getData();

    }

    public void getData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://z-clinic.herokuapp.com/api/covidInBg";


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        JSONObject jsonObject = null;
                        String statistic = "";
                        try {
                            jsonObject = new JSONObject(response);
                            String lastDayCases = jsonObject.getString("lastDayCases");
                            String allCases = jsonObject.getString("allCases");
                            String date = jsonObject.getString("date");
                            statistic = "Last Day Cases:" + lastDayCases +
                                    "\n"+"Total Cases: "+ allCases + "\n" + date;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        resultsTextView.setText(statistic);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultsTextView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);


    }

}
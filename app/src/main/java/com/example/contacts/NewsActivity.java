package com.example.contacts;

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
    ProgressDialog progressDialog;
    //Button displayData;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        resultsTextView = (TextView) findViewById(R.id.results);
        //displayData = (Button) findViewById(R.id.displayData);
        // implement setOnClickListener event on displayData button
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
                        System.out.println(response);
                        resultsTextView.setText("Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultsTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
/*
    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(NewsActivity.this);
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(myUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();

                    }

                    // return the data to onPostExecute method
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                AsyncHttpClient client=new AsyncHttpClient();
                AsyncHttpClient.BoundRequestBuilder getRequest
                        = client.prepareGet(myUrl);
                final ListenableFuture<Response> listenableFuture
                        = client.executeRequest(getRequest.build());
                Response response = listenableFuture.get();
                String res =response.getResponseBody("UTF-8");
                System.out.println(res);



                JSONObject jsonObject = new JSONObject(res);
                String lastDayCases = jsonObject.getString("lastDayCases");
                String allCases = jsonObject.getString("allCases");
                String date = jsonObject.getString("date");

                String statistic = "Total COVID cases are: " + lastDayCases +
                        "\n"+"All cases are: "+ allCases + " " + date;

                //Show the Textview after fetching data
                resultsTextView.setVisibility(View.VISIBLE);
                resultsTextView.setText(statistic);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // implement setOnClickListener event on displayData button
*/

}
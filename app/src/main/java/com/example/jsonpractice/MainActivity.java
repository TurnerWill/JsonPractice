package com.example.jsonpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jsonpractice.retrofit.RetrofitClientInstance;
import com.example.jsonpractice.retrofit.ShibeService;
import com.noname.jsonpractice.ShibeAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements ShibeAdapter.OnShibeClicked {

    private static final String TAG = "MSG";
    private static final String TAG2 = "MSG2";
    private Button button;
    private RecyclerView recyclerView;
    private com.noname.jsonpractice.ShibeAdapter shibeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofitRequest(1);
        //volleyRequest(3);

        button = findViewById(R.id.btn); // button used to switch between grid and linear views
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof GridLayoutManager)
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                else
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            }
        });
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        new ShibeTask().execute("50");

    }


    // this interface method goes from a clicked imageview to a full frame in PictureActivity
    @Override
    public void shibeClicked(String url) {
        Toast.makeText(this, "It's working, sort of.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
        intent.putExtra("url_address", url);
        startActivity(intent);
    }

    private void loadRecyclerView(List<String> strings) {

        ShibeAdapter shibeAdapter = new ShibeAdapter(strings, (ShibeAdapter.OnShibeClicked) MainActivity.this);
        recyclerView.setAdapter(shibeAdapter);

    }

    //***********************************
    //  Retrofit http url network request
    //***********************************
    public void retrofitRequest(int count){
        // 1:declare ShibeService and initialize it using the RetrofitClientInstance
        ShibeService shibeService = RetrofitClientInstance
                                    .getRetrofit()
                                    .create(ShibeService.class);

        //2. declare ShibeService return type and initialize it using the ShibeService
        Call<List<String>> shibeCall = shibeService.loadShibes(count);

        //3. Use the shibeCall from Step 2 and call the .enqueue method
        shibeCall.enqueue(new Callback<List<String>>() {

            @Override               // use keystroke: new, space, control space to auto generate in parameter space
            public void onResponse(Call<List<String>> call, retrofit2.Response<List<String>> response) {

                if (response.isSuccessful()){
                   // Log.d(TAG2, "onResponse: success");
                    response.body();

                }else {
                    Log.d(TAG2, "onResponse: failure");
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    //*********************************
    //      Volley  http url request
    //**********************************

    public void volleyRequest(int count){
        //1. setup url
        String baseUrl="http:/shibe.online/api/shibes";
        String query = "?count=" + count;
        String url = baseUrl + query;

        //2. create request queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //3. create json array request or json object request
        // then init it with new json array request or json object request
        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {// Param 2: success listener
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> list = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++){
                            try {
                               // Log.d(TAG, "onResponse" + response.get(0).toString());
                                list.add(response.get(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        loadRecyclerView(list);
                    }
                },
                new Response.ErrorListener() { // Param 3: error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        // 4. pass the request object from step 3 into the requestQueue object from step 2
        requestQueue.add(request);

    }

    //*********************************
    // native way http connection url
    //********************************

    class ShibeTask extends AsyncTask<String, Void, List<String>> {

        private final String TAG = "msg";

        @Override
        protected List<String> doInBackground(String... strings) {
            HttpURLConnection httpURLConnection = null;
            // Declare and Init variables
            String baseUrl = "http:/shibe.online/api/shibes";

            String query = "?count=" + strings[0];
            // this will collect all the json
            // Strings are immutable and thread safe
            StringBuilder result = new StringBuilder();

            try {
                // Create a URL object, passing the url string into the constructor
                URL url = new URL(baseUrl + query);

                // use the url object to create an internet connection
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // create a inputstream instance and initialize it with a bufferedinputstream
                // then pass the stream from the httpUrlConnection
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                // declare inputstreamreader and init with inputstream
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                // declare bufferedreader object and init it with the inputstream
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;

                // read each line from the bufferedreader object and append it into our result(StringBuilder)
                while ((line = reader.readLine()) != null) {
                    // if line is not null append to result
                    result.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // important to close the connection when done
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }

            Log.d(TAG, "do in background " + result);

            // Convert string(json) into List<String>
            // first remove brackets
            String removeBrackets = result.substring(1, result.length() - 1);
            // second replace quotes with nothing
            String removeQuotes = removeBrackets.replace("\"", "");
            // third split using commma, which creates a new line after every comma
            String[] urls = removeQuotes.split(",");

            return Arrays.asList(urls);
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            loadRecyclerView(strings);
        }
    }


}




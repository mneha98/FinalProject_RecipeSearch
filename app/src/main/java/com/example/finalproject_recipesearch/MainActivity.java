package com.example.finalproject_recipesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // creating que queue object
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        btnSearchClickEventHandler();
    }

    private StringRequest searchNameStringRequest() {
        /*final String API = "&api_key=Rj03HpAXEXrZDfrOx9sIU8W2Fk4kWcAlFm4u5Ldd";
        final String NAME_SEARCH = "&q=";
        final String DATA_SOURCE = "&ds=Standard Reference";
        final String FOOD_GROUP = "&fg=";
        final String SORT = "&sort=r";
        final String MAX_ROWS = "&max=25";
        final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://api.nal.usda.gov/ndb/search/?format=json";*/

        String url = "https://api.edamam.com/search?q=flour&beans&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response);
                            int count = result.getInt("count");
                            //JSONArray resultList = result.getJSONArray("item");
                            Log.d("Message1","It has probably gone through");
                            Log.d("Message2", "The number of recipes is: " + count);



                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(MainActivity.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void btnSearchClickEventHandler() {
        // cancelling all requests about this search if in queue
        //queue.cancelAll(TAG_SEARCH_NAME);

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchNameStringRequest();
        //stringRequest.setTag(TAG_SEARCH_NAME);

        // executing the request (adding to queue)
        queue.add(stringRequest);
    }

}

package com.example.finalproject_recipesearch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import static java.security.AccessController.getContext;

public class Results extends AppCompatActivity {
    /** creating queue object. Required for API call.*/
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Log.d("Message6.0", "Screen loaded with contents");
        queue = Volley.newRequestQueue(Results.this);
        updateRecipeList();
        /*findViewById(R.id.view).setOnClickListener(unused -> {
            startActivity(new Intent(Results.this, Recipe.class));
        });*/

    }

    public void updateRecipeList() {
        Log.d("Message6.2", "Entered updateRecipeList()");
        // cancelling all requests about this search if in queue
        //queue.cancelAll(TAG_SEARCH_NAME);

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchNameStringRequest();
        //stringRequest.setTag(TAG_SEARCH_NAME);

        // executing the request (adding to queue)
        queue.add(stringRequest);
    }

    private StringRequest searchNameStringRequest() {
        Log.d("Message0.0", "Entered searchNameStringRequest");

        final String API = "&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";
        StringBuilder ingredientSearch = new StringBuilder("flour");
        StringBuilder filterSearch = new StringBuilder();
        //final String FOOD_GROUP = "&fg=";
        //final String SORT = "&sort=r";
        final String MAX_ROWS = "&from=0&to=5";
        //final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://api.edamam.com/search?q=";

        for (String current : MainActivity.ingredientList) {
            ingredientSearch.append("+" + current);
        }
        for (String current : MainActivity.filterList) {
            filterSearch.append("&" + current);
        }
        String url = URL_PREFIX + ingredientSearch + filterSearch + API + MAX_ROWS;
        Log.d("Message0.3","GeneratedURL: " + url);


        // String url = "https://api.edamam.com/search?q=flour&beans&health=peanut-free&health=tree-nut-free&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";



        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Message0.1", "Entered onResponse");
                        try {
                            //addRecipe(response);
                            Log.d("Message0.2", "Entered try catch");
                            JSONObject result = new JSONObject(response);
                            Log.d("Message1", "It has probably gone through");
                            int count = result.getInt("count");
                            Log.d("Message2", "The number of recipes is: " + count);
                            //JSONArray hits = new JSONArray();
                            JSONArray hits = result.getJSONArray("hits");
                            LinearLayout recipeListLayout = findViewById(R.id.layoutRecipeList);
                            recipeListLayout.removeAllViews();
                            for (int i = 0; i < hits.length(); i++) {
                                View recipeChunk = getLayoutInflater().inflate(R.layout.chunk_recipe,
                                        recipeListLayout, false);
                                JSONObject currentObject = hits.getJSONObject(i);
                                JSONObject recipe = currentObject.getJSONObject("recipe");
                                String currentRecipeName = recipe.getString("label");
                                String currentRecipeLink = recipe.getString("url");
                                String currentRecipeImage = recipe.getString("image");
                                TextView recipeName = recipeChunk.findViewById(R.id.recipeName);
                                recipeName.setText(currentRecipeName);
                                TextView recipeLink = recipeChunk.findViewById(R.id.recipeLink);
                                recipeLink.setText(currentRecipeLink);
                                ImageView recipeImage = recipeChunk.findViewById(R.id.recipeImage);
                                /*Drawable d = (LoadImageFromWebOperations(currentRecipeImage));
                                recipeImage.setImageDrawable(d);*/
                                Picasso.get().load(currentRecipeImage).fit().into(recipeImage);
                                recipeListLayout.addView(recipeChunk);
                            }
                            //double yield = recipe.getDouble("yield");
                            //JSONArray resultList = result.getJSONArray("item");
                            //Log.d("Message3", "The recipe name is: " + recipeName);

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(Results.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(Results.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
        /*return new StringRequest(Request.Method.GET, url,
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
                            //JSONArray hits = new JSONArray();
                            //hits = result.getJSONArray("hits");
                            //JSONObject recipe = new JSONObject();
                            //recipe = hits.getJSONObject(0);
                            //String recipeName = recipe.getString("label");

                            //JSONArray resultList = result.getJSONArray("item");
                            Log.d("Message1","It has probably gone through");
                            Log.d("Message2", "The number of recipes is: " + count);
                            //Log.d("Message3", "The recipe name is: " + recipeName);



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
                });*/
    }

    /*private void addRecipe(String response) {
        Log.d("Message0.2", "Entered try catch");
        JSONObject result = new JSONObject(response);
        Log.d("Message1", "It has probably gone through");
        int count = result.getInt("count");
        Log.d("Message2", "The number of recipes is: " + count);
        //JSONArray hits = new JSONArray();
        JSONArray hits = result.getJSONArray("hits");
        LinearLayout recipeListLayout = findViewById(R.id.layoutRecipeList);
        recipeListLayout.removeAllViews();
        for (int i = 0; i < hits.length(); i++) {
            View recipeChunk = getLayoutInflater().inflate(R.layout.chunk_recipe,
                    recipeListLayout, false);
            JSONObject currentObject = hits.getJSONObject(i);
            JSONObject recipe = currentObject.getJSONObject("recipe");
            String currentRecipeName = recipe.getString("label");
            String currentRecipeLink = recipe.getString("url");
            TextView recipeName = recipeChunk.findViewById(R.id.recipeName);
            recipeName.setText(currentRecipeName);
            TextView recipeLink = recipeChunk.findViewById(R.id.recipeLink);
            recipeLink.setText(currentRecipeLink);
            recipeListLayout.addView(recipeChunk);
    }*/
    /*private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }*/
}

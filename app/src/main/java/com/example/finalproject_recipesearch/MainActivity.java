package com.example.finalproject_recipesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /** creating queue object. Required for API call.*/
    private RequestQueue queue;

    /** Stores ingredients that the user has.*/
    public static List<String> ingredientList = new ArrayList<>();

    /**Stores names of all the filters. */
    public static List<String> filterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.addIngredient).setOnClickListener(unused -> {
            addIngredient();
        });

        findViewById(R.id.findRecipe).setOnClickListener(unused -> {
            checkFilterList();
            findRecipeClicked();
        });



        //Required for API call
        queue = Volley.newRequestQueue(this);
        btnSearchClickEventHandler();
    }

    private JsonObjectRequest searchNameStringRequest() {

        /*final String API = "&api_key=Rj03HpAXEXrZDfrOx9sIU8W2Fk4kWcAlFm4u5Ldd";
        final String NAME_SEARCH = "&q=";
        final String DATA_SOURCE = "&ds=Standard Reference";
        final String FOOD_GROUP = "&fg=";
        final String SORT = "&sort=r";
        final String MAX_ROWS = "&max=25";
        final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://api.nal.usda.gov/ndb/search/?format=json";*/

        String url = "https://api.edamam.com/search?q=flour&beans&health=peanut-free&health=tree-nut-free&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new JsonObjectRequest(Request.Method.GET, url, null,
                      new Response.Listener<JSONObject>() {
                          @Override
                          public void onResponse(JSONObject response) {
                              try {
                                  int count = response.getInt("count");
                                  //JSONArray hits = new JSONArray();
                                  JSONArray hits = response.getJSONArray("hits");
                                  JSONObject recipe = hits.getJSONObject(0);
                                  String recipeName = recipe.get("label").toString();

                                  //JSONArray resultList = result.getJSONArray("item");
                                  Log.d("Message1", "It has probably gone through");
                                  Log.d("Message2", "The number of recipes is: " + count);
                                  Log.d("Message3", "The recipe name is: " + recipeName);
                                  // catch for the JSON parsing error
                              } catch (JSONException e) {
                                  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                              }
                          }
                      },
                      new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // display a simple message on the screen
                                Toast.makeText(MainActivity.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
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
    public void btnSearchClickEventHandler() {
        // cancelling all requests about this search if in queue
        //queue.cancelAll(TAG_SEARCH_NAME);

        // first StringRequest: getting items searched
        JsonObjectRequest stringRequest = searchNameStringRequest();
        //stringRequest.setTag(TAG_SEARCH_NAME);

        // executing the request (adding to queue)
        queue.add(stringRequest);
    }

    private void addIngredient() {
        EditText ingredientName = findViewById(R.id.ingredientName);
        if (ingredientName.getText().toString().equals("")) {
            return;
        }
        String ingredientToAdd = ingredientName.getText().toString();
        ingredientList.add(ingredientToAdd);
        ingredientName.setText("");
        updateIngredientList();
    }

    private void updateIngredientList() {
        LinearLayout ingredientListLayout = findViewById(R.id.layoutIngredientsList);
        ingredientListLayout.removeAllViews();
        for (String current : ingredientList) {
            View ingredientChunk = getLayoutInflater().inflate(R.layout.chunk_ingredient,
                    ingredientListLayout, false);
            TextView ingredientName = ingredientChunk.findViewById(R.id.ingredientName);
            ingredientName.setText(current);
            ingredientListLayout.addView(ingredientChunk);
            ingredientChunk.findViewById(R.id.removeIngredient).setOnClickListener(unused -> {
                ingredientList.remove(current);
                updateIngredientList();
            });
        }
    }

    private void checkFilterList() {
        CheckBox balanced = findViewById(R.id.balanced);
        CheckBox highProtein = findViewById(R.id.highProtein);
        CheckBox lowFat = findViewById(R.id.lowFat);
        CheckBox vegan = findViewById(R.id.vegan);
        CheckBox vegetarian = findViewById(R.id.vegetarian);
        CheckBox lowSugar = findViewById(R.id.lowSugar);
        CheckBox peanutFree = findViewById(R.id.peanutFree);
        CheckBox alcoholFree = findViewById(R.id.alcoholFree);
        if(balanced.isChecked()) {
            filterList.add("balanced");
        }
        if(highProtein.isChecked()) {
            filterList.add("diet=high-protein");
        }
        if(lowFat.isChecked()) {
            filterList.add("diet=low-fat");
        }
        if(vegan.isChecked()) {
            filterList.add("diet=vegan");
        }
        if(vegetarian.isChecked()) {
            filterList.add("diet=vegetarian");
        }
        if(lowSugar.isChecked()) {
            filterList.add("diet=low-sugar");
        }
        if (peanutFree.isChecked()) {
            filterList.add("health=peanut-free");
        }
        if (alcoholFree.isChecked()) {
            filterList.add("health=alcohol-free");
        }
    }

    private void findRecipeClicked() {
        startActivity(new Intent(MainActivity.this, Results.class));


    }

}

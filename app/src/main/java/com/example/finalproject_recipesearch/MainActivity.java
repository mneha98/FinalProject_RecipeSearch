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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages activity_main.
 * activity_main allows you to enter a list of ingredients you already have as well as
 * filters you would like to put and allows you to view a list of recipes that include
 * all the ingredients you have entered and fits all the filters checked
 * Leads to activity_results screen.
 */
public class MainActivity extends AppCompatActivity {
    /** creating queue object. Required for API call.*/
    //private RequestQueue queue;

    /** Stores ingredients that the user has.*/
    public static List<String> ingredientList = new ArrayList<>();

    /**Stores names of all the filters picked by user. */
    public static List<String> filterList = new ArrayList<>();

    /** App starts here
     * @param savedInstanceState saves previous state of screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.addIngredient).setOnClickListener(unused -> {
            addIngredient();
        });

        //Sets find recipe button to gone initially since at least one ingredient is required
        Button findRecipe = findViewById(R.id.findRecipe);
        findRecipe.setVisibility(View.GONE);
        findRecipe.setOnClickListener(unused -> {
            checkFilterList();
            findRecipeClicked();
        });


        //Required for API call
        //queue = Volley.newRequestQueue(this);
        //btnSearchClickEventHandler();

    }

    private void addIngredient() {
        Button findRecipe = findViewById(R.id.findRecipe);
        EditText ingredientName = findViewById(R.id.ingredientName);
        if (ingredientName.getText().toString().equals("")) {
            return;
        }
        String ingredientToAdd = ingredientName.getText().toString();
        ingredientList.add(ingredientToAdd);
        findRecipe.setVisibility(View.VISIBLE);
        ingredientName.setText("");
        updateIngredientList();
    }

    private void updateIngredientList() {
        Button findRecipe = findViewById(R.id.findRecipe);
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
                if (ingredientList.isEmpty()) {
                    findRecipe.setVisibility(View.GONE);
                }
                updateIngredientList();
            });
        }
    }

    /**
     * stores names of checked filter checkboxes in a list to use for API call
     */
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
            filterList.add("diet=balanced");
        } else filterList.remove("diet=balanced");
        if(highProtein.isChecked()) {
            filterList.add("diet=high-protein");
        } else filterList.remove("diet=high-protein");
        if(lowFat.isChecked()) {
            filterList.add("diet=low-fat");
        } else filterList.remove("diet=low-fat");
        if(vegan.isChecked()) {
            filterList.add("health=vegan");
        } else filterList.remove("health=vegan");
        if(vegetarian.isChecked()) {
            filterList.add("health=vegetarian");
        } else filterList.remove("health=vegetarian");
        if(lowSugar.isChecked()) {
            filterList.add("diet=low-sugar");
        } else filterList.remove("diet=low-sugar");
        if (peanutFree.isChecked()) {
            filterList.add("health=peanut-free");
        } else filterList.remove("health=peanut-free");
        if (alcoholFree.isChecked()) {
            filterList.add("health=alcohol-free");
        } else filterList.remove("health=alcohol-free");
    }

    private void findRecipeClicked() {
        Intent resultsScreen = new Intent(getApplicationContext(), Results.class);
        startActivity(resultsScreen);
    }

    //Moved to results screen. Left here for debugging if required
    /*private StringRequest searchNameStringRequest() {
        Log.d("Message0.0", "Entered searchNameStringRequest");

        /*final String API = "&api_key=Rj03HpAXEXrZDfrOx9sIU8W2Fk4kWcAlFm4u5Ldd";
        final String NAME_SEARCH = "&q=";
        final String DATA_SOURCE = "&ds=Standard Reference";
        final String FOOD_GROUP = "&fg=";
        final String SORT = "&sort=r";
        final String MAX_ROWS = "&max=25";
        final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://api.nal.usda.gov/ndb/search/?format=json";*/

    //final String url = "https://api.edamam.com/search?q=flour&beans&health=peanut-free&health=tree-nut-free&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";

    // 1st param => type of method (GET/PUT/POST/PATCH/etc)
    // 2nd param => complete url of the API
    // 3rd param => Response.Listener -> Success procedure
    // 4th param => Response.ErrorListener -> Error procedure
        /*return new StringRequest(Request.Method.GET, url,
                      new Response.Listener<String>() {
                          @Override
                          public void onResponse(String response) {
                              Log.d("Message0.1", "Entered onResponse");
                              try {
                                  Log.d("Message0.2", "Entered try catch");
                                  JSONObject result = new JSONObject(response);
                                  int count = result.getInt("count");
                                  //JSONArray hits = new JSONArray();
                                  JSONArray hits = result.getJSONArray("hits");
                                  JSONObject Object0 = hits.getJSONObject(0);
                                  JSONObject recipe = Object0.getJSONObject("recipe");
                                  String recipeName = recipe.getString("label");
                                  //double yield = recipe.getDouble("yield");

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
                      /*new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // display a simple message on the screen
                                Toast.makeText(MainActivity.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                            }
                        });*/
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
    //}
    /*public void btnSearchClickEventHandler() {
        // cancelling all requests about this search if in queue
        //queue.cancelAll(TAG_SEARCH_NAME);

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchNameStringRequest();
        //stringRequest.setTag(TAG_SEARCH_NAME);

        // executing the request (adding to queue)
        queue.add(stringRequest);
    }*/

}

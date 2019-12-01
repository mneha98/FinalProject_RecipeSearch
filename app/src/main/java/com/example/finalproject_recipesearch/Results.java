package com.example.finalproject_recipesearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.security.AccessController.getContext;

/**
 * Class that manages activity_results screen.
 * activity_results shows list fo recipes along with web links and pictures and allows you
 * to click on a recipe to view ingredients you need but don't have.
 * Leads to activity_individual_recipe screen.
 */
public class Results extends AppCompatActivity {

    /**
     * creating queue object. Required for API call.
     */
    private RequestQueue queue;

    /**
     * list to store missing ingredients.
     */
    public static List<RequiredIngredients> recipeButtonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        //Log.d("Message6.0", "Screen loaded with contents");

        //required for API call
        queue = Volley.newRequestQueue(Results.this);
        updateRecipeList();
        /*Intent intent = new Intent(getApplicationContext(), IndividualRecipe.class);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (v.getId() == 1000) {
                    intent.putExtra("Ingredient List index", 0);
                    startActivity(intent);
                }
                if (v.getId() == 1001) {
                    intent.putExtra("Ingredient List index", 1);
                    startActivity(intent);
                }
                if (v.getId() == 1002) {
                    intent.putExtra("Ingredient List index", 2);
                    startActivity(intent);
                }
                if (v.getId() == 1003) {
                    intent.putExtra("Ingredient List index", 3);
                    startActivity(intent);
                }
                if (v.getId() == 1004) {
                    intent.putExtra("Ingredient List index", 4);
                    startActivity(intent);
                }
            }
        };*/
        /*findViewById(R.id.view).setOnClickListener(unused -> {
            startActivity(new Intent(Results.this, Recipe.class));
        });*/
        /*Button button0 = recipeButtonList.get(0).button;
        button0.setOnClickListener(unused -> {
            Intent intent = new Intent(getApplicationContext(), IndividualRecipe.class);
            intent.putStringArrayListExtra("Ingredient List", recipeButtonList.get(1).requiredIngredientList);
            startActivity(intent);
        });
        for (RequiredIngredients current : recipeButtonList) {
            Button currentButton = current.button;
            currentButton.setOnClickListener(unused -> {
                //Bundle extra = new Bundle();
                //extra.putSerializable("objects", current.requiredIngredientList);
                Intent intent = new Intent(getApplicationContext(), IndividualRecipe.class);
                intent.putStringArrayListExtra("Ingredient List", current.requiredIngredientList);
                startActivity(intent);
            });
        }*/

        /*for (int i = 0; i < recipeButtonList.size(); i++) {

        }*/
        /*EditText recipeNo = findViewById(R.id.recipeNo);
        int a = parseInt(recipeNo.getText().toString());
        intent.putExtra("Ingredient List index", a);

        Button view = findViewById(R.id.view);
        view.setOnClickListener(unused -> {
            startActivity(intent);
        });*/

    }

    /**
     * calls function to make API call and adds to queue
     */
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

    /**
     * Function that makes the API call
     *
     * @return StringRequest object to be added to queue
     */
    private StringRequest searchNameStringRequest() {
        //Log.d("Message0.0", "Entered searchNameStringRequest");

        //API key and ID obtained from website
        final String API = "&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";
        //String to store all ingredient queries required by user
        StringBuilder ingredientSearch = new StringBuilder("salt");
        //String to store all filter queries required by user
        StringBuilder filterSearch = new StringBuilder();
        //Limits no of recipes returned by API call
        final String MAX_ROWS = "&from=0&to=5";
        final String URL_PREFIX = "https://api.edamam.com/search?q=";

        //Picks up all user inputted ingredients and appends concerned string
        for (String current : MainActivity.ingredientList) {
            ingredientSearch.append("+" + current);
        }
        //Picks up all user inputted filters and appends concerned string
        for (String current : MainActivity.filterList) {
            filterSearch.append("&" + current);
        }
        //Final url to which API call will be made
        String url = URL_PREFIX + ingredientSearch + filterSearch + API + MAX_ROWS;
        //Log.d("Message0.3","GeneratedURL: " + url);

        //Test url that definitely works. Left for debugging if needed
        // String url = "https://api.edamam.com/search?q=flour&beans&health=peanut-free&health=tree-nut-free&app_id=a48c16e0&app_key=ac643dd19616db4a4bd55efe28e568fb";


        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Message0.1", "Entered onResponse");
                        try {
                            //Log.d("Message0.2", "Entered try catch");
                            JSONObject result = new JSONObject(response);
                            //Log.d("Message1", "It has probably gone through");
                            int count = result.getInt("count");
                            //Log.d("Message2", "The number of recipes is: " + count);
                            JSONArray hits = result.getJSONArray("hits");
                            LinearLayout recipeListLayout = findViewById(R.id.layoutRecipeList);
                            Intent intent = new Intent(getApplicationContext(), IndividualRecipe.class);
                            recipeListLayout.removeAllViews();
                            //inflating and adding chunks for each recipe
                            for (int i = 0; i < hits.length(); i++) {
                                View recipeChunk = getLayoutInflater().inflate(R.layout.chunk_recipe,
                                        recipeListLayout, false);
                                JSONObject currentObject = hits.getJSONObject(i);
                                JSONObject recipe = currentObject.getJSONObject("recipe");
                                String currentRecipeName = recipe.getString("label");
                                String currentRecipeLink = recipe.getString("url");
                                String currentRecipeImage = recipe.getString("image");

                                //Setting recipe name as button
                                Button recipeNameButton = recipeChunk.findViewById(R.id.recipeName);
                                recipeNameButton.setText(currentRecipeName);
                                recipeNameButton.setId(1000 + i);
                                final int index = i;
                                recipeNameButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Log.i("TAG", "The index is" + index);
                                        intent.putExtra("Ingredient List index", index);
                                        startActivity(intent);
                                    }
                                });

                                //Setting recipe link as text view
                                TextView recipeLink = recipeChunk.findViewById(R.id.recipeLink);
                                recipeLink.setText(currentRecipeLink);

                                //Setting image
                                ImageView recipeImage = recipeChunk.findViewById(R.id.recipeImage);
                                Picasso.get().load(currentRecipeImage).fit().into(recipeImage);
                                recipeListLayout.addView(recipeChunk);

                                //Extracting required ingredients list.
                                ArrayList<String> ingredientsList = new ArrayList<>();
                                JSONArray ingredientLines = recipe.getJSONArray("ingredients");
                                for (int j = 0; j < ingredientLines.length(); j++) {
                                    JSONObject current = ingredientLines.getJSONObject(j);
                                    String currentItem = current.getString("text");
                                    ingredientsList.add(currentItem);
                                }
                                RequiredIngredients toAdd = new RequiredIngredients(recipeNameButton, ingredientsList);
                                recipeButtonList.add(toAdd);
                            }
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
    }
}

     //Basic code to make an API call. Left for debugging if needed
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

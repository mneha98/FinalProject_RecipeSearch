package com.example.finalproject_recipesearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Class that manages activity_individual_recipe screen.
 * activity_individual_recipe screen displays the list of missing ingredients for the
 * selected recipe by finding the difference of
 * the list of ingredients received from API call (required)
 * to list of ingredients given by user.
 * activity_individual_recipe is the last screen of the app.
 */
public class IndividualRecipe extends AppCompatActivity {

    /** List of missing ingredients. */
    private ArrayList<String> missingIngredientList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_recipe);
        Intent intent = getIntent();
        int index = intent.getIntExtra("Ingredient List index", 0);
        ArrayList<String> toPass = new ArrayList<>(Results.recipeButtonList.get(index).requiredIngredientList);
        compareList(toPass);
    }

    /**
     * Compares the two lists and populates the missing ingredient list
     * @param requiredIngredientList received from API call (in Results class)
     */
    private void compareList(ArrayList<String> requiredIngredientList) {
        for (String currentr : requiredIngredientList) {
            boolean isThere = true;
            for (String currenti : MainActivity.ingredientList) {
                if (currentr.toLowerCase().contains(currenti.toLowerCase())) {
                    break;
                } else {
                    isThere = false;
                }
            }
            if (!(isThere)) {
                missingIngredientList.add(currentr);
            }
        }
        showMissing();
    }

    /**
     * inflates and adds chunk to screen for each missing ingredient
     */
    private void showMissing() {
        LinearLayout missingListLayout = findViewById(R.id.layoutMissingIngredientsList);
        missingListLayout.removeAllViews();
        for (String current : missingIngredientList) {
            View missingChunk = getLayoutInflater().inflate(R.layout.chunk_missing_ingredient_name,
                    missingListLayout, false);
            //Setting missing ingredient name as text view
            TextView missingName = missingChunk.findViewById(R.id.missingIngredient);
            missingName.setText(current);
            missingListLayout.addView(missingChunk);
        }
    }
}

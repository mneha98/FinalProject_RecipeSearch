package com.example.finalproject_recipesearch;

import android.widget.Button;
import java.util.ArrayList;


/**
 * Helper class.
 */
public class RequiredIngredients {
    /** Button that decides which recipe it is. */
    public Button button;

    /**List of all ingredients required for this recipe. */
    public ArrayList<String> requiredIngredientList = new ArrayList<>();

    RequiredIngredients(Button b, ArrayList<String> l) {
        button = b;
        requiredIngredientList = l;
    }
}

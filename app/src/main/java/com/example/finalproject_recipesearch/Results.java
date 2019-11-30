package com.example.finalproject_recipesearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Results extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        /*findViewById(R.id.view).setOnClickListener(unused -> {
            startActivity(new Intent(Results.this, Recipe.class));
        });*/

    }

    private void updateRecipeList() {
    }
}

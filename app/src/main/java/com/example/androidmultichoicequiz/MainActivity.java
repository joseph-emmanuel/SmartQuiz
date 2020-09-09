package com.example.androidmultichoicequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.androidmultichoicequiz.Adapter.CategoryAdapter;
import com.example.androidmultichoicequiz.Common.SpaceDecoration;
import com.example.androidmultichoicequiz.DBHelper.DBHelper;
import com.example.androidmultichoicequiz.Model.Category;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycler_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Quiz");
        setSupportActionBar(toolbar);

        recycler_category=(RecyclerView)findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(this,2));

        //getting Screen size


        CategoryAdapter adapter=new CategoryAdapter(MainActivity.this, DBHelper.getInstance(this).getAllCategories());
        int spaceInPixel=4;
        recycler_category.addItemDecoration((new SpaceDecoration(spaceInPixel)));
        recycler_category.setAdapter(adapter );

    }
}
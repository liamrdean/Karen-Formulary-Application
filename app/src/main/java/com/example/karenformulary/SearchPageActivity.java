package com.example.karenformulary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater; 
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView; 
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchPageActivity extends AppCompatActivity {

	// List View object 
	ListView listView; 

	// Define array adapter for ListView 
	ArrayAdapter<String> adapter; 

	// Define array List for List View data 
	ArrayList<String> mylist;

	SearchHelper searchHelper = new SearchHelper();

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_search_page);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// initialise ListView with id 
		listView = findViewById(R.id.listView); 

		// Add items to Array List 
        //SearchHelper searchHelper = new SearchHelper();
		searchHelper.buildFromFile();

		// Quick fix
		/*
		String[] javawhy = new String[]{"jgh"};
		searchHelper.dictionary = null;
		searchHelper.dictionary = javawhy;
		*/
		listView.setOnItemClickListener(messageClickedHandler);


		if (searchHelper.dictionary == null) {
			searchHelper.dictionary = new String[0];
			Log.i("search", "Dictionary is null");
		}
		else {
			Log.i("search", "Dictionary is not null" + Arrays.toString(searchHelper.dictionary));
		}

        mylist = new ArrayList<String>(Arrays.asList(searchHelper.dictionary));



		// Set adapter to ListView 
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist); 
		Log.i("search", "adapter is null ?= " + Boolean.toString(adapter == null));

		listView.setAdapter(adapter); 
		//listView.setOnItemClickListener(messageClickedHandler);
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			// set up drug page here
			Log.i("Click", "Click!");
			ActivityDrugInfoPage.setDrugName((String) parent.getItemAtPosition(position));
			Intent intent = new Intent(ActivityMain.activityMain, ActivityDrugInfoPage.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate menu with items using MenuInflator 
		MenuInflater inflater = getMenuInflater(); 
		// FIXED THIS
		inflater.inflate(R.menu.menu, menu);

		// Initialise menu item search bar 
		// with id and take its object 
		MenuItem searchViewItem = menu.findItem(R.id.search_bar); 
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

		// attach setOnQueryTextListener 
		// to search view defined above 
		searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() { 
			// Override onQueryTextSubmit method which is call when submit query is searched 
			@Override
			public boolean onQueryTextSubmit(String query) { 
				if (searchHelper.search(query)) {
					adapter.getFilter().filter(query); 
				} else { 
					adapter.clear();
					adapter.addAll(searchHelper.autofill(query));
				} 
				return false; 
			} 

			@Override
			public boolean onQueryTextChange(String newText) { 
				adapter.getFilter().filter(newText); 
				return false; 
			} 
		}); 
		return super.onCreateOptionsMenu(menu); 
	} 
}

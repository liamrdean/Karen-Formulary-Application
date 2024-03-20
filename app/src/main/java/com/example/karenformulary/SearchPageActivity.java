   import android.os.Bundle; 
import android.support.v4.view.MenuItemCompat; 
import android.support.v7.app.AppCompatActivity; 
import android.view.Menu; 
import android.view.MenuInflater; 
import android.view.MenuItem; 
import android.widget.ArrayAdapter; 
import android.widget.ListView; 
import android.widget.SearchView; 
import android.widget.Toast; 
import java.util.ArrayList; 

public class SearchPageActivity extends AppCompatActivity { 

	// List View object 
	ListView listView; 

	// Define array adapter for ListView 
	ArrayAdapter<String> adapter; 

	// Define array List for List View data 
	ArrayList<String> mylist; 

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_search_page); 

		// initialise ListView with id 
		listView = findViewById(R.id.listView); 

		// Add items to Array List 
        SearchHelper searchHelper = new SearchHelper();
		searchHelper.buildFromFile();
        
        mylist = new ArrayList<String>(searchHelper.dictionary);
	
		// Set adapter to ListView 
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist); 
		
		listView.setAdapter(adapter); 
		listView.setOnItemClickListener(messageClickedHandler);
	} 

	private OnItemClickListener messageClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			// set up drug page here
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate menu with items using MenuInflator 
		MenuInflater inflater = getMenuInflater(); 
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
				if (searchHelper.search(query))) { 
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

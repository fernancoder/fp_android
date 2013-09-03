package com.example.fp_android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.view.Menu;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class FinderActivity extends ListActivity {
	
	EditText editTextFind;
	ArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finder);
		
		editTextFind = (EditText) findViewById(R.id.editTextFind);
	    editTextFind.addTextChangedListener(new TextWatcher()
	    {
	      @Override
	      public void afterTextChanged(Editable s) {}

	      @Override
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	      @Override
	      public void onTextChanged(CharSequence s, int start, int before, int count) {
	       	String terms = editTextFind.getText().toString();
	       	if ( terms.compareTo("") == 0 )
	          FinderActivity.this.clearList();
	       	else
	       	  query(terms);			
	      }
	    });

	    final ListView listTerms = (ListView) findViewById(android.R.id.list);
	}

	public void query(String terms)
	{
	    QueryTask task = new QueryTask();
	    task.execute(new String[] {terms});
	}
	
	private class QueryTask extends AsyncTask<String, Void, String> {
	    
		@Override
		protected String doInBackground(String... params) {
			return ApiClient.query(params[0]);
		}

	    @Override
	    protected void onPostExecute(String result) {
	        	
	      boolean okResponse = true;
	      try {
	        JSONObject jObjectRoot = new JSONObject(result);
		String status = jObjectRoot.getString("status");
		if ( status.compareTo("error") == 0 )
		  okResponse = false;
	      } catch (JSONException e) {
	        okResponse = false;
	      }
	        	
	      if ( okResponse )
	      {
	        try {
		  JSONObject jObjectRoot = new JSONObject(result);
		  JSONObject jObjectResult = jObjectRoot.getJSONObject("response");
		  long totalResults = jObjectResult.getLong("totalResults");					
		  if ( totalResults != 0 )
		  {						
		    JSONArray jArrayRecords = jObjectResult.getJSONArray("records");
		    int arrayLength = jArrayRecords.length();
		    String [] titles;
		    titles = new String[arrayLength+1];
		    for ( int i = 0; i < arrayLength; i++)
		    {
		      JSONObject jObjectRecord = jArrayRecords.getJSONObject(i);
		      titles[i] = jObjectRecord.getString("title");							
		      //Log.i(TAG, "Titulo: " + jObjectRecord.getString("title"));
		    }
							
		    if ( totalResults > arrayLength)
		    {
		      titles[arrayLength] = "...y " + String.valueOf(totalResults-arrayLength) + " más.";
		    }
		    else
		    {
		      titles[arrayLength] = "...no hay mas resultados.";
		    }
		    ListView listView = (ListView) findViewById(android.R.id.list);
		    adapter = new ArrayAdapter(FinderActivity.this,
		                                       android.R.layout.simple_list_item_1, titles);
		    listView.setAdapter(adapter);
		  }
		  else
		    FinderActivity.this.clearList();

	        } catch (JSONException e) {
		  okResponse = false;
		}
	      }
	      if ( !okResponse )
	      {
	        AlertDialog.Builder builder = new AlertDialog.Builder(FinderActivity.this);
	        builder.setMessage(R.string.query_error_message)
	                          .setTitle(R.string.query_error_caption);
	        builder.setPositiveButton(R.string.query_error_ok, null);
	        		
	        AlertDialog dialog = builder.create();
	        dialog.show();
	      }
	    }
	  } 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_finder, menu);
		return true;
	}
	
  public void clearList()
  {
    String [] titles;		
	titles = new String[0];
	ListView listView = (ListView) findViewById(android.R.id.list);
	adapter = new ArrayAdapter(FinderActivity.this,
	android.R.layout.simple_list_item_1, titles);
	listView.setAdapter(adapter);
  }

}

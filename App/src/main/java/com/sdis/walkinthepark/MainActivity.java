package com.sdis.walkinthepark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set the contents of the menu list */
        ArrayList<String> listItems = new ArrayList<String>();
        listItems.add("Activity Feed");
        listItems.add("Record");
        listItems.add("Plan");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        ListView mainListView = (ListView) findViewById(R.id.listView);
        mainListView.setAdapter(adapter);

        /* Handle the clicks of the menu list buttons */
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Context c = getApplicationContext();
                CharSequence text;
                Intent intent = new Intent();

                switch (position) {
                    case 0:
                        intent = new Intent(c, FeedActivity.class);
                        break;
                    case 1:
                        intent = new Intent(c, RecordActivity.class);
                        break;
                    case 2:
                        intent = new Intent(c, PlanActivity.class);
                        break;
                }

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

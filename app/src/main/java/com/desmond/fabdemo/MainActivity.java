package com.desmond.fabdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.pink_customed_icon_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();
            }
        });

        createFloatingActionMenu();
    }

    private void createFloatingActionMenu() {
        FloatingActionsMenu fam  = (FloatingActionsMenu) findViewById(R.id.green_expand_down_fam);

        final FloatingActionButton actionE = new FloatingActionButton(this);
        actionE.setTitle("action button");
        actionE.setVisibility(View.GONE);

        FloatingActionButton actionD = new FloatingActionButton(this);
        actionD.setTitle("Hide/Show Action below");
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionE.setVisibility(actionE.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        fam.addButton(actionE);
        fam.addButton(actionD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

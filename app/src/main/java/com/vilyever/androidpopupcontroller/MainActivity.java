package com.vilyever.androidpopupcontroller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vilyever.popupcontroller.PopupController;
import com.vilyever.popupcontroller.PopupDirection;

public class MainActivity extends AppCompatActivity {
    final MainActivity self = this;
    
    private PopupController popupController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                self.popupController.popupFromView(self.findViewById(R.id.titleLabel), PopupDirection.Up, true);
            }
        });

        self.popupController = new PopupController(self, R.layout.test_view);
        self.popupController.setPopupBackgroundColor(Color.BLUE);
        self.popupController.setEdgePadding(50, 50, 50, 50);
        self.popupController.setEdgeRoundedRadius(20);
        self.popupController.setPopupShadowRadius(30);
        self.popupController.setDirectionArrowHeight(50);

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

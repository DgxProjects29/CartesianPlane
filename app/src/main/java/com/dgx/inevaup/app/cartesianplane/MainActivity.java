package com.dgx.inevaup.app.cartesianplane;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CartesianView cartesianView;
    CartesianLogic cl = new CartesianLogic();
    Fraccion nu1,nu2,nu3,nu4,nu5,nu6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nu1 = new Fraccion(2);
        nu2 = new Fraccion(1);
        nu3 = new Fraccion(10);
        nu4 = new Fraccion(3);
        nu5 = new Fraccion(1);
        nu6 = new Fraccion(-50);

        cartesianView = findViewById(R.id.CanvasView);
        cartesianView.setBackgroundColor(Color.WHITE);//Color.rgb(245,244,244)

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // Log.d("TAGSIZE","W:  "+width+"   H:  "+height+"  ---  "+cl.isparallel(nu1,nu2,nu3,nu4,nu5,nu6));

        cartesianView.setHeightAndWidth(width,height);
        cartesianView.setEquation(nu1,nu2,nu3,nu4,nu5,nu6);

        findViewById(R.id.centerbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartesianView.centerPlane();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.opengraphinfo) {
            Rect displayRectangle = new Rect();
            Window window = this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            LayoutInflater inflater = LayoutInflater.from(this);
            View visor = inflater.inflate(R.layout.graphinfo, null);
            visor.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
            final AlertDialog.Builder nude1 = new AlertDialog.Builder(this);

            TextView Eq1_tex = visor.findViewById(R.id.Eq1_cart);
            TextView Eq2_tex = visor.findViewById(R.id.Eq2_cart);
            TextView Intercept = visor.findViewById(R.id.intersection_cart);

            nude1.setView(visor);
            final AlertDialog alert2 = nude1.create();
            alert2.show();
        }
        return super.onOptionsItemSelected(item);
    }
}

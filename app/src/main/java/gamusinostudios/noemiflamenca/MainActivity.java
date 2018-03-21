package gamusinostudios.noemiflamenca;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //Inicializamos fragmentManager aqui para poder cargar el fragment01 al iniciar la aplicacion
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Publicidad!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        //Publicidad!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        FloatingActionButton fab = findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acción del boton compartir
                CompartirAPP();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        //abrimos la aplicación mostrando el fragment01
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment01()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Crea contenedor
        ConstraintLayout contenedor = (ConstraintLayout) findViewById(R.id.contenedor);

        if (id == R.id.nav_inicio) {
            contenedor.removeAllViews();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment01()).commit();
        } else if (id == R.id.nav_vestidos) {
            contenedor.removeAllViews();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_vestidos()).commit();
            //listaVestidos();
        } else if (id == R.id.nav_faldas) {
            contenedor.removeAllViews();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_faldas()).commit();
        } else if (id == R.id.nav_accesorios) {
            contenedor.removeAllViews();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_accesorios()).commit();
        } else if (id == R.id.nav_contacto) {
            contenedor.removeAllViews();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment04()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void listaVestidos(){

        //Crea contenedor
        ConstraintLayout contenedor = (ConstraintLayout) findViewById(R.id.contenedor);
        //Crea TextView
        final TextView miTextView = new TextView(getApplicationContext());
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/scripts/galeria.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Respuesta correcta
                //miTextView.setText("Resultado: " + response);
                String string = response;
                String[] parts = string.split(",");
                String part1 = parts[0]; // 123
                String part2 = parts[1]; // 654321
                miTextView.setText(part2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Respuesta incorrecta
                miTextView.setText("La base de datos tardó mucho en responder...");
            }
        });
        queue.add(stringRequest);

        //Agrega propiedades al TextView.
        miTextView.setTextColor(Color.BLUE);

        //Agrega vistas al contenedor.
        contenedor.addView(miTextView);
    }

    public void CompartirAPP() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "La millor APP del mercat! (Aquí posarem el link de google play");
        try {
            startActivity(Intent.createChooser(intent, "Compartir APP"));
        } catch (android.content.ActivityNotFoundException ex) {
            //do something else
        }
    }
}


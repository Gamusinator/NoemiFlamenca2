package gamusinostudios.noemiflamenca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_vestidos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_vestidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_vestidos extends Fragment {

    Button btnSiguienteVestido, btnAnteriorVestido;
    ImageView Vestidos_View;
    TextView idVestido, colorVestido;
    String[] nombresArchivos;
    Vestido[] cadenaVestidos;
    int i = 0;
    int total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vestidos, container, false);

        cargarArray(v);

        btnAnteriorVestido = v.findViewById(R.id.botonAnteriorVestidos);
        btnSiguienteVestido = v.findViewById(R.id.botonSiguienteVestidos);
        Vestidos_View = v.findViewById(R.id.imageViewPrincipal);
        idVestido = v.findViewById(R.id.textViewIdVestidosResultado);
        colorVestido = v.findViewById(R.id.textViewColorVestidosResultado);

        btnAnteriorVestido.setOnClickListener(listener);
        btnSiguienteVestido.setOnClickListener(listener);

        // Inflate the layout for this fragment
        return v;
    }

    //Quan fem clic en un botó, siguiente o anterior...
    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            total = cadenaVestidos.length;
            int id = view.getId();
            if(id == R.id.botonSiguiente){
                i++;
                if(i == total) i = 0;
            }
            if(id == R.id.botonAnterior){
                i--;
                if(i == -1) i = total-1;
            }
            //carreguem la imatge a l'ImageView
            //String urlfoto = path + nombresArchivos[i];
            //Picasso.with(view.getContext()).load(urlfoto).into(Vestidos_View);
        }
    };

    public void cargarArray(View v){
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String URL = "http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/scripts/bajarVestidos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String string = response;
                // TODO: 31/01/2018 S'han de tractar les dades que ens arriben del fitxer php
                nombresArchivos = string.split(","); //Aquí tenemos la array cargada con los nombres de fichero
                colorVestido.setText(nombresArchivos[2]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Respuesta incorrecta
            }
        });
        queue.add(stringRequest);
    }
}

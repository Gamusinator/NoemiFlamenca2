package gamusinostudios.noemiflamenca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
 * {@link Fragment01.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment01#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment01 extends Fragment {

    Button btnSiguiente, btnAnterior;
    ImageView galeria;
    String path = "http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/imagenes/galeria/";
    String[] nombresArchivos;
    int i = 0;
    int total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment01, container, false);

        cargarArray(v);

        btnAnterior = v.findViewById(R.id.botonAnterior);
        btnSiguiente = v.findViewById(R.id.botonSiguiente);
        galeria = v.findViewById(R.id.imageViewPrincipal);

        btnAnterior.setOnClickListener(listener);
        btnSiguiente.setOnClickListener(listener);

        Picasso.with(v.getContext()).load(path+"/1.jpg").into(galeria);
        // Inflate the layout for this fragment
        return v;
    }
    //Quan fem clic en un botó, siguiente o anterior...
    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            total = nombresArchivos.length;
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
            String urlfoto = path + nombresArchivos[i];
            Picasso.with(view.getContext()).load(urlfoto).into(galeria);
        }
    };

    public void cargarArray(View v){
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String URL = "http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/scripts/galeria.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String string = response;
                nombresArchivos = string.split(","); //Aquí tenemos la array cargada con los nombres de fichero
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

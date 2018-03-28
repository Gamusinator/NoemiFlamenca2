package gamusinostudios.noemiflamenca;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_vestidos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_vestidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_accesorios extends Fragment {

    PhotoViewAttacher mAttacher;
    private View v;
    boolean isImageFitToScreen;
    LinearLayout accesorios, infoA1, infoA2;
    ActionBar barra;
    AdView publi;
    FloatingActionButton fab;

    private Button btnSiguienteAccesorio, btnAnteriorAccesorio, modoFoto;
    private PhotoView Accesorios_View;
    private TextView idAccesorio, colorAccesorio, descripcionAccesorio, precioAccesorio;
    private Accesorio accesorio;
    private List<Accesorio> listaAccesorios;
    private int posicionA = 0, total;
    private String path = "http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/imagenes/accesorios/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_accesorios, container, false);

        listaAccesorios =new ArrayList<Accesorio>();

        btnAnteriorAccesorio = v.findViewById(R.id.botonAnteriorAccesorios);
        btnSiguienteAccesorio = v.findViewById(R.id.botonSiguienteAccesorio);
        modoFoto = v.findViewById(R.id.modoFotoAccesorios);
        Accesorios_View = v.findViewById(R.id.imageViewAccesorios);
        colorAccesorio = v.findViewById(R.id.textViewColorAccesoriosResultado);
        descripcionAccesorio = v.findViewById(R.id.textViewDescripcionAccesorios);
        precioAccesorio = v.findViewById(R.id.textViewPrecioAccesoriosResultado);

        barra = ((AppCompatActivity)getActivity()).getSupportActionBar();
        publi = getActivity().findViewById(R.id.adView);
        fab = getActivity().findViewById(R.id.share);
        accesorios = v.findViewById(R.id.accesorios);
        infoA1 = v.findViewById(R.id.infoA1);
        infoA2 = v.findViewById(R.id.infoA2);

        btnSiguienteAccesorio.setOnClickListener(listener);
        btnAnteriorAccesorio.setOnClickListener(listener);

        new Mostrar().execute();

        modoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    barra.show();
                    fab.setVisibility(View.VISIBLE);
                    publi.setVisibility(View.VISIBLE);
                    infoA1.setVisibility(View.VISIBLE);
                    infoA2.setVisibility(View.VISIBLE);
                    accesorios.setPadding(16,0,16,50);

                }else{
                    isImageFitToScreen=true;
                    barra.hide();
                    fab.setVisibility(View.GONE);
                    publi.setVisibility(View.GONE);
                    infoA1.setVisibility(View.GONE);
                    infoA2.setVisibility(View.GONE);
                    accesorios.setPadding(0,16,0,16);
                }
            }
        });
        mAttacher = new PhotoViewAttacher(Accesorios_View);
        // Inflate the layout for this fragment
        return v;
    }

    //Decidir què fer quan s'apreten els butons anterior i siguiente
    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            total = listaAccesorios.size();
            int id = view.getId();
            if (id == R.id.botonSiguienteAccesorio) {
                posicionA++;
                if (posicionA == total) posicionA = 0;
            }
            if (id == R.id.botonAnteriorAccesorios) {
                posicionA--;
                if (posicionA == -1) posicionA = total - 1;
            }
            mostrarAccesorio(posicionA);
        }
    };

    private String mostrar(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://35.177.198.220/noemiFlamenca/scripts/bajarAccesorios.php");
        httppost.setHeader("Authorization", "Basic "+ Base64.encodeToString("scudgamu:2on2esdepros".getBytes(),Base64.URL_SAFE|Base64.NO_WRAP));
        String resultado="";
        HttpResponse response;
        try{
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            resultado=convertStreamToString(instream);
        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return resultado;
    }

    private String convertStreamToString(InputStream is) throws IOException{
        if (is !=null){
            StringBuilder sb = new StringBuilder();
            String line;
            try{
                BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                }
            }
            finally{
                is.close();
            }
            return sb.toString();
        }else{
            return "";
        }
    }

    private boolean filtrarDatos(){
        listaAccesorios.clear();
        String data=mostrar();
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try{
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("accesorio");
                for (int i = 0; i < jsonArray.length();i++){
                    accesorio=new Accesorio();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    accesorio.setId(jsonArrayChild.optString("id_accesorio"));
                    accesorio.setColor(jsonArrayChild.optString("color_accesorio"));
                    accesorio.setDescripcion(jsonArrayChild.optString("descripcion_accesorio"));
                    accesorio.setUrl(jsonArrayChild.optString("url_accesorio"));
                    accesorio.setPrecio(jsonArrayChild.optString("precio_accesorio"));
                    listaAccesorios.add(accesorio);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void mostrarAccesorio(final int posicion){
        getActivity().runOnUiThread (new Runnable(){
            @Override
            public void run() {
                if(!listaAccesorios.isEmpty()) {
                    Accesorio accesorios= listaAccesorios.get(posicion);
                    colorAccesorio.setText(accesorios.getColor());
                    descripcionAccesorio.setText(accesorios.getDescripcion());
                    String urlfoto = path + accesorios.getUrl();
                    Picasso.with(v.getContext()).load(urlfoto).into(Accesorios_View);
                    mAttacher.update();
                    if (accesorio.getPrecio().isEmpty()){
                        precioAccesorio.setText("---");
                    }else {
                        precioAccesorio.setText(accesorios.getPrecio() + "€");
                    }
                }else{
                    Toast.makeText(getContext(), "Ups! Actualmente no hay accesorios disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class Mostrar extends AsyncTask<String,String,String> {
        protected String doInBackground(String... params){
            if(filtrarDatos()) mostrarAccesorio(posicionA);
            return null;
        }
    }
}




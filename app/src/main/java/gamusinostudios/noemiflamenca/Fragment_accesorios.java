package gamusinostudios.noemiflamenca;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private View v;

    boolean isImageFitToScreen;
    LinearLayout accesorios, infoA1, infoA2;
    TextView titol;
    ActionBar barra;
    AdView publi;
    FloatingActionButton fab;

    private Button btnSiguienteAccesorio, btnAnteriorAccesorio;
    private ImageView Accesorios_View;
    private TextView idAccesorio, colorAccesorio, descripcionAccesorio;
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
        Accesorios_View = v.findViewById(R.id.imageViewAccesorios);
        idAccesorio = v.findViewById(R.id.textViewIdAccesoriosResultado);
        colorAccesorio = v.findViewById(R.id.textViewColorAccesoriosResultado);
        descripcionAccesorio = v.findViewById(R.id.textViewDescripcionAccesorios);

        titol = v.findViewById(R.id.textViewAccesorios);
        barra = ((AppCompatActivity)getActivity()).getSupportActionBar();
        publi = getActivity().findViewById(R.id.adView);
        fab = getActivity().findViewById(R.id.share);
        accesorios = v.findViewById(R.id.accesorios);
        infoA1 = v.findViewById(R.id.infoA1);
        infoA2 = v.findViewById(R.id.infoA2);

        btnSiguienteAccesorio.setOnClickListener(listener);
        btnAnteriorAccesorio.setOnClickListener(listener);

        new Mostrar().execute();

        Accesorios_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    //butons.setVisibility(View.VISIBLE);
                    titol.setVisibility(View.VISIBLE);
                    barra.show();
                    fab.setVisibility(View.VISIBLE);
                    publi.setVisibility(View.VISIBLE);
                    infoA1.setVisibility(View.VISIBLE);
                    infoA2.setVisibility(View.VISIBLE);
                    //galeria.setScaleType(ImageView.ScaleType.CENTER);
                    accesorios.setPadding(16,0,16,50);

                }else{
                    isImageFitToScreen=true;
                    //butons.setVisibility(View.GONE);
                    titol.setVisibility(View.GONE);
                    barra.hide();
                    fab.setVisibility(View.GONE);
                    publi.setVisibility(View.GONE);
                    infoA1.setVisibility(View.GONE);
                    infoA2.setVisibility(View.GONE);
                    //galeria.setScaleType(ImageView.ScaleType.CENTER);
                    accesorios.setPadding(0,16,0,16);
                }
            }
        });
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
        HttpPost httppost = new HttpPost("http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/scripts/bajarAccesorios.php");
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
                    idAccesorio.setText(accesorios.getId());
                    colorAccesorio.setText(accesorios.getColor());
                    descripcionAccesorio.setText(accesorios.getDescripcion());
                    String urlfoto = path + accesorios.getUrl();
                    Picasso.with(v.getContext()).load(urlfoto).into(Accesorios_View);
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




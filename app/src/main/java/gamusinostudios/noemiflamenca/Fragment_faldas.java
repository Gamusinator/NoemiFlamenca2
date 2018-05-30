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
public class Fragment_faldas extends Fragment {

    PhotoViewAttacher mAttacher;
    private View v;
    boolean isImageFitToScreen;
    LinearLayout faldas, infoF1, infoF2;
    ActionBar barra;
    AdView publi;
    FloatingActionButton fab;

    private Button btnSiguienteFalda, btnAnteriorFalda, modoFoto;
    private PhotoView Faldas_View;
    private TextView idFalda, colorFalda, descripcionFalda, precioFalda;
    private Falda falda;
    private List<Falda> listaFaldas;
    private int posicionF = 0, total;
    private String path = "http://ec2-35-177-65-76.eu-west-2.compute.amazonaws.com/noemiFlamenca/imagenes/faldas/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_faldas, container, false);

        listaFaldas =new ArrayList<>();

        btnAnteriorFalda = v.findViewById(R.id.botonAnteriorFaldas);
        btnSiguienteFalda = v.findViewById(R.id.botonSiguienteFalda);
        modoFoto = v.findViewById(R.id.modoFotoFaldas);
        Faldas_View = v.findViewById(R.id.imageViewFaldas);
        colorFalda = v.findViewById(R.id.textViewColorFaldasResultado);
        descripcionFalda = v.findViewById(R.id.textViewDescripcionFaldas);
        precioFalda = v.findViewById(R.id.textViewPrecioFaldasResultado);

        barra = ((AppCompatActivity)getActivity()).getSupportActionBar();
        publi = getActivity().findViewById(R.id.adView);
        fab = getActivity().findViewById(R.id.share);
        faldas = v.findViewById(R.id.faldas);
        infoF1 = v.findViewById(R.id.infoF1);
        infoF2 = v.findViewById(R.id.infoF2);

        btnSiguienteFalda.setOnClickListener(listener);
        btnAnteriorFalda.setOnClickListener(listener);

        new Mostrar().execute();

        modoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    modoFoto.setText(R.string.modo_foto);
                    isImageFitToScreen=false;
                    barra.show();
                    fab.setVisibility(View.VISIBLE);
                    publi.setVisibility(View.VISIBLE);
                    infoF1.setVisibility(View.VISIBLE);
                    infoF2.setVisibility(View.VISIBLE);
                    faldas.setPadding(16,0,16,50);

                }else{
                    modoFoto.setText(R.string.volver_modo_foto);
                    isImageFitToScreen=true;
                    barra.hide();
                    fab.setVisibility(View.GONE);
                    publi.setVisibility(View.GONE);
                    infoF1.setVisibility(View.GONE);
                    infoF2.setVisibility(View.GONE);
                    faldas.setPadding(0,16,0,16);
                }
            }
        });
        mAttacher = new PhotoViewAttacher(Faldas_View);
        // Inflate the layout for this fragment
        return v;
    }

    //Decidir què fer quan s'apreten els butons anterior i siguiente
    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
             total = listaFaldas.size();
             int id = view.getId();
             if (id == R.id.botonSiguienteFalda) {
                 posicionF++;
                 if (posicionF == total) posicionF = 0;
             }
             if (id == R.id.botonAnteriorFaldas) {
                 posicionF--;
                 if (posicionF == -1) posicionF = total - 1;
             }
             mostrarFalda(posicionF);
             mAttacher.update();
        }
    };

    private String mostrar(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://35.177.65.76/noemiFlamenca/scripts/bajarFaldas.php");
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
        listaFaldas.clear();
        String data=mostrar();
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try{
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("falda");
                for (int i = 0; i < jsonArray.length();i++){
                    falda=new Falda();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    falda.setId(jsonArrayChild.optString("id_falda"));
                    falda.setColor(jsonArrayChild.optString("color_falda"));
                    falda.setDescripcion(jsonArrayChild.optString("descripcion_falda"));
                    falda.setUrl(jsonArrayChild.optString("url_falda"));
                    falda.setPrecio(jsonArrayChild.optString("precio_falda"));
                    listaFaldas.add(falda);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void mostrarFalda(final int posicion){
        getActivity().runOnUiThread (new Runnable(){
            @Override
            public void run() {
                if(!listaFaldas.isEmpty()) {
                    Falda faldas= listaFaldas.get(posicion);
                    colorFalda.setText(faldas.getColor());
                    descripcionFalda.setText(faldas.getDescripcion());
                    String urlfoto = path + faldas.getUrl();
                    Picasso.with(v.getContext()).load(urlfoto).into(Faldas_View);
                    if (faldas.getPrecio().isEmpty()){
                        precioFalda.setText("---");
                    }else {
                        precioFalda.setText(faldas.getPrecio() + "€");
                    }
                }else{
                    Toast.makeText(getContext(), "Ups! Actualmente no hay faldas disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class Mostrar extends AsyncTask<String,String,String> {
        protected String doInBackground(String... params){
            if(filtrarDatos()) mostrarFalda(posicionF);
            return null;
        }
    }
}




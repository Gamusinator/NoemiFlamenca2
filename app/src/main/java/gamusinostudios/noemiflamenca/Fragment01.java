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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import uk.co.senab.photoview.PhotoViewAttacher;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment01.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment01#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment01 extends Fragment {

    PhotoViewAttacher mAttacher;
    Button btnSiguiente, btnAnterior, modoFoto;
    ImageView galeria;
    LinearLayout eventos;
    //TextView titol;
    ActionBar barra;
    AdView publi;
    FloatingActionButton fab;
    boolean isImageFitToScreen;
    String path = "http://35.177.198.220/noemiFlamenca/imagenes/galeria/";
    String[] nombresArchivos;
    int i = 0;
    int total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment01, container, false);

        new Mostrar().execute();

        btnAnterior = v.findViewById(R.id.botonAnterior);
        btnSiguiente = v.findViewById(R.id.botonSiguiente);
        modoFoto = v.findViewById(R.id.modoFotoGaleria);
        galeria = v.findViewById(R.id.imageViewPrincipal);
        //titol = v.findViewById(R.id.textView2);
        barra = ((AppCompatActivity)getActivity()).getSupportActionBar();
        publi = getActivity().findViewById(R.id.adView);
        fab = getActivity().findViewById(R.id.share);
        eventos = v.findViewById(R.id.eventos);


        btnAnterior.setOnClickListener(listener);
        btnSiguiente.setOnClickListener(listener);

        Picasso.with(v.getContext()).load(path+"/1.jpg").into(galeria);
        mAttacher = new PhotoViewAttacher(galeria);

        modoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    //butons.setVisibility(View.VISIBLE);
                    //titol.setVisibility(View.VISIBLE);
                    barra.show();
                    fab.setVisibility(View.VISIBLE);
                    publi.setVisibility(View.VISIBLE);
                    //galeria.setScaleType(ImageView.ScaleType.CENTER);
                    eventos.setPadding(16,0,16,50);

                }else{
                    isImageFitToScreen=true;
                    //butons.setVisibility(View.GONE);
                    //titol.setVisibility(View.GONE);
                    barra.hide();
                    fab.setVisibility(View.GONE);
                    publi.setVisibility(View.GONE);
                    //galeria.setScaleType(ImageView.ScaleType.CENTER);
                    eventos.setPadding(0,16,0,16);
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    //Quan fem clic en un botó, siguiente o anterior...
    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(nombresArchivos != null) {
                total = nombresArchivos.length;
                int id = view.getId();
                if (id == R.id.botonSiguiente) {
                    i++;
                    if (i == total) i = 0;
                }
                if (id == R.id.botonAnterior) {
                    i--;
                    if (i == -1) i = total - 1;
                }
                //carreguem la imatge a l'ImageView
                String urlfoto = path + nombresArchivos[i];
                Picasso.with(view.getContext()).load(urlfoto).into(galeria);
                mAttacher.update();
            }else{
                Toast.makeText(getContext(), "Ups! Actualmente no hay fotos disponibles. Comprueba tu conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void cargarArray(){


        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://35.177.198.220/noemiFlamenca/scripts/galeria.php");
        //autentificacio
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

        if (!resultado.isEmpty()){
            String string = resultado;
            nombresArchivos = string.split(","); //Aquí tenemos la array cargada con los nombres de fichero
        }
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

    class Mostrar extends AsyncTask<String,String,String> {
        protected String doInBackground(String... params){
            cargarArray();
            return null;
        }
    }
}

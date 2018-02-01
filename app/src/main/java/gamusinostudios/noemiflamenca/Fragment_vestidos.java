package gamusinostudios.noemiflamenca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_vestidos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_vestidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_vestidos extends Fragment {

    private Button btnSiguienteVestido, btnAnteriorVestido;
    private ImageView Vestidos_View;
    private TextView idVestido, colorVestido, descripcionVestido;
    private Vestido vestido;
    private List<Vestido> listaVestidos;
    private int posicion = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vestidos, container, false);

        listaVestidos=new ArrayList<Vestido>();

        btnAnteriorVestido = v.findViewById(R.id.botonAnteriorVestidos);
        btnSiguienteVestido = v.findViewById(R.id.botonSiguienteVestidos);
        Vestidos_View = v.findViewById(R.id.imageViewPrincipal);
        idVestido = v.findViewById(R.id.textViewIdVestidosResultado);
        colorVestido = v.findViewById(R.id.textViewColorVestidosResultado);
        descripcionVestido = v.findViewById(R.id.textViewDescripcionVestidos);



        btnSiguienteVestido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listaVestidos.isEmpty()){
                    posicion=listaVestidos.size()-1;
                    mostrarVestido(posicion);
                }else{
                    posicion++;
                    mostrarVestido(posicion);
                }
            }
        });

        btnAnteriorVestido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listaVestidos.isEmpty()){
                    if(posicion<=0){
                        posicion=0;
                        mostrarVestido(posicion);
                    }
                }else{
                    posicion--;
                    mostrarVestido(posicion);
                }
            }
        });
        new Mostrar().execute();
        // Inflate the layout for this fragment
        return v;
    }

    private String mostrar(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/scripts/bajarVestidos.php");
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
        listaVestidos.clear();
        String data=mostrar();
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try{
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("vestido");
                for (int i = 0; i < jsonArray.length();i++){
                    vestido=new Vestido();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    vestido.setId(jsonArrayChild.optInt("id_vestido"));
                    vestido.setColor(jsonArrayChild.optString("color_vestido"));
                    vestido.setDescripcion(jsonArrayChild.optString("descripcion_vestdo"));
                    vestido.setUrl(jsonArrayChild.optString("url_vestido"));
                    listaVestidos.add(vestido);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void mostrarVestido(final int posicion){
        Runnable runOnUiThread = new Runnable(){
            @Override
            public void run() {
                Vestido vestidos= listaVestidos.get(posicion);
                idVestido.setText(vestidos.getId());
                colorVestido.setText(vestidos.getColor());
                descripcionVestido.setText(vestidos.getDescripcion());
            }
        };
    }

    class Mostrar extends AsyncTask<String,String,String> {
        protected String doInBackground(String... params){
            if(filtrarDatos())mostrarVestido(posicion);
            return null;
        }
    }
}




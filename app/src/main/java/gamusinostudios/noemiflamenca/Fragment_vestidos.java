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
import android.widget.Toast;
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

    private View v;

    private Button btnSiguienteVestido, btnAnteriorVestido;
    private ImageView Vestidos_View;
    private TextView idVestido, colorVestido, descripcionVestido;
    private Vestido vestido;
    private List<Vestido> listaVestidos;
    private int posicion = 0, total;
    private String path = "http://ec2-35-177-198-220.eu-west-2.compute.amazonaws.com/noemiFlamenca/imagenes/vestidos/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_vestidos, container, false);

        listaVestidos=new ArrayList<Vestido>();

        btnAnteriorVestido = v.findViewById(R.id.botonAnteriorFaldas);
        btnSiguienteVestido = v.findViewById(R.id.botonSiguienteVestidos);
        Vestidos_View = v.findViewById(R.id.imageViewVestidos);
        idVestido = v.findViewById(R.id.textViewIdVestidosResultado);
        colorVestido = v.findViewById(R.id.textViewColorVestidosResultado);
        descripcionVestido = v.findViewById(R.id.textViewDescripcionVestidos);

        btnSiguienteVestido.setOnClickListener(listener);
        btnAnteriorVestido.setOnClickListener(listener);

        new Mostrar().execute();
        // Inflate the layout for this fragment
        return v;
    }

    //Decidir què fer quan s'apreten els butons anterior i siguiente
    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            total = listaVestidos.size();
            int id = view.getId();
            if (id == R.id.botonSiguienteVestidos) {
                posicion++;
                if (posicion == total) posicion = 0;
            }
            if (id == R.id.botonAnteriorFaldas) {
                posicion--;
                if (posicion == -1) posicion = total - 1;
            }
            mostrarVestido(posicion);
        }
    };

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
                    vestido.setId(jsonArrayChild.optString("id_vestido"));
                    vestido.setColor(jsonArrayChild.optString("color_vestido"));
                    vestido.setDescripcion(jsonArrayChild.optString("descripcion_vestido"));
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
        getActivity().runOnUiThread (new Runnable(){
            @Override
            public void run() {
                if(!listaVestidos.isEmpty()) {
                    Vestido vestidos= listaVestidos.get(posicion);
                    idVestido.setText(vestidos.getId());
                    colorVestido.setText(vestidos.getColor());
                    descripcionVestido.setText(vestidos.getDescripcion());
                    String urlfoto = path + vestidos.getUrl();
                    Picasso.with(v.getContext()).load(urlfoto).into(Vestidos_View);
                }else{
                    Toast.makeText(getContext(), "Ups! Actualmente no hay vestidos disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class Mostrar extends AsyncTask<String,String,String> {
        protected String doInBackground(String... params){
            if(filtrarDatos())mostrarVestido(posicion);
            return null;
        }
    }
}




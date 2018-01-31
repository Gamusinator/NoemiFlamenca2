package gamusinostudios.noemiflamenca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;


/**
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
    int[] fotoId = {
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_send,
            R.drawable.ic_menu_gallery
    };
    int i = 0;
    int total;

//    String[] fotoUrl={};
//    URL imageUrl = null;
//    HttpURLConnection conn = null;
//
//    try {
//
//        imageUrl = new URL("http://pagina.com/foto.jpg");
//        conn = (HttpURLConnection) imageUrl.openConnection();
//        conn.connect();
//        Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream());
//        img.setImageBitmap(imagen);
//
//    } catch (IOException e) {
//
//        e.printStackTrace();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment01, container, false);

        btnAnterior = v.findViewById(R.id.botonAnterior);
        btnSiguiente = v.findViewById(R.id.botonSiguiente);
        galeria = v.findViewById(R.id.imageViewPrincipal);

        btnAnterior.setOnClickListener(listener);
        btnSiguiente.setOnClickListener(listener);

        total = fotoId.length;

        // Inflate the layout for this fragment
        return v;
    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.botonSiguiente){
                i++;
                if(i == total) i = 0;
            }
            if(id == R.id.botonAnterior){
                i--;
                if(i == -1) i = total-1;
            }
            galeria.setImageResource(fotoId[i]);
        }
    };
}

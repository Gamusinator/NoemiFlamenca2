package gamusinostudios.noemiflamenca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.net.URL;


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
    int[] fotoId = {
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_send,
            R.drawable.ic_menu_gallery
    };
    int i = 0;
    int total;

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

        cargarImagen(v);

        // Inflate the layout for this fragment
        return v;
    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.botonSiguiente){
//                i++;
//                if(i == total) i = 0;
                Picasso.with(view.getContext()).load("https://scontent.fmad3-2.fna.fbcdn.net/v/t1.0-9/24909789_655290551527508_1181231841592933036_n.jpg?oh=9756ed45e70e42b812feb813cb63e247&oe=5ADD81DB").into(galeria);
            }
            if(id == R.id.botonAnterior){
//                i--;
//                if(i == -1) i = total-1;
                Picasso.with(view.getContext()).load("https://scontent.fmad3-2.fna.fbcdn.net/v/t1.0-9/26733397_676320916091138_122622102907252329_n.jpg?oh=5f05fe9fb89203847de2500e921d7470&oe=5B1A27D4").into(galeria);
            }
//            galeria.setImageResource(fotoId[i]);
        }
    };

    public void cargarImagen(View v){
        Picasso.with(v.getContext()).load("http://i.imgur.com/DvpvklR.png").into(galeria);
    }
}

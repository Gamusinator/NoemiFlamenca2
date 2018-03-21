package gamusinostudios.noemiflamenca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment04.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment04#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment04 extends Fragment implements View.OnClickListener{

    ImageButton Gmail;
    ImageButton Facebook;
    ImageButton Youtube;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Cargamos el fragment 4 en el contenedor
        View myView = inflater.inflate(R.layout.fragment_fragment04, container, false);

        //definimos el boton Gmail
        Gmail = (ImageButton) myView.findViewById(R.id.imageButton1);
        Gmail.setOnClickListener(this);

        //definimos el boton Facebook
        Facebook = (ImageButton) myView.findViewById(R.id.imageButton2);
        Facebook.setOnClickListener(this);

        //definimos el boton Youtube
        Youtube = (ImageButton) myView.findViewById(R.id.imageButton3);
        Youtube.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View view) {

        //switch para hacer una acción segun el botón que se haya presionado
        switch (view.getId()) {
            //boton correo
            case R.id.imageButton1:
                Toast.makeText(getContext(), "Abriendo editor de correo...", Toast.LENGTH_SHORT).show();
                sendEmail();
                break;
            //boton facebook
            case R.id.imageButton2:
                Toast.makeText(getContext(), "Abriendo facebook...", Toast.LENGTH_SHORT).show();
                navegateToUrl();
                break;
            //boton youtube
            case R.id.imageButton3:
                Toast.makeText(getContext(), "Abriendo youtube...", Toast.LENGTH_SHORT).show();
                openYoutube();
                break;

            default:
                break;
        }
    }

    public void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"minoemiflamenca@gmail.com"});
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Cáspitas! parece que no tienes correo Gmail!", Toast.LENGTH_SHORT).show();
        }
    }

    public void navegateToUrl(){
        Uri uriUrl = Uri.parse("http://www.facebook.com/minoemiflamenca/");
        Intent abrirFacebook = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(abrirFacebook);
    }

    public void openYoutube(){
        Uri uriUrl = Uri.parse("http://www.youtube.com/channel/UCJtN1kSAs8c_NALtv968WoA");
        Intent abrirFacebook = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(abrirFacebook);
    }
}

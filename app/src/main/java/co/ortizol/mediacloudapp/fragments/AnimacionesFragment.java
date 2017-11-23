package co.ortizol.mediacloudapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import co.ortizol.mediacloudapp.R;

/**
 * Representa el fragmento para efectuar animaciones.
 */
public class AnimacionesFragment extends Fragment implements View.OnClickListener {

    /**
     * FAB para iniciar la animación de un cubo.
     */
    private FloatingActionButton fabIniciarAnimacion;
    /**
     * Representa la imgen a aplicar la animación.
     */
    private ImageView imgMobius;

    /**
     * Representa la animación a aplicar a la imagen.
     */
    private Animation aniEscaladoRotacion;

    /**
     * Constructor por defecto del fragmento.
     */
    public AnimacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animaciones, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabIniciarAnimacion = (FloatingActionButton) view.findViewById(R.id.fabIniciarAnimacion);

        fabIniciarAnimacion.setOnClickListener(this);

        imgMobius = (ImageView) view.findViewById(R.id.imgMobius);

        aniEscaladoRotacion = AnimationUtils.loadAnimation(getContext(), R.anim.animacion_escalado_rotacion_mobius);
    }

    @Override
    public void onClick(View v) {
        imgMobius.startAnimation(aniEscaladoRotacion);
    }
}

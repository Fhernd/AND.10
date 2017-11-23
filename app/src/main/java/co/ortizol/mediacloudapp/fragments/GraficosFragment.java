package co.ortizol.mediacloudapp.fragments;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.ortizol.mediacloudapp.renderers.CuboRenderer;

public class GraficosFragment extends Fragment {

    private GLSurfaceView lienzo;

    public GraficosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lienzo = new GLSurfaceView(getContext());
        lienzo.setRenderer(new CuboRenderer(getContext()));

        return lienzo;
    }

}

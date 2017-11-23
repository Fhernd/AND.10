package co.ortizol.mediacloudapp.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.ortizol.mediacloudapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * Representa el fragmento para reproducir y grabar vídeo.
 */
public class VideoFragment extends Fragment {

    /**
     * Representa la ruta desde donde se carga un vídeo o se graba.
     */
    private TextView lblRutaVideo;
    /**
     * Representa el reproductor de vídeo.
     */
    private VideoView vvwVideo;

    /**
     * Código para representar la grabación de un archivo de vídeo.
     */
    private static int REQUEST_CODE_GRABACION_VIDEO = 1;
    /**
     * Código para representar la apertura de un archivo de vídeo.
     */
    private static int REQUEST_CODE_ABRIR_VIDEO = 2;

    /**
     * Ruta de los archivos de vídeo.
     */
    private static final String RUTA_VIDEOS = "videos_grabados";

    /**
     * URI para los vídeos.
     */
    private Uri videoUri;

    /**
     * Conjunto de permisos que se deben habilitar sobre este fragmento.
     */
    private static final String[] PERMISOS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Constructor por defecto del fragmento.
     */
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int leer1 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (leer1 == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISOS, REQUEST_CODE_GRABACION_VIDEO);
        }


        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lblRutaVideo = (TextView) view.findViewById(R.id.lblRutaVideo);
        vvwVideo = (VideoView) view.findViewById(R.id.vvwVideo);

        view.findViewById(R.id.btnGrabacionVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ittVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                File videosFolder = new File(Environment.getExternalStorageDirectory(), RUTA_VIDEOS);

                videosFolder.mkdirs();

                Date currentTime = Calendar.getInstance().getTime();

                String nombreArchivoVideo = String.format(Locale.US, "%d-%d-%d %d-%d.mp4", currentTime.getYear(), currentTime.getMonth(), currentTime.getDay(), currentTime.getHours(), currentTime.getMinutes());

                File video = new File(videosFolder, nombreArchivoVideo);

                videoUri = Uri.fromFile(video);

                ittVideo.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

                startActivityForResult(ittVideo, REQUEST_CODE_GRABACION_VIDEO);
            }
        });

        view.findViewById(R.id.btnAbrirVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ittAbrirAudio = new Intent();
                ittAbrirAudio.setType("video/*");
                ittAbrirAudio.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(ittAbrirAudio, getString(R.string.seleccion_archivo_video)), REQUEST_CODE_ABRIR_VIDEO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GRABACION_VIDEO && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), getString(R.string.video_guardado), Toast.LENGTH_LONG).show();

            vvwVideo.setVideoURI(videoUri);

            vvwVideo.start();

            vvwVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getActivity(), getString(R.string.video_reproduccion_finalizada), Toast.LENGTH_SHORT).show();
                }
            });

            lblRutaVideo.setText(String.format(Locale.US, "%s: %s", getString(R.string.ruta), videoUri.toString()));
        }

        if (requestCode == REQUEST_CODE_ABRIR_VIDEO && resultCode == RESULT_OK) {
            String patch = data.getDataString();

            vvwVideo.setVideoURI(Uri.parse(patch));

            vvwVideo.start();

            vvwVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getActivity(), getString(R.string.video_reproduccion_finalizada), Toast.LENGTH_SHORT).show();
                }
            });

            lblRutaVideo.setText(String.format(Locale.US, "%s: %s", getString(R.string.ruta), patch));
        }
    }
}

package co.ortizol.mediacloudapp.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.ortizol.mediacloudapp.R;


public class AudioFragment extends Fragment {

    /**
     * Botón para iniciar o detenar la reproducción de un audio o grabación.
     */
    private Button btnReproducirAudio;
    /**
     * Muestra la ruta del archivo o grabación.
     */
    private TextView lblRutaAudio;

    /**
     * Código para efectuar validaciones de permisos.
     */
    private static final int REQUEST_CODE = 1;

    /**
     * Código para representar la apertura de un archivo de audio.
     */
    private static final int REQUEST_CODE_ABRIR_AUDIO = 1;

    /**
     * Conjunto de permisos que se deben habilitar sobre este fragmento.
     */
    private static final String[] PERMISOS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * Determina si se está grabando un audio.
     */
    private boolean grabando = false;
    /**
     * Determina si se está reproduciendo un audio o grabación.
     */
    private boolean reproduciendo = false;

    /**
     * Reproductor de audio y grabaciones.
     */
    private MediaPlayer mediaPlayer;
    /**
     * Uri para la ruta del archivo de audio.
     */
    Uri rutaAudio;
    private Button btnIniciarGrabacion;
    /**
     * Nombre de la grabación.
     */
    private String nombreGrabacion;
    /**
     * Grabador de audios desde el micrófono.
     */
    private MediaRecorder mediaRecorder;

    /**
     * Constructor por defecto del fragmento.
     */
    public AudioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int leer1 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int leer2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

        if (leer1 == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISOS, REQUEST_CODE);
        }

        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        lblRutaAudio = (TextView) view.findViewById(R.id.lblRutaAudio);

        view.findViewById(R.id.btnAbrirAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grabando) {
                    detenerGrabacion();
                    btnIniciarGrabacion.setText(getString(R.string.iniciar_grabacion));
                    grabando = false;
                }

                Intent ittAbrirAudio = new Intent();
                ittAbrirAudio.setType("audio/*");
                ittAbrirAudio.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(ittAbrirAudio, getString(R.string.seleccion_archivo_audio)), REQUEST_CODE_ABRIR_AUDIO);
            }
        });

        btnReproducirAudio = (Button) view.findViewById(R.id.btnReproducirAudio);

        btnReproducirAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reproduciendo) {
                    detenerReproduccion();
                } else {
                    Toast.makeText(getContext(), getString(R.string.seleccione_audio_o_grabe), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnIniciarGrabacion = (Button) view.findViewById(R.id.btnIniciarGrabacion);

        btnIniciarGrabacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!grabando) {
                    Date currentTime = Calendar.getInstance().getTime();

                    nombreGrabacion = String.format(Locale.US, "%d-%d-%d %d-%d.3gp", currentTime.getYear(), currentTime.getMonth(), currentTime.getDay(), currentTime.getHours(), currentTime.getMinutes());

                    nombreGrabacion = Environment.getExternalStorageDirectory() + "/" + nombreGrabacion;

                    if (reproduciendo) {
                        detenerReproduccion();
                    }

                    iniciarGrabacion();

                    btnIniciarGrabacion.setText(getString(R.string.detener_grabacion));

                    grabando = true;
                } else {

                    detenerGrabacion();

                    grabando = false;

                    btnIniciarGrabacion.setText(getString(R.string.iniciar_grabacion));

                    iniciarReproduccion();

                    btnReproducirAudio.setText(getString(R.string.detener_audio));

                    reproduciendo = true;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ABRIR_AUDIO && resultCode == Activity.RESULT_OK) {
            String patch = data.getDataString();

            try {
                mediaPlayer = new MediaPlayer();
                rutaAudio = Uri.parse(patch);
                mediaPlayer.setDataSource(getActivity().getApplicationContext(), rutaAudio);
                mediaPlayer.prepare();
                mediaPlayer.start();

                reproduciendo = true;

                btnReproducirAudio.setText(getString(R.string.detener_audio));

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnReproducirAudio.setText(R.string.reproducir_audio);
                        lblRutaAudio.setText(getString(R.string.ruta_ningun_archivo_seleccionado));
                    }
                });

                lblRutaAudio.setText(String.format(Locale.US, "%s: %s", getString(R.string.ruta), rutaAudio.toString()));

            } catch (Exception e) {
                Toast.makeText(getActivity(), getString(R.string.error_abrir_audio), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Detiene la reproducción de audio.
     */
    private void detenerReproduccion() {
        mediaPlayer.stop();
        btnReproducirAudio.setText(getString(R.string.reproducir_audio));
        lblRutaAudio.setText(getString(R.string.ruta_ningun_archivo_seleccionado));
        reproduciendo = false;
    }

    /**
     * Inicia el proceso de grabación.
     */
    private void iniciarGrabacion() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(nombreGrabacion);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getContext(), getString(R.string.problema_grabacion), Toast.LENGTH_SHORT).show();
        }

        mediaRecorder.start();
    }

    /**
     * Detiene el proceso de grabación.
     */
    private void detenerGrabacion() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(getContext(), String.format(Locale.US, "%s:\n%s", getString(R.string.audio_guardado), nombreGrabacion), Toast.LENGTH_LONG).show();
    }

    /**
     * Inicia la reproducción de un audio grabado.
     */
    private void iniciarReproduccion() {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(nombreGrabacion);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    btnReproducirAudio.setText(R.string.reproducir_audio);
                    lblRutaAudio.setText(getString(R.string.ruta_ningun_archivo_seleccionado));
                }
            });

            lblRutaAudio.setText(String.format(Locale.US, "%s: %s", getString(R.string.ruta), nombreGrabacion));
        } catch (IOException e) {
            Toast.makeText(getContext(), getString(R.string.error_reproduccion_grabacion), Toast.LENGTH_SHORT).show();
        }
    }
}

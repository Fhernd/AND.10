package co.ortizol.mediacloudapp.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.ortizol.mediacloudapp.R;

/**
 * Representa el fragmento para mostrar imágenes cargadas o tomadas desde la cámara.
 */
public class ImagenFragment extends Fragment {

    /**
     * Representa la imagen cargada o capturada desde la cámara.
     */
    private ImageView imgImagen;
    /**
     * Representa la ruta de la imagen cargada o capturada.
     */
    private TextView lblRutaImagen;
    /**
     * Representa el botoón para abrir una imagen.
     */
    private Button btnAbrirImagen;
    /**
     * Representa el botón para capturar una imagen usando la cámara.
     */
    private Button btnCapturarImagen;

    /**
     * Bitmap para la imagen cargada o capturada desde la cámara.
     */
    private Bitmap bitmap = null;

    /**
     * Código para efectuar validaciones de permisos.
     */
    private static final int REQUEST_CODE = 1;

    /**
     * Código para representar la apertura de un archivo de imagen.
     */
    private static final int REQUEST_CODE_ABRIR_IMAGEN = 1;
    /**
     * Código para representar la captura de un archivo de imagen.
     */
    private static final int REQUEST_CODE_CAPTURAR_IMAGEN = 2;

    /**
     * Conjunto de permisos que se deben habilitar sobre este fragmento.
     */
    private static final String[] PERMISOS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Ruta sobre la que se almacenan las imágenes.
     */
    private final String RUTA_IMAGENES = "Imagenes";

    /**
     * Nombre del archivo de imagen.
     */
    private String nombreArchivo;

    /**
     * Constructor por defecto del fragmento.
     */
    public ImagenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int leer1 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int leer2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (leer1 == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISOS, REQUEST_CODE);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_imagen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgImagen = (ImageView) view.findViewById(R.id.imgImagen);
        lblRutaImagen = (TextView) view.findViewById(R.id.lblRutaImagen);

        btnAbrirImagen = (Button) view.findViewById(R.id.btnAbrirImagen);
        btnCapturarImagen = (Button) view.findViewById(R.id.btnCapturarImagen);

        btnAbrirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ittGaleriaImagenes = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ittGaleriaImagenes, REQUEST_CODE_ABRIR_IMAGEN);
            }
        });

        btnCapturarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File imagenFolder = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGENES);

                imagenFolder.mkdirs();

                Date currentTime = Calendar.getInstance().getTime();

                nombreArchivo = String.format(Locale.US, "%d-%d-%d %d-%d.jpg", currentTime.getYear(), currentTime.getMonth(), currentTime.getDay(), currentTime.getHours(), currentTime.getMinutes());

                File imagen = new File(imagenFolder, nombreArchivo);

                Uri uriImagen = Uri.fromFile(imagen);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);

                startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURAR_IMAGEN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getActivity();
        if(requestCode == REQUEST_CODE_ABRIR_IMAGEN && resultCode == Activity.RESULT_OK){
            Uri uriImagenSeleccionada = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(uriImagenSeleccionada, path, null, null, null);
            cursor.moveToFirst();
            int columna = cursor.getColumnIndex(path[0]);
            String pathImagen = cursor.getString(columna);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(pathImagen);
            BitmapFactory.Options options = new BitmapFactory.Options();
            int height= bitmap.getHeight();
            int width=bitmap.getWidth();
            float scaleA =((float)(width/2))/width;
            float scaleB =((float)(height/2))/height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleA,scaleB);
            Bitmap nuevaimagen= Bitmap.createBitmap(bitmap,0, 0 ,width,height,matrix,true);

            lblRutaImagen.setText(String.format(Locale.US, "%s: %s", getString(R.string.ruta), pathImagen));
            imgImagen.setImageBitmap(nuevaimagen);
        }

        if (requestCode == REQUEST_CODE_CAPTURAR_IMAGEN && resultCode == Activity.RESULT_OK){

            bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + RUTA_IMAGENES + "/" + nombreArchivo);

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            float scaleA = ((float) (width / 2)) / width;
            float scaleB = ((float) (height / 2)) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleA, scaleB);

            Bitmap nuevaImagen = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

            imgImagen.setImageBitmap(nuevaImagen);
            lblRutaImagen.setText(String.format(Locale.US, "%s: %s", getString(R.string.ruta), Environment.getExternalStorageDirectory() + "/" + RUTA_IMAGENES + "/" + nombreArchivo));
        }
    }
}

package com.groegcodedev.registrodebeneficiarios;

import android.graphics.Matrix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    // Identificadores para las solicitudes de la cámara
    private static final int REQUEST_IMAGE_CAPTURE_ACUSE_FRENTE = 1;
    private static final int REQUEST_IMAGE_CAPTURE_ACUSE_REVERSO = 2;
    private static final int REQUEST_IMAGE_CAPTURE_BENEFICIARIO = 3;
    private static final int REQUEST_IMAGE_CAPTURE_CREDENCIAL_FRENTE = 4;
    private static final int REQUEST_IMAGE_CAPTURE_CREDENCIAL_REVERSO = 5;
    private static final int REQUEST_IMAGE_CAPTURE_COMP_DOMICILIO = 6;
    /*Estas líneas definen constantes que representan los identificadores para las solicitudes de la cámara.
    Estos identificadores se utilizan cuando se inicia la actividad de la cámara para tomar fotos y se reciben en el
    método onActivityResult para identificar de qué solicitud proviene el resultado.*/

    // Vistas previas de las imágenes
    private ImageView previewAcuseFrente;
    private ImageView previewAcuseReverso;
    private ImageView previewBeneficiario;
    private ImageView previewCredencialFrente;
    private ImageView previewCredencialReverso;
    private ImageView previewCompDomicilio;
    /*Estas líneas declaran variables que representan las vistas previas de las imágenes que se mostrarán en la interfaz de usuario.*/

    // Estado de las fotos
    private boolean fotoAcuseFrenteTomada = false;
    private boolean fotoAcuseReversoTomada = false;
    private boolean fotoBeneficiarioTomada = false;
    private boolean fotoCredencialFrenteTomada = false;
    private boolean fotoCredencialrReversoTomada = false;
    private boolean fotoCompDomicilioTomada = false;
    /*Estas líneas declaran variables booleanas que mantienen el estado de si las fotos han sido tomadas o no.*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar las vistas previas
        previewCredencialFrente = findViewById(R.id.previewCredencialFrente);
        previewCredencialReverso = findViewById(R.id.previewCredencialReverso);
        previewAcuseFrente = findViewById(R.id.previewAcuseFrente);
        previewAcuseReverso = findViewById(R.id.previewAcuseReverso);
        previewCompDomicilio = findViewById(R.id.previewCompDomicilio);
        previewBeneficiario = findViewById(R.id.previewBeneficiario);

        // Configurar eventos de clic para los botones
        configureButtonClickEvents();
    }
    /*Estas líneas inicializan las vistas previas de las imágenes en la interfaz de usuario y configuran los eventos de clic para los
    botones llamando al método configureButtonClickEvents().*/

    private void configureButtonClickEvents() {
        // Botón para tomar foto de Credencial Frente
        ImageView imageAcuseFrente = findViewById(R.id.previewAcuseFrente);
        imageAcuseFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_ACUSE_FRENTE);
            }
        });

        // Botón para tomar foto de Credencial Reverso
        ImageView imageAcuseReverso = findViewById(R.id.previewAcuseReverso);
        imageAcuseReverso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_ACUSE_REVERSO);
            }
        });

        // Botón para tomar foto de Acuse Frente
        ImageView imageBeneficiario = findViewById(R.id.previewBeneficiario);
        imageBeneficiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_BENEFICIARIO);
            }
        });

        // Botón para tomar foto de Acuse Reverso
        ImageView imageCredencialFrente = findViewById(R.id.previewCredencialFrente);
        imageCredencialFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_CREDENCIAL_FRENTE);
            }
        });

        // Botón para tomar foto de Acuse Reverso
        ImageView imageCredencialReverso = findViewById(R.id.previewCredencialReverso);
        imageCredencialReverso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_CREDENCIAL_REVERSO);
            }
        });

        // Botón para tomar foto de Acuse Reverso
        ImageView imageComprobanteDomicilio = findViewById(R.id.previewCompDomicilio);
        imageComprobanteDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_COMP_DOMICILIO);
            }
        });

        // Repetir lo mismo para otros botones

        // Botón para guardar
        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setEnabled(false); // Deshabilitar inicialmente el botón de guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el folio y el nombre del beneficiario
                EditText editTextFolio = findViewById(R.id.editTextFolio);
                EditText editTextNombre = findViewById(R.id.editTextNombre);

                String folio = editTextFolio.getText().toString();
                String nombreBeneficiario = editTextNombre.getText().toString();

                // Verificar que se hayan introducido el folio y el nombre
                if (folio.isEmpty() || nombreBeneficiario.isEmpty()) {
                    // Mostrar un mensaje recordatorio si falta información
                    Toast.makeText(MainActivity.this, "Completa todos los campos antes de guardar", Toast.LENGTH_SHORT).show();
                } else if (!todasLasFotosTomadas()) {
                    // Mostrar un mensaje si faltan fotos
                    Toast.makeText(MainActivity.this, "Debes tomar todas las fotos antes de guardar", Toast.LENGTH_SHORT).show();
                } else {
                    // Guardar la información y resetear
                    guardarInformacion();
                    resetearCampos();
                    // Aquí puedes implementar la lógica para guardar la información y las imágenes
                }
            }
        });
    }
    /*Este método configura los eventos de clic para los botones. Para cada botón, se asocia un OnClickListener que ejecuta
    el método dispatchTakePictureIntent() cuando se hace clic en la vista de la imagen correspondiente. También se configura
    un OnClickListener para el botón de guardar, que verifica si se han introducido el folio y el nombre, y si todas
    las fotos requeridas han sido tomadas. Si es así, se llama a los métodos guardarInformacion() y resetearCampos().*/

    private void guardarInformacion() {
        // Obtener el folio y el nombre del beneficiario
        EditText editTextFolio = findViewById(R.id.editTextFolio);
        EditText editTextNombre = findViewById(R.id.editTextNombre);

        String folio = editTextFolio.getText().toString();
        String nombreBeneficiario = editTextNombre.getText().toString();

        // Verificar que se hayan introducido el folio y el nombre
        if (folio.isEmpty() || nombreBeneficiario.isEmpty()) {
            Toast.makeText(this, "Introduce el folio y el nombre del beneficiario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el nombre del directorio con el formato folio_nombreBeneficiario
        String directorioNombre = folio + "_" + nombreBeneficiario;

        // Obtener el directorio raíz de almacenamiento externo
        File directorioRaiz = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Registro de Beneficiarios");

        // Crear el directorio raíz si no existe
        if (!directorioRaiz.exists()) {
            directorioRaiz.mkdirs();
        }

        // Obtener la fecha actual para crear el directorio con la fecha del día
        String fechaActual = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(new Date());
        File directorioFecha = new File(directorioRaiz, fechaActual);

        // Crear el directorio con la fecha del día si no existe
        if (!directorioFecha.exists()) {
            directorioFecha.mkdirs();
        }

        // Obtener el directorio de almacenamiento externo con la fecha del día y el nombre del beneficiario
        File directorioAlmacenamiento = new File(directorioFecha, directorioNombre);

        // Crear el directorio si no existe
        if (!directorioAlmacenamiento.exists()) {
            directorioAlmacenamiento.mkdirs();
        }

        // Guardar las fotos en el directorio
        guardarFotosEnDirectorio(directorioAlmacenamiento, "AcuseFrente", previewAcuseFrente);
        guardarFotosEnDirectorio(directorioAlmacenamiento, "AcuseReverso", previewAcuseReverso);
        guardarFotosEnDirectorio(directorioAlmacenamiento, "Beneficiario", previewBeneficiario);
        guardarFotosEnDirectorio(directorioAlmacenamiento, "INEFrente", previewCredencialFrente);
        guardarFotosEnDirectorio(directorioAlmacenamiento, "INEReverso", previewCredencialReverso);
        guardarFotosEnDirectorio(directorioAlmacenamiento, "CompDomicilio", previewCompDomicilio);

        Toast.makeText(this, "Información guardada correctamente", Toast.LENGTH_SHORT).show();
    }
    /*Este método se encarga de guardar la información y las fotos en el directorio correspondiente. Comienza obteniendo
    el folio y el nombre del beneficiario desde los EditText. Luego, crea el nombre del directorio con el formato
    "folio_nombreBeneficiario". Después, obtiene o crea el directorio raíz en el almacenamiento externo y un directorio
    con la fecha actual dentro de él. Luego, crea el directorio con el nombre del beneficiario y guarda las fotos en ese
    directorio utilizando el método guardarFotosEnDirectorio().*/

    private void guardarFotosEnDirectorio(File directorio, String nombreFoto, ImageView preview) {
        // Obtener la imagen del ImageView
        Bitmap imagen = ((BitmapDrawable) preview.getDrawable()).getBitmap();

        // Redimensionar la imagen a un tamaño manejable
        int nuevoAncho = 800;
        int nuevoAlto = 600;
        Bitmap imagenRedimensionada = Bitmap.createScaledBitmap(imagen, nuevoAncho, nuevoAlto, true);

        // Crear el archivo de imagen en el directorio
        File archivoImagen = new File(directorio, nombreFoto + ".png");

        try {
            // Guardar la imagen en el archivo
            FileOutputStream stream = new FileOutputStream(archivoImagen);
            imagenRedimensionada.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*Este método recibe un directorio, el nombre de la foto y la vista previa de la imagen.
    Extrae la imagen del ImageView, crea un archivo de imagen en el directorio con el nombre proporcionado y guarda la imagen en ese archivo.*/

    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requestCode);
        }
    }
    /*Este método inicia la actividad de la cámara para tomar una foto. Crea un intent con la acción MediaStore.ACTION_IMAGE_CAPTURE
    y lo inicia utilizando startActivityForResult() con el código de solicitud proporcionado.*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Mostrar la imagen tomada en la visa previa correspondiente
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Obtener la orientación de la imagen desde los extras
            int orientacion = extras.getInt("orientacion", 0);

            // Rotar la imagen según la orientación
            Matrix matrix = new Matrix();
            matrix.postRotate(orientacion);
            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

            // Marcar la foto correspondiente como tomada
            switch (requestCode) {
                // ... (Código existente)
            }

            // Verificar si todas las fotos requeridas han sido tomadas
            if (todasLasFotosTomadas()) {
                // Habilitar El botón de guardar
                Button btnGuardar = findViewById(R.id.btnGuardar);
                btnGuardar.setEnabled(true);
            }

            // Marcar la foto correspondiente como tomada
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE_ACUSE_FRENTE:
                    previewAcuseFrente.setImageBitmap(imageBitmap);
                    fotoAcuseFrenteTomada = true;
                    break;
                case REQUEST_IMAGE_CAPTURE_ACUSE_REVERSO:
                    previewAcuseReverso.setImageBitmap(imageBitmap);
                    fotoAcuseReversoTomada = true;
                    break;
                case REQUEST_IMAGE_CAPTURE_BENEFICIARIO:
                    previewBeneficiario.setImageBitmap(imageBitmap);
                    fotoBeneficiarioTomada = true;
                    break;
                case REQUEST_IMAGE_CAPTURE_CREDENCIAL_FRENTE:
                    previewCredencialFrente.setImageBitmap(imageBitmap);
                    fotoCredencialFrenteTomada = true;
                    break;
                case REQUEST_IMAGE_CAPTURE_CREDENCIAL_REVERSO:
                    previewCredencialReverso.setImageBitmap(imageBitmap);
                    fotoCredencialrReversoTomada = true;
                    break;
                case REQUEST_IMAGE_CAPTURE_COMP_DOMICILIO:
                    previewCompDomicilio.setImageBitmap(imageBitmap);
                    fotoCompDomicilioTomada = true;
                    break;
            }

            // Verificar si todas las fotos requeridas han sido tomadas
            if (todasLasFotosTomadas()) {
                // Habilitar El botón de guardar
                Button btnGuardar = findViewById(R.id.btnGuardar);
                btnGuardar.setEnabled(true);
            }
        }
    }
    /*Este método se llama cuando se completa la actividad de la cámara. Verifica si el resultado es RESULT_OK,
    lo que indica que la foto se tomó con éxito. Luego, extrae la imagen de los extras del intent y la muestra
    en la vista previa correspondiente. Marca la foto como tomada actualizando la variable booleana correspondiente.
    Después, verifica si todas las fotos requeridas han sido tomadas y, si es así, habilita el botón de guardar.*/

    // Método para verificar si todas las fotos requeridas han sido tomadas
    private boolean todasLasFotosTomadas() {
        return fotoAcuseFrenteTomada && fotoAcuseReversoTomada &&
                fotoBeneficiarioTomada && fotoCredencialFrenteTomada &&
                fotoCredencialrReversoTomada && fotoCompDomicilioTomada;
    }
    /*Este método devuelve true si todas las fotos requeridas han sido tomadas y false en caso contrario.*/

    private void resetearCampos() {
        // Resetear los EditText
        EditText editTextFolio = findViewById(R.id.editTextFolio);
        EditText editTextNombre = findViewById(R.id.editTextNombre);
        editTextFolio.getText().clear();
        editTextNombre.getText().clear();

        // Resetear las imágenes
        previewAcuseFrente.setImageResource(android.R.color.transparent);
        previewAcuseReverso.setImageResource(android.R.color.transparent);
        previewBeneficiario.setImageResource(android.R.color.transparent);
        previewCredencialFrente.setImageResource(android.R.color.transparent);
        previewCredencialReverso.setImageResource(android.R.color.transparent);
        previewCompDomicilio.setImageResource(android.R.color.transparent);

        // Desactivar el botón de guardar
        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setEnabled(false);
    }

}


package es.upm.miw.SolitarioCelta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DB.Partida;
import DB.PartidaAdapter;
import DB.RepositorioPartida;

public class MainActivity extends AppCompatActivity {

	JuegoCelta mJuego;
    private final String LOG_TAG = "MiW_JUEGO_CELTA";
    private final String CLAVE_TABLERO = "TABLERO_SOLITARIO_CELTA";

    private SharedPreferences preferencias;

    RepositorioPartida db;
    ArrayList<Partida> partidas;

    public final static String tvIdentificador="";

	private final int[][] ids = {
		{       0,        0, R.id.p02, R.id.p03, R.id.p04,        0,        0},
        {       0,        0, R.id.p12, R.id.p13, R.id.p14,        0,        0},
        {R.id.p20, R.id.p21, R.id.p22, R.id.p23, R.id.p24, R.id.p25, R.id.p26},
        {R.id.p30, R.id.p31, R.id.p32, R.id.p33, R.id.p34, R.id.p35, R.id.p36},
        {R.id.p40, R.id.p41, R.id.p42, R.id.p43, R.id.p44, R.id.p45, R.id.p46},
        {       0,        0, R.id.p52, R.id.p53, R.id.p54,        0,        0},
        {       0,        0, R.id.p62, R.id.p63, R.id.p64,        0,        0}
	};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJuego = new JuegoCelta();
        mostrarTablero();

        db = new RepositorioPartida(getApplicationContext());
    }

    /**
     * Se ejecuta al pulsar una posición
     *
     * @param v Vista de la posición pulsada
     */
    public void posicionPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';
        int j = resourceName.charAt(2) - '0';

        mJuego.jugar(i, j);
        mostrarTablero();
        if (mJuego.juegoTerminado()) {
            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
        }
        if(mJuego.juegoTerminado()){
            db.add("Freddy", mJuego.getFechaPartida(), mJuego.numeroFichas());
        }
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++)
                if (ids[i][j] != 0) {
                    button = findViewById(ids[i][j]);
                    button.setChecked(mJuego.obtenerFicha(i, j) == 1);
                }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CLAVE_TABLERO, mJuego.serializaTablero());
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(CLAVE_TABLERO);
        mJuego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reinicarPartida:
                this.reiniciar_juego();
                break;
            case R.id.guardarPartida:
                this.accionAniadir();
                break;
            case R.id.recuperarPartida:
                this.recuperarPartidaFichero();
                break;
            case R.id.mejoresResultados:
                startActivity(new Intent(this, MejoresResultados.class));
                break;
            case R.id.eliminarMejoresResultados:
                this.eliminarMejoresResultados();
                break;
            case R.id.menuAbout:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.preferences:
                startActivity(new Intent(this, SCeltaPreferences.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reiniciar_juego() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ El programa será reiniciado, desea continuar ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptarReiniciarPartida();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelarReiniciarPartida();
            }
        });
        dialogo1.show();
    }

    public void aceptarReiniciarPartida() {
        Toast t=Toast.makeText(this,"EL juego ha sido reiniciado.", Toast.LENGTH_SHORT);
        t.show();
        mJuego.reiniciar();
        this.mostrarTablero();
    }

    public void cancelarReiniciarPartida() {
        Toast t=Toast.makeText(this,"Favor, continue con el juego.", Toast.LENGTH_SHORT);
        t.show();
    }

    /**
     * Crea e ingresa los datos al fichero
     */
    public void accionAniadir() {
        FileOutputStream fos;
        try {  // Añadir al fichero
            if (utilizarMemInterna()) {
                fos = openFileOutput(obtenerNombreFichero(), Context.MODE_APPEND); // Memoria interna
            } else {
                /* Comprobar estado SD card */
                String estadoTarjetaSD = Environment.getExternalStorageState();
                if (estadoTarjetaSD.equals(Environment.MEDIA_MOUNTED)) {
                    String rutaFich = getExternalFilesDir(null) + "/" + obtenerNombreFichero();
                    fos = new FileOutputStream(rutaFich, true);
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.aboutMessage),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
            }
            borrarContenido();  //Elimina los datos de la tabla previamente guardados
            fos.write(mJuego.serializaTablero().toString().getBytes()); //Almacena los datos de la tabla en el archivo
            fos.write('\n');
            fos.close();
            Log.i(LOG_TAG, "Click botón Añadir -> AÑADIR al fichero");
        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Si está vacío -> mostrar un Toast
     */
    void recuperarPartidaFichero() {
        boolean hayContenido = false;
        String tmp="";
        BufferedReader fin;
        try {
            if (utilizarMemInterna()) {
                fin = new BufferedReader(
                        new InputStreamReader(openFileInput(obtenerNombreFichero()))); // Memoria interna
            } else {
                String estadoTarjetaSD = Environment.getExternalStorageState();
                if (estadoTarjetaSD.equals(Environment.MEDIA_MOUNTED)) { /* SD card */
                    String rutaFich = getExternalFilesDir(null) + "/" + obtenerNombreFichero();
                    Log.i(LOG_TAG, "rutaSD=" + rutaFich);
                    fin = new BufferedReader(new FileReader(new File(rutaFich)));
                } else {
                    Log.i(LOG_TAG, "Estado SDcard=" + estadoTarjetaSD);
                    Toast.makeText(this, getString(R.string.aboutMessage), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String linea = fin.readLine();
            while (linea != null) {
                hayContenido = true;
                tmp=linea;
                linea = fin.readLine();
            }
            fin.close();
            Log.i(LOG_TAG, "MOSTRAR contenido fichero:"+ tmp);
        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
         if((tmp.trim()).equals(mJuego.serializaTablero().trim())){
            Toast.makeText(this, "No existen cambios para recuperar la partida", Toast.LENGTH_SHORT).show();
        }else{
           this.recuperar_juego(tmp);
        }
    }

    /**
     * Vaciar el contenido del fichero, la línea de edición y actualizar
     */
    protected void borrarContenido() {
        try {  // Vaciar el fichero
            FileOutputStream fos;
            if (utilizarMemInterna()) {
                fos = openFileOutput(obtenerNombreFichero(), Context.MODE_PRIVATE); // Memoria interna
            } else {
                /* Comprobar estado SD card */
                String estadoTarjetaSD = Environment.getExternalStorageState();
                if (estadoTarjetaSD.equals(Environment.MEDIA_MOUNTED)) {
                    String rutaFich = getExternalFilesDir(null) + "/" + obtenerNombreFichero();
                    fos = new FileOutputStream(rutaFich);
                } else {
                    Toast.makeText(this, getString(R.string.aboutMessage), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            fos.close();
            Log.i(LOG_TAG, "opción Limpiar -> VACIAR el fichero");
        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el nombre del fichero
     *
     * @return nombre del fichero
     */
    private String obtenerNombreFichero() {
        Log.i(LOG_TAG, "Metodo ObtenerNombreFichero: " + "Celta.txt");
        return "Celta.txt";
    }

    /**
     * Determina si prefiere memoria interna o externa
     *
     * @return valor lógico
     */
    private boolean utilizarMemInterna() {
        Log.i(LOG_TAG, "Memoria SD: metodo utilizarMemoriaInterna");
        return true;
    }

    public void recuperar_juego(final String tableroSerializado) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("La partida será recuperada, desea continuar ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptarRecuperarPartida(tableroSerializado);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelarRecuperarPartida();
            }
        });
        dialogo1.show();
    }

    public void aceptarRecuperarPartida(String tableroSerializado) {
        mJuego.deserializaTablero(tableroSerializado);
        this.mostrarTablero();
        Toast t=Toast.makeText(this,"La partida ha sido recuperada.", Toast.LENGTH_SHORT);
        t.show();

    }

    public void cancelarRecuperarPartida() {
        Toast t=Toast.makeText(this,"Favor, continue con el juego.", Toast.LENGTH_SHORT);
        t.show();
    }

    public void eliminarMejoresResultados() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("La lista de los mejores resultados de las partidas será eliminada, desea continuar?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptarEliminarMejoresResultados();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelarEliminarMejoresResultados();
            }
        });
        dialogo1.show();
    }

    public void aceptarEliminarMejoresResultados() {
        db.deleteAll();
        this.mostrarTablero();
        Toast t=Toast.makeText(this,"La lista de resultados ha sido eliminada.", Toast.LENGTH_SHORT);
        t.show();

    }

    public void cancelarEliminarMejoresResultados() {
        Toast t=Toast.makeText(this,"La lista de resultados no ha sido eliminada.", Toast.LENGTH_SHORT);
        t.show();
    }

}

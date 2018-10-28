package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

import DB.Partida;
import DB.PartidaAdapter;
import DB.RepositorioPartida;

public class MejoresResultados extends Activity {

    RepositorioPartida db;
    ArrayList<Partida> partidas;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado);

        db = new RepositorioPartida(getApplicationContext());

        try {
            partidas = db.getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("DB", "Lista de partidas " + partidas);

        ListView lvPartidas = findViewById(R.id.lvListadoPartida);
        lvPartidas.setAdapter(
                new PartidaAdapter(
                        this,
                        R.layout.listado_participantes,
                        partidas
                ));

    }
}

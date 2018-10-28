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
    public long ID = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado);

        db = new RepositorioPartida(getApplicationContext());
        long numElementos = db.count();
        Log.i("DB", "Número elementos en la base de Datos = " + String.valueOf(numElementos));

        //Inserta un cliente generado aleatoriamente
        ID = db.add("Freddy", "12-20-2018", 1);
        Log.i("DB", "ID DE PARTIDA................................................................. = " + String.valueOf(ID));

        try {
            partidas = db.getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("DB", "Partidas $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + partidas);

        ListView lvPartidas = findViewById(R.id.lvListadoPartida);
        lvPartidas.setAdapter(
                new PartidaAdapter(
                        this,
                        R.layout.listado_participantes,
                        partidas
                ));

        //lvPartidas.setAdapter(adaptador);
        //lvListado.setOnItemClickListener(this);
    }
}

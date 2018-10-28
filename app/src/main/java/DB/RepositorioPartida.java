package DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import DB.PartidaContract.tablaPartida;

public class RepositorioPartida extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DB_NAME = tablaPartida.TABLE_NAME + ".db";

    // Número de version
    private static final int DB_VERSION = 1;

    /**
     * Constructor
     *
     * @param contexto Context
     */
    public RepositorioPartida(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String consultaSQL = "CREATE TABLE " + tablaPartida.TABLE_NAME + " ("
                + tablaPartida.COL_NAME_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + tablaPartida.COL_NAME_NOMBRE_JUGADOR   + " TEXT, "
                + tablaPartida.COL_NAME_FECHA_PARTIDA    + " TEXT, "
                + tablaPartida.COL_NAME_NUMERO_PIEZAS    + " INTEGER)";
        db.execSQL(consultaSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String consultaSQL = "DROP TABLE IF EXISTS " + tablaPartida.TABLE_NAME;
        db.execSQL(consultaSQL);
        onCreate(db);
    }

    public long count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, tablaPartida.TABLE_NAME);
    }

    /**
     * A&ntilde;ade una nueva Partida a la tabla
     *
     * @param nombreJugador      Nombre del cliente
     * @param fechaPartida         DNI del cliente
     * @param numeroPiezas    Tlf. del cliente
     * @return Identificador de partida insertado (-1 si no se ha insertado)
     */
    public long add(String nombreJugador, String fechaPartida, int numeroPiezas) {

        // Obtiene la DB en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Mapa de valores: parejas nombreColumna:valor
        ContentValues valores = new ContentValues();
        valores.put(tablaPartida.COL_NAME_NOMBRE_JUGADOR, nombreJugador);
        valores.put(tablaPartida.COL_NAME_FECHA_PARTIDA, fechaPartida);
        valores.put(tablaPartida.COL_NAME_NUMERO_PIEZAS, numeroPiezas);

        // Realiza la inserción
        return db.insert(tablaPartida.TABLE_NAME, null, valores);
    }

    /**
     * Recupera todos las Clientes de la tabla
     *
     * @return array de Clientes
     */
    public ArrayList<Partida> getAll() throws ParseException {

        String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String consultaSQL = "SELECT * FROM " + tablaPartida.TABLE_NAME;
        ArrayList<Partida> listaClientes = new ArrayList<>();

        // Accedo a la DB en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(consultaSQL, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Partida partida = new Partida(
                        cursor.getInt(cursor.getColumnIndex(tablaPartida.COL_NAME_ID)),
                        cursor.getString(cursor.getColumnIndex(tablaPartida.COL_NAME_NOMBRE_JUGADOR)),
                        format.parse(cursor.getString(cursor.getColumnIndex(tablaPartida.COL_NAME_FECHA_PARTIDA))),
                        cursor.getInt(cursor.getColumnIndex(tablaPartida.COL_NAME_NUMERO_PIEZAS))
                );

                listaClientes.add(partida);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return listaClientes;
    }

    /**
     * Elimina todos los datos de la tabla
     */
    public int deleteAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(tablaPartida.TABLE_NAME,"1",null);
    }
}

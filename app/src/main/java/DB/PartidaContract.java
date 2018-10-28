package DB;

import android.provider.BaseColumns;

public class PartidaContract {

    private PartidaContract(){}

    public static abstract class tablaPartida implements BaseColumns {

        static final String TABLE_NAME                 = "partidas";

        static final String COL_NAME_ID                = _ID;
        static final String COL_NAME_NOMBRE_JUGADOR    = "nombreJugador";
        static final String COL_NAME_FECHA_PARTIDA     = "fechaPartida";
        static final String COL_NAME_NUMERO_PIEZAS     = "numeroPiezas";
    }
}

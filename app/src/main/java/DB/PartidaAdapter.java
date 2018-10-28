package DB;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.upm.miw.SolitarioCelta.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PartidaAdapter extends ArrayAdapter {

    private Context _contexto;
    private ArrayList<Partida> _partidas;
    private int idRecursoLayout;

    /**
     * Constructor
     * @param contexto   Contexto
     * @param partidas  Datos a representar
     */
    public PartidaAdapter(Context contexto,int resourse, ArrayList<Partida> partidas) {
        super(contexto, resourse, partidas);
        this._contexto = contexto;
        this.idRecursoLayout=resourse;
        this._partidas = partidas;
        setNotifyOnChange(true);
        Log.i("DB", "Partidas $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + this._partidas);
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // obtener o generar vista
        if (convertView == null) {
            LayoutInflater inflater =(LayoutInflater) _contexto
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(idRecursoLayout, parent, false);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Partida partida = _partidas.get(position);
        if (partida != null) {
            TextView tvId = convertView.findViewById(R.id.tvListadoParticipantesId);
            tvId.setText(Long.toString(partida.get_id()));

            TextView tvNombre = convertView.findViewById(R.id.tvListadoparticipantesNombreJugador);
            tvNombre.setText(partida.getNombreJugador());

            TextView tvfechaPartida = convertView.findViewById(R.id.tvListadoParticipantesFechaPartida);
            tvfechaPartida.setText(dateFormat.format(partida.getFechaPartida()));

            TextView tvNumeroPiezas = convertView.findViewById(R.id.tvListadoParticipantesNumeropiezas);
            tvNumeroPiezas.setText(Long.toString(partida.getNumeroPiezas()));
        }
        return convertView;
    }
}

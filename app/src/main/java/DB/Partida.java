package DB;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Partida implements Parcelable {

    private int _id;
    private String nombreJugador;
    private Date fechaPartida;
    private int numeroPiezas;

    public Partida(int _id, String nombreJugador, Date fechaPartida, int numeroPiezas) {
        this._id = _id;
        this.nombreJugador = nombreJugador;
        this.fechaPartida = fechaPartida;
        this.numeroPiezas = numeroPiezas;
    }

    public int get_id() {
        return _id;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Date getFechaPartida() {
        return fechaPartida;
    }

    public void setFechaPartida(Date fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    public int getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(int numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "_id=" + _id +
                ", nombreJugador='" + nombreJugador + '\'' +
                ", fechaPartida=" + fechaPartida +
                ", numeroPiezas=" + numeroPiezas +
                '}';
    }

    protected Partida(Parcel in) {
        _id = in.readInt();
        nombreJugador = in.readString();
        long tmpFechaPartida = in.readLong();
        fechaPartida = tmpFechaPartida != -1 ? new Date(tmpFechaPartida) : null;
        numeroPiezas = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(nombreJugador);
        dest.writeLong(fechaPartida != null ? fechaPartida.getTime() : -1L);
        dest.writeInt(numeroPiezas);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Partida> CREATOR = new Parcelable.Creator<Partida>() {
        @Override
        public Partida createFromParcel(Parcel in) {
            return new Partida(in);
        }

        @Override
        public Partida[] newArray(int size) {
            return new Partida[size];
        }
    };
}

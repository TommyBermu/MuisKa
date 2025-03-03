package com.muiska.clases;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class Publicacion implements Comparable<Publicacion> {
    private int pubId;
    private String titulo, descripcion;
    private byte[] link_imagen;
    private String fecha_publicacion, fecha_finalizacion;
    private boolean tipo;

    public Publicacion() {
    }

    public Publicacion(int pubId, String titulo, byte[] link_imagen, String descripcion, String fecha_finalizacion, String fecha_publicacion, boolean tipo) {
        this.pubId = pubId;
        this.titulo = titulo;
        this.link_imagen = link_imagen;
        this.descripcion = descripcion;
        this.fecha_finalizacion = fecha_finalizacion;
        this.fecha_publicacion = fecha_publicacion;
        this.tipo = tipo;
    }

    public int getPubId() {
        return pubId;
    }

    public boolean getTipo(){
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public byte[] getLink_imagen() {
        return link_imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha_finalizacion() {
        return fecha_finalizacion;
    }

    public String getFecha_publicacion() {
        return this.fecha_publicacion;
    }

    @Override
    public int compareTo(@NonNull Publicacion o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.US);
        try {
            if (Objects.requireNonNull(dateFormat.parse(this.getFecha_publicacion())).before(dateFormat.parse(o.getFecha_publicacion()))){
                return 1;
            }else if (Objects.requireNonNull(dateFormat.parse(this.getFecha_publicacion())).after(dateFormat.parse(o.getFecha_publicacion()))){
                return -1;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
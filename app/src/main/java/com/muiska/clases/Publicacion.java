package com.muiska.clases;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class Publicacion implements Comparable<Publicacion> {
    private String titulo, link_imagen, descripcion;
    private String fecha_publicacion, fecha_finalizacion;
    //private Tipo tipo; TODO implementar en el futuro

    public Publicacion() {
    }

    public Publicacion(String titulo, String link_imagen, String descripcion, String fecha_finalizacion, String fecha_publicacion) {
        this.titulo = titulo;
        this.link_imagen = link_imagen;
        this.descripcion = descripcion;
        this.fecha_finalizacion = fecha_finalizacion;
        this.fecha_publicacion = fecha_publicacion;
    }

    public enum Tipo {
        CONVOCATORIA,
        ANUNCIO
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink_imagen() {
        return link_imagen;
    }

    public void setLink_imagen(String link_imagen) {
        this.link_imagen = link_imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_finalizacion() {
        return fecha_finalizacion;
    }

    public void setFecha_finalizacion(String fecha_finalizacion) {
        this.fecha_finalizacion = fecha_finalizacion;
    }

    public String getFecha_publicacion() {
        return this.fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    @Override
    public int compareTo(@NonNull Publicacion o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
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
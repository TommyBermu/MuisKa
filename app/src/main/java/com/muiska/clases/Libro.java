package com.muiska.clases;

public class Libro {
    private String titulo, link_libro, descripcion_libro, imagen_portada;

    public Libro() {
        // Firebase lo necesita para poder deserializar
    }

    public Libro(String titulo, String link_libro, String descripcion_libro){
        this.titulo = titulo;
        this.link_libro = link_libro;
        this.descripcion_libro = descripcion_libro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink_libro() {
        return link_libro;
    }

    public void setLink_libro(String link_libro) {
        this.link_libro = link_libro;
    }

    public String getDescripcion_libro() {
        return descripcion_libro;
    }

    public void setDescripcion_libro(String descripcion_libro) {
        this.descripcion_libro = descripcion_libro;
    }

    public void getPDFname(){

    }
}


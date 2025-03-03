package com.muiska.clases;

public class Libro {
    private int idLibro, acceso;
    private String titulo, descripcion_libro, autor;
    private byte[] link_libro;

    public Libro() {
    }

    public Libro(int idLibro, int acceso, String titulo, byte[] link_libro, String descripcion_libro, String autor) {
        this.idLibro = idLibro;
        this.acceso = acceso;
        this.titulo = titulo;
        this.link_libro = link_libro;
        this.descripcion_libro = descripcion_libro;
        this.autor = autor;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public int getAcceso() {
        return acceso;
    }

    public String getTitulo() {
        return titulo;
    }

    public byte[] getLink_libro() {
        return link_libro;
    }

    public String getDescripcion_libro() {
        return descripcion_libro;
    }

    public String getAutor() {
        return autor;
    }
}


package com.muiska.clases;

public class Group {

    private int idGrupo, administradorUsuarioIdUsuario, miembros, acceso;
    private String nombre, descripcion;
    private byte[] linkPoster;

    public Group() {}

    public Group(int idGrupo, int administradorUsuarioIdUsuario, int miembros, int acceso, String nombre, String descripcion, byte[] linkPoster) {
        this.idGrupo = idGrupo;
        this.administradorUsuarioIdUsuario = administradorUsuarioIdUsuario;
        this.miembros = miembros;
        this.acceso = acceso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.linkPoster = linkPoster;
    }

    public Group(int idGrupo, String nombre) {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public int getAdministradorUsuarioIdUsuario() {
        return administradorUsuarioIdUsuario;
    }

    public int getMiembros() {
        return miembros;
    }

    public int getAcceso() {
        return acceso;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public byte[] getLinkPoster() {
        return linkPoster;
    }
}

package com.muiska.clases;

import java.util.Date;

public class PeticionIngresoConvocatoria {
    private int usuarioIdUsuario;
    private int convocatoriaPublicacionIdPublicacion;
    private int Peticion_idPeticion;
    private String nombre, apellidos, email;
    private Date fecha;
    private String cartaMotivacion;
    private String convNombre;

    public PeticionIngresoConvocatoria(int usuarioIdUsuario, String nombre, String apellidos, String email, Date fecha, String cartaMotivacion, int convocatoriaPublicacionIdPublicacion, String convNombre, int Peticion_idPeticion) {
        this.usuarioIdUsuario = usuarioIdUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fecha = fecha;
        this.cartaMotivacion = cartaMotivacion;
        this.convocatoriaPublicacionIdPublicacion = convocatoriaPublicacionIdPublicacion;
        this.convNombre = convNombre;
        this.Peticion_idPeticion = Peticion_idPeticion;
    }

    public int getPeticion_idPeticion() {
        return Peticion_idPeticion;
    }

    public PeticionIngresoConvocatoria(String convNombre, int convocatoriaPublicacionIdPublicacion){
        this.convNombre = convNombre;
        this.convocatoriaPublicacionIdPublicacion = convocatoriaPublicacionIdPublicacion;
    }

    public int getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getCartaMotivacion() {
        return cartaMotivacion;
    }

    public int getConvocatoriaPublicacionIdPublicacion() {
        return convocatoriaPublicacionIdPublicacion;
    }

    public String getConvNombre() {
        return convNombre;
    }
}

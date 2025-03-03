package com.muiska.clases;

public class FolderChange {
    public String name, email, document_url, letter_url, ref, carpeta;

    public FolderChange() {
    }

    public FolderChange(String name, String email, String document_url, String letter_url, String ref, String carpeta) {  //Añadir tambien el nombre, apellido y correo
        this.name = name;
        this.email = email;
        this.document_url = document_url;
        this.letter_url = letter_url;
        this.ref = ref;
        this.carpeta = carpeta;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument_url() {
        return document_url;
    }

    public String getLetter_url() {
        return letter_url;
    }

    public String getRef() {
        return ref;
    }

    public String getCarpeta() {
        return carpeta;
    }

}

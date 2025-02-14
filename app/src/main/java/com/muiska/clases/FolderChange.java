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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument_url() {
        return document_url;
    }

    public void setDocument_url(String document_url) {
        this.document_url = document_url;
    }

    public String getLetter_url() {
        return letter_url;
    }

    public void setLetter_url(String letter_url) {
        this.letter_url = letter_url;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }
}

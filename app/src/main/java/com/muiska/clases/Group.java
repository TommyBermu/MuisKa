package com.muiska.clases;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {
    private String name, link_poster, description;
    private HashMap<String, String> miembros;

    public Group() {}

    public Group(String name, String link_poster, String description, HashMap<String, String> miembros) {
        this.name = name;
        this.link_poster = link_poster;
        this.description = description;
        this.miembros = miembros;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink_poster() {
        return link_poster;
    }

    public void setLink_poster(String link_poster) {
        this.link_poster = link_poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, String> getMiembros() {
        return miembros;
    }

    public void setMiembros(HashMap<String, String> miembros) {
        this.miembros = miembros;
    }
}

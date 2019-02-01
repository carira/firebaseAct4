package com.example.parsero.qtaccesodatos.model;

public class Usuario {


    private String claveUser;
    private String userName;
    private String nombre;
    private String apellidos;
    private String direccion;

    public Usuario() {
    }

    public Usuario(String claveUser,String userName, String nombre, String apellidos, String direccion) {

        this.claveUser = claveUser;
        this.userName = userName;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "userName='" + userName + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }

    public String getClaveUser() {
        return claveUser;
    }

    public void setClaveUser(String claveUser) {
        this.claveUser = claveUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

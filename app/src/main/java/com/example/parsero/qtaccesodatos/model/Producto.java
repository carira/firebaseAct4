package com.example.parsero.qtaccesodatos.model;

public class Producto {

    public String nombreUsuario;
    private String nombre;
    private String descripcion;
    private String categoría;
    private String precio;
    private String id;
    private String claveProducto;

    public Producto() {
    }


    public Producto(String nombreUsuario, String nombre, String descripcion, String categoría, String precio, String id, String claveProducto) {
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoría = categoría;
        this.precio = precio;
        this.id = id;
        this.claveProducto = claveProducto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoría() {
        return categoría;
    }

    public void setCategoría(String categoría) {
        this.categoría = categoría;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClaveProducto() {
        return claveProducto;
    }

    public void setClaveProducto(String claveProducto) {
        this.claveProducto = claveProducto;
    }


}

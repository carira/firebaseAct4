package com.example.parsero.qtaccesodatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class menuPrincipal extends AppCompatActivity implements View.OnClickListener {

    Button btnConsultarUsuarios, btnConsultaByNombre, btnEditarUsuario, btnLogout, btnAñadirProducto, btnEditarProductos,btnMisFavoritos;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        btnConsultarUsuarios = (Button) findViewById(R.id.btnConsultarUsuarios);
        btnConsultarUsuarios.setOnClickListener(this);

        btnEditarUsuario = (Button) findViewById(R.id.btnEditarUsuario);
        btnEditarUsuario.setOnClickListener(this);

        btnAñadirProducto = (Button) findViewById(R.id.btnAñadirProducto);
        btnAñadirProducto.setOnClickListener(this);

        btnConsultaByNombre = (Button) findViewById(R.id.btnConsultaByNombre);
        btnConsultaByNombre.setOnClickListener(this);


        btnEditarProductos = (Button) findViewById(R.id.btnEditarProcutos);
        btnEditarProductos.setOnClickListener(this);

        btnMisFavoritos = (Button) findViewById(R.id.btnMisFavoritos);
        btnMisFavoritos.setOnClickListener(this);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnEditarUsuario:
                Intent i = new Intent(menuPrincipal.this, modificarUsuario.class);
                startActivity(i);
                break;

            case R.id.btnLogout:
                logout();
                break;

            case R.id.btnConsultarUsuarios:
                Intent a = new Intent(menuPrincipal.this, consultaSimple.class);
                startActivity(a);
                break;

            case R.id.btnAñadirProducto:
                Intent c = new Intent(menuPrincipal.this, anyadirProducto.class);
                startActivity(c);
                break;

            case R.id.btnEditarProcutos:
                Intent o = new Intent(menuPrincipal.this, editarProducto.class);
                startActivity(o);
                break;

            case R.id.btnConsultaByNombre:
                Intent p = new Intent(menuPrincipal.this, productosUsuario.class);
                startActivity(p);
                break;

            case R.id.btnMisFavoritos:
                Intent x = new Intent(menuPrincipal.this, MisFavoritos.class);
                startActivity(x);
                break;
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Se ha deslogueado con exito", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(menuPrincipal.this, MainActivity.class);
        startActivity(i);
    }


}

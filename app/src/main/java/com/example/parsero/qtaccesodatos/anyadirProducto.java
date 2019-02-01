package com.example.parsero.qtaccesodatos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parsero.qtaccesodatos.model.Producto;
import com.example.parsero.qtaccesodatos.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class anyadirProducto extends AppCompatActivity {

    EditText etNombre, etDescripcion, etPrecio, edadesrecomendads;
    Button btnAñadirProducto;
    Spinner spCategorias;
    private FirebaseAuth mAuth;
    DatabaseReference bbdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_producto);


        etNombre = (EditText) findViewById(R.id.etNombre);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        edadesrecomendads = (EditText) findViewById(R.id.edadesrecomendads);
        https://accounts.google.com// /signin/v2/identifier?passive=1209600&osid=1&continue=https%3A%2F%2Fconsole.firebase.google.com%2F%3Fhl%3Des-419&followup=https%3A%2F%2Fconsole.firebase.google.com%2F%3Fhl%3Des-419&hl=es-419&flowName=GlifWebSignIn&flowEntry=ServiceLogin

        //CARGAR SPINNER DE CATEGORIAS:
        spCategorias = (Spinner) findViewById(R.id.spCategorias);
        cargarSpinner();

        //OBTENEMOS LA REFERENCIA DE LA BBDD.. -->
        bbdd = FirebaseDatabase.getInstance().getReference("productos");

        //AÑADIR PRODUCTO
        btnAñadirProducto = (Button) findViewById(R.id.btnAñadirProducto);
        btnAñadirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OBETENEMOS LA CLAVE DEL USUARIO LOGUEADO PARA AÑADIR EL PRODUCTO CON LA MISMA ID...-->
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String nombre = etNombre.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String categoria = spCategorias.getSelectedItem().toString();
                String precio = etPrecio.getText().toString();
                String id = user.getUid();
                String edadrec = edadesrecomendads.getText().toString();

                anyadirProducto(nombre, descripcion, categoria, precio, id, edadrec);

            }
        });
    }


    //MÉTODO PARA CARGAR EL SPNINNER CON NUESTRAS CATEGORÍAS..:
    public void cargarSpinner() {
        ArrayAdapter<String> adaptador;
        ArrayList<String> listadoCategorias = new ArrayList<String>();

        listadoCategorias.add("Coches");
        listadoCategorias.add("Tecnología");
        listadoCategorias.add("Hogar");

        adaptador = new ArrayAdapter<String>(anyadirProducto.this, android.R.layout.simple_list_item_1, listadoCategorias);
        spCategorias.setAdapter(adaptador);

    }

    //MÉTODO PARA ANYADIR PRODUCTOS QUE LLAMARÁ A COMPROBAR DUPLICADOS.. -->
    public void anyadirProducto(String nombre, String descripcion, String categoria, String precio, String id,String edadrec) {
        if (!TextUtils.isEmpty(nombre) || !TextUtils.isEmpty(descripcion) || !TextUtils.isEmpty(categoria) || !TextUtils.isEmpty(precio)) {


            //ANTES DE AÑADIR EL PRODUCTO COMPROBAMOS QUE NO ESTÉ AÑADIDO UNO CON EL MISMO NOmBRE:
            Query q = bbdd.orderByChild("nombre").equalTo(nombre);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int cont = 0;
                    for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                        cont++;
                    }
                    if (cont > 0) {
                        Toast.makeText(anyadirProducto.this, "El nombre de producto ya ha sido utilizado", Toast.LENGTH_SHORT).show();
                    } else {
                        añadirProducto();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(anyadirProducto.this, "No se ha podido añadir el producto " + nombre, Toast.LENGTH_SHORT).show();
        }

    }

    public void añadirProducto() {
        getName();
    }

    //SIEMPRE QUE SE CREE UN PRODUCTO SE LE AÑADIRÁ EL USERNAME QUE LO CREÓ PARA LA HORA DE FILTRAR PRODUCTOS POR USERNAME
    public void getName() {
        bbdd = FirebaseDatabase.getInstance().getReference("usuarios");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String claveUserActual = user.getUid();
        Query q = bbdd.orderByChild("claveUser").equalTo(claveUserActual);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Usuario u1 = datasnapshot.getValue(Usuario.class);
                    String userName = u1.getUserName();
                    executeTask(userName);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void executeTask(String userName) {
        bbdd = FirebaseDatabase.getInstance().getReference("productos");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String nombre = etNombre.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String categoria = spCategorias.getSelectedItem().toString();
        String precio = etPrecio.getText().toString();
        String id = user.getUid();
        String nombreUsuario = userName;


        String clave = bbdd.push().getKey();
        Producto p1 = new Producto(nombreUsuario,nombre, descripcion, categoria, precio, id, clave);
        bbdd.child(clave).setValue(p1);
        Toast.makeText(anyadirProducto.this, "Se ha añadido con exito e producto " + nombre + " con el id : " + id, Toast.LENGTH_SHORT).show();
    }
}

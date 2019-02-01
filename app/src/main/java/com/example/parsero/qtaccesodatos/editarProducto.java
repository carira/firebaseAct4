package com.example.parsero.qtaccesodatos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parsero.qtaccesodatos.model.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class editarProducto extends AppCompatActivity {

    EditText etNombre, etDescripcion, etPrecio, etClave;
    Button btnEliminar, btnEditar;
    ListView listadoProductos;
    DatabaseReference bbdd;
    private FirebaseAuth mAuth;
    Spinner spCategorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etClave = (EditText) findViewById(R.id.etClave);


        //CARGAMOS EL LIST VIEW DEPENDIENDO DEL USUARIO QUE ESTÁ LOGUEADO... -->
        listadoProductos = (ListView) findViewById(R.id.lvProductos);

        //CARGAR SPINNER DE CATEGORIAS:
        spCategorias = (Spinner) findViewById(R.id.spCategorias);
        cargarSpinner();

        //OBTENEMOS LA CLAVE DEL USUARIO LOGUEADO:
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getUid();
        Toast.makeText(this, "El id actual es: " + id, Toast.LENGTH_SHORT).show();

        //REFERENCIA A LA BBDD:
        bbdd = FirebaseDatabase.getInstance().getReference("productos");

        //CARGAMOS EL LIST VIEW DEPENDIENDO DEL USUARIO LOGUEADO:
        Query q = bbdd.orderByChild("id").equalTo(id);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adapter;
                ArrayList<String> listado = new ArrayList<>();

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                    Producto p1 = datasnapshot.getValue(Producto.class);

                    String clave = p1.getClaveProducto();
                    String id = p1.getId();
                    String nombre = p1.getNombre();
                    String descripcion = p1.getDescripcion();
                    String precio = p1.getPrecio();
                    String categoria = p1.getCategoría();

                    listado.add("Clave editar producto :" + clave);
                    listado.add("Id: " + id);
                    listado.add("Nombre: " + nombre);
                    listado.add("Descripcion: " + descripcion);
                    listado.add("Precio: " + precio);
                    listado.add("Categoría: " + categoria);
                    listado.add("--------------------------");
                }
                adapter = new ArrayAdapter<String>(editarProducto.this, android.R.layout.simple_list_item_1, listado);
                listadoProductos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //BTN PARA EDITAR PRODUCTO
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Sacamos la clave del producto para buscarlo en bbdd...-->
                String claveProducto = etClave.getText().toString();

                if (!TextUtils.isEmpty(claveProducto)) {
                    Query q = bbdd.orderByChild("claveProducto").equalTo(claveProducto);

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String nombre = etNombre.getText().toString();
                            String descripcion = etDescripcion.getText().toString();
                            String precio = etPrecio.getText().toString();
                            String categoria = spCategorias.getSelectedItem().toString();

                            for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                String clave = datasnapshot.getKey();

                                if (!TextUtils.isEmpty(nombre)) {
                                    bbdd.child(clave).child("nombre").setValue(nombre);
                                    Toast.makeText(editarProducto.this, "Nombre modificado", Toast.LENGTH_SHORT).show();
                                }
                                    if (!TextUtils.isEmpty(descripcion)) {
                                        bbdd.child(clave).child("descripcion").setValue(descripcion);
                                        Toast.makeText(editarProducto.this, "Descripcion modificada", Toast.LENGTH_SHORT).show();
                                    }
                                        if (!TextUtils.isEmpty(precio)) {
                                            bbdd.child(clave).child("precio").setValue(precio);
                                            Toast.makeText(editarProducto.this, "Precio modificado", Toast.LENGTH_SHORT).show();

                                }
                                if(categoria != "Null"){
                                    bbdd.child(clave).child("categoria").setValue(categoria);
                                    Toast.makeText(editarProducto.this, "Categoria modificada", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //BTN PARA ELIMINAR UN PRODUCTOO
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SE ELIMINA UN PRODUCTO POR SU CLAVE...-->
                final String claveProducto = etClave.getText().toString();

                if (!TextUtils.isEmpty(claveProducto)) {
                    Query q = bbdd.orderByChild("claveProducto").equalTo(claveProducto);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                                String clave = datasnapshot.getKey();
                                DatabaseReference ref = bbdd.child(clave);
                                ref.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(editarProducto.this, "El producto con clave: " + claveProducto + " ha sido borrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(editarProducto.this, "Esta clave no existe o ya ha sido borrada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //MÉTODO PARA COMPROBAR QUE EL ITEM QUE SE INTENTA ELIMNAR ES DE SU PROPIEDAD..


    //MÉTODO PARA CARGAR EL SPNINNER CON NUESTRAS CATEGORÍAS..:
    public void cargarSpinner() {
        ArrayAdapter<String> adaptador;
        ArrayList<String> listadoCategorias = new ArrayList<String>();
        listadoCategorias.add("Null");
        listadoCategorias.add("Coches");
        listadoCategorias.add("Tecnología");
        listadoCategorias.add("Hogar");

        adaptador = new ArrayAdapter<String>(editarProducto.this, android.R.layout.simple_list_item_1, listadoCategorias);
        spCategorias.setAdapter(adaptador);

    }


}

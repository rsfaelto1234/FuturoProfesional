package com.create.rsfae.futuroprofesional.Registro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.create.rsfae.futuroprofesional.Inicio_Activity;
import com.create.rsfae.futuroprofesional.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Registro_Activity extends AppCompatActivity {

    Button btn_registro;
    EditText txt_correo,txt_usuario,txt_clave;

    FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_);
        //Metodo para la orientación del telefono
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Inicializamos nuestras variables con los objetos
        Inicializar();
    }

    private void Inicializar(){
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txt_correo = findViewById(R.id.txt_correo);
        txt_usuario= findViewById(R.id.txt_usuario);
        txt_clave= findViewById(R.id.txt_clave);
        btn_registro = findViewById(R.id.btn_registro);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarUsuario();
            }
        });
    }

    private void RegistrarUsuario() {
        final String nombre = txt_usuario.getText().toString().trim();
        final String correo = txt_correo.getText().toString().trim();
        final String clave = txt_clave.getText().toString().trim();

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(clave)) {
            firebaseAuth.createUserWithEmailAndPassword(correo, clave)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Crear la entrada de usuario correspondiente en la base de datos
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                final FirebaseUser user = task.getResult().getUser();
                                map.put("ID", user.getUid());
                                map.put("Email", correo);
                                map.put("Contraseña",clave);
                                //map.put("Foto",foto);
                                map.put("Ultima_Conexion", Calendar.getInstance(Locale.ENGLISH).getTimeInMillis());
                                DatabaseReference userDbRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid());userDbRef.setValue(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Actualizar el nombre de usuario
                                                    final UserProfileChangeRequest updateRequest = new UserProfileChangeRequest.Builder().setDisplayName(nombre).build();
                                                    firebaseAuth.getCurrentUser().updateProfile(updateRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            startActivity(new Intent(Registro_Activity.this, Inicio_Activity.class));
                                                            finish();
                                                        }
                                                    });
                                                } else {
                                                    //user.delete();
                                                    Toast.makeText(Registro_Activity.this,"Could not add the user to the database", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else
                                Toast.makeText(Registro_Activity.this, "error registering user", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}



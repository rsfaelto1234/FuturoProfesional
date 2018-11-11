package com.create.rsfae.futuroprofesional.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.create.rsfae.futuroprofesional.Inicio_Activity;
import com.create.rsfae.futuroprofesional.R;
import com.create.rsfae.futuroprofesional.RecuperarCuenta.RecuperarCuenta_Activity;
import com.create.rsfae.futuroprofesional.Registro.Registro_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login_Activity extends AppCompatActivity {

    private TextView texto_crear_cuenta,text_olvide_contrasena;
    private Button   btn_iniciar_sesion;
    private MaterialEditText txt_usuario,txt_password;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Inicializar();

        Observador();

    }

    private void Inicializar(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        txt_usuario= findViewById(R.id.txt_usuario);
        txt_password= findViewById(R.id.txt_password);
        texto_crear_cuenta = findViewById(R.id.texto_crear_cuenta);
        text_olvide_contrasena = findViewById(R.id.text_olvide_contrasena);

        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IniciarSesion();
            }
        });

        texto_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Registro_Activity.class));
            }
        });

        text_olvide_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, RecuperarCuenta_Activity.class));
            }
        });
    }

    private void Observador(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // NO ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(), "No hay sesion activa!", Toast.LENGTH_SHORT).show();
                }else{
                    // ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(), "La sesion esta activa!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login_Activity.this, Inicio_Activity.class));
                    finish();
                }
            }
        };
    }

    private void IniciarSesion(){
        String email = txt_usuario.getText().toString()+"@hotmail.com".trim();
        final String password = txt_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Ingrese el correo electronico!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Ingrese su contraseña!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Iniciando Sesión...");
        dialog.setCancelable(false);
        dialog.show();

        //Autentificar al Usuario
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Intent intent = new Intent(Login_Activity.this, Inicio_Activity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Login_Activity.this, "Usuario y/o Contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

package com.create.rsfae.futuroprofesional.RecuperarCuenta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.create.rsfae.futuroprofesional.Login.Login_Activity;
import com.create.rsfae.futuroprofesional.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RecuperarCuenta_Activity extends AppCompatActivity {

    ImageView btn_atras;
    MaterialEditText txt_recuperar_correo;
    Button btn_recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_cuenta_);
        Inicializar();

    }
    public void Inicializar(){
        txt_recuperar_correo = findViewById(R.id.txt_recuperar_correo);
        btn_atras = findViewById(R.id.btn_atras);
        btn_recuperar = findViewById(R.id.btn_recuperar);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecuperarCuenta_Activity.this, Login_Activity.class));
                finish();
            }
        });
        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperarCuenta();
            }
        });
    }

    public void RecuperarCuenta(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String email = txt_recuperar_correo.getText().toString().trim();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Enviando...");
        dialog.setCancelable(false);
        dialog.show();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Restablecer las instrucciones de correo electrónico enviada al " + email, Toast.LENGTH_LONG).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), email + " No existe", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

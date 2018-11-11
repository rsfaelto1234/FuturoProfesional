package com.create.rsfae.futuroprofesional;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Arrays;

import com.create.rsfae.futuroprofesional.Login.Login_Activity;
import com.create.rsfae.futuroprofesional.fragment.CenteredTextFragment;
import com.create.rsfae.futuroprofesional.NavigationDrawer.DrawerAdapter;
import com.create.rsfae.futuroprofesional.NavigationDrawer.DrawerItem;
import com.create.rsfae.futuroprofesional.NavigationDrawer.SimpleItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class Inicio_Activity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int MENU_INICIO = 0;
    private static final int MENU_TEST = 1;
    private static final int MENU_UNIVERSIDADES = 2;
    private static final int MENU_RESULTADOS = 3;
    private static final int MENU_UBICACION = 4;
    private static final int MENU_NOSOTROS = 5;
    private static final int MENU_CERRAR_SESION = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    JzvdStd jzvdStd;

    Button btn_cerrar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MENU_INICIO).setChecked(true),
                createItemFor(MENU_TEST),
                createItemFor(MENU_UNIVERSIDADES),
                createItemFor(MENU_RESULTADOS),
                createItemFor(MENU_UBICACION),
                createItemFor(MENU_NOSOTROS),
                //new SpaceItem(10),
                createItemFor(MENU_CERRAR_SESION)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(MENU_INICIO);
        //Inicializamos nuestras variables
        Inicializar();
        //Metodo para saber si la sesión esta activa
        Observador();

        jzvdStd = (JzvdStd) findViewById(R.id.videoplayer);
        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , "Video Motivacional" , Jzvd.SCREEN_WINDOW_NORMAL);

        //jzvdStd.changeUrl("","","as","");
       // jzvdStd.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");

    }

    public void Inicializar(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion);
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });
    }

    public void Observador (){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // NO ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(), "No hay sesion activa!, Interface_Activity", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Inicio_Activity.this, Login_Activity.class);
                    startActivityForResult(intent, 0);
                    finish();
                }else{
                    // ESTA LOGUEADO
                    Toast.makeText(getApplicationContext(), "La sesion esta activa!, Interface_Activity", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void CerrarSesion() {
        mFirebaseAuth.signOut();
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

    @Override
    public void onItemSelected(int position) {
        if (position == MENU_CERRAR_SESION) {
            CerrarSesion();
            startActivity(new Intent(Inicio_Activity.this, Login_Activity.class));
            finish();
        }
        //Log.d("Posicion: ", ""+position);
        slidingRootNav.closeMenu();

        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);

        showFragment((CenteredTextFragment) selectedScreen);
    }

    private void showFragment(CenteredTextFragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}

package org.dev4u.hv.guia2_moviles;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText txtURL;
    private TextView lblEstado;
    private Button btnDescargar;
    private RadioButton rdbtn1;
    private RadioButton rdbtn2;
    private EditText txbNombre;
    private ProgressBar prbProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializar
        txtURL       = (EditText) findViewById(R.id.txtURL);
        lblEstado    = (TextView) findViewById(R.id.lblEstado);
        btnDescargar = (Button)   findViewById(R.id.btnDescargar);
        rdbtn1 = findViewById(R.id.rdbtnCambiar);
        rdbtn2 = findViewById(R.id.rdbtnNocambiar);
        txbNombre = findViewById(R.id.txbNombre);
        prbProgress = findViewById(R.id.prbProgreso);

        setTitle("MM14-I04-001 GUIA 2");

        if (rdbtn1.isChecked()){
            txbNombre.setVisibility(View.VISIBLE);
        }else{
            txbNombre.setVisibility(View.INVISIBLE);
        }

        //evento onClick
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RevisarFormato()){
                    new Descargar(
                            MainActivity.this,
                            lblEstado,
                            btnDescargar,
                            rdbtn1,
                            prbProgress,
                            txbNombre
                    ).execute(txtURL.getText().toString());
                }
            }
        });

        verifyStoragePermissions(this);

        rdbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    txbNombre.setVisibility(View.INVISIBLE);
                }else{
                    txbNombre.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    //esto es para activar perimiso de escritura y lectura en versiones de android 6 en adelante
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private Boolean RevisarFormato(){
        String Value;
        String Formato;

        if (txtURL.getText().toString().isEmpty()){
            Toast.makeText(this, "No ha proporcionado ningun enlace.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(rdbtn1.isChecked()){
            Value =  txbNombre.getText().toString();
            if (Value.contains(".")){
                Formato = Value.substring(Value.lastIndexOf(".") +1 , Value.length() );
            }else{
                Formato = "";
            }

            if (Formato.equals("jpg")){
                return true;
            }

            if (Formato.equals("png")){
                return true;
            }

            if (Formato.equals("jpeg")){
                return true;
            }

            if (Formato.equals("ico")){
                return true;
            }
        }else{
            return true;
        }


        Toast.makeText(this, "El formato no es correcto (Ej. imagen.jpg)", Toast.LENGTH_LONG).show();

        return false;
    }
}

package com.example.camaramenulateral.ui.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.camaramenulateral.BuildConfig;
import com.example.camaramenulateral.MainActivity;
import com.example.camaramenulateral.R;
import com.example.camaramenulateral.database.BaseDeDatos;
import com.example.camaramenulateral.databinding.FragmentGalleryBinding;
import com.example.camaramenulateral.mapper.UriMapper;
import com.example.camaramenulateral.model.Foto;
import com.example.camaramenulateral.model.Tag;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.getSystemService;

public class GalleryFragment extends Fragment implements LocationListener {

    private static final int CODIGO = 1234;
    private FragmentGalleryBinding binding;
    private ImageView imagen;
    private Button sacar,guardar;
    private EditText titulo,tag1,tag2,tag3;
    private ActivityResultLauncher launcher;
    private Uri imageUri = Uri.EMPTY;
    private String imagePath;
    private BaseDeDatos database;
    private UriMapper mapper = new UriMapper();
    private double latitud;
    private double longitud;
    private String proveedor;
    private LocationManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents(root);
        onCLick();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = BaseDeDatos.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        setLauncher();
        setLocationSettings();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initComponents(View view){
        imagen = view.findViewById(R.id.imagen);
        sacar = view.findViewById(R.id.capturar);
        guardar = view.findViewById(R.id.guardar);
        titulo = view.findViewById(R.id.titulo);
        tag1 = view.findViewById(R.id.tag1);
        tag2 = view.findViewById(R.id.tag2);
        tag3 = view.findViewById(R.id.tag3);
    }

    public void onCLick(){
        sacar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!=
                        PackageManager.PERMISSION_GRANTED){
                    String[] permissions = new String[1];
                    permissions[0] = Manifest.permission.CAMERA;
                    requestPermissions(permissions,CODIGO);
                }else{
                    openCamara();
                }
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarImagen();
            }
        });
    }

    private void openCamara(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = null;
        try{
            photo = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(photo!=null) {
            imageUri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                    BuildConfig.APPLICATION_ID+".provider",
                    photo);
            camera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            launcher.launch(camera);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CODIGO){
            openCamara();
        }else{
            toaster("permiso denegado papa");
        }
    }

    private void toaster(String texto){
        Toast.makeText(getActivity(), texto, Toast.LENGTH_SHORT).show();;
    }

    private void setLauncher(){
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                            Log.i("testeo","he llegado aqui");
                            /*Bundle bundle = result.getData().getExtras();
                            Bitmap foto = (Bitmap) bundle.get("data");*/
                            //imageUri = result.getData().getData();
                            imagen.setImageURI(imageUri);
                        }else{
                            Log.i("testeo","no he llegado");
                        }
                    }
                });
    }

    private File createImageFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = "foto_"+time;

        File directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File archivo = File.createTempFile(nombre,".jpg",directorio);

        imagePath = "file: "+archivo.getAbsolutePath();
        return archivo;
    }

    public void guardarImagen(){
        if (imageUri != null) {

            Foto foto = new Foto();
            foto.setTitulo(titulo.getText().toString());
            foto.setTag1(tag1.getText().toString());
            foto.setTag2(tag2.getText().toString());
            foto.setTag3(tag3.getText().toString());
            foto.setUri(mapper.UriToString(imageUri));
            foto.setAltitud(latitud);
            foto.setLongitud(longitud);

            database.fotoDao().insert(foto);
            insertarTag(foto.getTag1());
            insertarTag(foto.getTag2());
            insertarTag(foto.getTag3());

            toaster("imagen guardada correctamente");
        }
    }

    public void insertarTag(String tag){
        List<Tag> tags = database.tagDao().selectByTag(tag);

        if(tags.isEmpty()){
            Tag etiqueta = new Tag();
            etiqueta.setTag(tag);

            database.tagDao().insertTag(etiqueta);
        }
    }

    private void setLocationSettings(){
        manager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        proveedor = manager.getBestProvider(criteria, false);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitud = location.getLatitude();
        longitud = location.getLongitude();
    }
}
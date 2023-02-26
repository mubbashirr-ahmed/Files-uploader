package com.example.intern;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class MyDocuments extends AppCompatActivity {

    StorageReference ref;
    List<String> files,status;
    RecyclerView recview;
    ImageView btn_upload;
    myadapter adapter;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_documents);
        ref=FirebaseStorage.getInstance().getReference();

        files=new ArrayList<>();
        status=new ArrayList<>();

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        adapter=new myadapter(files,status);
        recview.setAdapter(adapter);

        btn_upload=(ImageView)findViewById(R.id.upload);
        back= findViewById(R.id.ubackbtn);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), C_login.class));

            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Please Select Multiple Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            if(data.getClipData()!=null)
            {
                for(int i=0; i<data.getClipData().getItemCount();i++)
                {
                    Uri fileuri=data.getClipData().getItemAt(i).getUri();
                    String filename=getfilenamefromuri(fileuri);
                    files.add(filename);
                    status.add("loading");
                    adapter.notifyDataSetChanged();

                    final int index=i;
                    StorageReference uploader=ref.child("/MyDocuments").child(filename);
                    uploader.putFile(fileuri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    status.remove(index);
                                    status.add(index,"done");
                                    adapter.notifyDataSetChanged();
                                }
                            });

                }

            }

        }
    }

    public String getfilenamefromuri(Uri filepath)
    {
        String up = null;
        if (filepath.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(filepath, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    up = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }
            catch (CursorIndexOutOfBoundsException ex)
            {
                ex.printStackTrace();
            }
            finally {
                cursor.close();
            }
        }
        if (up == null) {
            up = filepath.getPath();
            int cut = up.lastIndexOf('/');
            if (cut != -1) {
                up = up.substring(cut + 1);
            }
        }
        return up;
    }

}
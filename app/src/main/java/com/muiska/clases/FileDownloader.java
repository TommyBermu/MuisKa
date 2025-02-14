package com.muiska.clases;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class FileDownloader {
    private long downloadId;
    public static final int REQUEST_CODE = 101;

    public void downloadFile(Context context, String url, String fileName) {
        try {
            // Obtener el DownloadManager
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            if (downloadManager != null) {
                    // Verificar si la URL es válida
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(fileName);
                    request.setDescription("Descargando archivo desde Firebase Storage...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                // Encolar la descarga y obtener el ID
                downloadId = downloadManager.enqueue(request);
                Toast.makeText(context, "Descargando archivo...", Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                            if (id == downloadId) {
                                // La descarga se completó, abre el archivo
                                openFile(context, fileName);
                                context.unregisterReceiver(this); // Desregistrar el receiver
                            }
                        }
                    }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);
                }

            } else {
                Toast.makeText(context, "Error al obtener el gestor de descargas", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error inesperado: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openFile(Context context, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        if (file.exists()) {
            Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No hay ninguna aplicación para abrir archivos PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "El archivo no se encontró", Toast.LENGTH_SHORT).show();
        }
    }
}

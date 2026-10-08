package com.example.ejemploguias.ejemplos.camara

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.ejemploguias.ui.components.VersionDemo
import java.io.File

private const val PREFS_PERFIL = "perfil_foto"

/**
 * Pantalla de perfil de la Guía 13, Parte 4: ImagenInteligente al centro
 * y dos botones (galería con GetContent, cámara con TakePicture + FileProvider).
 * Si la acción se cancela, el resultado llega en false/null y no cambia nada.
 */
@Composable
fun PerfilScreen(
    version: VersionDemo,
    viewModel: PerfilViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imagen by viewModel.imagen.collectAsState()
    var uriCamaraPendiente by remember { mutableStateOf<Uri?>(null) }
    var archivoCamara by remember { mutableStateOf<File?>(null) }
    var avisoCamara by remember { mutableStateOf<String?>(null) }

    val lanzadorGaleria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            if (version == VersionDemo.MEJORADA) {
                val persistida = guardarEnInterno(context, uri) ?: uri
                guardarReferencia(context, persistida, "galeria")
                viewModel.onImagenDesdeGaleria(persistida)
            } else {
                viewModel.onImagenDesdeGaleria(uri)
            }
        }
    }

    val lanzadorCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            val uriTemporal = uriCamaraPendiente
            if (uriTemporal != null) {
                if (version == VersionDemo.MEJORADA) {
                    val persistida = archivoCamara?.let { guardarEnInterno(context, it) } ?: uriTemporal
                    guardarReferencia(context, persistida, "camara")
                    viewModel.onImagenDesdeCamara(persistida)
                } else {
                    viewModel.onImagenDesdeCamara(uriTemporal)
                }
            }
        }
    }

    fun lanzarCamara() {
        val carpeta = File(context.cacheDir, "images").apply { mkdirs() }
        val archivo = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
        archivoCamara = archivo
        uriCamaraPendiente = uri
        avisoCamara = null
        try {
            lanzadorCamara.launch(uri)
        } catch (e: Exception) {
            avisoCamara = "No hay una app de cámara disponible en este dispositivo."
        }
    }

    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            avisoCamara = null
            lanzarCamara()
        } else {
            avisoCamara = "Permiso de cámara denegado. Actívalo en Ajustes para capturar fotos."
        }
    }

    LaunchedEffect(version) {
        if (version == VersionDemo.MEJORADA && viewModel.imagen.value == null) {
            val guardada = leerReferencia(context)
            if (guardada != null) {
                val (uri, origen) = guardada
                if (origen == "camara") {
                    viewModel.onImagenDesdeCamara(uri)
                } else {
                    viewModel.onImagenDesdeGaleria(uri)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineMedium)

        ImagenInteligente(
            uri = imagen,
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = if (imagen == null) {
                "Aún no tienes foto de perfil"
            } else {
                "Foto de perfil activa"
            },
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = { lanzadorGaleria.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Elegir de galería")
        }

        Button(
            onClick = {
                if (context.checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    lanzarCamara()
                } else {
                    lanzadorPermiso.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar foto")
        }

        avisoCamara?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = when (version) {
                VersionDemo.GUIA -> "Versión de la guía: la foto vive en cacheDir (images/) y solo en el VM; al reiniciar la app se pierde."
                VersionDemo.MEJORADA -> "Versión mejorada: la foto se copia a filesDir (images/) con timestamp; la URI persiste en el VM y sobrevive a rotación y reinicio."
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/** Copia un archivo temporal (cámara) al almacenamiento interno y devuelve su Uri expuesta. */
private fun guardarEnInterno(context: Context, origen: File): Uri? {
    return try {
        val carpeta = File(context.filesDir, "images").apply { mkdirs() }
        val destino = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
        origen.copyTo(destino, overwrite = true)
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", destino)
    } catch (e: Exception) {
        null
    }
}

/** Copia el contenido de un Uri (galería) al almacenamiento interno y devuelve su Uri expuesta. */
private fun guardarEnInterno(context: Context, origen: Uri): Uri? {
    return try {
        val carpeta = File(context.filesDir, "images").apply { mkdirs() }
        val destino = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
        val entrada = context.contentResolver.openInputStream(origen)
        if (entrada == null) {
            null
        } else {
            entrada.use { flujo ->
                destino.outputStream().use { salida -> flujo.copyTo(salida) }
            }
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", destino)
        }
    } catch (e: Exception) {
        null
    }
}

/** Recuerda (en SharedPreferences) la última foto persistida para sobrevivir a reinicios. */
private fun guardarReferencia(context: Context, uri: Uri, origen: String) {
    context.getSharedPreferences(PREFS_PERFIL, Context.MODE_PRIVATE)
        .edit()
        .putString("uri", uri.toString())
        .putString("origen", origen)
        .apply()
}

private fun leerReferencia(context: Context): Pair<Uri, String>? {
    val prefs = context.getSharedPreferences(PREFS_PERFIL, Context.MODE_PRIVATE)
    val uri = prefs.getString("uri", null) ?: return null
    val origen = prefs.getString("origen", null) ?: return null
    return Uri.parse(uri) to origen
}

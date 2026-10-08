package com.example.ejemploguias.ejemplos.camara

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploguias.ui.components.BloqueExplicacion
import com.example.ejemploguias.ui.components.PantallaExplicacion
import com.example.ejemploguias.ui.components.SelectorVersion
import com.example.ejemploguias.ui.components.VersionDemo

/** Demo en vivo de la Guía 13 con el switch "Versión de la guía | Versión mejorada". */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamaraDemoScreen(onAbrirExplicacion: () -> Unit) {
    var version by rememberSaveable { mutableStateOf(VersionDemo.GUIA) }
    val perfilViewModel: PerfilViewModel = viewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guía 13 · Cámara y galería") },
                actions = {
                    IconButton(onClick = onAbrirExplicacion) {
                        Icon(Icons.Default.Info, contentDescription = "¿Cómo se hizo?")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SelectorVersion(
                version = version,
                onCambiar = { version = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()
            PerfilScreen(
                version = version,
                viewModel = perfilViewModel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CamaraExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "Guía 13 · ¿Cómo se hizo?",
        bloques = bloquesCamara,
        onVolver = onVolver
    )
}

private val bloquesCamara = listOf(
    BloqueExplicacion(
        titulo = "Parte 1 · Permisos y FileProvider (Guía 13, Parte 1, pág. 2)",
        parrafos = listOf(
            "La guía pide declarar los permisos en AndroidManifest.xml y configurar el almacenamiento temporal con FileProvider. En esta demo el permiso CAMERA y el FileProvider ya vienen en el manifiesto del proyecto (no se edita).",
            "file_paths.xml expone dos rutas: cache-path images/ para la foto temporal de la guía y files-path images/ para la foto persistida de la mejora.",
            "La autoridad del provider es \${applicationId}.fileprovider; la app construye la Uri con FileProvider.getUriForFile(...) antes de lanzar la cámara. Como la app corre en API 33+, además se pide el permiso CAMERA en tiempo de ejecución."
        ),
        codigo = """
            <uses-permission android:name="android.permission.CAMERA" />

            <provider
                android:name="androidx.core.content.FileProvider"
                android:authorities="${'$'}{applicationId}.fileprovider"
                android:exported="false"
                android:grantUriPermissions="true">
                <meta-data
                    android:name="android.support.FILE_PROVIDER_PATHS"
                    android:resource="@xml/file_paths" />
            </provider>
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 2 · ViewModel de perfil (Guía 13, Parte 2, pág. 2)",
        parrafos = listOf(
            "PerfilViewModel guarda la Uri? de la foto en MutableStateFlow y la expone como StateFlow; onImagenDesdeGaleria(uri) y onImagenDesdeCamara(uri) son las dos funciones que la actualizan.",
            "PerfilScreen la observa con collectAsState(), así que la interfaz se repinta sola cada vez que cambia el estado, sin manualidades."
        ),
        codigo = """
            class PerfilViewModel : ViewModel() {
                private val _imagen = MutableStateFlow<Uri?>(null)
                val imagen: StateFlow<Uri?> = _imagen

                fun onImagenDesdeGaleria(uri: Uri?) { _imagen.value = uri }
                fun onImagenDesdeCamara(uri: Uri?) { _imagen.value = uri }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 3 · Componente ImagenInteligente (Guía 13, Parte 3, pág. 2)",
        parrafos = listOf(
            "ImagenInteligente.kt es el componente reutilizable que pide la guía: muestra la foto en forma circular con Modifier.clip(CircleShape) y, si no hay imagen, un ícono de persona sobre un círculo de color primaryContainer.",
            "Sin librerías externas (no hay Coil en el proyecto): la foto se decodifica con ImageDecoder desde el ContentResolver y se memoriza con remember(uri). Si el Uri ya no existe, se cae al ícono por defecto en vez de crashear."
        ),
        codigo = """
            @Composable
            fun ImagenInteligente(uri: Uri?, modifier: Modifier = Modifier) {
                val bitmap = remember(uri) {
                    if (uri == null) null
                    else ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(context.contentResolver, uri)
                    )
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = modifier.aspectRatio(1f).clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = modifier.aspectRatio(1f)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil por defecto",
                            modifier = Modifier.fillMaxSize(0.45f)
                        )
                    }
                }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 4 · PerfilScreen con launchers (Guía 13, Parte 4, pág. 2)",
        parrafos = listOf(
            "PerfilScreen muestra ImagenInteligente centrada y dos botones: \"Elegir de galería\" y \"Tomar foto\", ambos con rememberLauncherForActivityResult (párrafo 4 de la guía).",
            "Galería: ActivityResultContracts.GetContent() lanzado con el tipo \"image/*\". Cámara: ActivityResultContracts.TakePicture(); antes de lanzar se crea el archivo temporal en cacheDir/images, se expone con FileProvider y esa Uri se pasa al launcher.",
            "El permiso CAMERA está declarado en el manifiesto, así que se solicita en tiempo de ejecución con RequestPermission() antes de TakePicture()."
        ),
        codigo = """
            val lanzadorCamara = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicture()
            ) { exito ->
                if (exito) { // false = canceló: no se cambia nada (Parte 5)
                    viewModel.onImagenDesdeCamara(uriCamara)
                }
            }

            fun lanzarCamara() {
                val carpeta = File(context.cacheDir, "images").apply { mkdirs() }
                val archivo = File(carpeta, "foto_${'$'}{System.currentTimeMillis()}.jpg")
                val uri = FileProvider.getUriForFile(
                    context,
                    "${'$'}{context.packageName}.fileprovider",
                    archivo
                )
                lanzadorCamara.launch(uri)
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 5 · Prueba en dispositivo físico (Guía 13, Parte 5, pág. 2-3)",
        parrafos = listOf(
            "La guía exige ejecutar la app en un teléfono real: primero probar la galería, luego la cámara, confirmando que la foto se muestra correctamente.",
            "Y lo más importante: verificar que no crashee si se cancela la acción. Aquí se cumple porque un resultado en false (TakePicture) o null (GetContent) simplemente no toca el ViewModel.",
            "En la galería (GetContent) sí puedes probar en emulador; la cámara se valida en el dispositivo físico, como pide la guía."
        ),
        codigo = """
            val lanzadorGaleria = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                if (uri != null) { // cancelar devuelve null: el estado no cambia
                    viewModel.onImagenDesdeGaleria(uri)
                }
            }

            lanzadorGaleria.launch("image/*")
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 6 · Evidencia de trabajo colaborativo (Guía 13, Parte 6, pág. 3)",
        parrafos = listOf(
            "Commit con el mensaje \"Funcionalidad cámara y galería integrada\" en la rama feature/imagen-inteligente.",
            "Dos capturas (una desde galería y otra desde cámara) y la tarjeta de Trello \"Cámara y galería\" con su checklist: ViewModel, ImagenInteligente, galería, cámara, test en físico, GitHub y evidencia."
        )
    ),
    BloqueExplicacion(
        titulo = "Mejora recomendada: persistir la foto en filesDir",
        parrafos = listOf(
            "La guía deja la foto en cacheDir y su Uri solo en el VM: el sistema puede vaciar la cache y, al reiniciar la app, el VM vuelve a null, así que la foto \"desaparece\".",
            "La versión mejorada copia la foto a filesDir/images/foto_<timestamp>.jpg, guarda en el VM la Uri persistente y la recuerda en SharedPreferences. Así sobrevive a rotación y reinicio de la app: cambia el switch arriba y captura en cada versión para comparar.",
            "Las dos rutas (cache y files) ya están declaradas en file_paths.xml, así que ambas Uris se exponen con el mismo FileProvider."
        ),
        codigo = """
            private fun guardarEnInterno(context: Context, origen: Uri): Uri {
                val carpeta = File(context.filesDir, "images").apply { mkdirs() }
                val destino = File(carpeta, "foto_${'$'}{System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(origen)!!.use { entrada ->
                    destino.outputStream().use { salida -> entrada.copyTo(salida) }
                }
                return FileProvider.getUriForFile(
                    context,
                    "${'$'}{context.packageName}.fileprovider",
                    destino
                )
            }
        """
    )
)

package com.starkindustries.camerax.Frontend.Screens

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private suspend fun Context.getCameraProvider():ProcessCameraProvider = suspendCoroutine { continuation ->
    var cameraProviderFeature = ProcessCameraProvider.getInstance(this)
    cameraProviderFeature.addListener({
        continuation.resume(cameraProviderFeature.get())
    }
    ,ContextCompat.getMainExecutor(this))
}

@Composable
fun PermissionsComposible() {
    var permissions = listOf(android.Manifest.permission.CAMERA)
    var permissionGranted by remember {
        mutableStateOf(false)
    }
    var launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()
    , onResult = { permission->
        permissionGranted = permission[android.Manifest.permission.CAMERA]==true
        })

    if(permissionGranted)
        CameraScreen()
    else{
        Box(modifier = Modifier
            .fillMaxSize()
        , contentAlignment = Alignment.Center){
            Button(onClick = { launcher.launch(permissions.toTypedArray()) }) {
                Text(text = "Permissions"
                , fontSize = 20.sp
                , fontWeight = FontWeight.W500)
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CameraScreen(){
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val previewView:PreviewView = remember {
        PreviewView(context)
    }
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val preview = androidx.camera.core.Preview.Builder().build()
    val imageSelector = remember{
        ImageCapture.Builder().build()
    }

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner = lifeCycleOwner
            ,cameraSelector=cameraSelector
            ,preview
            ,imageSelector
        )
        preview.setSurfaceProvider(previewView.surfaceProvider )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
        , contentAlignment = Alignment.BottomCenter
    ){
        AndroidView(factory = { previewView }
            , modifier = Modifier
            .fillMaxSize())

        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(16.dp)
        , contentAlignment = Alignment.Center){
            IconButton(onClick = {
                captureImage(imageCapture = imageSelector,context=context)
            }

            , modifier = Modifier
                    .size(50.dp)
                    .background(Color.White, CircleShape)
                    .padding(8.dp)
                    .background(Color.Red, CircleShape)) {

            }
        }
    }
}

private fun captureImage(imageCapture:ImageCapture,context: Context){

    val name = "MyImage_${System.currentTimeMillis()}.jpg"

    var contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME,name)
        put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures/My-Gallery")
    }

    var outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    imageCapture.takePicture(
        outputOptions
    ,ContextCompat.getMainExecutor(context)
        ,object:ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(p0: ImageCapture.OutputFileResults?) {
                Toast.makeText(context, "Image Saved Successfully!!", Toast.LENGTH_SHORT).show()
            }

            override fun onError(p0: ImageCaptureException) {
                Toast.makeText(context, "Failed to Save Image!!,${p0.message.toString()}", Toast.LENGTH_SHORT).show()
            }

        })


}



//@Composable
//fun PermissionHanding(){
//
//    var permissionList = listOf(android.Manifest.permission.CAMERA)
//
//    var permissionGranted by remember {
//        mutableStateOf(false)
//    }
//
//    var launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permission->
//        permissionGranted = permission[android.Manifest.permission.CAMERA]==true
//    }
//
//    if(permissionGranted)
//        TestComposible()
//    else{
//        Box(modifier = Modifier
//            .fillMaxSize()
//        , contentAlignment = Alignment.Center){
//            Button(onClick = { launcher.launch(permissionList.toTypedArray()) }) {
//                Text(text = "Permissions"
//                , fontSize = 20.sp
//                , fontWeight = FontWeight.W500
//                )
//            }
//        }
//    }
//
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun TestComposible(){
//
//    var context = LocalContext.current
//    var lifeCycleOwner = LocalLifecycleOwner.current
//    var previewView = remember {
//        PreviewView(context)
//    }
//    var preview:androidx.camera.core.Preview = androidx.camera.core.Preview.Builder().build()
//    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//    var imageCapture = remember{
//        ImageCapture.Builder().build()
//    }
//
//    LaunchedEffect(Unit) {
//        var cameraProvider = context.getCameraProvider()
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(
//            lifecycleOwner = lifeCycleOwner
//            , cameraSelector = cameraSelector
//            ,preview
//            ,imageCapture
//        )
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//    }
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//        , contentAlignment = Alignment.BottomCenter
//    ){
//        AndroidView(factory = { previewView }
//            , modifier = Modifier
//                .fillMaxSize())
//
//        Box(modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Black.copy(alpha = 0.7f))
//            .padding(16.dp)
//            , contentAlignment = Alignment.Center){
//            IconButton(onClick = {
//
//            }
//
//                , modifier = Modifier
//                    .size(50.dp)
//                    .background(Color.White, CircleShape)
//                    .padding(8.dp)
//                    .background(Color.Red, CircleShape)) {
//
//            }
//        }
//    }
//
//}


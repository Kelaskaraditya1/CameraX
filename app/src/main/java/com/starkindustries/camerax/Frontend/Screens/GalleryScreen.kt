package com.starkindustries.camerax.Frontend.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.starkindustries.camerax.R

@Composable
fun GalleryScreen(){
    var imageUri by remember{
        mutableStateOf<Uri?>(null)
    }

    var galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri->
        imageUri=uri
    }

    Box(modifier = Modifier
        .size(200.dp)){
        Image(painter = if(imageUri!=null)
            rememberAsyncImagePainter(model = imageUri)
        else
            painterResource(id = R.drawable.profile)
            , contentDescription = ""
        , modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        , contentScale = ContentScale.Crop)
        Box(modifier = Modifier
            .fillMaxSize()
            , contentAlignment = Alignment.BottomEnd){
            if(imageUri==null){
                Image(painter = painterResource(id = R.drawable.plus)
                    , contentDescription = ""
                    , modifier = Modifier
                        .size(40.dp)
                        .offset(y = -50.dp, x = -10.dp)
                        .clickable {
                            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        })
            }
        }
    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GalleryPreview(){
    GalleryScreen()
}
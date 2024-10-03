package com.example.dairyman.uiComponent

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dairyman.Data.Model.userdataModel.SignInState
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.DarkBackground
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.uiComponent.HomeScreen.ScreenA
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import kotlin.math.sin

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    navController: NavController,
    onSignOut: () -> Unit
){
    Scaffold (
        containerColor = Background,
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TopAppBarView(
            title = "Accounts",
            onBackNavClicked = { navController.navigate(ScreenA) }
        )}){
        Column {

        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = Primary)
        ){
        Column(modifier = Modifier
            .padding(it)
            .padding(24.dp, 0.dp)
            ) {
            Spacer(modifier = Modifier.height(8.dp))
        if(FirebaseAuth.getInstance().currentUser!=null){
           UserProfile( )
        }
        else{
            DefaultProfile()
        }
        }
        }
            Box (modifier = Modifier.padding(24.dp)
            ){

        SignInSignOutButton(onSignOut=onSignOut, onSignInClick = onSignInClick)
            }
        }
    }
    val context= LocalContext.current
    LaunchedEffect(key1 = state.signInError, block = {
        state.signInError?.let { error->
            Toast.makeText(context, error,Toast.LENGTH_LONG).show()
        }
    })


}



@Composable
fun UserProfile( ) {
        val user=FirebaseAuth.getInstance().currentUser
    Row (verticalAlignment = Alignment.CenterVertically){

        UserProfilePhoto()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
    Text(text =user?.displayName.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Background)
        Text(text =user?.email.toString() , color = Background)
    }
    }

}


@Composable
fun DefaultProfile(){
    UserProfilePhoto()
    }
@Composable
fun UserProfilePhoto() {
    if(FirebaseAuth.getInstance().currentUser?.photoUrl==null){
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
                imageVector = Icons.Default.AccountCircle,
                tint = Background,
                contentDescription = null)
    }else {
        AsyncImage(
            model = FirebaseAuth.getInstance().currentUser?.photoUrl,
            contentDescription = "Photo",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(50))

            ,
            contentScale = ContentScale.Crop
        )
    }

}
@Composable
fun SignInSignOutButton(onSignOut: () -> Unit,onSignInClick: () -> Unit){
    if(FirebaseAuth.getInstance().currentUser?.photoUrl==null){
        Box(modifier = Modifier
            .shadow(
                elevation = 3.dp,
                RoundedCornerShape(12.dp),
                clip =true,
                ambientColor = Color.Black,
                spotColor = Color.Black

            )
            .padding(1.dp, 1.dp, 1.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = DarkBackground)
            .padding(24.dp, 8.dp)
            .clickable { onSignInClick() },
            contentAlignment = Alignment.Center){
                Text(text = "SignIn")
        }
    }
    else{
        Box(modifier = Modifier
            .shadow(
                elevation = 3.dp,
                RoundedCornerShape(12.dp),
                clip =true,
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .padding(1.dp, 1.dp, 1.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = DarkBackground)
            .padding(24.dp, 8.dp)
            .clickable { onSignOut() },
            contentAlignment = Alignment.Center){
                Text(text = "Logout", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }

}


@Serializable
object ScreenD
package com.example.myapplicationjetpcomp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationjetpcomp.ui.theme.MyApplicationJetpCompTheme
import com.example.myapplicationjetpcomp.api.RetrofitClient
import com.example.myapplicationjetpcomp.api.authentication.AuthenticationApi
import com.example.myapplicationjetpcomp.api.authentication.AuthorisationRequest
import com.example.myapplicationjetpcomp.ui.activity.RegistrationActivity
import com.example.myapplicationjetpcomp.ui.activity.ResultActivity
import com.example.myapplicationjetpcomp.ui.other.DemoField
import com.example.myapplicationjetpcomp.ui.other.DemoPasswordField
import com.example.myapplicationjetpcomp.ui.other.DemoUsernameField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationJetpCompTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Login()

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Login() {


    var token = remember(){
        mutableStateOf("Ожидание токена")
    }

    /*
    Пример правильных данных
    "username": "user222",
  "email": "shtigun@yandex.ru",
  "password": "Shtigun1999M"

  "username": "testUser",
  "email": "testUser@yandex.ru",
  "password": "testUser123"
     */
    var username by remember {
        mutableStateOf("user222")
    }
    var password by remember {
        mutableStateOf("Shtigun1999M")
    }
    var errorMessage = remember {
        mutableStateOf("")
    }
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(35.dp)
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween){
        Text(
            text = "Token: "+ token.value,
            fontSize = 5.sp
        )
        LoginHeader()
        Spacer(modifier = Modifier.height(70.dp))
        LoginFields(username,
            password,
            errorMessage,
            onUsernameChange={
                username=it
            },
            onPasswordChange={
                password=it
            })

        val context = LocalContext.current
        LoginFooter(onSignInClick={
            if(username.equals("")||password.equals("")){
                errorMessage.value="Не все поля заполнены"
            }else{
                errorMessage.value=""
                Auth(token,username,password)
                /*val intent = Intent(context, ResultActivity::class.java)
                intent.putExtra("token", token.value)
                context.startActivity(intent)*/
            }
        },
            onSignUpClick={
                context.startActivity(Intent(context, RegistrationActivity::class.java))
            })
    }
}

@Composable
fun LoginHeader()
{
    Column {
        Text(text="Войдите в аккаунт",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LoginFields(username: String,
                password: String,
                errorMessage: MutableState<String>,
                onUsernameChange: (String) -> Unit,
                onPasswordChange: (String) -> Unit)
{


    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        DemoUsernameField(username, onUsernameChange)
        DemoPasswordField(password, onPasswordChange)
        Text(modifier = Modifier.padding(25.dp,10.dp),
            color= Color.Red,
            textAlign= TextAlign.Start,
            text = errorMessage.value)
    }
}

@Composable
fun LoginFooter(onSignInClick: () -> Unit,
                onSignUpClick: () -> Unit)
{
    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        Button(modifier = Modifier.size(210.dp,40.dp),
            onClick = onSignInClick) {
            Text(text = "Войти")
        }
        Spacer(modifier = Modifier.height(150.dp))
        Column(horizontalAlignment=Alignment.CenterHorizontally) {
            Text(text = "Ещё нет аккаунта?")
            TextButton(onClick = onSignUpClick) {
                Text(text = "Зарегистрироваться")
            }
        }
    }
}


fun Auth(tokenString: MutableState<String>,username: String, password: String)
{
    val client=RetrofitClient().getClient()
    val api=client.create(AuthenticationApi::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            tokenString.value =api.authorisation(
                AuthorisationRequest(username,password))
                .token
        }
        catch (e: Exception) {
            tokenString.value ="Ошибка подключения к api или введены неверные данные"
        }
    }
}






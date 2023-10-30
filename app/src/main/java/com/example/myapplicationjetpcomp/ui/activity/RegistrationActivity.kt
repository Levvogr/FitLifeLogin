package com.example.myapplicationjetpcomp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationjetpcomp.Auth
import com.example.myapplicationjetpcomp.LoginFields
import com.example.myapplicationjetpcomp.LoginFooter
import com.example.myapplicationjetpcomp.LoginHeader
import com.example.myapplicationjetpcomp.MainActivity
import com.example.myapplicationjetpcomp.api.RetrofitClient
import com.example.myapplicationjetpcomp.api.authentication.AuthenticationApi
import com.example.myapplicationjetpcomp.api.authentication.AuthorisationRequest
import com.example.myapplicationjetpcomp.api.authentication.RegistrationRequest
import com.example.myapplicationjetpcomp.ui.other.DemoEmailField
import com.example.myapplicationjetpcomp.ui.other.DemoPasswordField
import com.example.myapplicationjetpcomp.ui.other.DemoUsernameField
import com.example.myapplicationjetpcomp.ui.theme.MyApplicationJetpCompTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Registration()
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Registration() {
    MyApplicationJetpCompTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var token = remember(){
                mutableStateOf("Ожидание токена")
            }
            var username by remember {
                mutableStateOf("testUser")
            }
            var email by remember {
                mutableStateOf("testUser@yandex.ru")
            }
            var password by remember {
                mutableStateOf("testUser123")
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
                RegHeader()
                Spacer(modifier = Modifier.height(5.dp))
                RegFields(username,
                    email,
                    password,
                    errorMessage,
                    onUsernameChange={
                        username=it
                    },
                    onPasswordChange={
                        password=it
                    },
                    onEmailChange={
                        email=it
                    })

                val context = LocalContext.current
                RegFooter(onSignInClick={
                    if(username.equals("")||password.equals("")||email.equals("")){
                        errorMessage.value="Не все поля заполнены"
                    }else{
                        errorMessage.value=""
                        Reg(token,username,email,password)
                        /*val intent = Intent(context, ResultActivity::class.java)
                        intent.putExtra("token", token.value)
                        context.startActivity(intent)*/
                    }
                },
                    onSignUpClick={
                        context.startActivity(Intent(context, MainActivity::class.java))
                    })
            }
        }
    }
}
@Composable
fun RegHeader()
{
    Column {
        Text(text="Создайте аккаунт",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RegFields(username: String,
              email: String,
              password: String,
              errorMessage: MutableState<String>,
              onUsernameChange: (String) -> Unit,
              onPasswordChange: (String) -> Unit,
              onEmailChange: (String) -> Unit)
{


    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        DemoUsernameField(username, onUsernameChange)
        DemoEmailField(email, onEmailChange)
        DemoPasswordField(password, onPasswordChange)
        Text(modifier = Modifier.padding(25.dp,10.dp),
            color= Color.Red,
            textAlign= TextAlign.Start,
            text = errorMessage.value)
    }
}

@Composable
fun RegFooter(onSignInClick: () -> Unit,
                onSignUpClick: () -> Unit)
{
    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        Button(modifier = Modifier.size(210.dp,40.dp),
            onClick = onSignInClick) {
            Text(text = "Зарегистрироваться")
        }
        Spacer(modifier = Modifier.height(150.dp))
        Column(horizontalAlignment=Alignment.CenterHorizontally) {
            Text(text = "Уже есть аккаунт?")
            TextButton(onClick = onSignUpClick) {
                Text(text = "Войти")
            }
        }
    }
}

fun Reg(tokenString: MutableState<String>, username: String, email: String, password: String)
{
    val client= RetrofitClient().getClient()
    val api=client.create(AuthenticationApi::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            tokenString.value =api.registration(
                RegistrationRequest(username,email,password))
                .token
        }
        catch (e: Exception) {
            tokenString.value ="Ошибка подключения к api или введены неверные данные"
        }
    }
}
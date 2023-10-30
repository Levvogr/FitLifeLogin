package com.example.myapplicationjetpcomp.ui.other

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplicationjetpcomp.R
import com.example.myapplicationjetpcomp.api.RetrofitClient
import com.example.myapplicationjetpcomp.api.authentication.AuthenticationApi
import com.example.myapplicationjetpcomp.api.authentication.AuthorisationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoField(value:String,
              onValueChange: (String) -> Unit,
              label:String,
              placeholder: String,
              visualTransformation: VisualTransformation=VisualTransformation.None,
              keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
              leadingIcon: @Composable (()->Unit)?=null,
              trailingIcon: @Composable (()->Unit)?=null){

    OutlinedTextField(
        modifier = Modifier.size(305.dp,65.dp),
        value = value,
        onValueChange = onValueChange,
        label={
            Text(text=label)
        },
        placeholder={
            Text(text=placeholder)
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
        )
}

@Composable
fun DemoUsernameField(username: String,
                      onUsernameChange: (String) -> Unit){

    DemoField(value=username,
        onValueChange=onUsernameChange,
        label="Имя пользователя",
        placeholder="Введите имя пользователя",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next),
        leadingIcon = {
            Icon(Icons.Default.AccountCircle,"Username")
        }
    )
}

@Composable
fun DemoPasswordField(password: String,
                      onPasswordChange: (String) -> Unit) {

    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    DemoField(value=password,
        onValueChange=onPasswordChange,
        label="Пароль",
        placeholder="Введите пароль",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go),
        leadingIcon = {
            Icon(Icons.Default.Lock,"Password")
        },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(painter = if(passwordVisibility){
                    painterResource(id = R.drawable.baseline_visibility_24)
                }else{
                    painterResource(id = R.drawable.baseline_visibility_off_24)
                },
                    contentDescription = "visibility")
            }
        },
        visualTransformation = if(passwordVisibility){
            VisualTransformation.None
        }else{
            PasswordVisualTransformation()
        }
    )
}
@Composable
fun DemoEmailField(email: String,
                   onEmailChange: (String) -> Unit)
{

    DemoField(value=email,
        onValueChange=onEmailChange,
        label="e-mail",
        placeholder="Введите e-mail",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next),
        leadingIcon = {
            Icon(Icons.Default.Email,"Email")
        }
    )
}



package br.senai.sp.jandira.loginppdm.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import br.senai.sp.jandira.loginppdm.R
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import androidx.lifecycle.lifecycleScope
import br.senai.sp.jandira.loginppdm.service.RetrofitHelper
import br.senai.sp.jandira.loginppdm.service.UsuarioService
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(lifecycleCoroutineScope: LifecycleCoroutineScope) {
    var fotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    //Referencia para acesso e manipulação do cloud Storage e firestore
    lateinit var storageRef: StorageReference
    lateinit var fibaseFirestore: FirebaseFirestore
    storageRef = FirebaseStorage.getInstance().reference.child("images")
    fibaseFirestore = FirebaseFirestore.getInstance()

    var context = LocalContext.current

    //Criar o objeto que  abrirá a galeria e retornará a Uri da imagem selecionada
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            fotoUri = it
        }
    }
    Log.e("foto", "EditarFoto: ${fotoUri}")

    var painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(fotoUri).build()
    )


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        var emailState by remember {
            mutableStateOf("")
        }

        var senhaState by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier
                    .width(400.dp)
                    .height(210.dp)
            )


            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cadastro",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(41, 182, 246)
                )
                Text(
                    text = "Faça cadastro na Symbian!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(160, 156, 156)
                )

                Spacer(modifier = Modifier.height(45.dp))

                //FORM
                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .shadow(2.dp, CircleShape)
                                .background(Color.Gray)
                                .clickable {
                                    launcher.launch("image/*")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(38.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = emailState,
                        onValueChange = { emailState = it },
                        shape = RoundedCornerShape(16.dp),
//                        leadingIcon = {
//                            Icon(
//                                painter = painterResource(id = R.drawable.baseline_email_24),
//                                contentDescription = null,
//                                tint = Color(207, 6, 240)
//                            )
//                        },
                        label = {
                            Text(
                                text = "E-mail",
                                color = Color.DarkGray
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            cursorColor = Color(41, 182, 246)
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = senhaState,
                        onValueChange = { senhaState = it },
                        shape = RoundedCornerShape(16.dp),
                        label = {
                            Text(
                                text = "Senha",
                                color = Color.DarkGray

                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            cursorColor = Color(41, 182, 246)
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            onClick = {
                                uploadImage(imageUri = fotoUri!!, context = context, emailState, senhaState, lifecycleCoroutineScope)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(Color(41, 182, 246))
                        ) {
                            Row() {
                                Text(
                                    text = "Salvar",
                                    color = Color.White
                                )
                            }

                        }


                    }
                }
            }


        }
    }
}

private fun uploadImage(imageUri: Uri, context: Context, email: String, senha: String, lifecycleCoroutineScope: LifecycleCoroutineScope) {
    //Referencia para acesso e manipulação do cloud Storage e firestore
    lateinit var storageRef: StorageReference
    lateinit var fibaseFirestore: FirebaseFirestore
    storageRef = FirebaseStorage.getInstance().reference.child("images")
    fibaseFirestore = FirebaseFirestore.getInstance()

    storageRef = storageRef.child(System.currentTimeMillis().toString())

    lateinit var usuarioService: UsuarioService
    usuarioService = RetrofitHelper.getInstance().create(UsuarioService::class.java)

    imageUri?.let {
        storageRef.putFile(it).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                storageRef.downloadUrl.addOnSuccessListener { uri ->

                    val map = HashMap<String, Any>()
                    map["pic"] = uri.toString()

                    Log.i("uri", "$uri")

                    fibaseFirestore.collection("images").add(map)
                        .addOnCompleteListener { firestoreTask ->

                            if (firestoreTask.isSuccessful) {

                                lifecycleCoroutineScope.launch {
                                    val body = JsonObject().apply {
                                        addProperty("login", email)
                                        addProperty("senha", senha)
                                        addProperty("imagem", uri.toString())
                                    }

                                    val result = usuarioService.cadastrarUsuario(body)

                                    if(result.isSuccessful){
                                        Toast.makeText(
                                            context,
                                            "Usuario cadastrado com sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }else{
                                        Log.e("erro", "TelaCadastro: ${result.body()}", )
                                    }
                                }

                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao tentar realizar o upload.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }
                }

            } else {

                Toast.makeText(context, "Erro ao tentar realizar o upload.", Toast.LENGTH_SHORT).show()

            }

        }
    }}


//@Composable
//@Preview
//fun LoginScreenPreview() {
//    LoginScreen()
//}
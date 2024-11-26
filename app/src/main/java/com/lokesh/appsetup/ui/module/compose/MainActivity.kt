package com.lokesh.appsetup.ui.module.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lokesh.appsetup.R
import org.intellij.lang.annotations.JdkConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultPreview()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "preview")
@Composable
fun DefaultPreview() {
    val ctx = LocalContext.current
    MaterialTheme {
        ProductListScreen()
    }
}

@Composable
fun ProductListScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(55.dp),
                containerColor = colorResource(id = R.color.white),
                shape = CircleShape,
                content = {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_arrows_rotate),
                        contentDescription = null,
                        tint = colorResource(id = R.color.color_primary_variant))
                },
            )
        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = colorResource(id = R.color.header_light)
            ) {
                Column {
                    InfoToolBar("${stringResource(R.string.last_product)} ", false)
                    CategoryList()
                }
            }
        }
    )
}

@Composable
fun ProductRow(image: String, head: String, subHead: String) {
    Card(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(0),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.yacht_link_disable)
        )
    ) {
        Column {
            Image(
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Inside,
                painter = painterResource(id = R.drawable.ic_arrows_rotate),
                contentDescription = null)
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = head, style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp, color = colorResource(id = R.color.white))
                Text(text = subHead, fontSize = 14.sp, color = colorResource(id = R.color.white))
            }
        }
    }
}

@Composable
fun ProductDetailScreen(internet: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.header_light))
    ) {
        Column {
            Text(
                text = stringResource(if (!internet) R.string.offline else R.string.online),
                fontSize = 11.sp,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.color_primary_variant)
                    )
                    .padding(2.dp),
                textAlign = TextAlign.Center
            )
            Image(
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                contentScale = ContentScale.Inside,
                painter = painterResource(id = R.drawable.ic_arrows_rotate),
                contentDescription = null)
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "head", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp, color = colorResource(id = R.color.white))
                Text(text = "fkgkfndk", fontSize = 14.sp, color = colorResource(id = R.color.white))
            }
        }
    }
}

@Composable
fun InfoToolBar(lastItem: String, internet: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.color_primary_variant)
            )
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(if (!internet) R.string.offline else R.string.online), fontSize = 11.sp, color = colorResource(id = R.color.white))
        Text(text = lastItem, fontSize = 11.sp, color = colorResource(id = R.color.white))
    }
}

@Composable
fun CategoryListScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(55.dp),
                containerColor = colorResource(id = R.color.white),
                shape = CircleShape,
                content = {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_arrows_rotate),
                        contentDescription = null,
                        tint = colorResource(id = R.color.color_primary_variant))
                },
            )
        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = colorResource(id = R.color.header_light)
            ) {
                Column {
                    InfoToolBar("${stringResource(R.string.last_category)} ", false)
                    CategoryList()
                }
            }
        }
    )
}

@Composable
fun CategoryList() {
    val numbers = (0..20).toList()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(numbers) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Number")
                Text(text = "  $it",)
            }
        }
    }
}

@Composable
fun CategoryRow(str: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = colorResource(id = R.color.header_light)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = str, color = colorResource(id = R.color.white))
    }
}
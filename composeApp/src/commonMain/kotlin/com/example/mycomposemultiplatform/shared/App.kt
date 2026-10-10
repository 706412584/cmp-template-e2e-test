package com.example.mycomposemultiplatform.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 共享界面：三个平台（Android / Desktop / Web）跑的是同一份代码。
 *
 * 计数器是 Compose 跨平台的最小闭环——remember 状态、点击、重组。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var count by remember { mutableStateOf(0) }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Compose Multiplatform") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "点击了 $count 次",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Button(
                    onClick = { count++ },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("点我")
                }
            }
        }
    }
}

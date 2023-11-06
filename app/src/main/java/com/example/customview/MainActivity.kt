package com.example.customview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.customview.ui.theme.CustomViewTheme
import com.example.customview.views.FlowLayout
import com.example.customview.views.conversation

class MainActivity : ComponentActivity() {
    private lateinit var flowLayout: FlowLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        flowLayout = findViewById(R.id.flow_layout)
        for (i in 0 until 16) {
            val item = layoutInflater.inflate(R.layout.item_icon_layout, null, false)
            flowLayout.addView(item)
        }

//        setContent {
//            CustomViewTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    conversation(msgList = getListData())
//                }
//            }
//        }
    }
}

data class Message(
    var title: String,
    var subTitle: String
)

fun getListData(): List<Message> {
    val msgList = mutableListOf<Message>()
    for (i in 0..20) {
        msgList.add(Message("title", "I am subTitle"))
    }
    return msgList
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CustomViewTheme {
//        conversation(getListData())
//    }
//}
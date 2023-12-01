package com.example.customview

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDrawableLayout()
    }

    private fun initDrawableLayout() {
        setContentView(R.layout.layout_drawable_item)
        findViewById<TextView>(R.id.tv_content).apply {
            setOnClickListener {
                Toast.makeText(context, "点击了content", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<TextView>(R.id.tv_menu).apply {
            setOnClickListener {
                Toast.makeText(context, "点击了menu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFlowLayout() {
        setContentView(R.layout.layout_main)
        val flowLayout: FlowLayout = findViewById(R.id.flow_layout)
        for (i in 0 until 16) {
            val item = layoutInflater.inflate(R.layout.item_icon_layout, null, false)
            flowLayout.addView(item)
        }
    }

    private fun initComposeLayout() {
        setContent {
            CustomViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    conversation(msgList = getListData())
                }
            }
        }
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
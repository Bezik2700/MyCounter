package igor.second.mycounter

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import igor.second.mycounter.data.MainViewModel
import igor.second.mycounter.data.NameEntity
import igor.second.mycounter.ui.theme.MyCounterTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        super.onCreate(savedInstanceState)
        setContent {
            MyCounterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenForActivity()
                }
            }
        }
    }
}

@Composable
fun MainScreenForActivity(
    mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)
) {

    val itemList = mainViewModel.itemList.collectAsState(initial = emptyList())
    var showDialogInfo by rememberSaveable { mutableStateOf(false) }
    val counter by rememberSaveable { mutableIntStateOf(0) }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialogInfo = true }) {
                Icon(
                    Icons.Sharp.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
                    BottomAppBar (modifier = Modifier.fillMaxWidth()) {
                        Text(text = "$counter")
                    }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){ innerPadding ->
        Card (modifier = Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.primaryContainer)) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 64.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Sharp.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = {  },
                    modifier = Modifier.weight(0.5f)) {
                    Text(text = "22 april 2024")
                }
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Sharp.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Divider(color = Color.Blue, thickness = 1.dp)
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)) {
                items(itemList.value) { item ->
                    ListItem(item,
                        {
                            mainViewModel.nameEntity = it
                            mainViewModel.newPrice.value = it.price
                            mainViewModel.newText.value = it.name
                            mainViewModel.newCounter.value = it.counter
                        },
                        {
                            mainViewModel.deleteItem(it)
                        }
                    )
                }
            }
        }
        if (showDialogInfo){
            Dialog(onDismissRequest = { showDialogInfo = false }) {
                Card(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 64.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    TextField(
                        value = mainViewModel.newText.value,
                        onValueChange = {mainViewModel.newText.value = it},
                        label = {Text(text = "Enter your price...")},
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        maxLines = 1
                    )
                    TextField(
                        value = mainViewModel.newPrice.value,
                        onValueChange = {mainViewModel.newPrice.value = it},
                        label = {Text(text = "Enter your comments...")},
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.None
                        )
                    )
                    TextField(
                        value = mainViewModel.newCounter.value,
                        onValueChange = {mainViewModel.newCounter.value = it},
                        label = {Text(text = "Enter your ...")},
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        maxLines = 1
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            if (
                                mainViewModel.newText.value.isNotEmpty() &&
                                mainViewModel.newPrice.value.isNotEmpty()){
                                mainViewModel.insertItem()
                            }
                            showDialogInfo = false
                        }
                    ) {
                        Icon(
                            Icons.Sharp.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox() {
    val context = LocalContext.current
    val coffeeDrinks = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun test(){
    var date by remember { mutableStateOf("") }
    AndroidView(factory = { CalendarView(it) },
        update = { it.setOnDateChangeListener {
            calendarView: CalendarView, year, month, day ->
            date = "$day - $month - $year"
        }
    })
}

@Composable
fun ListItem(item: NameEntity,
             onClick: (NameEntity) -> Unit,
             onClickDelete: (NameEntity) -> Unit){
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, end = 16.dp, bottom = 5.dp)
        .clickable {
            onClick(item)
        }){
        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Column {
                Row {
                    Text(
                        text = item.name,
                        fontSize = 24.sp)
                    Spacer(modifier = Modifier.padding(start = 4.dp))
                    Text(
                        text = item.counter,
                        fontSize = 10.sp)
                }
                Text(
                    text = item.price,
                    fontSize = 16.sp)
            }
            IconButton(onClick = { onClickDelete (item) }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
        }
    }
    Divider(color = Color.Red, thickness = 1.dp)
}
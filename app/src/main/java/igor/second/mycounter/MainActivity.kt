package igor.second.mycounter

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import igor.second.mycounter.data.MainViewModel
import igor.second.mycounter.data.NameEntity
import igor.second.mycounter.ui.theme.MyCounterTheme
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            MyCounterTheme {
                MainScreenForActivity()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenForActivity(
    mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)
) {

    // Date information start
    val context = LocalContext.current
    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()

    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()
    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth.0${month + 1}.$year"
        }, year, month, day
    )
    // Date information end

    var currencyFrom = arrayOf("BY", "USD", "EUR", "RUB")
    var categoryFrom = arrayOf("SHOP", "CAFE", "BANK", "AUTO")

    val itemList = mainViewModel.itemList.collectAsState(initial = emptyList())
    var showDialogInfo by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Column(
                        modifier = Modifier
                            .padding(end = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            DropMenuActivity(
                                array = currencyFrom,
                                stringControl = mainViewModel.newCurrency
                            )
                            DropMenuActivity(
                                array = categoryFrom,
                                stringControl = mainViewModel.newCategory
                            )
                            Button(
                                shape = RectangleShape,
                                onClick = {
                                    datePickerDialog.show()
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),
                            ) {
                                Text(
                                    text = "onClick",
                                    modifier = Modifier
                                        .size(width = 50.dp, height = 30.dp)
                                        .padding(top = 5.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialogInfo = true }) {
                Icon(
                    Icons.Sharp.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    ) { innerPadding ->
        Card(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Divider(color = Color.Blue, thickness = 1.dp)
            Text(text = date.value)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                items(itemList.value) { item ->
                    ListItem(item,
                        {
                            mainViewModel.nameEntity = it
                            mainViewModel.newPrice.value = it.price
                            mainViewModel.newText.value = it.name
                            mainViewModel.newCurrency.value = it.nextCurrency
                            mainViewModel.newCategory.value = it.nextCategory
                            mainViewModel.newShowMyDialog.value = it.showMyDialog
                        },
                        {
                            mainViewModel.deleteItem(it)
                        }
                    )
                }
            }
        }
        if (showDialogInfo) {
            Dialog(onDismissRequest = { showDialogInfo = false }) {
                Card(
                    modifier = Modifier
                        .padding(innerPadding)
                        .clip(RoundedCornerShape(16.dp))
                        .border(width = 3.dp, color = Color.Blue, shape = RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Enter your purchase details")
                        TextField(
                            value = mainViewModel.newText.value,
                            onValueChange = { mainViewModel.newText.value = it },
                            label = { Text(text = "Enter your price...") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Words
                            ),
                            maxLines = 1,
                        )
                        TextField(
                            value = mainViewModel.newPrice.value,
                            onValueChange = { mainViewModel.newPrice.value = it },
                            label = { Text(text = "Enter your comments...") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.None
                            )
                        )
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                if (
                                    mainViewModel.newText.value.isNotEmpty() &&
                                    mainViewModel.newPrice.value.isNotEmpty()
                                ) {
                                    mainViewModel.insertItem()
                                    datePickerDialog.datePicker
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropMenuActivity(
    stringControl: MutableState<String>,
    array: Array<String>
) {
    
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .size(width = 120.dp, height = 50.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        OutlinedTextField(
            value = stringControl.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            textStyle = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .menuAnchor()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .size(120.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            array.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item)
                    },
                    onClick = {
                        stringControl.value = item
                        expanded = false
                    }
                )
            }
        }
    }
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
                }
                Text(
                    text = item.price,
                    fontSize = 16.sp)
                Text(
                    text = item.nextCurrency
                )
                Text(
                    text = item.nextCategory
                )
                
            }
            IconButton(onClick = { onClickDelete (item) }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
        }
    }
    Divider(color = Color.Red, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun MainScreenForActivityPreview() {

}
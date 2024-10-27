package com.umermahar.smbinolmialdistribution

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umermahar.smbinolmialdistribution.ui.theme.SMBinolmialDistributionTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SMBinolmialDistributionTheme {
                val state by viewModel.state.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            }
                        )
                    }
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        state = state,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    state: MainState,
    onEvent: (MainEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TextField for Pass Rate input (Decimal)
        OutlinedTextField(
            value = state.passRate,
            onValueChange = {
                onEvent(MainEvent.OnPassRateChange(percentage = it))
            },
            label = { Text("Enter Pass Rate (e.g., 70%)") },
            placeholder = { Text("Pass Rate") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for Subject Count input (Integer)
        OutlinedTextField(
            value = state.noOfSubjects,
            onValueChange = {
                onEvent(MainEvent.OnNoOfSubjectChange(num = it))
            },
            label = { Text("Enter Number of Subjects (2-10)") },
            placeholder = { Text("Number of Subjects") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if(state.probability != null) {
            Text(text = state.probability.toString())
        }

        state.errorRes?.let {
            Text(
                text = stringResource(id = state.errorRes),
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onEvent(MainEvent.OnCalculateButtonClick)
        }) {
            Text(text = stringResource(id = R.string.calculate))
        }
    }
}

@Composable
@Preview
fun PreviewMain() {
    MainContent(
        state = MainState()
    ) {

    }
}

package com.example.lunchtray.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchtray.db.DepositUiStateDBO
import com.example.lunchtray.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositListScreen(
    viewModel: DepositViewModel = viewModel(),

) {
    val deposits by viewModel.users.collectAsState(initial = emptyList())
    val numberedDeposits by viewModel.numberedDeposits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.deposit_list_field)) },
                actions = {
                    if (deposits.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearAllDeposits() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.deposit_list_delete_all)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (deposits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.deposit_list_nothing),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = numberedDeposits,
                    ) { (deposit, displayNumber) ->
                        DepositItem(
                            deposit = deposit,
                            displayNumber = displayNumber,
                            onDelete = {
                                viewModel.deleteDeposit(deposit)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DepositItem(
    deposit: DepositUiStateDBO,
    displayNumber: Int,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.deposit_list_cont, displayNumber),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.deposit_list_delete)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Основная информация
            DepositInfoRow(
                label = stringResource(R.string.deposit_params_field_1),
                value = deposit.initialDeposit.formatCurrency()
            )
            DepositInfoRow(
                label = stringResource(R.string.results_f_3_1),
                value = deposit.annualRate.formatPercent()
            )
            DepositInfoRow(
                label = stringResource(R.string.monthly_topup_field_1),
                value = deposit.monthlyTopUp.formatCurrency()
            )
            DepositInfoRow(
                label = stringResource(R.string.results_f_3_2) + " (мес.)",
                value = deposit.periodMonths.toInt().toString()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Результаты
            DepositInfoRow(
                label = stringResource(R.string.results_f_1_1),
                value = deposit.finalAmount.formatCurrency(),
                isHighlighted = true
            )
            DepositInfoRow(
                label = stringResource(R.string.results_f_1_2),
                value = deposit.totalInterest.formatCurrency(),
                isHighlighted = true
            )
        }
    }
}

@Composable
fun DepositInfoRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isHighlighted) MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ) else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (isHighlighted) MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ) else MaterialTheme.typography.bodyMedium
        )
    }
}

// Extension function для форматирования процентов
fun Double.formatPercent(): String {
    return "%.2f%%".format(this)
}
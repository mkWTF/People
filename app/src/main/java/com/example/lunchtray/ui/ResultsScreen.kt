/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lunchtray.R
import com.example.lunchtray.model.DepositUiState

@SuppressLint("DefaultLocale")
@Composable
fun ResultsScreen(
    depositUiState: DepositUiState,
    onRestartButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        // Раздел с итоговыми суммами
        Text(
            text = stringResource(R.string.results_f_1),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        ResultRow(
            label = stringResource(R.string.results_f_1_1),
            value = depositUiState.finalAmount.formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_1_2),
            value = depositUiState.totalInterest.formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_1_3),
            value = "${String.format("%.2f", depositUiState.profitPercentage)}%",
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)),
            thickness = dimensionResource(R.dimen.thickness_divider)
        )

        // Раздел с параметрами вклада
        Text(
            text = stringResource(R.string.deposit_params),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        ResultRow(
            label = stringResource(R.string.deposit_params_field_1),
            value = depositUiState.initialDeposit.formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.monthly_topup_field_1),
            value = depositUiState.monthlyTopUp.formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_2_3),
            value = (depositUiState.monthlyTopUp * depositUiState.periodMonths).formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_2_4),
            value = depositUiState.totalInvested.formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(
            thickness = dimensionResource(R.dimen.thickness_divider),
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
        )

        // Раздел с условиями
        Text(
            text = stringResource(R.string.results_f_3),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        ResultRow(
            label = stringResource(R.string.results_f_3_1),
            value = "${String.format("%.2f", depositUiState.annualRate)}%",
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_3_2),
            value = "${depositUiState.periodMonths} месяцев",
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_3_3),
            value = "${String.format("%.4f", depositUiState.annualRate / 12)}%",
            modifier = Modifier.fillMaxWidth()
        )

        // Статистика
        HorizontalDivider(
            thickness = dimensionResource(R.dimen.thickness_divider),
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
        )

        Text(
            text = stringResource(R.string.results_f_4),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        ResultRow(
            label = stringResource(R.string.results_f_4_1),
            value = if (depositUiState.profitPercentage > 0) stringResource(R.string.results_f_4_prof) else stringResource(R.string.results_f_4_nprof),
            modifier = Modifier.fillMaxWidth()
        )

        ResultRow(
            label = stringResource(R.string.results_f_4_2),
            value = (depositUiState.totalInterest / depositUiState.periodMonths).formatCurrency(),
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка возврата
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onRestartButtonClicked
            ) {
                Text(stringResource(R.string.results_new).uppercase())
            }
        }

        // Добавляем дополнительный отступ внизу для удобства скролла
        Text(
            text = "",
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

/**
 * Компонент для отображения строки результата
 */
@Composable
fun ResultRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun ResultsScreenPreview() {
    ResultsScreen(
        depositUiState = DepositUiState(),
        onRestartButtonClicked = {},
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    )
}
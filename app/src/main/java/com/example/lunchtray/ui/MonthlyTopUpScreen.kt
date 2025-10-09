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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lunchtray.R

/**
 * Экран для ввода ежемесячного пополнения и периода вклада
 * Заменяет собой SideDishMenuScreen
 */
@Composable
fun MonthlyTopUpScreen(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: (Double, Int) -> Unit, // monthlyTopUp, periodMonths
) {
    // Локальное состояние для текстовых полей
    var monthlyTopUp by rememberSaveable { mutableStateOf("") }
    var periodMonths by rememberSaveable { mutableStateOf("") }

    // Валидация формы - кнопка "Далее" активна только когда оба поля заполнены корректно
    val isFormValid = monthlyTopUp.isNotEmpty() &&
            periodMonths.isNotEmpty() &&
            monthlyTopUp.toDoubleOrNull() != null &&
            periodMonths.toIntOrNull() != null &&
            (periodMonths.toIntOrNull() ?: 0) > 0

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {

        // Поле для ежемесячного пополнения
        OutlinedTextField(
            monthlyTopUp, { newValue ->
                // Разрешаем только числа и точку для десятичных дробей
                if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                    monthlyTopUp = newValue
                }
            }, Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), label = { Text(stringResource(R.string.monthly_topup_field_1)) },
            placeholder = { Text(stringResource(R.string.monthly_topup_field_1_placeholder)) },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        // Поле для периода в месяцах
        OutlinedTextField(
            value = periodMonths,
            onValueChange = { newValue ->
                // Разрешаем только целые числа
                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                    periodMonths = newValue
                }
            },
            label = { Text(stringResource(R.string.monthly_topup_field_2)) },
            placeholder = { Text(stringResource(R.string.monthly_topup_field_2_placeholder)) },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Информационный текст
        Text(
            text = stringResource(R.string.monthly_topup_info),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Пример расчета
        if (isFormValid) {
            val topUp = monthlyTopUp.toDouble()
            val period = periodMonths.toInt()
            val totalTopUps = topUp * period

            Text(
                text = "За $period месяцев вы добавите: ${totalTopUps.formatCurrency()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Группа кнопок внизу экрана
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            // Кнопка "Отмена"
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onCancelButtonClicked
            ) {
                Text(stringResource(R.string.cancel).uppercase())
            }

            // Кнопка "Далее" - активна только при корректно заполненной форме
            Button(
                modifier = Modifier.weight(1f),
                enabled = isFormValid,
                onClick = {
                    // Безопасное преобразование, т.к. форма прошла валидацию
                    val topUp = monthlyTopUp.toDouble()
                    val period = periodMonths.toInt()
                    onNextButtonClicked(topUp, period)
                }
            ) {
                Text(stringResource(R.string.next).uppercase())
            }
        }

        // Добавляем дополнительный отступ внизу для удобства скролла
        Text(
            text = "",
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Preview
@Composable
fun MonthlyTopUpPreview(){
    MonthlyTopUpScreen(
        onNextButtonClicked = { _, _ -> },
        onCancelButtonClicked = {},
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    )
}
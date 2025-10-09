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
import androidx.compose.ui.unit.dp
import com.example.lunchtray.R

@Composable
fun DepositParamsScreen(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: (Double, Double) -> Unit,
) {
    // Локальное состояние для текстовых полей
    var initialDeposit by rememberSaveable { mutableStateOf("") }
    var annualRate by rememberSaveable { mutableStateOf("") }

    // Валидация формы - кнопка "Далее" активна только когда оба поля заполнены корректно
    val isFormValid = initialDeposit.isNotEmpty() &&
            annualRate.isNotEmpty() &&
            initialDeposit.toDoubleOrNull() != null &&
            annualRate.toDoubleOrNull() != null

    // Используем Column с verticalScroll
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Поле для ввода первоначального взноса
        OutlinedTextField(
            value = initialDeposit,
            onValueChange = { newValue ->
                // Разрешаем только числа и точку для десятичных дробей
                if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                    initialDeposit = newValue
                }
            },
            label = { Text(stringResource(R.string.deposit_params_field_1)) },
            placeholder = { Text(stringResource(R.string.deposit_params_field_1_placeholder)) },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Поле для ввода годовой процентной ставки
        OutlinedTextField(
            value = annualRate,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                    annualRate = newValue
                }
            },
            label = { Text(stringResource(R.string.deposit_params_field_2)) },
            placeholder = { Text(stringResource(R.string.deposit_params_field_2_placeholder)) },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Информационный текст
        Text(
            text = stringResource(R.string.deposit_params_info),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

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
                    val deposit = initialDeposit.toDouble()
                    val rate = annualRate.toDouble()
                    onNextButtonClicked(deposit, rate)
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
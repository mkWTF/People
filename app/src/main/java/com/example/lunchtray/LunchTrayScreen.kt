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
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.ResultsScreen
import com.example.lunchtray.ui.DepositViewModel
import com.example.lunchtray.ui.DepositParamsScreen
import com.example.lunchtray.ui.MonthlyTopUpScreen
import com.example.lunchtray.ui.StartOrderScreen
import com.example.lunchtray.ui.DepositListScreen

enum class DepositScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    DepositParams(title = R.string.deposit_params),
    MonthlyTopUp(title = R.string.monthly_topup),
    Results(title = R.string.results),
    DepositList(title = R.string.deposit_list)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = DepositScreen.valueOf(
        backStackEntry?.destination?.route ?: DepositScreen.Start.name
    )
    // Create ViewModel
    val viewModel: DepositViewModel = viewModel()

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // TODO: Navigation host
        NavHost(
            navController = navController,
            startDestination = DepositScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = DepositScreen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(DepositScreen.DepositParams.name)
                    },
                    onDBButtonClicked = {
                        navController.navigate(DepositScreen.DepositList.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = DepositScreen.DepositParams.name) {
                DepositParamsScreen(
                    onCancelButtonClicked = {
                        viewModel.resetDeposit()
                        navController.popBackStack(DepositScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = { initialDeposit, annualRate ->
                        viewModel.updateDepositParams(initialDeposit, annualRate)
                        navController.navigate(DepositScreen.MonthlyTopUp.name)
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
            composable(route = DepositScreen.MonthlyTopUp.name) {
                MonthlyTopUpScreen(
                    onCancelButtonClicked = {
                        viewModel.resetDeposit()
                        navController.popBackStack(DepositScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = { monthlyTopUp, periodMonths ->
                        viewModel.updateMonthlyTopUp(monthlyTopUp, periodMonths)
                        viewModel.calculateResults()
                        navController.navigate(DepositScreen.Results.name)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(route = DepositScreen.Results.name) {
                ResultsScreen(
                    depositUiState = uiState,
                    onRestartButtonClicked = {
                        viewModel.resetDeposit()
                        navController.popBackStack(DepositScreen.Start.name, inclusive = false)
                    },
                    onDBButtonClicked = {
                        viewModel.insertUser(uiState)
                        viewModel.resetDeposit()
                        navController.popBackStack(DepositScreen.Start.name, inclusive = false)
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(
                            start = dimensionResource(R.dimen.padding_medium),
                            end = dimensionResource(R.dimen.padding_medium),
                        )
                )
            }
            composable(route = DepositScreen.DepositList.name){
                DepositListScreen ()
            }
        }
    }
}

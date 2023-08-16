package com.neverdiesoul.stockapp.view.composable.navigation.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neverdiesoul.stockapp.R

private enum class IconToggleButtonIndex(val index: Int) {
    NONE(-1),APPOINTED(0), MARKET(1), RESERVATION(2)
}

private enum class DropdownMenuItemsForPossible(val resId: Int, val value: Int) {
    POSSIBLE(R.string.detail_possible, -1),
    MAXIMUM(R.string.detail_order_amount_dropdown_maximum, 0),
    PERCENT50(R.string.detail_order_amount_dropdown_50percent, 50),
    PERCENT25(R.string.detail_order_amount_dropdown_25percent, 25),
    PERCENT10(R.string.detail_order_amount_dropdown_10percent, 10)
}

private enum class DropdownMenuItemsForComparedToTheCurrentPrice(val resId: Int, val value: Int) {
    COMPARED_TO_THE_CURRENT_PRICE(R.string.detail_compared_to_the_current_price, -1),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_15PERCENT(R.string.detail_compared_to_the_current_price_dropdown_15percent,15),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_10PERCENT(R.string.detail_compared_to_the_current_price_dropdown_10percent,10),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_5PERCENT(R.string.detail_compared_to_the_current_price_dropdown_5percent,5),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_0PERCENT(R.string.detail_compared_to_the_current_price_dropdown_0percent,0),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_MINUS_5PERCENT(R.string.detail_compared_to_the_current_price_dropdown_minus_5percent,-5),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_MINUS_10PERCENT(R.string.detail_compared_to_the_current_price_dropdown_minus_10percent,-10),
    COMPARED_TO_THE_CURRENT_PRICE_DROPDOWN_MINUS_15PERCENT(R.string.detail_compared_to_the_current_price_dropdown_minus_15percent,-15)
}

@Composable
fun OrderBuyTabContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val screenWidth = (LocalConfiguration.current.screenWidthDp.dp)
    val density = LocalDensity.current
    var dropdownMenusWidthForPossible by remember {
        mutableStateOf(0)
    }
    var dropdownMenusWidthForComparedToTheCurrentPrice by remember {
        mutableStateOf(0)
    }
    var comparedToTheCurrentPriceTextViewWidth by remember {
        mutableStateOf(0)
    }

    var checkedIconToggleButtonIndex by remember {
        mutableStateOf(IconToggleButtonIndex.NONE.index)
    }

    var selectedDropdownMenuItemsForPossible by remember {
        mutableStateOf(DropdownMenuItemsForPossible.POSSIBLE)
    }

    var isDropdownMenuExpandedForPossible by remember {
        mutableStateOf(false)
    }

    var selectedDropdownMenuItemsForComparedToTheCurrentPrice by remember {
        mutableStateOf(DropdownMenuItemsForComparedToTheCurrentPrice.COMPARED_TO_THE_CURRENT_PRICE)
    }

    var isDropdownMenuExpandedForComparedToTheCurrentPrice by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight())
        {
            IconToggleButton(modifier = Modifier
                .fillMaxWidth()
                .weight(.3f, true), checked = checkedIconToggleButtonIndex == IconToggleButtonIndex.APPOINTED.index, onCheckedChange = { checkedIconToggleButtonIndex = IconToggleButtonIndex.APPOINTED.index }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(modifier = Modifier.align(Alignment.CenterVertically), painter = if (checkedIconToggleButtonIndex == IconToggleButtonIndex.APPOINTED.index) painterResource(id = R.drawable.baseline_check_circle_24) else painterResource(id = R.drawable.baseline_circle_24), contentDescription = stringResource(id = R.string.detail_buy_tab_appointed))
                    Text(text = stringResource(id = R.string.detail_buy_tab_appointed))
                }
            }

            IconToggleButton(modifier = Modifier
                .fillMaxWidth()
                .weight(.3f, true), checked = checkedIconToggleButtonIndex == IconToggleButtonIndex.MARKET.index, onCheckedChange = { checkedIconToggleButtonIndex = IconToggleButtonIndex.MARKET.index }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(modifier = Modifier.align(Alignment.CenterVertically), painter = if (checkedIconToggleButtonIndex == IconToggleButtonIndex.MARKET.index) painterResource(id = R.drawable.baseline_check_circle_24) else painterResource(id = R.drawable.baseline_circle_24), contentDescription = stringResource(id = R.string.detail_buy_tab_market))
                    Text(text = stringResource(id = R.string.detail_buy_tab_market))
                }
            }

            IconToggleButton(modifier = Modifier
                .fillMaxWidth()
                .weight(.3f, true), checked = checkedIconToggleButtonIndex == IconToggleButtonIndex.RESERVATION.index, onCheckedChange = { checkedIconToggleButtonIndex = IconToggleButtonIndex.RESERVATION.index }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(modifier = Modifier.align(Alignment.CenterVertically), painter = if (checkedIconToggleButtonIndex == IconToggleButtonIndex.RESERVATION.index) painterResource(id = R.drawable.baseline_check_circle_24) else painterResource(id = R.drawable.baseline_circle_24), contentDescription = stringResource(id = R.string.detail_buy_tab_reservation))
                    Text(text = stringResource(id = R.string.detail_buy_tab_reservation))
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 5.dp, end = 5.dp)) {
            Text(text = stringResource(id = R.string.detail_order_possible), modifier = Modifier.padding(start = 5.dp, end = 5.dp))
            Text(text = "0 KRW", modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp), style = TextStyle(textAlign = TextAlign.End, fontSize = 20.sp, fontWeight = FontWeight.Bold))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .weight(.65f, true)
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = Color(red = 221, green = 221, blue = 221),
                    shape = RectangleShape
                )) {
                Text(text = stringResource(id = R.string.detail_order_amount), modifier = Modifier.padding(start = 5.dp))
                Text(text = "0", modifier = Modifier
                    .weight(1.0f, true)
                    .padding(end = 5.dp), style = TextStyle(textAlign = TextAlign.End))
            }

            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .weight(.35f, true)
                .fillMaxHeight()
                .padding(start = 5.dp)
                .background(Color(238, 238, 238))
                .clickable {
                    isDropdownMenuExpandedForPossible = true
                })
            {
                Text(text = "${stringResource(id = selectedDropdownMenuItemsForPossible.resId)}  ▼")

                DropdownMenu(modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(red = 221, green = 221, blue = 221),
                        shape = RectangleShape
                    )
                    .onGloballyPositioned {
                        dropdownMenusWidthForPossible = with(density) {
                            it.size.width
                        }
                    },
                    expanded = isDropdownMenuExpandedForPossible, onDismissRequest = {
                        isDropdownMenuExpandedForPossible = false
                        Toast.makeText(context,"onDismissRequest",Toast.LENGTH_SHORT).show()
                    }
                ) {
                    enumValues<DropdownMenuItemsForPossible>().forEachIndexed { index, value ->
                        if (index == 0) return@forEachIndexed
                        val showingText = stringResource(id = value.resId)
                        DropdownMenuItem(modifier = Modifier.fillMaxWidth().height(33.dp), text = { Text(text = showingText) },
                            onClick = {
                                selectedDropdownMenuItemsForPossible = value
                                Toast.makeText(context,showingText,Toast.LENGTH_SHORT).show()
                                isDropdownMenuExpandedForPossible = false
                            })
                    }
                }
            }


        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .weight(.65f, true)
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = Color(red = 221, green = 221, blue = 221),
                    shape = RectangleShape
                )) {
                Text(text = stringResource(id = R.string.detail_order_price), modifier = Modifier.padding(start = 5.dp))
                Text(text = "0", modifier = Modifier
                    .weight(1.0f, true)
                    .padding(end = 5.dp), style = TextStyle(textAlign = TextAlign.End))
            }

            Row(modifier = Modifier
                .weight(.35f, true)
                .fillMaxHeight()
                .padding(start = 5.dp)) {
                TextButton(modifier = Modifier
                    .weight(.5f, true)
                    .fillMaxHeight(), shape = RectangleShape, onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(238,238,238), contentColor = Color.Black)) {
                    Text(text = "－", style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold))
                }

                TextButton(modifier = Modifier
                    .weight(.5f, true)
                    .padding(start = 5.dp)
                    .fillMaxHeight(), shape = RectangleShape, onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(238,238,238), contentColor = Color.Black)) {
                    Text(text = "＋", style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold))
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .weight(.65f, true)
                .fillMaxHeight()
                .background(Color(238, 238, 238))
                .clickable {
                    isDropdownMenuExpandedForComparedToTheCurrentPrice = true
                })
            {
                Text(text = "${stringResource(id = selectedDropdownMenuItemsForComparedToTheCurrentPrice.resId)}  ▼",
                modifier = Modifier.onGloballyPositioned {
                    comparedToTheCurrentPriceTextViewWidth = with(density) {
                        it.size.width
                    }
                })

                DropdownMenu(modifier = Modifier
                    .width(screenWidth.minus(comparedToTheCurrentPriceTextViewWidth.dp))
                    .wrapContentHeight()
                    .align(Alignment.BottomStart)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(red = 221, green = 221, blue = 221),
                        shape = RectangleShape
                    )
                    .onGloballyPositioned {
                        dropdownMenusWidthForComparedToTheCurrentPrice = with(density) {
                            it.size.width
                        }
                    },
                    expanded = isDropdownMenuExpandedForComparedToTheCurrentPrice, onDismissRequest = {
                        isDropdownMenuExpandedForComparedToTheCurrentPrice = false
                        Toast.makeText(context,"onDismissRequest",Toast.LENGTH_SHORT).show()
                    }
                ) {
                    enumValues<DropdownMenuItemsForComparedToTheCurrentPrice>().forEachIndexed { index, value ->
                        if (index == 0) return@forEachIndexed
                        val showingText = stringResource(id = value.resId)
                        DropdownMenuItem(modifier = Modifier.fillMaxWidth().height(33.dp), text = { Text(text = showingText) },
                            onClick = {
                                selectedDropdownMenuItemsForComparedToTheCurrentPrice = value
                                Toast.makeText(context,showingText,Toast.LENGTH_SHORT).show()
                                isDropdownMenuExpandedForComparedToTheCurrentPrice = false
                            })
                    }
                }
            }

            Spacer(modifier = Modifier.weight(.35f,true))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
            .border(
                width = 1.dp,
                color = Color(red = 221, green = 221, blue = 221),
                shape = RectangleShape
            )) {
            Text(text = stringResource(id = R.string.detail_total_order_price), modifier = Modifier.padding(start = 5.dp))
            Text(text = "0 KRW", modifier = Modifier
                .weight(1.0f, true)
                .padding(end = 5.dp), style = TextStyle(textAlign = TextAlign.End))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
            Button(modifier = Modifier.weight(.5f,true).fillMaxHeight(), shape = RectangleShape, colors = ButtonDefaults.buttonColors(containerColor = Color(119,119,119), contentColor = Color.White), onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.detail_reset))
            }
            Button(modifier = Modifier.weight(.5f, true).fillMaxHeight().padding(start = 5.dp), shape = RectangleShape, colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White), onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.detail_buy))
            }
        }
    }

    LaunchedEffect(Unit) {
        checkedIconToggleButtonIndex = IconToggleButtonIndex.APPOINTED.index
    }
}
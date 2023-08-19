package com.neverdiesoul.stockapp.view.composable.navigation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import com.neverdiesoul.stockapp.viewmodel.model.CoinOrderbookUnitForDetailView

enum class OrderModalBottomSheetType(val titleRedId: Int) {
    STYLE1(R.string.order_bottom_sheet_title1),STYLE2(R.string.order_bottom_sheet_title2)
}

private enum class OrderModalBottomSheetTabGroup(val resId: Int, val index: Int) {
    ORDER_AMOUNT(R.string.order_bottom_sheet_title1_tab1, 0),
    ORDER_TOTAL_PRICE(R.string.order_bottom_sheet_title1_tab2, 1)
}

/**
 * ModalBottomSheet을 사용하려 했으나, 하단 소프트키와 겹치는 현상이 있고, ModalBottomSheet 자체 버그(아직 실험단계의 컴포넌트이니까..)인 듯 하여 AnimatedVisibility를 활용하여 BottomSheet를 비슷하게 만들어 봤음..
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OrderBottomSheet(modifier: Modifier = Modifier, showBottomSheetKeyboard: Boolean, realTimeCoinCurrentPrice: CoinCurrentPriceForView, orderBookHogaPriceItemList: List<CoinOrderbookUnitForDetailView>, style: OrderModalBottomSheetType, onClick: ()->Unit, onDismiss: ()->Unit) {
    var selectedTabIndex by remember {
        mutableStateOf(OrderModalBottomSheetTabGroup.ORDER_AMOUNT.index)
    }
    AnimatedVisibility(modifier = Modifier.then(modifier), visible = showBottomSheetKeyboard, enter = fadeIn(), exit = fadeOut()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .animateEnterExit(enter = slideInVertically(initialOffsetY = { it / 2 }),
                exit = slideOutVertically(targetOffsetY = { it / 2 })
            ))
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween)
            {
                /**
                 * BottomSheet 타이틀
                 */
                Text(modifier = Modifier.padding(start = 10.dp),text = stringResource(id = OrderModalBottomSheetType.STYLE1.titleRedId), style = TextStyle(fontSize = 20.sp))
                /**
                 * X버튼(BottomSheet Close)
                 */
                Icon(modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable { onDismiss() }, painter = painterResource(id = R.drawable.baseline_close_24), contentDescription = "OrderModalBottomSheetCloseButton")
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(238, 238, 238)))
            var lazyColumnHeight by remember {
                mutableStateOf(0.dp)
            }
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Box(modifier = Modifier
                    .weight(.25f, true)
                    .height(lazyColumnHeight)) {
                    /**
                     * 호가 리스트
                     */
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        val onListItemClick = { coinOrderbookUnitForDetailView: CoinOrderbookUnitForDetailView->

                        }

                        items(orderBookHogaPriceItemList) {
                            OrderBookPriceItem(realTimeCoinCurrentPrice, it, onListItemClick, isOnlyOrderBookHogaItem = true)
                        }
                    }
                }
                Spacer(modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color(238, 238, 238)))
                val density = LocalDensity.current
                Column(modifier = Modifier
                    .weight(.75f, true)
                    .wrapContentHeight()
                    .onSizeChanged {
                        with(density) {
                            lazyColumnHeight = it.height.toDp()
                        }
                    }) {
                    /**
                     * 주문수량 / 주문총액 탭
                     */
                    TabRow(selectedTabIndex = selectedTabIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        tabs = {
                            enumValues<OrderModalBottomSheetTabGroup>().forEachIndexed { tabIndex, tabValue ->
                                Tab(modifier = Modifier.background(color = if (tabIndex == selectedTabIndex) Color.White else Color(red = 238, green = 238, blue = 238)),
                                    selectedContentColor = Color(red = 51, green = 51, blue = 51),
                                    unselectedContentColor = Color(red = 102, green = 102, blue = 102),
                                    selected = tabIndex == selectedTabIndex,
                                    onClick = {
                                        selectedTabIndex = tabIndex
                                    },
                                    content = { Text(text = stringResource(id = tabValue.resId), modifier = Modifier.padding(10.dp)) })
                            }
                        },
                        divider = {},
                        indicator = {}
                    )
                    Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween) {
                            /**
                             * 주문가격
                             */
                            Text(text = stringResource(id = R.string.order_bottom_sheet_order_price))
                            /**
                             * 주문가격(실제값)
                             */
                            Text(text = "")
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween) {
                            /**
                             * 주문수량
                             */
                            Text(text = stringResource(id = R.string.order_bottom_sheet_order_amount))
                            /**
                             * 주문수량(실제값)
                             */
                            Text(text = "")
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(238, 238, 238)))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween) {
                            /**
                             * 주문총액
                             */
                            Text(text = stringResource(id = R.string.order_bottom_sheet_order_total_price))
                            /**
                             * 주문총액(실제값)
                             */
                            Text(text = ""+"KRW", style = TextStyle(fontWeight = FontWeight.Bold))
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 5.dp,
                            color = Color(237, 239, 244),
                            shape = RectangleShape
                        )
                        .background(Color.White)) {
                        Text(text = "0 KRW", modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(end = 5.dp)
                            .align(Alignment.Center)
                            .background(Color.White), style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.End))
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(237, 239, 244))) {
                        /**
                         * +1000만, +100만, +10만, +1만
                         */
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_plus_ten_million), style = TextStyle(textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_plus_one_million), style = TextStyle(textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_plus_one_hundred_thousand), style = TextStyle(textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_plus_ten_thousand), style = TextStyle(textAlign = TextAlign.Center))
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        /**
                         * 1, 2, 3, 최대
                         */
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "1", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "2", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "3", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color(237, 239, 244))
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_maximum), style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        /**
                         * 4, 5, 6, 50%
                         */
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "4", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "5", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "6", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color(237, 239, 244))
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "50%", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        /**
                         * 7, 8, 9, 25%
                         */
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "7", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "8", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "9", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color(237, 239, 244))
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "25%", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        /**
                         * ., 0, BackSpace, 10%
                         */
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = ".", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "0", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_backspace_24), contentDescription = "backspace")
                        }
                        TextButton(modifier = Modifier
                            .weight(.25f, true)
                            .height(IntrinsicSize.Min)
                            .background(Color(237, 239, 244))
                            .border(
                                width = 0.5.dp,
                                shape = RectangleShape,
                                color = Color(221, 221, 221)
                            ), onClick = { /*TODO*/ }) {
                            Text(text = "10%", style = TextStyle(color = Color(51,51,51), textAlign = TextAlign.Center))
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()) {
                        Button(modifier = Modifier
                            .weight(.5f, true)
                            .height(IntrinsicSize.Min), shape = RectangleShape, colors = ButtonDefaults.buttonColors(containerColor = Color(119,119,119), contentColor = Color.White), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_reset))
                        }
                        Button(modifier = Modifier
                            .weight(.5f, true)
                            .height(IntrinsicSize.Min)
                            .padding(start = 5.dp), shape = RectangleShape, colors = ButtonDefaults.buttonColors(containerColor = Color(red = 9, green = 54, blue = 135), contentColor = Color.White), onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.order_bottom_sheet_button_confirm))
                        }
                    }
                }
            }
        }
    }

}
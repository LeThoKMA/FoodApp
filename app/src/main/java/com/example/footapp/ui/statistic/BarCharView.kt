package com.example.footapp.ui.statistic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barchartcompose.ui.theme.GreenYellow
import com.example.barchartcompose.ui.theme.LimeGreen
import com.example.footapp.model.ItemStatistic
import com.example.footapp.utils.formatToPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BarCharView(
    stoneValue: Int,
    barDatas: MutableList<ItemStatistic>,
) {
    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit, block = {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000L)
            isVisible = true
        }
    })

    val values = listOf(
        stoneValue.times(5),
        stoneValue.times(4),
        stoneValue.times(3),
        stoneValue.times(2),
        stoneValue.times(1),
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .padding()
                .wrapContentSize(),
        ) {
            values.forEachIndexed { index, i ->
                Text(
                    text = i.formatToPrice(),
                    modifier = Modifier
                        .padding(
                            bottom = if (index == values.lastIndex) 100.dp else 80.dp,
                            top = if (index == 0) 80.dp else 0.dp,
                        )
                        .height(25.dp),
                    fontSize = 18.sp,
                )
            }
        }

        Box(
            modifier = Modifier.width(1.5.dp).height(600.dp).padding(bottom = 20.dp)
                .background(Color.Gray),
        )

        LazyRow(
            modifier = Modifier.padding(start = 16.dp).height(600.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            itemsIndexed(barDatas) { index, item ->
                var showValue by remember {
                    mutableStateOf(false)
                }
                val gradientBrush = Brush.verticalGradient(
                    colors = listOf(
                        LimeGreen, // Màu nhạt ở trên cùng
                        GreenYellow, // Màu đậm dưới cùng
                    ),
                )
                val targetHeight = ((item.revenue.div(values[0].toFloat())).times(500f))
                AnimatedVisibility(
                    visible = isVisible,
                    modifier = Modifier.wrapContentSize(),
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 3000),
                    ) +
                            expandVertically(
                                expandFrom = Alignment.Top,
                                animationSpec = tween(durationMillis = 3000),
                            ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().align(Alignment.Bottom),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (showValue) Text(text = item.revenue.formatToPrice())

                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp)
                                .animateContentSize()
                                .width(50.dp)
                                .height(targetHeight.dp)
                                .padding(top = 12.5.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(gradientBrush)
                                .clickable { showValue = !showValue },
                        ) {
                        }

                        Text(
                            text = "Tháng ${item.time}",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 20.dp),
                        )
                    }
                }
            }
        }
    }
}

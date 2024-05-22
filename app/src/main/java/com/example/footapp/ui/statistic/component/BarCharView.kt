package com.example.footapp.ui.statistic.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barchartcompose.ui.theme.GreenYellow
import com.example.barchartcompose.ui.theme.LimeGreen
import com.example.footapp.model.ItemStatistic
import com.example.footapp.utils.formatToPrice

@Composable
fun BarCharView(
    stoneValue: Int,
    barDatas: MutableList<ItemStatistic>,
) {
    val animVisibleState = remember { MutableTransitionState(false) }.apply { targetState = true }
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
                    text = if (i != 0) i.formatToPrice() else "",
                    modifier = Modifier
                        .padding(
                            bottom = if (index == values.lastIndex) 100.dp else 80.dp,
                            top = if (index == 0) 80.dp else 0.dp,
                        )
                        .height(25.dp)
                        .width(100.dp),
                    fontSize = 18.sp,
                )
            }
        }

        Box(
            modifier = Modifier
                .width(1.5.dp)
                .height(600.dp)
                .padding(bottom = 20.dp)
                .background(Color.Gray),
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize(),

                ) {
                values.forEachIndexed { index, i ->
                    // top-down
                    val lineY = if (index == 0) {
                        80f // pivot
                    } else {
                        (105f.times(index + 1) - 21.5f) // (pivot+height)*(index +1) - ((height + textSize)/2)
                    }
                    drawLine(
                        color = Color.Gray,
                        start = Offset(-10f, lineY),
                        end = Offset(10f, lineY),
                        strokeWidth = 2f,
                    )
                }
            }
        }
        LazyRow(
            modifier = Modifier
                .padding(start = 16.dp)
                .height(600.dp),
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
                    visibleState = animVisibleState,
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
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Bottom),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (showValue) Text(text = item.revenue.formatToPrice())

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .animateContentSize()
                                .width(50.dp)
                                .height(if (targetHeight > 600) (600 - 20).dp else targetHeight.dp)
                                .padding(top = 12.5.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(gradientBrush)
                                .clickable { showValue = !showValue },
                        ) {
                        }
                        Text(
                            text = "Tháng ${item.time}",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .height(20.dp)
                        )
                    }
                }
            }
        }
    }
}

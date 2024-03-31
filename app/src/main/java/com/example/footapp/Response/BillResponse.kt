package com.example.footapp.Response

import com.example.footapp.model.PromotionModel

data class BillResponse(
    val id: Int? = 0,
    val totalPrice: Int? = 0,
    val givenPromotion: List<PromotionModel>? = emptyList(),
    val qrResponse: QrResponse? = QrResponse(),
) {
    val promotion: Int
        get() {
            if (givenPromotion?.isNotEmpty() == true) {
                return givenPromotion.maxOf { it1 -> it1.percentage }
            } else {
                return 0
            }
        }
}

data class QrResponse(
    val code: String = "",
    val desc: String = "",
    val data: QrData = QrData(),
)

data class QrData(
    val qrCode: String = "",
    val qrDataURL: String = "",
){
    val qrDataString:String get() = qrDataURL.removePrefix("data:image/png;base64,")
}

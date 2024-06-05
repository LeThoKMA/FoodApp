package com.example.footapp.ui.Order

import com.example.footapp.DAO.BannerDao
import com.example.footapp.DAO.BillDao
import com.example.footapp.DAO.ItemDao
import com.example.footapp.DAO.QrDefaultDao
import com.example.footapp.DAO.TypeDBDao
import com.example.footapp.Response.BaseResponse
import com.example.footapp.Response.BaseResponseNoBody
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.Response.QrResponse
import com.example.footapp.model.BillWithItems
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBillRequest
import com.example.footapp.model.dbmodel.BannerDB
import com.example.footapp.model.dbmodel.ItemDB
import com.example.footapp.model.dbmodel.QrDefault
import com.example.footapp.model.dbmodel.TypeDB
import com.example.footapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val typeDBDao: TypeDBDao,
    private val billDao: BillDao,
    private val bannerDao: BannerDao,
    private val qrDefaultDao: QrDefaultDao,
    private val apiService: ApiService
) {
    private val dispatcher = Dispatchers.IO
    suspend fun insertType(typeDB: TypeDB) = withContext(dispatcher) {
        typeDBDao.insertIfNotExists(typeDB)
    }

    suspend fun insertItem(itemDB: ItemDB) = withContext(dispatcher) {
        itemDao.insertIfNotExists(itemDB)
    }

    suspend fun getAllType(): Flow<List<TypeDB>> = withContext(dispatcher) {
        flow { emit(typeDBDao.getAllType()) }
    }

    suspend fun getAllItemByType(idType: Int): Flow<List<ItemDB>> = withContext(dispatcher) {
        flow { emit(itemDao.getItemByType(idType)) }
    }

    suspend fun getAllItem(): Flow<List<ItemDB>> = withContext(dispatcher) {
        flow { emit(itemDao.getAllItem()) }
    }

    suspend fun getAllBillInLocal(): List<BillWithItems> = withContext(dispatcher) {
        billDao.getAllBillItem()
    }

    suspend fun confirmBill(list: List<DetailItemChoose>): Flow<BaseResponse<BillResponse>> =
        withContext(dispatcher) {
            val bilLRequest: MutableList<ItemBillRequest> = mutableListOf()
            list.forEach { bilLRequest.add(ItemBillRequest(it.id, it.count, it.totalPrice)) }
            flow { emit(apiService.makeBill(bilLRequest)) }
        }

    suspend fun fetchItems(): Flow<Pair<BaseResponse<List<Item>>, BaseResponse<List<CategoryResponse>>>> =
        withContext(dispatcher) {
            flow {
                emit(Pair(apiService.getItems(), apiService.getCategory()))
            }
        }

    suspend fun getProductByType(id: Int): Flow<BaseResponse<List<Item>>> = withContext(dispatcher)
    {
        flow { emit(apiService.getProductByType(id)) }
    }

    suspend fun postBillOffline(item: BillWithItems) =
        withContext(dispatcher) {
            val items = item.items.map { ItemBillRequest(it.productId, it.count, it.totalPrice) }
            item.items[0].staffId?.let { apiService.postBillOffline(items, it) }
        }

    suspend fun deleteBill(item: BillWithItems) = withContext(dispatcher) {
        billDao.deleteBill(item.bill)
    }

    suspend fun getQrDefault(): Flow<BaseResponse<QrResponse>> = withContext(dispatcher) {
        flow { emit(apiService.getQrDefault()) }
    }

    suspend fun insertQr(qrResponse: QrResponse) = withContext(dispatcher) {
        qrDefaultDao.insertIfNotExists(QrDefault(url = qrResponse.data.qrDataURL))
    }

    suspend fun getAllBanner(): Flow<List<BannerDB>> = withContext(dispatcher) {
        flow { emit(bannerDao.getAllBanner()) }
    }
}
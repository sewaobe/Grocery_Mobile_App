package com.store.grocery_store_app.data.repository.impl

import com.store.grocery_store_app.data.api.ApiService
import com.store.grocery_store_app.data.models.response.VoucherResponse
import com.store.grocery_store_app.data.repository.VoucherRepository
import com.store.grocery_store_app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VoucherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
):VoucherRepository{
    override suspend fun getAllVoucher(): Flow<Resource<List<VoucherResponse>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllVoucher()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success && apiResponse.data != null) {
                        val vouchers = apiResponse.data
                        emit(Resource.Success(vouchers))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Phản hồi rỗng từ server"))
            } else {
                emit(Resource.Error("Lấy voucher thất bại: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Lỗi HTTP: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Không thể kết nối đến máy chủ"))
        } catch (e: Exception) {
            emit(Resource.Error("Lỗi không xác định: ${e.message}"))
        }
    }

}
package com.example.diploma.ui.home

import androidx.lifecycle.*
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.data.repository.LocationRepository
import com.example.diploma.data.repository.ProductRepository
import com.example.diploma.ui.productInfo.ProductList
import com.example.diploma.ui.productInfo.SupermarketProduct
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val productRepository: ProductRepository
): ViewModel() {

    private val _cityResult = MutableLiveData<Result<List<City>>>()
    val cityResult: LiveData<Result<List<City>>> = _cityResult

    private val _currentCity = MutableLiveData<City>()
    val currentCity: LiveData<City> = _currentCity

    private val _supermarketResult = MutableLiveData<Result<List<Supermarket>>>()
    val supermarketResult: LiveData<Result<List<Supermarket>>> = _supermarketResult

    private val _cheaperProductResult = MutableLiveData<Result<MutableList<SupermarketProduct>>>()
    val cheaperProductResult: LiveData<Result<MutableList<SupermarketProduct>>> = _cheaperProductResult

    private val _currentSupermarket = MutableLiveData<Supermarket>()
    val currentSupermarket: LiveData<Supermarket> = _currentSupermarket

    private val _product = MutableLiveData<Result<SupermarketProduct>>()
    val product: LiveData<Result<SupermarketProduct>> = _product

    val purchaseList: LiveData<List<PurchaseListProduct>> = productRepository.loadSupermarketProducts().asLiveData()

    private val _purchaseResult = MutableLiveData<Result<BaseResponse>>()
    val purchaseResult: LiveData<Result<BaseResponse>> = _purchaseResult

    fun fetchCity() {
        viewModelScope.launch {
            locationRepository.fetchCity().collect {
                if (it.status == Result.Status.SUCCESS) {
                    _cityResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _cityResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _cityResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun saveCurrentCity(city: City) {
        _currentCity.value = city
    }

    fun fetchSupermarket(cityId: Int) {
        viewModelScope.launch {
            locationRepository.fetchSupermarket(cityId).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _supermarketResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _supermarketResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _supermarketResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun saveCurrentSupermarket(supermarket: Supermarket) {
        _currentSupermarket.value = supermarket
    }

    fun fetchProduct(barcode: String, supermarketId: Int) {
        viewModelScope.launch {
            productRepository.fetchProduct(barcode, supermarketId).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _product.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _product.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _product.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun fetchCheaperProduct(barcode: String, cityId: Int, price: Double) {
        viewModelScope.launch {
            productRepository.fetchCheaperProduct(barcode, cityId, price).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _cheaperProductResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _cheaperProductResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _cheaperProductResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun confirmPurchase(token: String) {
        val listProduct = mutableListOf<ProductList>()
        purchaseList.value?.forEach {
            listProduct.add((ProductList(it.id, it.quantity)))
        }
        viewModelScope.launch {
            productRepository.confirmPurchase(token, listProduct).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _purchaseResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _purchaseResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _purchaseResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun addProductToPurchaseList() {
        viewModelScope.launch {
            val purchaseListProduct = _product.value?.data!!
            productRepository.saveProductToPurchaseList(
                PurchaseListProduct(
                    purchaseListProduct.id!!, purchaseListProduct.price!!, purchaseListProduct.product.name!!, 1)
            )
        }
    }

    fun clearPurchaseList() {
        viewModelScope.launch {
            productRepository.clearPurchaseList()
        }
    }

    fun deletePurchaseListItem(position: Int) {
        viewModelScope.launch {
            productRepository.deleteProductFromPurchaseList(purchaseList.value?.get(position)!!)
        }
    }

    fun decreaseQuantityPurchaseList(position: Int) {
        val purchaseListProduct: PurchaseListProduct = purchaseList.value?.get(position)!!
        purchaseListProduct.quantity -= 1
        viewModelScope.launch {
            productRepository.adjustPurchaseListProductQuantity(purchaseListProduct)
        }
    }

    fun increaseQuantityPurchaseList(position: Int) {
        val purchaseListProduct: PurchaseListProduct = purchaseList.value?.get(position)!!
        purchaseListProduct.quantity += 1
        viewModelScope.launch {
            productRepository.adjustPurchaseListProductQuantity(purchaseListProduct)
        }
    }

    fun clearCheaperProductList() {
        _cheaperProductResult.value?.data?.clear()
    }

}

package com.oimmei.oipharma.app.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(val shopkeeper: Boolean) : ViewModel() {
    class ProfileViewModelFactory(private val shopkeeper: Boolean) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ProfileViewModel(shopkeeper) as T
    }

    private fun checkCompletion() {
        _isCompleted.value = (
                (_firstName.value?.isNotBlank() == true
                        && _lastName.value?.isNotBlank() == true) && _email.value.isNotBlank()
                )
        if (shopkeeper) {
            _isCompleted.value = _isCompleted.value &&
                    (_shopName.value?.isNotBlank() == true
                            && _address.value?.isNotBlank() == true && _phone.value?.isNotBlank() == true)
        }
    }

//    fun logout(callback: () -> Unit) {
//        UserHelper.setUser(null)
//
////        UserHelper.userLogged = false
//        Constants.AUTH_HEADER = null
//        callback.invoke()
//    }

    val TAG: String = ProfileViewModel::class.java.simpleName

    internal var _firstName: MutableStateFlow<String?> = MutableStateFlow(null)
    var firstName = _firstName.asStateFlow()
    fun setFirstName(value: String) {
        _firstName.value = value.trim()
        checkCompletion()
    }

    internal var _lastName: MutableStateFlow<String?> = MutableStateFlow(null)
    var lastName = _lastName.asStateFlow()
    fun setLastName(value: String) {
        _lastName.value = value.trim()
        checkCompletion()
    }

    val _isCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    internal var _email: MutableStateFlow<String> = MutableStateFlow("")
    var email = _email.asStateFlow()
    fun setEmail(anEmail: String) {
        _email.value = anEmail.trim()
        checkCompletion()
    }

    internal var _shopName: MutableStateFlow<String?> = MutableStateFlow(null)
    var shopName = _shopName.asStateFlow()
    fun setShopName(value: String) {
        _shopName.value = value.trim()
        checkCompletion()
    }

    internal var _address: MutableStateFlow<String?> = MutableStateFlow(null)
    var address = _address.asStateFlow()
    fun setAddress(value: String) {
        _address.value = value.trim()
        checkCompletion()
    }

    internal var _phone: MutableStateFlow<String?> = MutableStateFlow(null)
    var phone = _phone.asStateFlow()
    fun setPhone(value: String) {
        _phone.value = value.trim()
        checkCompletion()
    }

//    fun updateProfile() {
//
//            UserHelper.updateProfile(
//                firstName = _firstName.value,
//                lastName = _lastName.value,
//                email = _email.value,
//                address = _address.value,
//                phone = _phone.value,
//                shopName = _shopName.value
//            ) { baseUser: BaseUser?, err: ResponseError? ->
//                err?.run {
//                    Toast.makeText(OIApplication.context, err.toString(), Toast.LENGTH_SHORT).show()
//                } ?: run {
//                    Toast.makeText(
//                        OIApplication.context,
//                        "Dati aggiornati con successo",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//    }

    private val _next = MutableStateFlow(false)
    val next = _next.asStateFlow()
}
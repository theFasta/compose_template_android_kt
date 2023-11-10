package com.oimmei.oipharma.app.utils

import android.content.Context
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.oimmei.oipharma.app.R
import java.util.regex.Pattern


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 2019-09-26 - 12:50
 * @lastupdate 2023-01-26
 * Copyright Oimmei Srls 2015,2016,2017,2018,2019,2021,2022,2023 - www.oimmei.com
 */

class FieldValidator {

    companion object {

        internal const val PASSWORD_SPECIAL_CHARS = "@\$!%*?&;^_"
        private val REGEX_EMAIL: String =
            "^([a-zA-Z0-9_\\-\\.\\+]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        const val REGEX_CODICE_FISCALE =
            "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$"

        const val REGEX_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&^_])[A-Za-z\\d@\$!%*?&^_]{8,}\$"

        const val REGEX_PHONE = "^((\\+)(\\d{1,3}([- ])?)){1}(\\d+)\$"

        fun validateFields(context: Context, fields: Array<TextInputEditText>): Boolean {
            var canDo = true
            fields.forEach {
                canDo = validateField(context, canDo, it)
            }
            return canDo
        }

        fun validateField(
            context: Context, otherCanDo: Boolean = true, field: TextInputEditText
        ): Boolean {
            var canDo = true

            val til = (field.parent.parent as TextInputLayout)

            if (field.text.isNullOrBlank()) {
                til.error = context.getString(R.string.field_cannot_be_left_empty)
                til.isErrorEnabled = true
                canDo = false
            } else {
                til.error = null
                til.isErrorEnabled = false
            }
            return canDo && otherCanDo
        }

        fun passwordsMatch(context: Context, password: EditText, password_confirmation: EditText): Boolean {
            val match =
                password.text.toString().trim().equals(password_confirmation.text.toString().trim())
            if (!match) {
                (password.parent.parent as TextInputLayout).isErrorEnabled = true
                (password.parent.parent as TextInputLayout).error =
                    context.getString(R.string.passwords_must_match)
            } else {
                (password.parent.parent as TextInputLayout).isErrorEnabled = false
                (password.parent.parent as TextInputLayout).error = null
            }
            return match
        }

        private var pattern: Pattern = Pattern.compile(REGEX_CODICE_FISCALE)

        fun isFiscalCodeValid(code: String): Boolean {
            return pattern.matcher(code.trim()).matches()
        }

        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(REGEX_EMAIL).matcher(email).matches()
        }

        fun isEmailValid(field: TextInputEditText, context: Context): Boolean {
            val til = (field.parent.parent as TextInputLayout)
            if (field.text.toString().isNullOrBlank()) {
                // Il campo e' vuoto

                return if (field.text.isNullOrBlank()) {
                    til.error = context.getString(R.string.field_cannot_be_left_empty)
                    til.isErrorEnabled = true
                    false
                } else {
                    til.error = null
                    til.isErrorEnabled = false
                    true
                }
            } else {
                // Il campo non e' vuoto, vediamo se e' un email valida
                val email = field.text.toString()

                val match = Pattern.compile(REGEX_EMAIL).matcher(email).matches()

                return if (!match) {
                    til.error = context.getString(R.string.invalid_address)
                    til.isErrorEnabled = true
                    false
                } else {
                    til.error = null
                    til.isErrorEnabled = false
                    true
                }
            }
        }

        fun isPasswordValid(password: String): Boolean {
            return Pattern.compile(REGEX_PASSWORD).matcher(password).matches()
        }

        fun isPasswordValid(field: TextInputEditText, context: Context): Boolean {
            val til = (field.parent.parent as TextInputLayout)
            if (field.text.toString().isNullOrBlank()) {
                // Il campo e' vuoto

                return if (field.text.isNullOrBlank()) {
                    til.error = context.getString(R.string.field_cannot_be_left_empty)
                    til.isErrorEnabled = true
                    false
                } else {
                    til.error = null
                    til.isErrorEnabled = false
                    true
                }
            } else {
                // Il campo non e' vuoto, vediamo se e' una password valida
                val password = field.text.toString()

                val match = Pattern.compile(REGEX_PASSWORD).matcher(password).matches()

                return if (!match) {
                    til.error =
                        context.getString(R.string.password_rules).format(PASSWORD_SPECIAL_CHARS)
//                        string PASSWORD_RULES = "La password deve essere lunga almeno 8 caratteri e contenere almeno una lettera maiuscola, una minuscola, un numero ed uno tra i seguenti caratteri speciali %s"
                    til.isErrorEnabled = true
                    false
                } else {
                    til.error = null
                    til.isErrorEnabled = false
                    true
                }
            }
        }

        fun checkEmailValidity(
            fields: Array<TextInputEditText>,
            context: Context,
            checkMatch: Boolean,
            textInputEditTextForMessage: TextInputEditText
        ): Boolean {
            var canDo = validateFields(context, fields)
            if (!canDo) {
                return false
            } else {
                fields.forEach {
                    canDo = canDo && isEmailValid(it, context)
                }
                return if (!canDo) false
                else if (checkMatch) {
                    val arr = fields.distinctBy { it.text.toString().trim() }
                    val match = arr.size == 1
                    if (match) {
                        (textInputEditTextForMessage.parent.parent as TextInputLayout).apply {
                            error = null
                            isErrorEnabled = false
                        }
                        return true
                    } else {
//                        Toast.makeText(
//                            context,
//                            "Gli indirizzi non corrispondono",
//                            Toast.LENGTH_LONG
//                        ).show()
                        (textInputEditTextForMessage.parent.parent as TextInputLayout).apply {
                            error = context.getString(R.string.addresses_dont_match)
                            isErrorEnabled = true
                        }
                        return false
                    }
                } else {
                    true
                }
            }


        }

        fun validatePhoneNumber(field: TextInputEditText, context: Context): Boolean {
            val til = (field.parent.parent as TextInputLayout)
            if (field.text.toString().isNullOrBlank()) {
                // Il campo e' vuoto

                return if (field.text.isNullOrBlank()) {
                    til.error = context.getString(R.string.field_cannot_be_left_empty)
                    til.isErrorEnabled = true
                    false
                } else {
                    til.error = null
                    til.isErrorEnabled = false
                    true
                }
            } else {
                // Il campo non e' vuoto, vediamo se e' un numero di telefono valido
                val phone = field.text.toString()

                val match = Pattern.compile(REGEX_PHONE).matcher(phone).matches()

                return if (!match) {
                    til.error =
                        context.getString(R.string.phone_number_explanation)
                    til.isErrorEnabled = true
                    false
                } else {
                    til.error = null
                    til.isErrorEnabled = false
                    true
                }
            }
        }

        fun validatePhoneNumber(phone: String, context: Context): Boolean {
            return if (phone.isBlank()) {
                // Il campo e' vuoto
                false
            } else {
                // Il campo non e' vuoto, vediamo se e' un numero di telefono valido
                val match = Pattern.compile(REGEX_PHONE).matcher(phone).matches()

                match
            }
        }
    }
}
package com.asiasquare.byteg.shoppingdemo

import android.content.Context

class Settings(context: Context) {
    private val appContext = context.applicationContext


    val backendUrl: String
        get() {
            return BASE_URL
        }

    val backendBaseUrl: String
        get() {
            return BACKEND_BASE_URL
        }

    val publishableKey: String
        get() {
            return PUBLISHABLE_KEY
        }

    val stripeAccountId: String?
        get() {
            return STRIPE_ACCOUNT_ID
        }


    internal companion object {
        /**
         * Three-letter ISO [currency code](https://stripe.com/docs/api/payment_intents/object#payment_intent_object-currency),
         * in lowercase. Must be a [supported currency](https://stripe.com/docs/currencies).
         */
        const val CURRENCY = "USD"

        const val COUNTRY = "us"

        /**
         * Set to the base URL of your test backend. If you are using
         * [example-mobile-backend](https://github.com/stripe/example-mobile-backend),
         * the URL will be something like `https://hidden-beach-12345.herokuapp.com/`.
         */
        private const val BASE_URL = "https://whispering-river-24738.herokuapp.com/"
        private const val BACKEND_BASE_URL ="https://serene-cliffs-31164.herokuapp.com/"

        /**
         * Set to publishable key from https://dashboard.stripe.com/test/apikeys
         */
        private const val PUBLISHABLE_KEY = "pk_test_51IhAu2EYP2XsA89m5vqvoeyFVniVcI881wi53n8QdChng5nBUd9tDstbVE0GV103PZs2pRU7PKYpQ3YAWzxcnVy300OG92K2eN"

        /**
         * Optionally, set to a Connect Account id to use for API requests to test Connect
         *
         * See https://dashboard.stripe.com/test/connect/accounts/overview
         */
        private val STRIPE_ACCOUNT_ID: String? = null


    }
}
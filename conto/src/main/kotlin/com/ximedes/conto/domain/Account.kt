package com.ximedes.conto.domain

data class Account(val accountID: String, val owner: String, val description: String, val minimumBalance: Long, var balance: Long){
    constructor(accountID: String, owner: String, description: String, minimumBalance: Long): this(accountID, owner, description, minimumBalance, 0L)
}

data class AccountCriteria(val ownerID: String? = null, val accountID: String? = null)
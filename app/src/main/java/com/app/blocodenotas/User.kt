package com.app.blocodenotas

class User {

    var email: String? = null
    var password: String? = null



    constructor(){}

    constructor(email: String?, password: String?){

        this.email  = email
        this.password = password

    }
}
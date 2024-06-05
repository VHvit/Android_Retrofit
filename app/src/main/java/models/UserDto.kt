package models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
class UserDto {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var login: String = ""
    var password = ""

    constructor()

    constructor(
        login: String,
        password: String,
    ) {
        this.login = login
        this.password = password
    }
}
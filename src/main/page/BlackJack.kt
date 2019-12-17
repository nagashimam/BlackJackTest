package main.page

import org.openqa.selenium.WebDriver

class BlackJack (private val driver: WebDriver){
    fun open(){
        driver.get("https://nagashimam.github.io/BlackJack/")
    }
}
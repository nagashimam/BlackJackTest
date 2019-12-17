package main.page

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.util.concurrent.TimeUnit


internal class BlackJackTest {
    private val driver = ChromeDriver(ChromeOptions())
    private val blackJack = BlackJack(driver)

    @BeforeEach
    fun setUp() {
        blackJack.open()
    }

    @Test
    internal fun play10000times() {
        for (i in 0 until 1) {
            println("${i + 1}回目")
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS)
            play()
        }
    }

    private fun play() {
        try {
            if (!hasBurst(driver.findElement(By.id("more")))) {
                // 15を超えたら、コンピューターが引くのを待つ
                (driver.findElement(By.id("stop")) ?: throw IllegalStateException("要素がない")).click()
                WebDriverWait(driver, 20).until(alertIsPresent())?.accept()
            }
        } catch (e: UnhandledAlertException) {
            return
        }
    }

    private fun hasBurst(drawButton: WebElement): Boolean {
        return runBlocking {
            delay(500L)
            when {
                isAlertPresent() -> true
                Integer.parseInt(driver.findElement(By.id("score")).text) >= 15 -> false
                else -> {
                    drawButton.click()
                    hasBurst(drawButton)
                }
            }
        }
    }

    private fun isAlertPresent(): Boolean {
        return try {
            driver.title
            false
        } catch (e: UnhandledAlertException) {
            true
        }
    }

    @AfterEach
    fun tearDown() {
        driver.getScreenshotAs(OutputType.FILE)?.copyTo(File("screenshot.png"), true)
    }
}
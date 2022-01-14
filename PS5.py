from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from fake_useragent import UserAgent
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import datetime

target_time = datetime.datetime.now()
target_time = target_time.replace(hour=14, minute=00, second=55, microsecond=0)

print("waiting")

while True:
    if datetime.datetime.now() == target_time or datetime.datetime.now() > target_time:

        driver = webdriver.Chrome()
        # driver.get("https://www.walmart.com/ip/Grand-Theft-Auto-V-Premium-Edition-Rockstar-Games-PlayStation-4-710425570322/280167762?athcpid=280167762&athpgid=athenaItemPage&athcgid=null&athznid=PWVUB&athieid=v0&athstid=CS004&athguid=1525d8cd-007-175be1ded93935&athancid=null&athena=true")
        driver.get("https://www.walmart.com/ip/Sony-PlayStation-5-Digital-Edition/493824815")

        try:
            # select = Select(driver.find_element_by_xpath("//select[@aria-label='Quantity']"))
            # select.select_by_visible_text('2')

            driver.find_element_by_xpath("//span[text()='Add to cart']").click()

            element = WebDriverWait(driver, 90).until(
                EC.presence_of_element_located((By.XPATH, "//span[text()='Check out']"))
            )

            driver.find_element_by_xpath("//span[text()='Check out']").click()

            print("requests sent")

            break

        except NoSuchElementException:
            print("trying again")
            driver.refresh()
import urllib2
import time
import requests
import lxml.html
from bs4 import BeautifulSoup as soup
from selenium import webdriver

driver=webdriver.Chrome('C:/Users/simran/Downloads/chromedriver/chromedriver.exe')
driver2=webdriver.Chrome('C:/Users/simran/Downloads/chromedriver/chromedriver.exe')

for i in range(37,250):
    extraction_link='https://www.allrecipes.com/recipes/76/appetizers-and-snacks/?internalSource=top%20hubs&referringContentType=Homepage&page='+str(i)
    driver.get(extraction_link)
    page=driver.execute_script("return document.documentElement.outerHTML")
    page_soup = soup(page, 'html.parser')    
    h3= page_soup.find_all("h3","fixed-recipe-card__h3")
    for x in h3:
        print('\n')
        y=x.find('a')
        link_recipe=y.get('href')        
        print(link_recipe)               
        print("HEY!")
        driver2.get(link_recipe)
        page2=driver2.execute_script("return document.documentElement.outerHTML")
        page_soup2 = soup(page2, 'html.parser')
        link_title=page_soup2.title.string        
        content=driver2.page_source
        filename="E://webscarpe/"+link_title+'.html'
        Html_file= open(filename,"w")
        Html_file.write(content.encode('utf8'))
        print("HEY5")
        Html_file.close()    
driver.close()
driver2.close()
             

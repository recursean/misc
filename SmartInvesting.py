###################################################################################
# Python script that scrapes http://wpn.mlsmatrix.com for today's active listings #
# and retrieves information about similiar households that were previously sold   #
#								7/15/2017										  #
#							   Sean McShane										  #
###################################################################################

#Notes:
#	Take screenshot - browser.save_screenshot(r"c:\users\sean\documents\out.png");
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import ElementNotVisibleException
import time
import csv
import os

class Listing:
	mls_number = "";
	list_price = "";
	agent_name = "";
	agent_phone_number = "";
	agent_email = "";
	zip_code = "";
	year_built = "";
	year_built_range = "";
	address = "";
	county = "";
	school_district = "";
	area = "";
	bedroom_count = "";
	full_bath_count = "";
	p_bath_count = "";
	architecture = "";
	style = "";
	property_type = "";
	construction = "";
	sold_price_avg = "";
	sold_houses_count = "";
	taxes = "";
	margin = "";

def main():
	browser = webdriver.PhantomJS();
	browser.set_window_size(1120, 550);

	login_to_mls(browser);
	search_active_listings(browser);
	listings = scrape_active_results(browser);
	listings = search_and_scrape_sold_listings(browser, listings);
	write_to_csv(listings);

	browser.quit();

def login_to_mls(browser):
	print 'Logging into MLS...'
	#Getting login page...
	browser.get('http://wpn.mlsmatrix.com/Matrix/login.aspx');

	notLoaded = True;
	while notLoaded:
		try:
			#On login page and filling out credentials...
			browser.find_element_by_id('m_tbName').send_keys('225958');
			browser.find_element_by_id('m_tbPassword').send_keys('225958');

			browser.find_element_by_id('m_imgbtnLogin').click();

			notLoaded = False;

		except NoSuchElementException:
			#page might not have loaded yet
			time.sleep(0.5);
		except StaleElementReferenceException:
			browser.refresh();			

def search_active_listings(browser):
	print 'Searching active listings for today...'
	#now on main dashboard

	#Getting search page...
	browser.get('http://wpn.mlsmatrix.com/Matrix/Search/Residential')

	#now on search page and filling out current date
	notLoaded = True;
	while notLoaded:
		try:
			browser.find_element_by_id('FmFm17_Ctrl16_101_Ctrl16_TB').send_keys(time.strftime("%m/%d/%Y") + '+');
			browser.find_element_by_name('Fm17_Ctrl16_LB').click();

			notLoaded = False;

		except NoSuchElementException:
			#page might not have loaded yet
			time.sleep(0.5);
		except StaleElementReferenceException:
			browser.refresh();
	#wait for date to be finish filling out
	time.sleep(0.5);

	notLoaded = True;
	while notLoaded:
		try:
			browser.find_element_by_id('m_ucSearchButtons_m_lbSearch').click();

			notLoaded = False;

		except NoSuchElementException:
			#page might not have loaded yet
			time.sleep(0.5);
		except StaleElementReferenceException:
			browser.refresh();
			
def scrape_active_results(browser):
	#Getting results page...
	notLoaded = True;
	while notLoaded:
		try:
			total_households = int(browser.find_element_by_id('m_lblPagingSummary').text.split('of')[1].split('<span')[0].strip());

			#Clicking on first result...
			(browser.find_elements_by_class_name('d1m1')[6]).find_element_by_tag_name('a').click();

			notLoaded = False
		except IndexError:
			#page might not have loaded yet
			time.sleep(0.5);
		except NoSuchElementException:
			#page might not have loaded yet
			time.sleep(0.5);
		except ElementNotVisibleException:
			#page might not have loaded yet
			time.sleep(0.5);
		except StaleElementReferenceException:
			browser.refresh();

	print 'Scraping results (' + str(total_households) + ' households)...'
	listings = [None] * total_households;
	previous_mls_number = "";

	for current_household in range(1,total_households+1):
		new_listing = Listing();
		#print 'On result ' + str(current_household);
		notLoaded = True;
		while notLoaded:
			try:
				#new listing might not have loaded yet
				if (browser.find_elements_by_class_name('d47m18')[1].text == "" or browser.find_elements_by_class_name('d47m18')[1].text == previous_mls_number):
					time.sleep(0.5)
					continue;
				else:
					new_listing.mls_number = browser.find_elements_by_class_name('d47m18')[1].text;
					new_listing.list_price = browser.find_elements_by_class_name('d47m27')[0].text;
					new_listing.agent_name = browser.find_elements_by_class_name('d47m54')[7].text;
					new_listing.agent_phone_number = browser.find_elements_by_class_name('d47m57')[6].text;
					new_listing.agent_email = browser.find_elements_by_class_name('d47m54')[8].text;
					new_listing.zip_code = browser.find_elements_by_class_name('d47m27')[2].text;
					new_listing.year_built = browser.find_elements_by_class_name('d47m51')[0].text;
					new_listing.year_built_range = str(int(new_listing.year_built)-5) + '-' + str(int(new_listing.year_built)+5)
					new_listing.address = browser.find_elements_by_class_name('d47m28')[0].text;
					new_listing.county = browser.find_elements_by_class_name('d47m28')[2].text;
					new_listing.school_district = browser.find_elements_by_class_name('d47m30')[3].text;
					new_listing.area = browser.find_elements_by_class_name('d47m28')[1].text;
					new_listing.bedroom_count = browser.find_elements_by_class_name('d47m27')[3].text;
					new_listing.full_bath_count = ''.join(i for i in browser.find_elements_by_class_name('d47m31')[0].text[9:] if i.isdigit())
					new_listing.p_bath_count = ''.join(i for i in browser.find_elements_by_class_name('d47m32')[0].text[9:] if i.isdigit())
					new_listing.architecture = browser.find_elements_by_class_name('d47m51')[1].text;
					new_listing.style = browser.find_elements_by_class_name('d47m50')[1].text;
					new_listing.property_type = browser.find_elements_by_class_name('d47m50')[0].text;
					new_listing.construction = browser.find_elements_by_class_name('d47m50')[2].text;
					new_listing.taxes = browser.find_elements_by_class_name('d47m57')[3].text;

					listings[current_household-1] = new_listing;
					previous_mls_number = new_listing.mls_number;
					notLoaded = False;
					if(current_household < total_households):
						browser.find_element_by_id('m_DisplayCore_dpy3').click();

			except IndexError as e:
				time.sleep(0.5);
			except StaleElementReferenceException:
				browser.refresh();

	return listings;

def search_and_scrape_sold_listings(browser, listings):
	print 'Searching for sold listings...';

	#Getting search page...
	browser.get('http://wpn.mlsmatrix.com/Matrix/Search/Residential');

	count = 0;
	for listing_detail in listings:
		count += 1;
		print 'On sold listing ' + str(count);
		notLoaded = True;
		while notLoaded:
			try:
				browser.find_element_by_id('FmFm17_Ctrl16_104_Ctrl16_TB').send_keys("01/01/2012+");
				browser.find_elements_by_name('Fm17_Ctrl16_LB')[1].click();

				price_sum = 0;
				price_count = 0;

				#On search page and filling out info
				browser.find_element_by_id('Fm17_Ctrl19_TextBox').send_keys(listing_detail.zip_code);
				browser.find_element_by_id('Fm17_Ctrl82_TB').send_keys(listing_detail.year_built_range);
				browser.find_element_by_id('Fm17_Ctrl75_LB').send_keys(listing_detail.county);
				browser.find_element_by_id('Fm17_Ctrl77_LB_TB').send_keys(listing_detail.school_district);
				browser.find_element_by_id('Fm17_Ctrl74_LB_TB').send_keys(listing_detail.area);
				browser.find_element_by_id('Fm17_Ctrl9_TB').send_keys(listing_detail.bedroom_count);
				browser.find_element_by_id('Fm17_Ctrl13_TB').send_keys(listing_detail.full_bath_count);
				browser.find_element_by_id('Fm17_Ctrl14_TB').send_keys(listing_detail.p_bath_count);
				#browser.find_element_by_id('Fm17_Ctrl76_LB').send_keys(listing_detail.architecture);
				#browser.find_element_by_id('Fm17_Ctrl83_LB').send_keys(listing_detail.style);
				#browser.find_element_by_id('Fm17_Ctrl153_LB').send_keys(listing_detail.property_type);

				#wait for information to finish filling out
				time.sleep(0.5);

				browser.find_element_by_id('m_ucSearchButtons_m_lbSearch').click();
				notLoaded = False;
			except NoSuchElementException as e:
				print e;
				#page might not have loaded yet
				print 'Waiting...'
				time.sleep(0.5);
			except StaleElementReferenceException:
				print 'Refreshing page...'
				browser.refresh();	
			except Exception as e:
				print e;
				browser.save_screenshot(r"c:\users\sean\documents\out.png");
		print 'Getting search results...'
		#Getting search results...
		notLoaded = True;
		reloads = 0;
		skip = False;
		while notLoaded:
			try:
				total_households = int(browser.find_element_by_id('m_lblPagingSummary').text.split('of')[1].split('<span')[0].strip())
				notLoaded = False
			except IndexError:
				if(reloads < 5):
					#print 'not loaded yet'
					time.sleep(1)
					reloads += 1;
				else:
					#print 'No sold results'
					skip = True;
					break;
			except NoSuchElementException:
				#page might not have loaded yet
				time.sleep(0.5);
			except StaleElementReferenceException:
				browser.refresh();

		if not skip:
			#wait for page to load
			time.sleep(1);

			keepGoing = True;
			while keepGoing:
				try:
					prices = browser.find_elements_by_class_name('d1m11')
					for price in range(len(prices)):
						if price % 2 == 0:
							continue;
						else:
							try:
								price_sum += int(''.join(i for i in prices[price].text[1:] if i.isdigit()));
							except Exception as e:
								print e;

							price_count += 1;
					if total_households - price_count > 0: #25 -> 100?
						browser.find_element_by_id('m_upPaging').find_elements_by_tag_name('a')[len(browser.find_element_by_id('m_upPaging').find_elements_by_tag_name('a'))-1].click();
						time.sleep(2);
					else:
						keepGoing = False;

					price_avg = float(price_sum) / float(price_count);
				except NoSuchElementException:
					#page might not have loaded yet
					time.sleep(0.5);
				except StaleElementReferenceException:
					browser.refresh();
				except Exception as e:
					print e;
					time.sleep(0.5);

			#print str(price_avg) + ' (' + str(price_count) + ' homes)';
		else:
			price_avg = '0';

		listing_detail.sold_price_avg = price_avg;
		listing_detail.sold_houses_count = price_count;
		
		browser.get('http://wpn.mlsmatrix.com/Matrix/Search/Residential');
		time.sleep(0.5);

	return listings;

def write_to_csv(listings):
	file_path = os.path.expanduser('~') + "\\MLSResults\\" + time.strftime("%m%d%Y") + '.csv';

	print 'Writing to ' + file_path;
	
	if not os.path.exists(os.path.expanduser('~') + "\\MLSResults\\"):
	    os.makedirs(os.path.expanduser('~') + "\\MLSResults\\");

	with open(file_path, 'wb') as csv_file:
		writer = csv.writer(csv_file, delimiter=',');
		writer.writerow(['MLS_Number','List_Price','Agent_Name','Agent_Phone','Agent_Email','Zip_Code','Year_Built','Year_Built_Range','Address','County','School_District', 'Area','Bedroom_Count','Full_Bath_Count','P_Bath_Count','Architecture','Style','Property_Type','Construction', 'Avg_Sold_Price', 'Margin_Difference', 'Sold_Houses_Count', 'Taxes'])	

		for listing_detail in listings:	
			if(float(listing_detail.sold_price_avg) != 0):
				listing_detail.margin = float(listing_detail.list_price.replace('$', '').replace(',', '')) / float(listing_detail.sold_price_avg)
			else:
				listing_detail.margin = 0;
			if(listing_detail.p_bath_count == ""):
				listing_detail.p_bath_count = 0;
			writer.writerow([listing_detail.mls_number,listing_detail.list_price,listing_detail.agent_name,listing_detail.agent_phone_number,listing_detail.agent_email,listing_detail.zip_code,listing_detail.year_built,listing_detail.year_built_range,listing_detail.address,listing_detail.county,listing_detail.school_district,listing_detail.area,listing_detail.bedroom_count,listing_detail.full_bath_count,listing_detail.p_bath_count,listing_detail.architecture,listing_detail.style,listing_detail.property_type,listing_detail.construction, listing_detail.sold_price_avg, listing_detail.margin,listing_detail.sold_houses_count,listing_detail.taxes])	

main();
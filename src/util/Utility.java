package util;

import model.City;
import model.CoolWeatherDB;
import model.Country;
import model.Province;
import android.text.TextUtils;

public class Utility {
	/*
	 * �����ʹ������������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length>0){
				for(String p : allProvinces){
					String[] array = p.split("|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//�����������ݴ洢��Province
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	/*
	 * �����ʹ������������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length>0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//�����������ݴ洢��Province
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/*
	 * �����ʹ������������ص��ؼ�����
	 */
	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCountries = response.split(",");
			if(allCountries != null && allCountries.length>0){
				for(String c : allCountries){
					String[] array = c.split("\\|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					//�����������ݴ洢��Province
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
	}
}
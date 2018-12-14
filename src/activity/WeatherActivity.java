package activity;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;

public class WeatherActivity extends ActionBarActivity {
	
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		//��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		temp1Text = (TextView)findViewById(R.id.temp1);
		temp2Text = (TextView)findViewById(R.id.temp2);
		currentDateText = (TextView)findViewById(R.id.current_date);
		String countryCode = getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			//���ؼ����ž�ȥ������
			publishText.setText("ͬ����.....");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}
	}
	
	/*
	 * ��ѯ�ؼ���������Ӧ���������ţ��� Javadoc��
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	private void queryWeatherCode(String countryCode){
		String address = "http://www.weather.com.cn/data/list3/city" + countryCode +".xml";
		queryFromServer(address,"countryCode");
	}
	/*
	 * ��ѯ������������Ӧ���������� Javadoc��
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	
	private void queryWeatherInfo(String weatherCode){
		String address  = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address,"weatherCode");
	}
	/*
	 * ���ݴ���Ĳ���ȥ��������ѯ
	 */
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO �Զ����ɵķ������
				if("countryCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//���������ص������н�������������
						String[] array = response.split("\\/");
						if(array != null && array.length == 2){
							String weatherCode  =array[1];
							queryWeatherInfo(weatherCode);
						} else if("weatherCode".equals(type)){
							//���������ص�������Ϣ
							Utility.handleWeatherResponse(WeatherActivity.this, response);
							runOnUiThread(new Runnable(){

								@Override
								public void run() {
									// TODO �Զ����ɵķ������
									showWeather();
								}
								
							});
						}	
					}
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO �Զ����ɵķ������
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO �Զ����ɵķ������
						publishText.setText("ͬ��ʧ��");
					}
					
				});
			}
		});
	}
	/*
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ��������
	 */
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name",""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publis_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

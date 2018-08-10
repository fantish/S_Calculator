package com.example.calculator;

import android.R.layout;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AboutMe extends Activity{
	private String[] data = { "Author:SQL", "Corporation:XMJH",
							"Help:Readme below" ,"Ƶ�������ʹ�ù���\n1��������Ƶ�������������Ƶ�㣬������UP_S����" +
							"DOWN��Ȼ������1��1388Ƶ��ţ���\"=\"�ż��ɵõ������" +
							"\n2)������Ƶ�ʷ���������Ƶ��ţ�������ȷ��Ƶ�ʷ�Χ��Ȼ���INV�����ɵ����л�����Ƶ��š�"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AboutMe.this, android.R.layout.simple_list_item_1, data);
				ListView listView = (ListView) findViewById(R.id.list_view);
				listView.setAdapter(adapter);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		
	}

/**
 * ��ʾsettings��ͼ��Ĵ���
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_no_aboutme, menu);
		return true;
	}
	//����ʵ��actionbar�ϵİ�ť������в����ķ�ӳ�Ĺ���
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	         switch (item.getItemId()) {  
	         case R.id.action_settings:  
	                Toast.makeText(this, "�����ˡ�settings��������", Toast.LENGTH_SHORT).show(); 	                      
	                return true;  
	            case android.R.id.home:	//actionbar�ķ���ͼ��idֵ��Ӧ���ǹ̶���
	            	Toast.makeText(this, "��ֱ���˳�APP�����Ƿ�����һ��", Toast.LENGTH_SHORT).show(); 
	            	finish();
	                return true; 
	            default:  
	                return super.onOptionsItemSelected(item);  
	         }  
	    }
}
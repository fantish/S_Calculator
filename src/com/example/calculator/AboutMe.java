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
							"Help:Readme below" ,"频点计算器使用规则：\n1）、根据频点号来推算上下频点，先输入UP_S或者" +
							"DOWN，然后输入1到1388频点号，按\"=\"号即可得到结果。" +
							"\n2)、根据频率反推上下行频点号，输入正确的频率范围，然后点INV，即可得上行或下行频点号。"};
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
 * 显示settings的图标的代码
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_no_aboutme, menu);
		return true;
	}
	//用来实现actionbar上的按钮点击后有操作的反映的功能
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	         switch (item.getItemId()) {  
	         case R.id.action_settings:  
	                Toast.makeText(this, "你点击了“settings”按键！", Toast.LENGTH_SHORT).show(); 	                      
	                return true;  
	            case android.R.id.home:	//actionbar的返回图标id值，应该是固定的
	            	Toast.makeText(this, "不直接退出APP，而是返回上一层", Toast.LENGTH_SHORT).show(); 
	            	finish();
	                return true; 
	            default:  
	                return super.onOptionsItemSelected(item);  
	         }  
	    }
}
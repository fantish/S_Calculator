package com.example.calculator;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.calculator.InputItem.InputType;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
	
	private TextView mShowResultTv;  //显示结果
	private TextView mShowInputTv;   //显示输入的字符
	private static String num = "0"; // 显示的结果
	public static double df1;
	private Button mCBtn;
	private Button mDelBtn;
	private Button mAddBtn;
	private Button mSubBtn;
	private Button mMultiplyBtn;
	private Button mDividebtn;
	private Button mZeroButton;
	private Button mOnebtn;
	private Button mTwoBtn;
	private Button mThreeBtn;
	private Button mFourBtn;
	private Button mFiveBtn;
	private Button mSixBtn;
	private Button mSevenBtn;
	private Button mEightBtn;
	private Button mNineBtn;
	private Button mPointtn;
	private Button mEqualBtn;
	private Button mUpBtn;  //sql
	private Button mDownBtn;  //sql
	private Button mInvBtn;   //sql 频点号逆转换按钮
	
	private HashMap<View,String> map; //将View和String映射起来 
	private List<InputItem> mInputList; //定义记录每次输入的数
	//因为List是集合框架，List接口类在定义是已经是List<E>，所以这里声明是直接确定了E的具体类型。
	//所有的标准集合接口都是泛型化的 —— Collection<V>、List<V>、Set<V> 和 Map<K,V>。
	
	private int mLastInputstatus = INPUT_NUMBER; //记录上一次输入状态
	
	private int sInputstatus = OTHERS_UPDOWN;	//记录上下行按钮输入状态
	
	public static final int INPUT_UP = 1; 
	public static final int OTHERS_UPDOWN = 0;
	public static final int INPUT_DOWN = -1; 
	
	public static final int INPUT_NUMBER = 1; 
	public static final int INPUT_POINT = 0;
	public static final int INPUT_OPERATOR = -1;
	public static final int END = -2;
	public static final int ERROR= -3;
	public static final int SHOW_RESULT_DATA = 1;
	public static final String nan = "NaN";
	public static final String infinite = "∞";
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		public void handleMessage(Message msg) {
			
			if(msg.what == SHOW_RESULT_DATA){
				mShowResultTv.setText(mShowInputTv.getText());	//获取上一次在mShowInputTv的数据显示在mShowResultTv
				mShowInputTv.setText(mInputList.get(0).getInput());
				clearScreen(mInputList.get(0));
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	//	setTitle("Yanis"); 
		//设置 actionbar 返回键
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		//导包是import java.lang.reflect.Field;  
		//解决 Actionbar 溢出菜单不显示的问题
		try {   
			ViewConfiguration mconfig = ViewConfiguration.get(this);  
			       Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");  
			       if(menuKeyField != null) {  
			           menuKeyField.setAccessible(true);  
			           menuKeyField.setBoolean(mconfig, false);  
			       }  
			   } catch (Exception ex) {  
			   } 

		initView(); 
		initData(); 
	}
	/**
	 * 初始化view
	 */
	private void initView() {
		mShowResultTv = (TextView) this.findViewById(R.id.show_result_tv);
		mShowInputTv = (TextView)this.findViewById(R.id.show_input_tv);
		mCBtn = (Button)this.findViewById(R.id.c_btn);
		mDelBtn= (Button)this.findViewById(R.id.del_btn);
		mAddBtn= (Button)this.findViewById(R.id.add_btn);
		mMultiplyBtn= (Button)this.findViewById(R.id.multiply_btn);
		mDividebtn= (Button)this.findViewById(R.id.divide_btn);
		mZeroButton = (Button)this.findViewById(R.id.zero_btn);
		mOnebtn= (Button)this.findViewById(R.id.one_btn);
		mTwoBtn= (Button)this.findViewById(R.id.two_btn);
		mThreeBtn= (Button)this.findViewById(R.id.three_btn);
		mFourBtn= (Button)this.findViewById(R.id.four_btn);
		mFiveBtn= (Button)this.findViewById(R.id.five_btn);
		mSixBtn= (Button)this.findViewById(R.id.six_btn);
		mSevenBtn= (Button)this.findViewById(R.id.seven_btn);
		mEightBtn= (Button)this.findViewById(R.id.eight_btn);
		mNineBtn= (Button)this.findViewById(R.id.nine_btn);
		mPointtn= (Button)this.findViewById(R.id.point_btn);
		mEqualBtn= (Button)this.findViewById(R.id.equal_btn);
		mSubBtn = (Button)this.findViewById(R.id.sub_btn);
		mUpBtn = (Button)this.findViewById(R.id.up_btn);   //sql
		mDownBtn = (Button)this.findViewById(R.id.down_btn);   //sql
		mInvBtn = (Button)this.findViewById(R.id.inv_btn);    //sql   
		setOnClickListener();//调用监听事件
		
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		if(map == null)
			map = new HashMap<View, String>();
		map.put(mAddBtn,getResources().getString(R.string.add));
		map.put(mMultiplyBtn,getResources().getString(R.string.multply));
		map.put(mDividebtn,getResources().getString(R.string.divide));
		map.put(mSubBtn, getResources().getString(R.string.sub));
		map.put(mZeroButton ,getResources().getString(R.string.zero));
		map.put(mOnebtn,getResources().getString(R.string.one));
		map.put(mTwoBtn,getResources().getString(R.string.two));
		map.put(mThreeBtn,getResources().getString(R.string.three));
		map.put(mFourBtn,getResources().getString(R.string.four));
		map.put(mFiveBtn,getResources().getString(R.string.five));
		map.put(mSixBtn,getResources().getString(R.string.six));
		map.put(mSevenBtn,getResources().getString(R.string.seven));
		map.put(mEightBtn,getResources().getString(R.string.eight));
		map.put(mNineBtn,getResources().getString(R.string.nine));
		map.put(mPointtn,getResources().getString(R.string.point));
		map.put(mEqualBtn,getResources().getString(R.string.equal));
		map.put(mUpBtn, getResources().getString(R.string.up_btns));  //sql    注意是string而不是id    在string.xml定义的
		map.put(mDownBtn, getResources().getString(R.string.down_btns));  //sql
	
		mInputList = new ArrayList<InputItem>();
		mShowResultTv.setText("shang-mian-test");    //负责上面显示用
		mShowInputTv.setText("xia-mian-test");		 //负责下面显示用
		clearAllScreen();
	}

	/**
	 * 设置监听事件
	 */
	private void setOnClickListener() {
		mCBtn.setOnClickListener(this);
		mDelBtn.setOnClickListener(this);
		mAddBtn.setOnClickListener(this);
		mMultiplyBtn.setOnClickListener(this);
		mDividebtn.setOnClickListener(this);
		mSubBtn.setOnClickListener(this);
		mZeroButton.setOnClickListener(this);
		mOnebtn.setOnClickListener(this);
		mTwoBtn.setOnClickListener(this);
		mThreeBtn.setOnClickListener(this);
		mFourBtn.setOnClickListener(this);
		mFiveBtn.setOnClickListener(this);
		mSixBtn.setOnClickListener(this);
		mSevenBtn.setOnClickListener(this);
		mEightBtn.setOnClickListener(this);
		mNineBtn.setOnClickListener(this);
		mPointtn.setOnClickListener(this);
		mEqualBtn.setOnClickListener(this);
		mUpBtn.setOnClickListener(this);  //sql
		mDownBtn.setOnClickListener(this);  //sql
		mInvBtn.setOnClickListener(this);  //sql
				
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.c_btn:
			clearAllScreen();
			break;
		case R.id.del_btn:
			back();
			break;
		case R.id.point_btn:
			inputPoint(arg0);
			break;
		case R.id.equal_btn:
			operator();
			break;
		case R.id.add_btn:
		case R.id.sub_btn:
		case R.id.multiply_btn:
		case R.id.divide_btn:
			inputOperator(arg0);
			break;
		case R.id.up_btn:    
			sInputstatus = INPUT_UP;	
			df1=operatorUpOrDown(arg0);	
			
			break;
		case R.id.down_btn:		//先输入down，再输入数字（带超出范围提示功能），最后输入等于号出结果。
			sInputstatus = INPUT_DOWN;	//add by sql
			df1=operatorUpOrDown(arg0);
		
			break;
		case R.id.inv_btn:
			sInvertOperator();
			break;
		default:
			inputNumber(arg0);
			break;
		}
	}
	/**
	 * 点击=之后开始运算
	 */
	private void operator() {
		if (sInputstatus != OTHERS_UPDOWN) {
			mShowResultTv.setText(mShowInputTv.getText());	//实现输入的数据上屏
			num = (mInputList.get(0).getInput());	//从mInputList中获取输入的数据值
			String regex = "\\d+\\.\\d+";	//判断输入字符串是否为小数，如果是重新输入。
			if (num.matches(regex)) {
				mShowInputTv.setText("非法输入！");
				clearScreen(mInputList.get(0));
				sInputstatus = OTHERS_UPDOWN;
				return;
			}
			if(Integer.parseInt(num)>=1389 || Integer.parseInt(num)<1){	//Double.parseDouble(num)
						//Integer.parseInt(num) 化字符串为整数，但输入必须为整数样式的字符串，否则app奔溃退出。如161.0等
					mShowInputTv.setText("1~1388，重新输入!");
					return;
				}
			num= BigDecimal.valueOf(0.0216).multiply(new BigDecimal(num)).stripTrailingZeros().toString();	
			num= BigDecimal.valueOf(df1).add(new BigDecimal(num)).stripTrailingZeros().toString();	
			mInputList.set(0,new InputItem( String.valueOf(num),InputItem.InputType.DOUBLE_TYPE));
			mShowInputTv.setText(mInputList.get(0).getInput());
			clearScreen(mInputList.get(0));	
			sInputstatus = OTHERS_UPDOWN;
			return;
		}
		if(mLastInputstatus == END ||mLastInputstatus == ERROR || mLastInputstatus == INPUT_OPERATOR|| mInputList.size()==1){
			return;
		}
		mShowResultTv.setText("");
		startAnim();
		findHighOperator(0);
		if(mLastInputstatus != ERROR){
			findLowOperator(0);
		}
		mHandler.sendMessageDelayed(mHandler.obtainMessage(SHOW_RESULT_DATA), 300);
	}
	
	private void startAnim(){
		mShowInputTv.setText(mShowInputTv.getText()+getResources().getString(R.string.equal));
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.screen_anim);
		mShowInputTv.startAnimation(anim);
	}
	/**
	 * 输入点
	 * @param view
	 */
	private void inputPoint(View view) {
		if(mLastInputstatus == INPUT_POINT){
			return;
		}
		if(mLastInputstatus == END || mLastInputstatus == ERROR){
			clearInputScreen();
		}
		String key = map.get(view);
		String input = mShowInputTv.getText().toString();
		if(mLastInputstatus == INPUT_OPERATOR){
			input = input+"0";
		} 
		mShowInputTv.setText(input+key);
		addInputList(INPUT_POINT, key);
	}
	/**
	 * 输入数字
	 * @param view
	 */
	private void inputNumber(View view){
		if(mLastInputstatus == END || mLastInputstatus == ERROR){
			clearInputScreen();
		}
		String key = map.get(view);
		if("0".equals(mShowInputTv.getText().toString())){
			mShowInputTv.setText(key);
		}else{
		mShowInputTv.setText(mShowInputTv.getText() + key);
		}
		addInputList(INPUT_NUMBER, key);
	}
	/**
	 * 输入运算符
	 * @param view
	 */
	private void inputOperator(View view) {
		if(mLastInputstatus == INPUT_OPERATOR || mLastInputstatus == ERROR){
			return;
		}
		if(mLastInputstatus == END){
			mLastInputstatus = INPUT_NUMBER;
		}

		String key = map.get(view);
		if("0".equals(mShowInputTv.getText().toString())){
			mShowInputTv.setText("0"+key);
			mInputList.set(0,new InputItem("0",InputItem.InputType.INT_TYPE));
		}else{
		mShowInputTv.setText(mShowInputTv.getText() + key);
		}
		addInputList(INPUT_OPERATOR, key);
	}
	/**
	 * 这是用来进行区分上行还是下行频点换算的基数的，默认返回0
	 * @author Fantish
	 */
	private double operatorUpOrDown(View view) {
		String key = map.get(view);
		mShowInputTv.setText(key);
		if(sInputstatus == INPUT_DOWN){			
			return 2169.9892;
		}else if (sInputstatus == INPUT_UP) {		
			return 1979.9892;
		}else {
			return 0;
		}
	}
	/**
	 * 用来根据输入的频率反向推算出S频道对应的频点号，
	 * 输入的数据介于1979.9892~2010(up) 和 2169.9892~2200(down)，否则提示“超出范围，重新输入”。
	 * @author Fantish	
	 */
	private void sInvertOperator() {
		String str = mShowInputTv.getText().toString();
		if(str.length() == 1){	//防止首先点INV按钮软件崩溃额情况，提示正确规范操作方法。
			if (str.equals("0")) {
				mShowInputTv.setText("请正确规范输入!");
				return;	
			}					
		}
		
		if (mLastInputstatus == ERROR || mLastInputstatus == INPUT_OPERATOR) {// mLastInputstatus == INPUT_POINT 不能加这个
			return;		//mLastInputstatus == END 去掉它可以实现频点、频点号来回逆向转换 
		}		
		int inv;	//获得的输入频点的取整值
		String inv_upordown;	//在频点号前面显示上行下行的字符串
			mShowResultTv.setText(mShowInputTv.getText());	//实现输入的数据上屏
			num = (mInputList.get(0).getInput());	//从mInputList中获取输入的数据值
				if((Double.parseDouble(num) >1980.0 && Double.parseDouble(num) < 2010 ) || (Double.parseDouble(num) >2170 && Double.parseDouble(num) < 2200 ) ){
					mShowInputTv.setText("你的输入正确!");
					if ((Double.parseDouble(num) >1980 && Double.parseDouble(num) < 2010 )) {						
						num= BigDecimal.valueOf(-1980).add(new BigDecimal(num)).stripTrailingZeros().toString();
						inv_upordown = "UP ";
					}	//用1980比-1979.9892更准确，更易取中间值
					else {
						num= BigDecimal.valueOf(-2170).add(new BigDecimal(num)).stripTrailingZeros().toString();	
						inv_upordown = "DN ";
					}
					
					inv = (int)(Double.parseDouble(num)/0.0216)+1;
					mInputList.set(0,new InputItem(inv_upordown + String.valueOf(inv),InputItem.InputType.INT_TYPE));
					mShowInputTv.setText(mInputList.get(0).getInput());
					
				}else {
					mShowInputTv.setText("超范围，重新输入!");								
				}
				
				clearScreen(mInputList.get(0));	
	}
	
	/**
	 * 回退操作
	 */
	private void back() {
		if(mLastInputstatus == ERROR){
			clearInputScreen();
		}
		String str = mShowInputTv.getText().toString();
		if(str.length() != 1){
			mShowInputTv.setText(str.substring(0, str.length()-1));
			backList();
		}else{
			mShowInputTv.setText(getResources().getString(R.string.zero));
			clearScreen(new InputItem("",InputItem.InputType.INT_TYPE));
		}
	}
	/**
	 * 回退InputList操作
	 */
	private void backList() {
		InputItem item = mInputList.get(mInputList.size() - 1);
		if (item.getType() == InputItem.InputType.INT_TYPE) {
			//获取到最后一个item,并去掉最后一个字符
			String input = item.getInput().substring(0,
					item.getInput().length() - 1);
			//如果截完了，则移除这个item，并将当前状态改为运算操作符
			if ("".equals(input)) {
				mInputList.remove(item);
				mLastInputstatus = INPUT_OPERATOR;
			} else {
				//否则设置item为截取完的字符串，并将当前状态改为number
				item.setInput(input);
				mLastInputstatus = INPUT_NUMBER;
			}
			//如果item是运算操作符 则移除。
		} else if (item.getType() == InputItem.InputType.OPERATOR_TYPE) {
			mInputList.remove(item);
			if (mInputList.get(mInputList.size() - 1).getType() == InputItem.InputType.INT_TYPE) {
				mLastInputstatus = INPUT_NUMBER;
			} else {
				mLastInputstatus = INPUT_POINT;
			}
			//如果当前item是小数
		} else {
			String input = item.getInput().substring(0,
					item.getInput().length() - 1);
			if ("".equals(input)) {
				mInputList.remove(item);
				mLastInputstatus = INPUT_OPERATOR;
			} else {
				if (input.contains(".")) {
					item.setInput(input);
					mLastInputstatus = INPUT_POINT;
				} else {
					item.setInput(input);
					mLastInputstatus = INPUT_NUMBER;
				}
			}
		}
	}
	//清理屏
	private void clearAllScreen() {
		
		clearResultScreen();
		clearInputScreen();
		sInputstatus = OTHERS_UPDOWN;
	}
	private void clearResultScreen(){
		mShowResultTv.setText("");
	}
	
	private void clearInputScreen() {
		mShowInputTv.setText(getResources().getString(R.string.zero));
		mLastInputstatus = INPUT_NUMBER;
		mInputList.clear();
		mInputList.add(new InputItem("", InputItem.InputType.INT_TYPE));
	}
	//计算完成
	private void clearScreen(InputItem item) {
		if(mLastInputstatus != ERROR){
			mLastInputstatus = END;
		}
		mInputList.clear();
		mInputList.add(item);
	}
	
	//实现高级运算    乘除法运算
	public int findHighOperator(int index) {
		if (mInputList.size() > 1 && index >= 0 && index < mInputList.size())
			for (int i = index; i < mInputList.size(); i++) {
					InputItem item = mInputList.get(i);
				if (getResources().getString(R.string.divide).equals(item.getInput())
						|| getResources().getString(R.string.multply).equals(item.getInput())) {
					int a,b; double c,d;   //因为a b是int型的原因，造成乘法的输入值乘法结果不能大于65536*32768=2^31。
					if(mInputList.get(i - 1).getType() == InputItem.InputType.INT_TYPE){
						a = Integer.parseInt(mInputList.get(i - 1).getInput());
						if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
							b = Integer.parseInt(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.multply).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(a * b),InputItem.InputType.INT_TYPE));
							}else{
								if(b == 0){
									mLastInputstatus = ERROR;
									if(a==0){
										clearScreen(new InputItem(nan,InputType.ERROR));
									}else{
										clearScreen(new InputItem(infinite,InputType.ERROR));
									}
									return -1;
								}else if(a % b != 0){
									mInputList.set(i - 1,new InputItem(String.valueOf((double)a / b),InputItem.InputType.DOUBLE_TYPE));
								}else{
									mInputList.set(i - 1,new InputItem(String.valueOf((Integer)a / b),InputItem.InputType.INT_TYPE));
								}
							}
						}else{
							d = Double.parseDouble(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.multply).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(a * d),InputItem.InputType.DOUBLE_TYPE));
							}else{
								if(d == 0){
									mLastInputstatus = ERROR;
									if(a==0){
										clearScreen(new InputItem(nan,InputType.ERROR));
									}else{
										clearScreen(new InputItem(infinite,InputType.ERROR));
									}
									return -1;
								}
								mInputList.set(i - 1,new InputItem(String.valueOf(a / d),InputItem.InputType.DOUBLE_TYPE));	
							}
						}
					}else{
						c = Double.parseDouble(mInputList.get(i-1).getInput());
						if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
							b = Integer.parseInt(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.multply).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(c* b),InputItem.InputType.DOUBLE_TYPE));
							}else{
								if(b== 0){
									mLastInputstatus = ERROR;
									if(c==0){
										clearScreen(new InputItem(nan,InputType.ERROR));
									}else{
										clearScreen(new InputItem(infinite,InputType.ERROR));
									}
									return -1;
								}
								mInputList.set(i - 1,new InputItem(String.valueOf(c / b),InputItem.InputType.DOUBLE_TYPE));	
							}
						}else{
							d = Double.parseDouble(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.multply).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(mul(c,d)),InputItem.InputType.DOUBLE_TYPE));
							}else{
								if(d == 0){
									mLastInputstatus = ERROR;
									if(c==0){
										clearScreen(new InputItem(nan,InputType.ERROR));
									}else{
										clearScreen(new InputItem(infinite,InputType.ERROR));
									}
									return -1;
								}
								mInputList.set(i - 1,new InputItem(String.valueOf(div(c, d)),InputItem.InputType.DOUBLE_TYPE));	
							}
						}
					}
					mInputList.remove(i + 1);
					mInputList.remove(i);
					return findHighOperator(i);
				}
			}
		return -1;

	}
	//  低级运算  加减法
	public int findLowOperator(int index) {
		if (mInputList.size()>1 && index >= 0 && index < mInputList.size())
			for (int i = index; i < mInputList.size(); i++) {
					InputItem item = mInputList.get(i);
				if (getResources().getString(R.string.sub).equals(item.getInput())
						|| getResources().getString(R.string.add).equals(item.getInput())) {
					int a,b; double c,d;
					if(mInputList.get(i - 1).getType() == InputItem.InputType.INT_TYPE){
						a = Integer.parseInt(mInputList.get(i - 1).getInput());
						if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
							b = Integer.parseInt(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.add).equals(item.getInput())){
							mInputList.set(i - 1,new InputItem( String.valueOf(a + b),InputItem.InputType.INT_TYPE));
							}else{
							mInputList.set(i - 1,new InputItem(String.valueOf(a - b),InputItem.InputType.INT_TYPE));	
							}
						}else{
							d = Double.parseDouble(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.add).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(a + d),InputItem.InputType.DOUBLE_TYPE));
							}else{
								mInputList.set(i - 1,new InputItem(String.valueOf(a - d),InputItem.InputType.DOUBLE_TYPE));	
							}
						}
					}else{
						c = Double.parseDouble(mInputList.get(i-1).getInput());
						if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
							b = Integer.parseInt(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.add).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(c + b),InputItem.InputType.DOUBLE_TYPE));
							}else{
								mInputList.set(i - 1,new InputItem(String.valueOf(c - b),InputItem.InputType.DOUBLE_TYPE));	
							}
						}else{
							d = Double.parseDouble(mInputList.get(i + 1).getInput());
							if(getResources().getString(R.string.add).equals(item.getInput())){
								mInputList.set(i - 1,new InputItem( String.valueOf(add(c, d)),InputItem.InputType.DOUBLE_TYPE));
							}else{
								mInputList.set(i - 1,new InputItem(String.valueOf(sub(c,d)),InputItem.InputType.DOUBLE_TYPE));	
							}
						}
					}
					mInputList.remove(i + 1);
					mInputList.remove(i);
					return findLowOperator(i);
				}
			}
		return -1;

	}
	//currentStatus 当前状态  9  "9" "+"
	void addInputList(int currentStatus,String inputChar){
		switch (currentStatus) {
		case INPUT_NUMBER:
			if(mLastInputstatus == INPUT_NUMBER){
				InputItem item = (InputItem)mInputList.get(mInputList.size()-1);
				item.setInput(item.getInput()+inputChar);
				item.setType(InputItem.InputType.INT_TYPE);
				mLastInputstatus = INPUT_NUMBER;
			}else if(mLastInputstatus == INPUT_OPERATOR){
				InputItem item = new InputItem(inputChar, InputItem.InputType.INT_TYPE);
				mInputList.add(item);
				mLastInputstatus = INPUT_NUMBER;
			}else if(mLastInputstatus == INPUT_POINT){
				InputItem item = (InputItem)mInputList.get(mInputList.size()-1);
				item.setInput(item.getInput()+inputChar);
				item.setType(InputItem.InputType.DOUBLE_TYPE);
				mLastInputstatus = INPUT_POINT;
			}
		
			break;
		case INPUT_OPERATOR:
				InputItem item = new InputItem(inputChar, InputItem.InputType.OPERATOR_TYPE);
				mInputList.add(item);
				mLastInputstatus = INPUT_OPERATOR;
			break;
		case INPUT_POINT://point
			 if(mLastInputstatus == INPUT_OPERATOR){
				 InputItem item1 =  new InputItem("0"+inputChar,InputItem.InputType.DOUBLE_TYPE);
				 mInputList.add(item1); 
				 mLastInputstatus = INPUT_POINT;
			}else{
				InputItem item1 = (InputItem)mInputList.get(mInputList.size()-1);
				item1.setInput(item1.getInput()+inputChar);
				item1.setType(InputItem.InputType.DOUBLE_TYPE);
				mLastInputstatus = INPUT_POINT;
			}
			break;
			
		}
	}
	
	   public static Double div(Double v1,Double v2){
	        BigDecimal b1 = new BigDecimal(v1.toString());
	        BigDecimal b2 = new BigDecimal(v2.toString());
	        return b1.divide(b2,10,BigDecimal.ROUND_HALF_UP).doubleValue();
	    }
	   
	   public static Double sub(Double v1,Double v2){
	        BigDecimal b1 = new BigDecimal(v1.toString());
	        BigDecimal b2 = new BigDecimal(v2.toString());
	        return b1.subtract(b2).doubleValue();
	    }
	   
	   public static Double add(Double v1,Double v2){
	        BigDecimal b1 = new BigDecimal(v1.toString());
	        BigDecimal b2 = new BigDecimal(v2.toString());
	        return b1.add(b2).doubleValue();
	    }
	   
	   public static Double mul(Double v1,Double v2){
	        BigDecimal b1 = new BigDecimal(v1.toString());
	        BigDecimal b2 = new BigDecimal(v2.toString());
	        return b1.multiply(b2).doubleValue();
	    }
	   /**
	    * 显示settings的图标的代码
	    */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		//用来实现actionbar上的按钮点击后有操作的反映的功能
		 @Override
		    public boolean onOptionsItemSelected(MenuItem item) {
		         switch (item.getItemId()) {  
		         case R.id.action_settings:  
		                Toast.makeText(this, "你点击了“settings”按键！", Toast.LENGTH_SHORT).show();  		                
		                return true;  
		            case R.id.about:  		         
		            	Intent intent = new Intent(MainActivity.this,AboutMe.class);
		            	startActivity(intent);
		                return true;  
		            case android.R.id.home:	//actionbar的返回图标id值，应该是固定的
		            	Toast.makeText(this, "已经在最顶层！", Toast.LENGTH_SHORT).show(); 
		                 return true; 
		            default:  
		                return super.onOptionsItemSelected(item);  
		         }  
		    }
}

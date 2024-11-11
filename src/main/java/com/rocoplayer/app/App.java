package com.rocoplayer.app;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.rocoplayer.app.config.AppConfig;
import com.rocoplayer.app.ui.MainFrame;




/**
 * 启动类
 * @author ZJ
 *
 */
public class App {

	public static void main(String[] args) {

		AppConfig.init();
		
		initTheme();

		MainFrame mainFrame = new MainFrame();
		mainFrame.init();



	}
	
	/**
	 * 设置主题
	 */
	private static void initTheme() {
		//可以直接这样设置主题
//		FlatLightLaf.setup();

		//暗色主题
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//白色主题
/*		try {
		    UIManager.setLookAndFeel( new FlatLightLaf() );
		} catch( Exception ex ) {
			ex.printStackTrace();
		}*/
	}








}

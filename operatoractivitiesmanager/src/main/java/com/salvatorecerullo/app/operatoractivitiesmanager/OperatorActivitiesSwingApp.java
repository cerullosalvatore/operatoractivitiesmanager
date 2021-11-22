package com.salvatorecerullo.app.operatoractivitiesmanager;

import java.awt.EventQueue;

import com.salvatorecerullo.app.operatoractivitiesmanager.view.swing.OperatorActivitiesManagerView;

public class OperatorActivitiesSwingApp {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperatorActivitiesManagerView frame = new OperatorActivitiesManagerView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

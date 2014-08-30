package com.jiec.contact.model;

import net.sf.json.JSONObject;


public class Message implements java.io.Serializable{

	private static int sSeq = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int mesSeq;
	
	private int mesCmd;
	
	private JSONObject object;

	public Message() {
		
	}

	public Message(int mesCmd, JSONObject object) {
		super();
		this.mesSeq = sSeq++;
		this.mesCmd = mesCmd;
		this.object = object;
	}



	public int getMesSeq() {
		return mesSeq;
	}

	public void setMesSeq(int mesSeq) {
		this.mesSeq = mesSeq;
	}

	public int getMesCmd() {
		return mesCmd;
	}

	public void setMesCmd(int mesCmd) {
		this.mesCmd = mesCmd;
	}

	public JSONObject getObject() {
		return object;
	}

	public void setObject(JSONObject object) {
		this.object = object;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}

package gogoal.api;


public class Constant {

	/**
	 * 前端cook参数
	 */
	public final static String  Cookie_Date="date";
	public final static String  Cookie_Accountid="Accou";
	public final static String  Cookie_Token="ton";
	public final static String  Cookie_AuotLogin="al";
	
	public final static int     Token_RefreshDate=25;
	
	/**
	 * api 
	 * 
	 */
	public final static String  API_Login="v1/account/login";
	public final static String  API_Logout="v1/account/logout";
	public final static String  API_Token="v1/account/getToken";
	public final static String 	API_LoginStatus="v1/account/loginstatus";
	public final static String  API_Upload="v1/userinfo/upload";
}

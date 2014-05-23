package com.royalstone.pos.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

/**	Operator ģ��ϵͳ�еĲ�����Ա,��������Ա,�����鳤�Ƚ�ɫ.
 * @version 1.0 2004.05.25
 * @author  Mengluoyi, Royalstone Co., Ltd.
 */
public class Operator implements Serializable
{
	/**
	 * @param id	����ԱID
	 * @param pin	��¼����(����)
	 * @param name	����Ա����
	 * @param native_privileges		���в���Ȩ��
	 */
	public Operator( String id, String pin, String name, int[] native_privileges )
	{
		this.id   = id;
		this.pin  = pin;
		this.name = name;
		this.native_privileges = new int[ native_privileges.length ];
		System.arraycopy( native_privileges, 0, this.native_privileges, 0, native_privileges.length );
		Arrays.sort( this.native_privileges );
	}

        /**
         * @param id	����ԱID
         * @param pin	��¼����(����)
         * @param name	����Ա����
         * @param max_disc ����Ա���Ĵ��۽���
         * @param native_privileges		���в���Ȩ��
         */
        public Operator( String id, String pin, String name,int max_disc, int[] native_privileges )
        {
                this.id   = id;
                this.pin  = pin;
                this.name = name;
                this.max_disc = max_disc;
                this.native_privileges = new int[ native_privileges.length ];
                System.arraycopy( native_privileges, 0, this.native_privileges, 0, native_privileges.length );
                Arrays.sort( this.native_privileges );
        }


        // �ٻ�Ԥ����--------
    public Operator (String money, boolean isA)
    {
    	this.money = money;
    	this.isA = isA;
    	
    	}
    
    public Operator(int count, boolean isa)
    {
    	this.count = count;
    	this.isa = isa;
    	}
    
    // ��ȡ��
    public Operator(BigDecimal coun, String typeid, String goodsid){
    	
    	this.coun = coun;
    	this.typeid = typeid;
    	this.goodsid =  goodsid;
    	
    	}
    
    public int getcoun(){
    	return (int)Math.rint(coun.doubleValue());
    	}
    public String gettypeid(){
    	if (typeid == null){
    		return null;
    		}
    	return typeid.substring(0,4);
    	}
    public String getgoodsid(){
    	return goodsid;
    	}
    
    BigDecimal coun;
    String typeid = null;
    String goodsid = null;
    
    
    
    // ��ȡԤ������ˮ��
    public Operator (int orderid)
    {
    	this.orderid = orderid;
    	}
    
    public int getorderid()
    {
    	return orderid;
    	}
    
    int orderid;
    String money;
    boolean isA = true;
    
    int count;
    boolean isa = false;
    
    public String getMoney(){
    	return money;
    	}
    
    public boolean getisA(){
    	return isA;
    	}
    
    public int getCount(){
    	return count;
    	}
    public boolean getisa(){
    	return isa;
    	}
     // ---------------

	/**
	 * @param id	����ԱID
	 */
	public Operator( String id )
	{
		this.id = id;
		this.pin = "";
		this.name = "";
	}

	/**
	 * @return	����ԱID
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return	����Ա����
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return		����Ա����������
	 */
	public String getPinEncrypted()
	{
		return pin;
	}

        /**
         *
         * @return �˲���Ա�����ۿ۽���
         */
        public int getMax_Disc(){
          return max_disc;
        }

	/**	�˺������Դ����ID���������У��,��Ҫ��������Ա��¼����Ȩ����.
	 * @param id	����ԱID
	 * @param pin	����Ա���������(����)
	 * @return		true	����У��ͨ��;<br/>
	 * 				false	����У��ʧ��.
	 */
	public boolean check( String id, String pin )
	{
		return ( this.id.equals( id ) && this.pin.equals( encryptPin( id, pin ) ) );
	}

	/**	У������
	 * @param pin	��������
	 * @return		true	У��ɹ�;<br/>
	 * 				false	У��ʧ��.
	 */
	public boolean checkPlainPin( String pin )
	{
		return ( this.pin.equals( encryptPin( this.id, pin ) ) );
	}

	/**	�˺��������޸Ĳ���Ա������.
	 * @param pin_plain		��������
	 */
	public void setPlainPin( String pin_plain )
	{
		this.pin = encryptPin( id, pin_plain );
	}

	/**	�˺���������Ա�Ĳ���Ȩ��.
	 * @param fun	��������
	 * @return		true	Ȩ�޼��ͨ��<br/>
	 * 				false	Ȩ�޼��ʧ��.����Ա��Ȩ����ָ���Ĳ���.
	 */
	public boolean hasPrivilege( int fun )
	{
		if( native_privileges != null
		  && Arrays.binarySearch( native_privileges, fun ) >= 0 ) return true;
		if( added_privileges != null
		  && Arrays.binarySearch( added_privileges, fun ) >= 0 ) return true;
		return false;
	}

	/**
	 * ���ò���ԱȨ��. ���ô˺�����,����Ա��ʧȥ����Ȩ����õ�Ȩ��,ֻӵ������Ȩ��.
	 */
	public void resetPrivilege()
	{
		added_privileges = null;
		authorizer	 = "";
	}

	/**	��Ȩ����. ���ô˺�����, �����߽����supervisor �Ĳ���Ȩ��.
	 * @param supervisor	��Ȩ��Ա
	 */
	public void addPrivilege( Operator supervisor )
	{
		int[] au_right = supervisor.native_privileges;
		int   len      = au_right.length;
		this.added_privileges = new int[ len ];
		System.arraycopy( au_right, 0, added_privileges, 0, len );
		Arrays.sort( this.added_privileges );

		this.authorizer = supervisor.getId();
	}

	/**	���ܵ�¼����
	 * @param id		��¼ID
	 * @param pin_plain	��¼��������
	 * @return			��¼���������
	 */
	public String encryptPin( String id, String pin_plain )
	{
		long k = 123456789;

		for( int i=0; i < id.length(); i++){
			long	a = ( (int)id.charAt(i) ) % 13 + 1;
			k = ( k * a ) % 9999999 + 1;
		}

		k = k % 98989898 + 99;
		for(int i=0; i<  pin_plain.length(); i++){
			long	a = ( (int)pin_plain.charAt(i) ) % 17 + 1;
			k = ( k % 9876543 + 1 ) * a;
		}

		DecimalFormat df = new DecimalFormat( "00000000" );
		return df.format( k % 100000000 );
	}

	/**
	 * for debug use.
	 */
	public String toString()
	{
		String s = " [ ";
		if( native_privileges.length >0 ) s += native_privileges[0];
		for (int i=1; i<=native_privileges.length-1; i++ )
			s += ", " + native_privileges[i];
		s += " ] ";
		return id + ":" + pin + ":" + name + s;
	}

	/**
	 * <code>id</code>		����ԱID
	 */
	private String id;
	/**
	 * <code>pin</code>		����Ա������(����)
	 */
	private String pin;		// encrypted pin.
	/**
	 * <code>name</code>	����Ա����
	 */
	private String name;

      /**
      * <code>max_disc</code> ����Ա�ܽ����ۿ۵�������,Ĭ��Ϊ0.
      */
       private int max_disc=0;

	/**
	 * <code>native_privileges</code>	����Ա�Լ���ӵ�е�Ȩ��<br/>
	 * <code>added_privileges</code>	����Ȩ��������õ�Ȩ��
	 */
	private int[]  native_privileges = null, added_privileges  = null;

	/**
	 * <code>authorizer</code>			��Ȩ��Ա
	 */
	private String authorizer = "";
}

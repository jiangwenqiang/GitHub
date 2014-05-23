package com.royalstone.pos.common;

final public class PosFunction
{
	public static String toString( int key )
	{
		// 格式的改动
		if (key == RESERVE)		return "RESERVE";
		if (key == ENTER) 		return "ENTER";
		if (key == CANCEL) 		return "CANCEL";
		if (key == QUANTITY) 	return "QUANTITY";
		if (key == RMB) 		return "RMB";
		if (key == HKD) 		return "HKD";
		if (key == USD) 		return "USD";
		if (key == CASH) 		return "CASH";
		if (key == CHEQUE) 		return "CHEQUE";
		if (key == VOUCHER) 	return "VOUCHER";
		if (key == CARDSHOP) 	return "CARDSHOP";
		if (key == CARDMEMBER) 	return "CARDMEMBER";
		if (key == CARDBANK) 	return "CARDBANK";
		if (key == CORRECT) 	return "CORRECT";
		if (key == QUICKCORRECT) 	return "QUICKCORRECT";
		if (key == WITHDRAW) 	return "WITHDRAW";
		if (key == ALTPRICE) 	return "ALTPRICE";
		if (key == DISCOUNT) 	return "DISCOUNT";
		if (key == DISCTOTAL) 	return "DISCTOTAL";
		if (key == DISCMONEY) 	return "DISCMONEY";
		if (key == SUBTOTAL) 	return "SUBTOTAL";
		if (key == TOTAL) 		return "TOTAL";
		if (key == EXIT) 		return "EXIT";
		if (key == NEWPASS) 	return "NEWPASS";
		if (key == LOCK) 		return "LOCK";
		if (key == HOLD) 		return "HOLD";
		if (key == CASHOUT) 	return "CASHOUT";
		if (key == FIND)		return "FIND";
		if (key == CASHIN) 		return "CASHIN";
		if (key == DELETE) 		return "整单删除";
		if (key == UP) 			return "UP";
		if (key == DOWN) 		return "DOWN";
		if (key == BACKSPACE) 	return "BACKSPACE";
		if (key == CLEAR) 		return "CLEAR";
		if (key == GOODS) 		return "GOODS";
		if (key == PAYMENT) 	return "PAYMENT";
		if (key == PRICE) 		return "PRICE";
		if (key == PIN) 		return "PIN";
		if (key == LOGON) 		return "LOGON";
		if (key == FLEE)		return "逃车";
		if (key == SAMPLE)		return "抽样检测";
		if (key == OILTEST)		return "油机测试";
		if (key == ALTVAVLUE)	return "地磅修改";
        if (key == OPENCASHBOX)	return "OPENCASHBOX";
        if (key == CardRA)      return "RaCard";
        
        
		if (key == OFFLINECLOSEWORKTURN)    return "OFFLINECLOSEWORKTURN";
		// 增加两个码值 预销售与提货
		// 301 302
		if (key == FUTRUESELL)				return "生成预定单";
		if (key == BRINGFORWARD)			return "BringForward";
		
		// 增加赠送.机团赠送.碰销..
		if (key == LARGESS)					return "确定赠送";
		if (key == GROUPLARGESS)			return "确定机团赠送";
		if (key == LARGESSL)				return "确定碰销";
		if (key == LARGESSCOUPON)           return "确定赠券";
		if (key == PROPERTY)				return "选择生产属性";
		
		return "UNDEFINED";
	}
	
	public final static int	RESERVE		= 100;		
	public final static int	ENTER		= 10;		// 确认
	public final static int	CANCEL		= 89;		// 放弃
	public final static int	QUANTITY	= 66;		// 数量
	public final static int	HKD			= 85;			 
	public final static int	RMB			= 86;			
	public final static int	USD			= 87;
	public final static int	CASH		= 71;		// 现金
	public final static int	CHEQUE		= 69;		// 支票
	public final static int	VOUCHER		= 68;		// 代币券
	public final static int	CARDSHOP	= 74;		// 提货卡
	public final static int	CARDMEMBER	= 34;		// 会员卡
	public final static int	CARDBANK	= 75;		// 银行卡
	public final static int	CARDLOAN	= 83;		// 挂账卡//挂账卡借用色码
	public final static int	CORRECT		= 67;		// 更正
	public final static int	QUICKCORRECT 	= 73;	// 即更
	public final static int	WITHDRAW	= 70;		// 退货
	public final static int	ALTPRICE	= 77;		// 变价
	public final static int	DISCOUNT	= 123;      // 单项折扣
	public final static int DISCTOTAL	= 125;		// 总额折扣
	public final static int ALTVAVLUE	= 122;		// 地磅修改
	public final static int	SUBTOTAL	= 80;		// 小计
	public final static int	TOTAL		= 79;		// 合计
	public final static int	EXIT		= 82;		// 退出
	public final static int	NEWPASS		= 78;		// 改密码
	public final static int	LOCK		= 84;		// 加锁/解锁
	public final static int	HOLD		= 72;		// 挂单
	public final static int  FIND       = 91;    	// 查询价格
	public final static int	CASHOUT		= 62;		// 出款
	public final static int	CASHIN		= 60;		// 入款
	public final static int	DELETE		= 38;		// 整单取消
	public final static int WAITER		= 40;		// 营业员输入
	public final static int DISCMONEY	= 41;		// 金额折扣
	public final static int PRINTLASTSHEET = 43;	// 重打上一单
	public final static int RETPAYMENT	= 63;		// 退款
	public final static int DELIVERY	= 76;		// 送货
	public final static int S2G			= 81;		// 超市柜组转换
	public final static int COLORSIZE	= 83;		// 色码
	public final static int GROUP		= 90;		// 柜组
	public final static int OFFLINE		= 124;		// 联/单机切换
	public final static int PROPERTY	= 94;		// 商品属性键
	// internal functions
	public final static int	BACKSPACE	= 8;		// 退格
	public final static int	CLEAR		= 88;		// 清除
	public final static int SHIFT		= 96;		// 班结
	public final static int FLEE		= 97;		// 逃车
	public final static int SAMPLE		= 98;		// 抽样检测
	public final static int OILTEST		= 99;		// 油机测试
	public final static int	POINT		= 46;
	public final static int BIZERO		= 200;		// "00"
	public final static int	UP			= 201;
	public final static int	DOWN		= 202;
	
	// virtual functions
	public final static int	GOODS		= 203;
	public final static int	PAYMENT		= 204;		// 结算
	public final static int	PRICE		= 205;
	public final static int	PIN			= 206;
	public final static int	LOGON		= 207;
	public final static int	UNDEFINED	= 255;
    public final static int OPENCASHBOX    =92;   	// 开钱箱
	public final static int OFFLINECLOSEWORKTURN     =93;     //脱机班结
	public final static int SHOWCASHBOX	=126;   // 显示钱箱状态
	public final static int ICCARD			=127;   // IC卡支付
    public final static int Coupon			=128;   // 饼券
    public final static int ticket			=129;   // 重打任意小票
    // 增加预销售与提货的码值
    public final static int FUTRUESELL 	= 301;	// 预销售
    public final static int BRINGFORWARD 	= 302;	// 提 货s
    
    public final static int LARGESS = 303; 			// 赠送
    public final static int GROUPLARGESS = 304; 	// 机团赠送
    public final static int LARGESSL = 305; 		// 碰销
    public final static int CARDCHANGE = 306;		// 会员卡积分兑换
    public final static int LARGESSCOUPON = 307; 	// 赠券
    public final static int CouponPay = 308;		// 券支付 20090802
    public final static int CardRA =309;			// 储值卡充值 20100526

}

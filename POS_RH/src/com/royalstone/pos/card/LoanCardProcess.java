package com.royalstone.pos.card;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.GoodsList;
import com.royalstone.pos.common.PosContext;
import com.royalstone.pos.common.Sale;
import com.royalstone.pos.core.PosSheet;
import com.royalstone.pos.gui.LoanCardConfirmMainCard;
import com.royalstone.pos.gui.LoanCardConfirmSubCard;
import com.royalstone.pos.gui.MSRInput;
import com.royalstone.pos.shell.pos;
import com.royalstone.pos.util.Value;

/**
 * ���˿������̣���ҵ�񷽷����á�
 * ��װ�˰�����ˢ������ѯ���û�ȷ�ϡ�֧���ȵĶ���
 * @author liangxinbiao
 */
public class LoanCardProcess {

	private SimpleDateFormat sdfDateTime =
		new SimpleDateFormat("yyyyMMddHHmmssSSS");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmssSSS");

	private String payTotal = "0";
	private String exceptionInfo;
	private String cardNo;
	private String cardBalance;

	private LoanCardPayVO payVO;
	private ILoanCard loanCard;
	private boolean isConfirm = false;

	public LoanCardProcess() {

		File dir = new File("autorever");
		if (!dir.exists())
			dir.mkdir();

	}

	/**
	 * ��һ����ʼʱˢ��
	 * @return ����
	 */
	public LoanCardQueryVO readLoanCardNum() {
		MSRInput msrInput = new MSRInput("��ˢ��:","loan");

		msrInput.show();

		try {
			while (!msrInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String inputCode = msrInput.getInputcode();

		if (!msrInput.isConfirm()) {
			isConfirm = false;
			return null;
		} else {
			isConfirm = true;
		}

		if (inputCode == null && inputCode.equals("")) {
			exceptionInfo = "���Ŵ���,�����������!";
			return null;
		}

		String cardNo = null;
		String secrety = null;

		String cardValue[] = inputCode.split("=");
		if (cardValue.length != 2) {
			cardNo = cardValue[0];
			secrety = "0";
		} else {
			cardNo = cardValue[0];
			secrety = cardValue[1];
		}

		loanCard = LoanCardFactory.createInstance();
		LoanCardQueryVO queryVO = loanCard.query(cardNo, secrety);
		if (queryVO != null && queryVO.getExceptioninfo() == null) {

			if (!checkValid(queryVO))
				return null;
		}
		if (exceptionInfo == null) {
			if (queryVO != null && queryVO.getExceptioninfo() != null) {
				exceptionInfo = queryVO.getExceptioninfo() + ",�����������!";
			} else {
				if (queryVO == null)
					exceptionInfo = "�������,�����������!";
			}
		}

		return queryVO;
	}

	/**
	 * ��һ����ʼʱˢ��������µĹ��˿��Ĳ�ѯ��ȷ���Ƿ�֧��
	 * @param inputCode ����
	 * @return �Ƿ�ȷ��֧��
	 */
	public boolean reprocess(String inputCode) {

		if (pos.core.getValue().getValueToPay() < 0) {
			exceptionInfo = "���˿�֧������Ϊ��,�����������!";
			return false;
		}

		String cardNo = null;
		String secrety = null;

		String cardValue[] = inputCode.split("=");
		if (cardValue.length != 2) {
			cardNo = cardValue[0];
			secrety = "0";
		} else {
			cardNo = cardValue[0];
			secrety = cardValue[1];
		}

		loanCard = LoanCardFactory.createInstance();
		LoanCardQueryVO queryVO = loanCard.query(cardNo, secrety);

		if (queryVO != null && queryVO.getExceptioninfo() == null) {

			if (!canconsume(queryVO))
				return false;

			if (!checkSheetMax(queryVO))
				return false;

			try {
				int iTenderAmount;
				if (pos.core.getValue().getValueToPay()
					> Integer.parseInt(queryVO.getDetail())) {
					iTenderAmount = Integer.parseInt(queryVO.getDetail());
				} else {
					iTenderAmount = pos.core.getValue().getValueToPay();
				}
				if (iTenderAmount < 0)
					iTenderAmount = 0;
				String tenderAmount = (new Value(iTenderAmount)).toValStr();

				if (pos.core.getValue().getValueToPay() > iTenderAmount) {
					exceptionInfo = "���˿������ѽ���,�����������!";
					return false;
				}

				boolean isConfirm = false;
				if (queryVO.getSubcardNo() != null
					&& !queryVO.getSubcardNo().equals("")) { //�ӿ�
					LoanCardConfirmSubCard loanCardConfirm =
						new LoanCardConfirmSubCard();
					loanCardConfirm.setCardNo(cardNo);
					loanCardConfirm.setTenderAmount(tenderAmount);
					loanCardConfirm.setCardAmount(
						(new Value(Integer.parseInt(queryVO.getDetail())))
							.toString());
					String cardBalance =
						(new Value(Integer.parseInt(queryVO.getDetail())
							- iTenderAmount))
							.toString();
					loanCardConfirm.setAtferTenderAmount(cardBalance);
					loanCardConfirm.setCustName(queryVO.getCustName());
					loanCardConfirm.setCarID(queryVO.getCarID());

					String mainAftercardBalance =
						(new Value(Integer.parseInt(queryVO.getMainBalance())
							- iTenderAmount))
							.toString();

					loanCardConfirm.setMainCardBalance(
						(new Value(Integer.parseInt(queryVO.getMainBalance())))
							.toString());
					loanCardConfirm.setAfterMaincCardBalance(
						mainAftercardBalance);

					loanCardConfirm.show();
					isConfirm = loanCardConfirm.isConfirm();

					this.cardNo = queryVO.getSubcardNo();
					this.cardBalance = cardBalance;

				} else { //����
					LoanCardConfirmMainCard loanCardConfirm =
						new LoanCardConfirmMainCard();
					loanCardConfirm.setCardNo(cardNo);
					loanCardConfirm.setTenderAmount(tenderAmount);
					loanCardConfirm.setCardAmount(
						(new Value(Integer.parseInt(queryVO.getDetail())))
							.toString());
					String cardBalance =
						(new Value(Integer.parseInt(queryVO.getDetail())
							- iTenderAmount))
							.toString();
					loanCardConfirm.setAtferTenderAmount(cardBalance);
					loanCardConfirm.setCustName(queryVO.getCustName());
					if (queryVO.getCredit() != null
						&& !queryVO.getCredit().equals("")
						&& !queryVO.getCredit().equals("0")) {
						loanCardConfirm.setPercent(
							(
								new Value(
									(int) ((Double
										.parseDouble(queryVO.getDetail())
										/ Double.parseDouble(
											queryVO.getCredit()))
								* 10000)))
								.toString()
								+ "%");
					} else {
						loanCardConfirm.disablePercent();
					}

					loanCardConfirm.show();
					isConfirm = loanCardConfirm.isConfirm();

					this.cardNo = cardNo;
					this.cardBalance = cardBalance;
				}

				if (isConfirm) {

					PosContext context = PosContext.getInstance();

					payVO = new LoanCardPayVO();
					if (queryVO.getSubcardNo() != null
						&& !queryVO.getSubcardNo().trim().equals("")) {
						payVO.setCardno(queryVO.getCardNo());
						payVO.setSubcardno(cardNo);
					} else {
						payVO.setCardno(cardNo);
					}
					payVO.setCashierid(context.getCashierid());
					payVO.setCdseq("0");
					payVO.setPassword(secrety);
					payVO.setPayvalue(Integer.toString(iTenderAmount));
					payVO.setPosid(context.getPosid());
					payVO.setSheetid(Integer.toString(context.getSheetid()));
					payVO.setShopid(context.getStoreid());
					payVO.setTime(sdfTime.format(new Date()));

					payTotal =
						Integer.toString(
							(int) Math.rint(
								Double.parseDouble(tenderAmount) * 100));

				}

				return isConfirm;
			} catch (Exception ex) {
				ex.printStackTrace();
				exceptionInfo = "��Ч����,�����������!";
			}
		}
		if (exceptionInfo == null) {
			if (queryVO != null && queryVO.getExceptioninfo() != null) {
				exceptionInfo = queryVO.getExceptioninfo();
			} else {
				if (queryVO == null)
					exceptionInfo = "�������,�����������!";
			}
		}
		return false;
	}

	/**
	 * ��֧��ʱ��ˢ��������µĿ��Ĳ�ѯ��ȷ���Ƿ�֧��
	 * @return �Ƿ�ȷ��֧��
	 */
	public boolean process() {

		if (pos.core.getValue().getValueToPay() < 0) {
			exceptionInfo = "���˿�֧������Ϊ��,�����������!";
			return false;
		}

		MSRInput msrInput = new MSRInput();
		msrInput.show();

		try {
			while (!msrInput.isFinish())
				Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String inputCode = msrInput.getInputcode();

		if (!msrInput.isConfirm())
			return false;

		if (inputCode == null && inputCode.equals("")) {
			exceptionInfo = "���Ŵ���,�����������!";
			return false;
		}

		String cardNo = null;
		String secrety = null;

		String cardValue[] = inputCode.split("=");
		if (cardValue.length != 2) {
			cardNo = cardValue[0];
			secrety = "0";
		} else {
			cardNo = cardValue[0];
			secrety = cardValue[1];
		}

		loanCard = LoanCardFactory.createInstance();
		LoanCardQueryVO queryVO = loanCard.query(cardNo, secrety);

		if (queryVO != null && queryVO.getExceptioninfo() == null) {

			if (!checkValid(queryVO))
				return false;
			//yao �޸� 05_03_07	
			if (!checkSheetMax(queryVO))
				return false;
            //-------------------------
			try {
				int iTenderAmount;
				if (pos.core.getValue().getValueToPay()
					> Integer.parseInt(queryVO.getDetail())) {
					iTenderAmount = Integer.parseInt(queryVO.getDetail());
				} else {
					iTenderAmount = pos.core.getValue().getValueToPay();
				}
				if (iTenderAmount < 0)
					iTenderAmount = 0;
				String tenderAmount = (new Value(iTenderAmount)).toValStr();

				boolean isConfirm = false;
				if (queryVO.getSubcardNo() != null
					&& !queryVO.getSubcardNo().equals("")) { //�ӿ�
					LoanCardConfirmSubCard loanCardConfirm =
						new LoanCardConfirmSubCard();
					loanCardConfirm.setCardNo(cardNo);
					loanCardConfirm.setTenderAmount(tenderAmount);
					loanCardConfirm.setCardAmount(
						(new Value(Integer.parseInt(queryVO.getDetail())))
							.toString());
					String cardBalance =
						(new Value(Integer.parseInt(queryVO.getDetail())
							- iTenderAmount))
							.toString();
					loanCardConfirm.setAtferTenderAmount(cardBalance);
					loanCardConfirm.setCustName(queryVO.getCustName());
					loanCardConfirm.setCarID(queryVO.getCarID());

					String mainAftercardBalance =
						(new Value(Integer.parseInt(queryVO.getMainBalance())
							- iTenderAmount))
							.toString();

					loanCardConfirm.setMainCardBalance(
						(new Value(Integer.parseInt(queryVO.getMainBalance())))
							.toString());
					loanCardConfirm.setAfterMaincCardBalance(
						mainAftercardBalance);

					loanCardConfirm.show();
					isConfirm = loanCardConfirm.isConfirm();

					this.cardNo = queryVO.getSubcardNo();
					this.cardBalance = cardBalance;

				} else { //����
					LoanCardConfirmMainCard loanCardConfirm =
						new LoanCardConfirmMainCard();
					loanCardConfirm.setCardNo(cardNo);
					loanCardConfirm.setTenderAmount(tenderAmount);
					loanCardConfirm.setCardAmount(
						(new Value(Integer.parseInt(queryVO.getDetail())))
							.toString());
					String cardBalance =
						(new Value(Integer.parseInt(queryVO.getDetail())
							- iTenderAmount))
							.toString();
					loanCardConfirm.setAtferTenderAmount(cardBalance);
					loanCardConfirm.setCustName(queryVO.getCustName());
					if (queryVO.getCredit() != null
						&& !queryVO.getCredit().equals("")
						&& !queryVO.getCredit().equals("0")) {
						loanCardConfirm.setPercent(
							(
								new Value(
									(int) ((Double
										.parseDouble(queryVO.getDetail())
										/ Double.parseDouble(
											queryVO.getCredit()))
								* 10000)))
								.toString()
								+ "%");
					} else {
						loanCardConfirm.disablePercent();
					}

					loanCardConfirm.show();
					isConfirm = loanCardConfirm.isConfirm();

					this.cardNo = cardNo;
					this.cardBalance = cardBalance;
				}

				if (isConfirm) {

					PosContext context = PosContext.getInstance();

					payVO = new LoanCardPayVO();
					if (queryVO.getSubcardNo() != null
						&& !queryVO.getSubcardNo().trim().equals("")) {
						payVO.setCardno(queryVO.getCardNo());
						payVO.setSubcardno(cardNo);
					} else {
						payVO.setCardno(cardNo);
					}
					payVO.setCashierid(context.getCashierid());
					payVO.setCdseq("0");
					payVO.setPassword(secrety);
					payVO.setPayvalue(Integer.toString(iTenderAmount));
					payVO.setPosid(context.getPosid());
					payVO.setSheetid(Integer.toString(context.getSheetid()));
					payVO.setShopid(context.getStoreid());
					payVO.setTime(sdfTime.format(new Date()));

					payTotal =
						Integer.toString(
							(int) Math.rint(
								Double.parseDouble(tenderAmount) * 100));

				}

				return isConfirm;
			} catch (Exception ex) {
				ex.printStackTrace();
				exceptionInfo = "��Ч����,�����������!";
			}
		}
		if (exceptionInfo == null) {
			if (queryVO != null && queryVO.getExceptioninfo() != null) {
				exceptionInfo = queryVO.getExceptioninfo() + ",�����������!";
			} else {
				if (queryVO == null)
					exceptionInfo = "�������,�����������!";
			}
		}
		return false;
	}

	/**
	 * ���˿���֧������֧��ǰ��д�����ļ�����֧���ɹ�ʱɾ�������ļ�
	 * @return �Ƿ�֧���ɹ�
	 */
	public boolean performPay() {
		synchronized (pos.Lock) {
			try {
				String filename =
					"autorever"
						+ File.separator
						+ sdfDateTime.format(new Date())
						+ ".xml";
				FileOutputStream fs = new FileOutputStream(filename);
				Document doc = new Document(payVO.toElement());
				XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
				outputter.output(doc, fs);
				fs.flush();
				fs.close();
				String result = loanCard.pay(payVO);
				if (result != null && result.equals("1")) {
					File file = new File(filename);
					file.delete();
					return true;
				} else {
					exceptionInfo = result + ",�����������!";
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				exceptionInfo = "��Ч����,�����������!";
				return false;
			}
		}
	}

	/**
	 *
	 * @return ����֧���ܶ�
	 */
	public String getPayTotal() {
		return payTotal;
	}

	/**
	 * @return ���ش�����ʾ
	 */
	public String getExceptionInfo() {
		return exceptionInfo;
	}

	/**
	 * ���Թ��˿��Ƿ������ѣ������Ƿ����ڱ������Ѻ��Ƿ��в��������Ź��˿����ѵ���Ʒ
	 * @param queryVO
	 * @return �Ƿ�Ϸ�
	 */
	public boolean checkValid(LoanCardQueryVO queryVO) {
		PosContext context = PosContext.getInstance();
		if (queryVO.getSubcardNo() != null
			&& !queryVO.getSubcardNo().trim().equals("")) { //�ӿ�

			boolean found = false;
			ArrayList shopList = queryVO.getShopIDs();
			if (shopList != null) {
				for (int i = 0; i < shopList.size(); i++) {
					if (((String) shopList.get(i))
						.equals(context.getStoreid())) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				exceptionInfo = "�˿������ڱ�������,�����������!";
				return false;
			}

			ArrayList deptList = queryVO.getDeptIDs();
			if (deptList == null || !checkSheet(deptList)) {
				exceptionInfo = "�ⵥ���в��ܱ��˿����ѵ���Ʒ,�����������!";
				return false;
			}

		} else { //����
			boolean found = false;
			ArrayList shopList = queryVO.getShopIDs();
			if (shopList != null) {
				for (int i = 0; i < shopList.size(); i++) {
					if (((String) shopList.get(i))
						.equals(context.getStoreid())) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				exceptionInfo = "�˿������ڱ�������,�����������!";
				return false;
			}
		}

		return true;
	}

	/**
	 * ���Թ��˿��Ƿ��в��������Ź��˿����ѵ���Ʒ
	 * @param queryVO
	 * @return
	 */
	public boolean canconsume(LoanCardQueryVO queryVO) {
		PosContext context = PosContext.getInstance();
		if (queryVO.getSubcardNo() != null
			&& !queryVO.getSubcardNo().trim().equals("")) {
			ArrayList deptList = queryVO.getDeptIDs();
			if (deptList == null || !checkSheet(deptList)) {
				exceptionInfo = "�ⵥ���в��ܱ��˿����ѵ���Ʒ,�����������!";
				return false;
			}
		}
		return true;
	}

	/**
	 * ���Ե�ǰ��������Ʒ��С��ʱ����deptList��
	 * @param deptList
	 * @return
	 */
	private boolean checkSheet(ArrayList deptList) {
		DecimalFormat df = new DecimalFormat("000000");
		for (int i = 0; i < deptList.size(); i++) {
			deptList.set(
				i,
				df.format(Integer.parseInt((String) deptList.get(i))));
		}
		PosSheet sheet = pos.core.getPosSheet();
		for (int i = 0; i < sheet.getSaleLen(); i++) {
			Sale sale = sheet.getSale(i);
			int qty = sheet.getSoldQty(sale.getGoods());
		   //-------------------------------
		    String strDepid=sale.getDeptid();
			String strBigGroup="";
			String strMidGroup="";
		    if(strDepid!=null&&strDepid.length()==6){
		        strBigGroup=df.format(Integer.parseInt(strDepid.substring(0,2)));
		        strMidGroup=df.format(Integer.parseInt(strDepid.substring(1,4)));
		    }
		    if(deptList.contains("000000") && qty > 0)
		      break; 
		    
		    if(!deptList.contains(strBigGroup) && qty > 0)
			   if(!deptList.contains(strMidGroup) && qty > 0)	
		   //----------------------------------
			      if (!deptList.contains(sale.getDeptid()) && qty > 0)
				     return false;
		}
		return true;
	}

	/**
	 * ���Թ����Ƿ񳬳������޶�
	 * @param queryVO
	 * @return
	 */
	private boolean checkSheetMax(LoanCardQueryVO queryVO) {
		try {
			DecimalFormat df = new DecimalFormat("000000");

			PosSheet sheet = pos.core.getPosSheet();
			GoodsList goodsList = pos.core.getGoodList();
			Goods oil =
				goodsList.find(
					df.format(Integer.parseInt(queryVO.getOilGoodsID())));
			int toPay = sheet.getValue().getValueToPay();
			int maxCanPay =
				(int) (oil.getPrice()
					* Double.parseDouble(queryVO.getMaxOilQtyPerTrans()));
					
			/*yao�޸�  05_03_07 ԭ����Ϊ if (toPay > maxCanPay) 
			 * 
			 */
			//------- 
			int maxOil=(int)Double.parseDouble(queryVO.getMaxOilQtyPerTrans()); 
			if (maxOil!=0&&toPay > maxCanPay) {
			//------
				exceptionInfo = "�˵����������޶�,�����������!";
				return false;
			}

		} catch (Exception ex) {
			exceptionInfo = "�Ҳ����ο���Ʒ,�����������!";
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @return ����
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 *
	 * @return �������Ѻ�����
	 */
	public String getCardBalance() {
		return cardBalance;
	}

	/**
	 * @return �û��Ƿ�ˢ��
	 */
	public boolean isConfirm() {
		return isConfirm;
	}

}

package com.royalstone.pos.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.royalstone.pos.card.SHCardPayVO;
import com.royalstone.pos.card.SHCardQueryVO;

/**
 * 储值卡的EJB接口
 * @deprecated 
 * @author liangxinbiao
 */
public interface ShoppingCard
    extends EJBObject {
  public SHCardQueryVO query(String CardNo, String secrety) throws
      RemoteException;

  public String pay(SHCardPayVO cardpay) throws RemoteException;

  public String autoRever(SHCardPayVO cardpay) throws RemoteException;
}

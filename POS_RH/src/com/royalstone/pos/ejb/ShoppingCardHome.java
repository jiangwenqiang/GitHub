package com.royalstone.pos.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * ��ֵ����EJB��Home�ӿ�
 * @deprecated
 * @author liangxinbiao
 */
public interface ShoppingCardHome
    extends EJBHome {
  public ShoppingCard create() throws CreateException, RemoteException;
}

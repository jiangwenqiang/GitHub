package com.royalstone.pos.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.royalstone.pos.common.AccurateList;
import com.royalstone.pos.common.Goods;
import com.royalstone.pos.common.GoodsCombList;
import com.royalstone.pos.common.GoodsCutList;
import com.royalstone.pos.common.GoodsExtList;
import com.royalstone.pos.common.GoodsList;
import com.royalstone.pos.complex.DiscComplexList;
import com.royalstone.pos.db.DiscMinister;
import com.royalstone.pos.db.PosMinister;
import com.royalstone.pos.favor.DiscountList;
import com.royalstone.pos.util.ZipFile;
import com.royalstone.pos.web.util.DBConnection;

/**
 * @author fire
 */

public class UpdatePriceServlet extends HttpServlet {
    private int fileCount=0;
    private int currentCount;
    private static String RESPONSE_CONTENT_TYPE ="text/html";
    private StringBuffer barcodeMapStr=new StringBuffer();
	public void init() throws ServletException {

    }
    public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
            int iResult=this.getPriceCount();
            double result= (double)iResult;
            int selectCount=50;
            int count=(int)Math.ceil(result/(double)selectCount);
        //-------------------------------------
            response.setContentType(RESPONSE_CONTENT_TYPE);
            ServletOutputStream sos = response.getOutputStream();
        ObjectOutputStream bos=new   ObjectOutputStream(sos);
        System.out.println("fileCount"+count);
			bos.writeObject(Integer.toString(count));
        bos.flush();
            this.fileCount=count;
            String maxCode="0";
            List priceList=new ArrayList();
            int i=0;
             for(;i<count;i++){
                 String strFile="../download/price/price"+i+".xml";
                 File newFile=new File(strFile);
                 try {
                     newFile.createNewFile();
                 } catch (IOException e) {
                 	 e.printStackTrace();
                     System.out.println("生成文件："+strFile+"发生错误！");
                      bos.writeObject(Integer.toString(-1));
                      bos.flush();
                      break;
                 }
                 try {
					maxCode=this.savePriceTable(maxCode,strFile,selectCount);
				} catch (Exception e1) {
					e1.printStackTrace();
					 bos.writeObject(Integer.toString(-1));
                     bos.flush();
                     break;
				}
                 priceList.add(maxCode);
                 this.currentCount=i;

                 bos.writeObject(Integer.toString(i));
                 bos.flush();
            }
       if(i==count){

          File mapFile=new File("../download/price/pricemap.ini");
          if(mapFile.exists())
               mapFile.delete();

          if(priceList!=null&&priceList.size()>0){
             StringBuffer sbf=new StringBuffer();
             for(int j=0;j<priceList.size();j++){
                 sbf.append(j+"="+(String)priceList.get(j)+"\n");
             }
              FileOutputStream fos=null;
              FileOutputStream barFos=null;
              try {
                  fos = new FileOutputStream(mapFile);
                  fos.write(sbf.toString().getBytes());
                  barFos=new FileOutputStream("../download/price/barcodemap.ini");
                  barFos.write(barcodeMapStr.toString().getBytes());
              } catch (IOException e) {
                   e.printStackTrace();
                    System.out.println("生成映射文件时发生错误！");
              } finally{
                 if(fos!=null)
                     try {
                         fos.close();
                     } catch (IOException e) {}
              }

          }
        //-----------------------------
        int finishFlag=0;
           try {
               this.genPromoTable();
           } catch (Exception e) {
               e.printStackTrace();
               System.out.println("生成促销文件时发生错误！");
               finishFlag=1;
           }

           //--------------------------------
        try {
                ZipFile.zip("../download/price","../download/pricetable.zip");
                ZipFile.zip("../download/promotable","../download/promotable.zip");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                System.out.println("生成压缩文件时发生错误！");
                finishFlag=1;
            }
        if(finishFlag==0)
           bos.writeObject(Integer.toString(1));
        else
           bos.writeObject(Integer.toString(-1));
        bos.flush();
       }
        bos.close();
	}
  public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
      

  }



   private int getPriceCount(){
        Connection con = null;
               Context ctx = null;
               DataSource ds = null;
               int priceSize=0;
               ResultSet rs=null;
               try {
                   ctx = new InitialContext();
                   if (ctx != null)
                       ds = (DataSource) ctx.lookup("java:comp/env/dbpos");
                   if (ds != null)
                       con = ds.getConnection();

                   if (con != null) {
                      PreparedStatement pstmt =
                       con.prepareStatement(
                           " SELECT count(*)as priceSize FROM price_lst; ");
                        rs = pstmt.executeQuery();
                        if(rs.next())
                         priceSize=rs.getInt(1);
                   }

               } catch (Exception e) {
                   e.printStackTrace();
               } finally {
                   if(rs!=null)
                       try {
                           rs.close();
                       } catch (SQLException e) {}
                   DBConnection.closeAll(null, null, con);
               }
       return priceSize;
   }
   private  String savePriceTable(String minCode,String fileName,int selectCount)throws Exception{
       Connection con = null;
		Context ctx = null;
		DataSource ds = null;
        FileOutputStream fos=null;
		try {
	       ctx = new InitialContext();
			if (ctx != null)
				ds = (DataSource) ctx.lookup("java:comp/env/dbpos");
			if (ds != null)
				con = ds.getConnection();

			if (con != null) {
				GoodsList glst = PosMinister.getGoodsList(con,minCode,selectCount);
                fos=new FileOutputStream(new File(fileName));
				XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
				outputter.setTextTrim(true);
                Document doc=null;
                try {
                    doc = new Document(glst.toElement());
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                    throw(e);
                }
                outputter.output(doc, fos);

             //生成barcode映射列表 --------------------
                if(glst!=null&&glst.size()>0){
                    for(int i=0;i<glst.size();i++){
                       Goods tempGood=glst.getGoodsByIndex(i);
                        String barcode=tempGood.getBarcode();
                          if(barcode==null) barcode="";
                        String vgno=tempGood.getVgno();
                          if(vgno==null)    vgno="";
                        barcodeMapStr.append(barcode+"="+vgno+"\n");
                    }
                }
             //--------------------------------------
                return glst.getMaxVgno();

			}


		} catch (Exception e) {
			e.printStackTrace();
			throw(e);
		} finally {
            if(fos!=null)
                try {
                    fos.close();
                } catch (IOException e) {}
			DBConnection.closeAll(null, null, con);
		}




       return null;
   }
   private void genPromoTable()throws Exception{
         DiscountList discount_lst;
        GoodsExtList goodsext_lst;
        AccurateList accurateList;
        GoodsCombList goodsCombList;
        GoodsCutList goodsCutList;
        DiscComplexList favor_lst;
        DiscMinister minister;
        Connection con = null;
		Context ctx = null;
		DataSource ds = null;
        FileOutputStream fos=null;
		try {
	       ctx = new InitialContext();
			if (ctx != null)
				ds = (DataSource) ctx.lookup("java:comp/env/dbpos");
			if (ds != null)
				con = ds.getConnection();

			if (con != null) {
               minister = new DiscMinister( con );
                File promoFile=new File("../download/promotable");
                   if(!promoFile.exists())
                     promoFile.mkdir();
               discount_lst=  minister.getDiscountList( con );
                  discount_lst.dump("../download/promotable/discount.lst");
              // favor_lst=Db4DiscComplex.getComplexList( con );
              //    favor_lst.unload("../download/price/promoTable/favor.lst");
               goodsext_lst=PosMinister.getGoodsExtList(con);
                  goodsext_lst.toXMLFile("../download/promotable/priceExt.xml");
               accurateList=PosMinister.getAccurateList(con);
                  accurateList.toXMLFile("../download/promotable/accurate.xml");
               goodsCombList=PosMinister.getGoodsCombList(con);
                  goodsCombList.toXMLFile("../download/promotable/pricecomb.xml");
               goodsCutList=PosMinister.getGoodsCutList(con);
                  goodsCutList.toXMLFile("../download/promotable/pricecut.xml");
               cardTypeToXmlFile("../download/promotable/cardtype.xml",PosMinister.getCardType(con));
            }

         } catch (Exception e) {
                        e.printStackTrace();
                        throw(e);
         } finally {
                        if(fos!=null)
                            try {
                                fos.close();
                            } catch (IOException e) {}
                        DBConnection.closeAll(null, null, con);
         }


   }
    private void cardTypeToXmlFile(String file,Element element)throws Exception{
        FileOutputStream out=null;
        try {
			XMLOutputter outputter = new XMLOutputter("  ", true, "GB2312");
			outputter.setTextTrim(true);
			out = new FileOutputStream(file);
			outputter.output(new Document(element), out);
			out.flush();
			out.close();
		} catch (Exception e) {
            e.printStackTrace();
            throw (e);
		}finally{
            if(out!=null)
                try {
                    out.close();
                } catch (IOException e) {}
        }
    }

	public void destroy() {
		super.destroy();

	}


}

package com.royalstone.pos.loader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.royalstone.pos.common.OperatorList;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.util.Response;

/**
 * @author root
 *
 */
public class OperatorLoader {

	// 测试主入口类
	public static void main(String[] args) 
	{
		OperatorLoader w = new OperatorLoader( "172.16.7.163", 9090 );
		try {
			w.download( "operator.NEW.lst" );
		} catch (IOException e) {
			System.out.println( "Failed!" );
			// e.printStackTrace();
		}
	}

	// 设置IP，端口号
	public OperatorLoader( String host, int port )
	{
		this.host = host;
		this.port = port;
	}
	
	public void download( String file ) throws IOException
	{
		Response result = null;

		// 客户端传入Servelet，方法中中需要要POS机的HOST PORT
		// HOST PORT 来源配置文档POS.ini
		servlet = new URL( "http", host, port, "/pos41/DispatchServlet" );
		// 建立连接
		conn = (HttpURLConnection) servlet.openConnection();

		Object[] params = new Object[1];

		// 指定“下载操作员资料”类
		params[0] = "com.royalstone.pos.web.command.OperatorCommand";

		// 封装远程方法调用中有参数的类
		// MarshalledValue是创建类，它继承java.io.Serializable
		MarshalledValue mvI = new MarshalledValue(params);
		System.out.println( "Invoke OperatorCommand! " );
        MarshalledValue mvO = null;
        try {
        	// 客户端远程调用入口
            mvO = HttpInvoker.invoke(conn, mvI);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object[] results = null;

		if (mvO != null) {
			results = mvO.getValues();
		}

		if (results != null && results.length > 0) {
			result = (Response) results[0];
			OperatorList lst = (OperatorList) result.getObject();
			if(lst!=null)lst.dump( file );
			System.out.println( result );
		}

	}

	private URL servlet;
	private HttpURLConnection conn;
	private String host;
	private int port;
}

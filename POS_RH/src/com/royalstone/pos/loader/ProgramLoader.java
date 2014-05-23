package com.royalstone.pos.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.royalstone.pos.gui.DialogConfirm;
import com.royalstone.pos.gui.StartFrame;
import com.royalstone.pos.invoke.FileDownload;
import com.royalstone.pos.invoke.HttpInvoker;
import com.royalstone.pos.invoke.MarshalledValue;
import com.royalstone.pos.util.JarVersion;
import com.royalstone.pos.util.POSVersionVO;
import com.royalstone.pos.util.UnZipFile;
import com.royalstone.pos.util.UpdateFile;
import com.royalstone.pos.util.UpdateList;

/**
 * 程序下载更新
 * @author liangxinbiao
 */
public class ProgramLoader {

	private String host;
	private String port;
	private String posid;
	private FileDownload fileDownload;
	private POSVersionVO version;

	public ProgramLoader(String file)
		throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		host = prop.getProperty("server");
		port = prop.getProperty("port");
		posid = prop.getProperty("posid");
		fileDownload = new FileDownload(host, port);

	}

	/**
	 * @deprecated 
	 */
	public void loadLoader() {
		if (version != null && version.getLoaderVersion() != null) {
			String strLocalVersionNo =
				JarVersion.getVersion("program/loader.jar", "Loader-Version");
			int serverVersionNo = 0;
			int localVersionNo = 0;
			try {
				serverVersionNo =
					Integer.parseInt(mytrim(version.getLoaderVersion()));
				localVersionNo = Integer.parseInt(strLocalVersionNo);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
			if (localVersionNo == 0 || serverVersionNo > localVersionNo) {
				fileDownload.download("program/loader.jar", "loader.jar");
			}
		}
	}

	/**
	 * @deprecated 
	 */
	public void loadProgram() {
		if (version != null && version.getPosVersion() != null) {
			String strLocalVersionNo =
				JarVersion.getVersion("program/posv41.jar", "POS-Version");
			int serverVersionNo = 0;
			int localVersionNo = 0;
			try {
				serverVersionNo =
					Integer.parseInt(mytrim(version.getPosVersion()));
				localVersionNo = Integer.parseInt(strLocalVersionNo);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
			if (localVersionNo == 0 || serverVersionNo > localVersionNo) {
				fileDownload.download("program/posv41.jar", "posv41.NEW.jar");
				rename("posv41.NEW.jar", "posv41.jar", "posv41.BAK.jar");
			}
		}
	}

	/**
	 * @deprecated 
	 */
	public void loadDrvier() {
		if (version != null && version.getDriverVersion() != null) {

			String strLocalVersionNo =
				JarVersion.getVersion("drv/driver.zip", "Driver-Version");
			int serverVersionNo = 0;
			int localVersionNo = 0;
			try {
				serverVersionNo =
					Integer.parseInt(mytrim(version.getDriverVersion()));
				localVersionNo = Integer.parseInt(strLocalVersionNo);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}

			if (localVersionNo == 0 || serverVersionNo > localVersionNo) {
				fileDownload.download("drv/driver.zip", "drv/driver.NEW.zip");
				File newFile = new File("drv/driver.NEW.zip");
				if (newFile.exists()) {
					rename(
						"drv/driver.NEW.zip",
						"drv/driver.zip",
						"drv/driver.BAK.zip");

					UnZipFile unZipFile = new UnZipFile();
					try {
						unZipFile.unZip("drv/driver.zip", "drv");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private UpdateList getUpdateList() throws IOException {
		URL servlet =
			new URL("http://" + host + ":" + port + "/pos41/DispatchServlet");
		HttpURLConnection conn = (HttpURLConnection) servlet.openConnection();

		Object[] params = new Object[2];

		params[0] = "com.royalstone.pos.web.command.GetUpdateListCommand";
		params[1] = posid;

		MarshalledValue mvI = new MarshalledValue(params);
		System.out.println("Invoke GetUpdateListCommand! ");
		MarshalledValue mvO = HttpInvoker.invokeWithException(conn, mvI);

		Object[] results = null;

		if (mvO != null) {
			results = mvO.getValues();
		}

		if (results != null && results.length > 0) {
			return (UpdateList) results[0];
		}

		return null;
	}

	private void updateCheck() {

		try {
			URL servlet =
				new URL(
					"http://" + host + ":" + port + "/pos41/DispatchServlet");
			HttpURLConnection conn =
				(HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[2];

			params[0] = "com.royalstone.pos.web.command.CompleteUpdateCommand";
			params[1] = posid;

			MarshalledValue mvI = new MarshalledValue(params);
			System.out.println("Invoke CompleteUpdateCommand! ");
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void loadMisc() throws IOException{

		UpdateList updateList = getUpdateList();
		if (updateList != null) {
			boolean complete = false;
			for (int i = 0; i < updateList.size(); i++) {
				UpdateFile updateFile = updateList.get(i);
				if (updateFile != null
					&& updateFile.getSrcPath() != null
					&& updateFile.getDestPath() != null) {
					complete =
						fileDownload.download(
							updateFile.getSrcPath(),
							updateFile.getDestPath());
				}
			}
			if (complete)
				updateCheck();
		}
	}

	public static void rename(String fnew, String fcurrent, String fbak) {

		File newFile = new File(fnew);
		if (newFile.exists()) {

			File currentFile = new File(fcurrent);
			if (currentFile.exists()) {

				File bakFile = new File(fbak);
				if (bakFile.exists()) {
					bakFile.delete();
				}

				currentFile.renameTo(new File(fbak));
				newFile.renameTo(new File(fcurrent));

			} else {
				newFile.renameTo(new File(fcurrent));
			}
		}
	}

	/**
	 * @deprecated
	 */
	public void fetchVersion() {
		try {
			URL servlet =
				new URL(
					"http://" + host + ":" + port + "/pos41/DispatchServlet");
			HttpURLConnection conn =
				(HttpURLConnection) servlet.openConnection();

			Object[] params = new Object[1];

			params[0] = "com.royalstone.pos.web.command.GetVersionCommand";

			MarshalledValue mvI = new MarshalledValue(params);
			System.out.println("Invoke OperatorCommand! ");
			MarshalledValue mvO = HttpInvoker.invoke(conn, mvI);

			Object[] results = null;

			if (mvO != null) {
				results = mvO.getValues();
			}

			if (results != null && results.length > 0) {
				version = (POSVersionVO) results[0];
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FileLock lock=null;
		try {
			FileOutputStream fos=new FileOutputStream("lock");
			lock=fos.getChannel().tryLock();
			if(lock==null){
				JOptionPane.showMessageDialog(
					null,"POS程序已经运行！");
				System.exit(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"严重错误！");
			System.exit(1);
		}

		StartFrame startFrame = new StartFrame();
		startFrame.show();

		ProgramLoader programLoader;
		try {
			programLoader = new ProgramLoader("pos.ini");
			System.out.println("download misc .......");
			do{
			try{
				programLoader.loadMisc();
				break;
			}catch(IOException ex){
				if(!confirm("下载程序更新失败，重试(确认)或继续(取消)?")){
					break;				
				}
			}}while(true);
			

			lock.release();
			Process p = Runtime.getRuntime().exec("main.bat");

			Thread.sleep(10000);
			startFrame.dispose();
			System.exit(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("ERROR: Connot open pos.ini, exit ...");
			System.exit(2);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("ERROR: Connot read pos.ini, exit ...");
			System.exit(2);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	private String mytrim(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

	private static boolean confirm(String s) {
		DialogConfirm confirm = new DialogConfirm();
		confirm.setMessage(s);
		confirm.show();

		return (confirm.isConfirm());
	}
}

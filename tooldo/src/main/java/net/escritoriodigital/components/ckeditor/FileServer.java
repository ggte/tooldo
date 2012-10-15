/**
 * 
 */
package net.escritoriodigital.components.ckeditor;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author andrefabbro
 *         http://myfreecode.blogspot.com.br/2008/05/simple-java-file
 *         -server-servlet-eng.html
 * 
 */
public class FileServer extends HttpServlet {
	transient private Logger log = Logger.getLogger(FileServer.class.getName());

	private final static int LOG_ERROR = 0;
	private final static int LOG_WARN = 1;
	private final static int LOG_DEBUG = 2;
	private final static int LOG_INFO = 3;

	private static ArrayList users = new ArrayList();

	/**
	 * shared context for files
	 */
	private static HashMap files;

	public FileServer() {

	}

	protected void doHead(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// reserve for future request.getLocale(); =)
		String requestPath = URLDecoder.decode(request.getPathInfo(), "UTF-8");
		String remouteAddress = request.getRemoteAddr();
		String method = request.getMethod();
		String sessionId = request.getSession().getId();

		log(LOG_INFO, "{2} request: {0} from : {1} sessionId: {3} params: {4}",
				requestPath, remouteAddress, method, sessionId,
				request.getParameterMap());

		Properties appProp = loadProperties();
		log(LOG_DEBUG, "File Server props: {0}", appProp);

		if (appProp.containsKey("callback")) {

		}

		String filename = requestPath
				.substring(requestPath.lastIndexOf('/') + 1);

		File file = getFile(appProp, filename);
		long start = 0;
		long end = 0;
		Enumeration enu = request.getHeaderNames();
		while (enu.hasMoreElements()) {
			String obj = (String) enu.nextElement();
			if (obj.equalsIgnoreCase("range")) {
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				String rangeData = request.getHeader(obj);
				rangeData = rangeData.substring(rangeData.indexOf('=') + 1)
						.trim();
				String positions[] = rangeData.split("-");
				try {
					start = Long.parseLong(positions[0].trim());
					if (positions.length > 1 && positions[1].length() > 0) {
						end = Long.parseLong(positions[1].trim());
					}
				} catch (Exception e) {
					log(LOG_ERROR, "Range Error. range: {0} ",
							request.getHeader(obj), e);
				}
				log(LOG_DEBUG, "Partial request. start pos: {0}, end pos {1}",
						start, end);
			}
		}

	}

	private boolean sendContent(File file, HttpServletResponse response,
			long start, long end, Properties appProps) throws IOException,
			InterruptedException {
		int buffersize = 1024 * 16;
		int sleep = 0;
		boolean result = false;
		if (appProps.containsKey("max.speed")) {
			int speed = Integer.parseInt(appProps.getProperty("max.speed"));
			sleep = (int) (((float) buffersize / speed) * 1000);
		}

		response.setContentType("application/octet-stream");
		response.addHeader("Accept-Ranges", "bytes");

		response.addHeader("Content-Disposition", "attachment; filename="
				+ file.getName());
		if (end > 0) {
			response.addHeader("Content-Range", "bytes " + start + "-" + end
					+ "/" + (end + 1));
		}

		long size = file.length();
		if (start > 0 && end > 0) {
			size = end - start;
		}
		if (start > 0 && end == 0) {
			size = file.length() - start;
		}

		response.addHeader("Content-Length", "" + size);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));

		if (start > 0) {
			bis.skip(start);
		}

		BufferedOutputStream bos = new BufferedOutputStream(
				response.getOutputStream());

		byte buffer[] = new byte[buffersize];

		int read = bis.read(buffer);
		long dataSend = 0;
		while (read > 0) {

			if (end > 0 && start + dataSend + read > end) {
				bos.write(buffer, 0, (int) (end - start - dataSend));
				bos.flush();
				break;
			} else {
				bos.write(buffer, 0, read);
				bos.flush();
			}
			
			read = bis.read(buffer);
		}
		
		bos.close();
		bis.close();
		return result;
	}

	private Properties loadProperties() throws IOException {
		Properties props = new Properties();
		props.load(this.getClass()
				.getResourceAsStream("/fileserver.properties"));
		return props;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {

	}

	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) {

	}

	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) {

	}

	private void log(int logtype, String message, Object... obj) {
		StringBuffer sb = new StringBuffer();
		MessageFormat format = new MessageFormat(message);
		sb.append(format.format(message, obj));
		switch (logtype) {
		case LOG_DEBUG:
			log.debug(sb);
			break;
		case LOG_ERROR:
			log.debug(sb, (Throwable) obj[obj.length - 1]);
			break;
		case LOG_INFO:
			log.info(sb);
			break;
		case LOG_WARN:
			log.warn(sb);
			break;
		}
	}

	public File getFile(Properties appProp, String filename)
			throws FileNotFoundException {
		if (files == null || !files.containsKey(filename)) {
			if (files == null) {
				files = new HashMap();
			} else {
				files.clear();
			}
			String paths = appProp.getProperty("files.dir");
			String[] path = paths.split(";");
			for (int i = 0; i < path.length; i++) {
				String ph = path[i].split(",")[0];
				Collection coll = FileUtils.listFiles(new File(ph), null, true);
				Iterator fileIterator = coll.iterator();
				while (fileIterator.hasNext()) {
					File o = (File) fileIterator.next();
					files.put(o.getName(), o);
				}
			}
		}
		if (!files.containsKey(filename)) {
			throw new FileNotFoundException("FILE NOT FOUND: " + filename);
		}
		return (File) files.get(filename);
	}
}

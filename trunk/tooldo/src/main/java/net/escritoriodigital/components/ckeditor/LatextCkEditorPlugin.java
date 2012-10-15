package net.escritoriodigital.components.ckeditor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Esse servlet é responsável por interceptar o request do 
 * ckeditor pelo plugin equation e entregar o recurso correto do 
 * plugin que está na pasta resources
 * 
 * @author andrefabbro
 * 
 */
public class LatextCkEditorPlugin extends HttpServlet {

	private static final long serialVersionUID = -8147570868199214729L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int buffersize = 1024 * 16;

		// ex:
		// /tooldo/org.richfaces.resources/javax.faces.resource/org.richfaces.ckeditor/plugins/equation/plugin.js
		String uri = req.getRequestURI();
		String replaceStr = req.getContextPath()
				+ "/org.richfaces.resources/javax.faces.resource/org.richfaces.ckeditor/plugins/equation/";
		String filePath = uri.replace(replaceStr, "ckeditor/plugins/equation/");

		InputStream bis = this.getClass().getClassLoader()
				.getResourceAsStream(filePath);

		if (bis != null) {

			OutputStream bos = new BufferedOutputStream(resp.getOutputStream());

			byte buffer[] = new byte[buffersize];

			int read = bis.read(buffer);

			while (read > 0) {
				bos.write(buffer, 0, read);
				bos.flush();
				read = bis.read(buffer);
			}

			bos.close();
		}

		bis.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

}

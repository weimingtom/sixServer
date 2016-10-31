/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ngame.server.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.ngame.util.IOUtil;

/**
 *
 * @author Beykery
 */
public class TestHttpConnection
{

	public static void main(String[] args) throws MalformedURLException, IOException
	{
		URL console = new URL("http://101.251.195.142:8080/download/logparser.jar");
		HttpURLConnection conn = (HttpURLConnection) console.openConnection();
		conn.setRequestProperty("User_Agent", "dlfkdlkj");
		conn.connect();
		int code = conn.getResponseCode();
		if (code == 200)
		{
			InputStream is = conn.getInputStream();
			byte[] content = IOUtil.getContent(is);

			System.out.println(content.length);
		}
		conn.disconnect();
	}
}

package com.kuci.image.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author walkersing
 *
 * on:下午3:33:42 
 */
public class CommandUtil {

	public static void command(String command) {
		Runtime runtime = null;
		InputStream stderr = null;
		BufferedReader errReader = null;
		try {
			runtime = Runtime.getRuntime();
			Process process = runtime.exec(command);
			stderr = process.getErrorStream();
			final InputStream stdout = process.getInputStream();
			errReader = new BufferedReader(new InputStreamReader(stderr));

			new Thread(new Runnable() {
				public void run() {
					String lineOut = null;
					BufferedReader outReader = new BufferedReader(
							new InputStreamReader(stdout));

					try {
						while ((lineOut = outReader.readLine()) != null) {
							System.out.println("lineOut: " + lineOut);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (stdout != null)
								stdout.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			String lineErr = null;
			while ((lineErr = errReader.readLine()) != null) {
				System.out.println("lineErr: " + lineErr);
			}

			try {
				int exitVal = process.waitFor();
				System.out.println("exitVal: " + exitVal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("It is done...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stderr != null)
					stderr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}

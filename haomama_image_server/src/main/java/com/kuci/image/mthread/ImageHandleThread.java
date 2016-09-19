package com.kuci.image.mthread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.kuci.image.model.ImageData;
import com.kuci.image.model.ImageInfo;

/**
 * @author walkersing
 *
 * on:下午3:58:08 
 */
public class ImageHandleThread {

	private static final int NTHREADS = 100;
	private static final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
	private static final CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
	
	static {
		initImageThread();
	}
	
	public static void addTask(final ImageInfo imageInfo) {
		completionService.submit(new Callable<ImageData>() {
			public ImageData call() {
				return imageInfo.imgHandle();
			}
		});
	}

	public static Future<ImageData> getResult() {
		try {
			return completionService.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

	public static void initImageThread() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						getResult();
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						Thread.currentThread().interrupt();
					}
				}
			}
		}).start();
	}
}

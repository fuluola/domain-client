package com.fuluola.springboot;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lifu.utils.WebUtil;

public class JsoupTest {

	public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		
		String path = "https://www.78977b.com/draw-pk10-today.html";
		String id = "tr-725313";
		Element element = WebUtil.getHtmlContent(path,id);
		Elements ele2 = element.getElementsByClass("td-box cai-num size-32 center pk10-num");
		System.out.println("==============start===============");
		System.out.println(Arrays.toString(ele2.text().split(" ")));
		System.out.println("================end================");
	}
}

package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import android.net.Uri;

import com.leon.biuvideo.beans.resourcesBeans.Danmaku;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.DanmakuType;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @Author Leon
 * @Time 2021/4/17
 * @Desc 弹幕数据解析类
 */
public class DanmakuParser {

    private InflaterInputStream inflaterInputStream;
    private InputStream inputStream;

    public List<Danmaku> parseData (String cid) {
        try {

            URL url = new URL(BiliBiliAPIs.DANMAKU + "?oid=" + cid);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 需要对响应结果进行解压
                inputStream = httpURLConnection.getInputStream();
                inflaterInputStream = new InflaterInputStream(inputStream);

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document document = documentBuilder.parse(inflaterInputStream);

                Element rootElement = document.getDocumentElement();

                NodeList elements = rootElement.getElementsByTagName("d");

                int elementsLength = elements.getLength();
                List<Danmaku> danmakuList = new ArrayList<>(elementsLength);
                for (int i = 0; i < elementsLength; i++) {
                    Danmaku danmaku = new Danmaku();
                    Node node = elements.item(i);

                    NamedNodeMap attributes = node.getAttributes();
                    String textContent = attributes.getNamedItem("p").getTextContent();
                    String[] split = textContent.split(",");
                    danmaku.showIndex = Float.parseFloat(split[0]);

                    int danmakuType = Integer.parseInt(split[1]);
                    if (danmakuType > 7) {
                        continue;
                    } else {
                        switch (danmakuType) {
                            case 4:
                                danmaku.danmakuType = DanmakuType.BOTTOM_DANMAKU;
                                break;
                            case 5:
                                danmaku.danmakuType = DanmakuType.TOP_DANMAKU;
                                break;
                            case 6:
                                danmaku.danmakuType = DanmakuType.REVERSE_DANMAKU;
                                break;
                            default:
                                danmaku.danmakuType = DanmakuType.NORMAL_DANMAKU;
                                break;
                        }
                    }

                    danmaku.danmakuSize = Integer.parseInt(split[2]);
                    danmaku.danmakuColor = Integer.parseInt(split[3]);
                    danmaku.danmakuTimestamp = Long.parseLong(split[4]);
                    danmaku.danmakuPoolType = Integer.parseInt(split[5]);
                    danmaku.content = node.getTextContent();

                    danmakuList.add(danmaku);
                }

                danmakuList.sort(new Comparator<Danmaku>() {
                    @Override
                    public int compare(Danmaku o1, Danmaku o2) {
                        if (o1.showIndex > o2.showIndex) {
                            return 1;
                        } else if (o1.showIndex < o2.showIndex) {
                            return -1;
                        }

                        return 0;
                    }
                });

                return danmakuList;
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (inflaterInputStream != null) {
                    inflaterInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


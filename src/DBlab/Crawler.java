package DBlab;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	private static final int numberOfPage = 30;
	private boolean crawling = true; // �κ��� Ȱ���� ���� ������ �ƴ��� ��Ÿ��
	
	private LinkedHashMap<String, UrlBean> crawledList = null; //������ ���� ���� ����ϱ� ���� �� 
	private LinkedHashMap<String, UrlBean> toCrawlList = null; //������ ������ ���� ����ϱ� ���� �� 
	private ArrayList<UrlBean> urlList = null; // url Bean ��ü���� ����ϱ� ���� �� (���߿� �Ѳ����� insert �ϱ� ����)
	private UrlDao urlDao = null; //  url Bean ��ü�� ���� Data Access Object
	
	public static void main(String[] args) throws IOException {
//		String[][] siteList = {
//				{"google", "https://www.google.com/"},
//				{"youtube", "https://www.youtube.com/"},
//				{"facebook", "https://www.facebook.com/"},
//				{"reddit", "https://www.reddit.com/"},
//				{"amazon", "https://www.amazon.com/"},
//				{"wikipedia", "https://www.wikipedia.org/"},
//				{"yahoo", "https://www.yahoo.com/"},
//				{"twitter", "https://twitter.com/"},
//				{"ebay", "https://www.ebay.com/"},
//				{"netflix", "https://www.netflix.com/"},
//				{"instagram", "https://www.instagram.com/"}
//		};

		
		String[][] siteList = {
				{"tripadvisor", "https://www.tripadvisor.com/us"}
		};
		
		Crawler crawler = new Crawler();
		for(int i=0; i<1; i++){
			crawler.crawl(siteList[i][0], siteList[i][1], 100000);
		}
		
	}
	
	public void crawl(String site, String startUrl, int maxUrls) throws IOException {
		crawledList = new LinkedHashMap<String, UrlBean>(); //������ ���� ���� ����ϱ� ���� �� 
		toCrawlList = new LinkedHashMap<String, UrlBean>(); //������ ������ ���� ����ϱ� ���� �� 
		urlList = new ArrayList<UrlBean>(); // url Bean ��ü
		urlDao = new UrlDao(); // url Bean ��ü
		int depth = 0;
		int count = 0;
		int randomPointer = 0;
		int[] randomNumber = new int[numberOfPage];
		
		// �ߺ� ���� ���� 10�� ����
		getRandomNumber(randomNumber);
		
		//�����ϱ� ���� ����Ʈ�� ����ڰ� ��������� ������ ����Ʈ�� ���
		toCrawlList.put(startUrl, new UrlBean(site, 0, -1, startUrl, depth)); 
		
		while (crawling && toCrawlList.size() > 0) { 
			if (maxUrls != -1) { //�ִ� url������ ���� �Ǿ� �ִٸ� 
				if (crawledList.size() >= maxUrls) { //������ ����Ʈ�� ������ �ִ� url���� �Ѿ �� ����!!
					break;
				}
			}
			
			// 1. toCrawlList�κ��� 1���� url get�ϰ�, �� url�� toCrawlList���� ���� (pop ����)
			UrlBean url = toCrawlList.values().iterator().next();
			toCrawlList.remove(url.getPage()); 
			
			// ���� url ���� ���
//			if(url.getNoPage() == randomNumber[randomPointer]){
//				System.out.println( url.getSite() + " " + url.getNoPage() + " " + url.getNoParent() + " " + url.getPage() + " "  + url.getDepth() );
				System.out.println(url.getPage());
				// ���� �湮�� url DB�� ����
				
			    //urlDao.insertUrl(url);
//			    if(++randomPointer >= numberOfPage) break;
//			}
		    
			
			// 2. �� url�� �̹� Ž���ߴٴ� �ǹ̷� crwaledList�� ����
			crawledList.put(url.getPage(), new UrlBean(url));
			
			// url html �ڵ� ����
			Document doc;
			try{
				doc = Jsoup.connect(url.getPage()).get();
			}catch(IOException e){
				continue;
			}catch(Exception e){
				continue;
			}
			
			String pageContents = doc.html();	
		    LinkedHashMap<String, UrlBean> links = new LinkedHashMap<String, UrlBean>();
		    String value = null;
		    
		    
			// 3. url ������ ��ũ�� �����Ͽ� toCrawlList�� ���� (field ���� �� �����Ͽ� ����)
			if (pageContents != null && pageContents.length() > 0) {
				Elements LinkEls = doc.select("a[href]");
				for (Element linkEl : LinkEls) {
					if(linkEl.attr("href").contains("http"))
						value = linkEl.attr("href").trim();
						if( !links.containsKey(value) && !toCrawlList.containsKey(value) && !crawledList.containsKey(value)
								&& !links.containsKey(value+"/") && !toCrawlList.containsKey(value+"/") && !crawledList.containsKey(value+"/") )
							if(value != null && value.contains(site) )
								links.put(value, new UrlBean(site, ++count, url.getNoPage(), value, url.getDepth()+1));
				}
			}
	
			// 4. ��� parsing �ؿ� link ���� toCrawlList�� ������λ���			
			toCrawlList.putAll(links);			
			
		}

	}
	
	public static void getRandomNumber(int[] randomNumber) {
		for(int i=0; i<randomNumber.length; i++){
			randomNumber[i] = (int)(Math.random()*1001);
			for(int j=0; j<i; j++){
				if(randomNumber[i] == randomNumber[j]){
					i--;
					break;
				}
			}
		}
		Arrays.sort(randomNumber);
	}
}
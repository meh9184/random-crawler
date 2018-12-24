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
	private boolean crawling = true; // 로봇의 활동이 진행 중인지 아닌지 나타냄
	
	private LinkedHashMap<String, UrlBean> crawledList = null; //수집이 끝난 것을 기록하기 위한 것 
	private LinkedHashMap<String, UrlBean> toCrawlList = null; //앞으로 수집할 것을 기록하기 위한 것 
	private ArrayList<UrlBean> urlList = null; // url Bean 객체들을 기록하기 위한 것 (나중에 한꺼번에 insert 하기 위해)
	private UrlDao urlDao = null; //  url Bean 객체에 대한 Data Access Object
	
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
		crawledList = new LinkedHashMap<String, UrlBean>(); //수집이 끝난 것을 기록하기 위한 것 
		toCrawlList = new LinkedHashMap<String, UrlBean>(); //앞으로 수집할 것을 기록하기 위한 것 
		urlList = new ArrayList<UrlBean>(); // url Bean 객체
		urlDao = new UrlDao(); // url Bean 객체
		int depth = 0;
		int count = 0;
		int randomPointer = 0;
		int[] randomNumber = new int[numberOfPage];
		
		// 중복 없는 난수 10개 생성
		getRandomNumber(randomNumber);
		
		//수집하기 위한 리스트에 사용자가 출발점으로 지정한 사이트를 기록
		toCrawlList.put(startUrl, new UrlBean(site, 0, -1, startUrl, depth)); 
		
		while (crawling && toCrawlList.size() > 0) { 
			if (maxUrls != -1) { //최대 url갯수가 설정 되어 있다면 
				if (crawledList.size() >= maxUrls) { //수집된 리스트의 갯수가 최대 url수를 넘어같 때 중지!!
					break;
				}
			}
			
			// 1. toCrawlList로부터 1개의 url get하고, 그 url을 toCrawlList에서 삭제 (pop 연산)
			UrlBean url = toCrawlList.values().iterator().next();
			toCrawlList.remove(url.getPage()); 
			
			// 현재 url 정보 출력
//			if(url.getNoPage() == randomNumber[randomPointer]){
//				System.out.println( url.getSite() + " " + url.getNoPage() + " " + url.getNoParent() + " " + url.getPage() + " "  + url.getDepth() );
				System.out.println(url.getPage());
				// 현재 방문한 url DB에 삽입
				
			    //urlDao.insertUrl(url);
//			    if(++randomPointer >= numberOfPage) break;
//			}
		    
			
			// 2. 본 url을 이미 탐색했다는 의미로 crwaledList에 삽입
			crawledList.put(url.getPage(), new UrlBean(url));
			
			// url html 코드 추출
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
		    
		    
			// 3. url 내부의 링크들 추출하여 toCrawlList에 삽입 (field 값들 잘 조정하여 삽입)
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
	
			// 4. 방금 parsing 해온 link 들을 toCrawlList에 순서대로삽입			
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
package DBlab;

public class UrlBean {
	private String site;
	private int noPage;
	private int noParent;
	private String page;
	private int depth;
	
	public UrlBean(UrlBean url) {
		super();
		this.site = url.site;
		this.noPage = url.noPage;
		this.noParent = url.noParent;
		this.page = url.page;
		this.depth = url.depth;
	}
	
	public UrlBean(String site, int noPage, int noParent, String page, int depth) {
		super();
		this.site = site;
		this.noPage = noPage;
		this.noParent = noParent;
		this.page = page;
		this.depth = depth;
	}

	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getNoPage() {
		return noPage;
	}
	public void setNoPage(int noUrl) {
		this.noPage = noUrl;
	}
	public int getNoParent() {
		return noParent;
	}
	public void setNoParent(int noParent) {
		this.noParent = noParent;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	
}

package com.env.qualitymetrics.dto;

public class Page {
	//起始页
	int initPage = 1;
	//总页数
	int totalPage;;
	//当前页
	int currentPage;
	//总记录
	int total;
	//每页显示数量
	int pageSize=10;
	
	public int getInitPage() {
		return initPage;
	}
	public void setInitPage(int initPage) {
		this.initPage = initPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageNumber) {
		this.pageSize = pageNumber;
	}
}

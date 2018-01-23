package com.bt.om.web;

import java.util.ArrayList;
import java.util.List;

import com.bt.om.entity.ID;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: Pagination.java, v 0.1 2015年10月15日 下午9:05:13 hl-tanyong Exp $
 */
public class Pagination extends ID {
	private static final long serialVersionUID = 1L;

	private static int SHOW_PAGE_NUM = 10;

	private int total_page;

	private int current_page;

	private long total_row;

	private long real_start_row;

	private long start_row;

	private long real_end_row;

	private long end_row;

	private int start_page;

	private int end_page;

	private int previous_page = -1;

	private int next_page = -1;

	private List<Page> vPages;

	private int showNum;

	private Page prePage;

	private Page NPage;

	private int last_page_start_row;

	/**
	 * Constructor for the Pager object
	 * 
	 * @param total_count
	 *            Description of the Parameter
	 * @param current_page
	 *            Description of the Parameter
	 * @param show_num
	 *            Description of the Parameter
	 */
	public Pagination(long newTotalCount, int newStartRow, int newShowNum) {

		showNum = newShowNum;
		total_page = (int) (Math.floor((newTotalCount - 1) / newShowNum) + 1);

		current_page = (newStartRow / newShowNum) + 1;
		current_page = (current_page <= 0) ? 1 : current_page;
		current_page = (current_page > total_page) ? total_page : current_page;

        total_row = newTotalCount;
        real_start_row = newStartRow + 1;
        start_row = (current_page - 1) * newShowNum + 1;
        end_row = start_row + newShowNum - 1;
        end_row = (end_row > newTotalCount) ? newTotalCount : end_row;
        real_end_row = real_start_row + newShowNum - 1;
        real_end_row = (real_end_row > newTotalCount) ? newTotalCount : real_end_row;
        last_page_start_row = (total_page - 1) * newShowNum;
        int num = SHOW_PAGE_NUM;
        if ((current_page - num / 2) > 0) {
            start_page = current_page - num / 2;
            num = num / 2;
        } else {
            start_page = 1;
            num = SHOW_PAGE_NUM - current_page + 1;
        }

		if ((current_page + num - 1) < total_page) {
			end_page = current_page + num - 1;
		} else {
			end_page = total_page;
		}
		vPages = new ArrayList<Page>();
		end_page = (end_page > total_page) ? total_page : end_page;
		for (int i = start_page; i <= end_page; i++) {
			vPages.add(new Page(i));
		}

		if (current_page > 1) {
			previous_page = current_page - 1;
			prePage = new Page(previous_page);
		}

		if (current_page < total_page) {
			next_page = current_page + 1;
			NPage = new Page(next_page);
		}

	}

	/**
	 * Gets the totalPage attribute of the Pagination object
	 * 
	 * @return The totalPage value
	 */
	public int getTotalPage() {
		return total_page;
	}

	/**
	 * Gets the currentPage attribute of the Pagination object
	 * 
	 * @return The currentPage value
	 */
	public int getCurrentPage() {
		return current_page;
	}

	/**
	 * Gets the totalRow attribute of the Pagination object
	 * 
	 * @return The totalRow value
	 */
	public long getTotalRow() {
		return total_row;
	}

	/**
	 * Gets the startRow attribute of the Pagination object
	 * 
	 * @return The startRow value
	 */
	public long getStartRow() {
		return start_row;
	}

	/**
	 * Gets the endRow attribute of the Pagination object
	 * 
	 * @return The endRow value
	 */
	public long getEndRow() {
		return end_row;
	}

	/**
	 * Gets the startPage attribute of the Pagination object
	 * 
	 * @return The startPage value
	 */
	public int getStartPage() {
		return start_page;
	}

	/**
	 * Gets the endPage attribute of the Pagination object
	 * 
	 * @return The endPage value
	 */
	public int getEndPage() {
		return end_page;
	}

	/**
	 * Gets the previousPage attribute of the Pagination object
	 * 
	 * @return The previousPage value
	 */
	public int getPreviousPage() {
		return previous_page;
	}

	/**
	 * Gets the nextPage attribute of the Pagination object
	 * 
	 * @return The nextPage value
	 */
	public int getNextPage() {
		return next_page;
	}

	public class Page {
		private int page;

		private int startRow;

		public Page(int newPage) {
			page = newPage;
			startRow = (newPage - 1) * showNum;
		}

		/**
		 * @return
		 */
		public int getPage() {
			return page;
		}

		/**
		 * @return
		 */
		public int getStartRow() {
			return startRow;
		}

		/**
		 * @param i
		 */
		public void setPage(int i) {
			page = i;
		}

		/**
		 * @param i
		 */
		public void setStartRow(int i) {
			startRow = i;
		}

	}

	/**
	 * @return
	 */
	public Page getNPage() {
		return NPage;
	}

	/**
	 * @return
	 */
	public Page getPrePage() {
		return prePage;
	}

	/**
	 * @param page
	 */
	public void setNPage(Page page) {
		NPage = page;
	}

	/**
	 * @param page
	 */
	public void setPrePage(Page page) {
		prePage = page;
	}

	public int getLastPageStartRow() {
		return last_page_start_row;
	}

	public List<Page> getVPages() {
		return vPages;
	}

	public void setVPages(List<Page> pages) {
		vPages = pages;
	}

	public long getRealStartRow() {
		return real_start_row;
	}

	public void setRealStartRow(int real_start_row) {
		this.real_start_row = real_start_row;
	}

	public long getRealEndRow() {
		return real_end_row;
	}

	public void setRealEndRow(int real_end_row) {
		this.real_end_row = real_end_row;
	}

	public int getShowNum() {
		return showNum;
	}

	public void setShowNum(int showNum) {
		this.showNum = showNum;
	}

}
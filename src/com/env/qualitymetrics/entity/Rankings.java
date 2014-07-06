package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity  
@Table(name="tbl_rankings")
public class Rankings {
	
	  	@Id  
	    @GeneratedValue(generator="system-native")  
	    @GenericGenerator(name = "system-native",strategy="native")  
	    @Column(length=100) 
	    private int rank_id;
	  	
	  	@Column(length=1000)
	  	private String rank_period;
	  	
	 	@Column(length=10000)
	  	private String rank_detail ;

		public int getRank_id() {
			return rank_id;
		}

		public void setRank_id(int rank_id) {
			this.rank_id = rank_id;
		}

		public String getRank_period() {
			return rank_period;
		}

		public void setRank_period(String rank_period) {
			this.rank_period = rank_period;
		}

		public String getRank_detail() {
			return rank_detail;
		}

		public void setRank_detail(String rank_detail) {
			this.rank_detail = rank_detail;
		}
}

package com.env.qualitymetrics.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Sprint_score_item_pk implements Serializable{

	/**
	 * Sprint_score_item的主键
	 */
	private static final long serialVersionUID = -4739968789587789107L;
	
	private int sprint_id;
	private int score_item_id;
	public int getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(int sprint_id) {
		this.sprint_id = sprint_id;
	}
	public int getScore_item_id() {
		return score_item_id;
	}
	public void setScore_item_id(int score_item_id) {
		this.score_item_id = score_item_id;
	}
}
